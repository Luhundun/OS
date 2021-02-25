package fileManage;

import control.OS;
import hardware.Block;

/**
 * @ClassName: SuperBlock
 * @Description: 模拟超级块
 * @Author: Lu Ning
 * @Date: 2021/1/29 0:15
 * @Version: v1.0
 *
 * 这些final修饰的属性代表这这属性在超级块的位置
 * 方便编程时的理解，其功能类似于枚举类
 */
public class SuperBlock extends Block {
    final short inodeTableUsedBlocks = 0;                      //硬盘inode表所占盘块数
    final short filesUsedBlocks = 1;                           //文件所占盘块数
    final short freeBlocksSumInMemory = 2;                        //内存中登记的空闲物理块数量
    final short freeBlocksInMemoryMap = 3;                    //位示图法管理内存中用户空闲物理块，大小6B
    final short freeInodesSumInMemory = 7;                               //内存中登记的空闲inode数
    final short freeInodesNumInMemory = 8;                 //位示图法管理内存中登记的空闲inode编号，大小4B
    final short freeInodesSumInDisk = 10;                               //硬盘中登记的空闲inode数
    final short freeInodesNumInDisk = 11;                          //位示图法管理内存中登记的空闲inode编号，共64B
    final short freeBlocksInDiskGroupLink = 43;                        //硬盘中登记的存放成组链表的第一个块号
    final short freeBlocksSumInDisk = 44;                      //记录磁盘中的空闲盘块数目
    final short ifChanged = 45;                               //记录超级块装载入内存后是否有变动
    final short runTime = 255;


    /**
     * @Description: 重装操作系统时构造一个超级块，它在硬盘的1号盘块.
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/1/29 10:11
     */
    public SuperBlock(){
        super();
        blockUsedBytes = 512;
        block[inodeTableUsedBlocks] = 64;       //硬盘inode表默认占据64个盘块
        block[filesUsedBlocks] = 0;             //初始时文件占了
        block[freeBlocksSumInMemory] = 48;  //内存分配给用户24KB，初始时内存中空闲块有48个
//        block[freeBlocksInMemoryNum]          //初始全部空闲，默认为0
        block[freeInodesSumInMemory] = 32;         //内存中分配2KB给活动inode表，最多存放32个inode
//        block[freeInodesNumInMemory[]
        block[freeInodesSumInDisk] = 512;          //硬盘中最多存放512个inode
        block[freeBlocksInDiskGroupLink] = 66;      //初始的成组空闲链表首块为66号盘块
        block[ifChanged] = 0;
        block[runTime] = 0;
        OS.disk.writeWholeBlock((short) 1,this);
    }

    /**
     * @Description: 从硬盘中的第一块读取到超级块
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/16 17:17
     */
    public SuperBlock(Boolean load){
        if(!load){
            Block firstBlock = OS.disk.findBlockByDno((short)1);
            blockUsedBytes = 512;
            dno = 1;
            for(int i=0;i<256;i++){
                block[i] = firstBlock.readAWord(i);
            }
        }
    }


    /**
     * @Description: 开机时，将超级块写进内存
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/29 10:10
     */
    public void writeInMemory(){

    }

    /**
     * @Description: 有需要时，将超级块从内存写回磁盘
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/29 10:11
     */
    public void writeBackDisk(){

    }



    public short getInodeTableUsedBlocks() {
        return block[inodeTableUsedBlocks];
    }

    public short getFilesUsedBlocks() {
        return block[filesUsedBlocks];
    }

    public synchronized void setFilesUsedBlocks(short setNum){
        block[filesUsedBlocks] = setNum;
    }

    public short getFreeBlocksSumInMemory() {
        return block[freeBlocksSumInMemory];
    }

    public synchronized void setFreeBlocksSumInMemory(short setNum){
        block[freeBlocksSumInMemory] = setNum;
    }

    /**
     * @Description: 返回一个关于内存中空闲物理块的位示图
     * @param: []
     * @return: boolean[]
     * @auther: Lu Ning
     * @date: 2021/2/2 14:29
     */
    public boolean[][] getFreeBlocksInMemoryMap() {
//        boolean[] map = new boolean[48];
//        int index = 0;
//        for(int i = freeBlocksInMemoryMap; i< freeInodesSumInMemory; i++){
//            StringBuffer temp = new StringBuffer(Integer.toBinaryString(block[i]));
//            while (temp.length()<16){
//                temp.insert(0, '0');
//            }
//            String binary = temp.toString();
//            for (int bit=0;bit<16;bit++){
//                map[index] = binary.indexOf(bit) != '0';
//            }
//        }
//        return map;
        boolean[][] map = new boolean[3][16];
        int index = 0;
        //位图的储存类型为short，一个short16位对应16个inode。false为空闲
        for(int i = 0; i< 3; i++){
            short thisShort = block[i+freeBlocksInMemoryMap];

            if(thisShort >= 0){                     //根据正负判断最高位
                map[i][15] = false;
            } else {
                map[i][15] = true;
                thisShort += 32768;
            }
            for(int j=0;j<15;j++){
                map[i][j] = (thisShort%2)==1;
                thisShort /= (short)2;
            }
        }
        return map;
    }

    /**
     * @Description: 更新内存中空闲物理块
     * @param: [map]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/2 14:46
     */
    public void setFreeBlocksInMemoryMap(boolean[][] map){
//        int index = 0;
//        while (index<4){
//            StringBuffer temp = new StringBuffer();
//            for (int i=0;i<16;i++){
//                temp.append(map[i]?'1':'0');
//            }
//            block[freeBlocksInMemoryMap + index] = Short.parseShort(temp.toString(),16);
//            index++;
//        }
        for (int i = 0; i< 3; i++) {
            short thisShort = 0;
            for (int j = 14; j >= 0; j--) {
                thisShort *= 2;
                thisShort += map[i][j] ? 1 : 0;
            }
            if (map[i][15]) {
                thisShort -= 32768;
            }
            block[i + freeBlocksInMemoryMap] = thisShort;
        }
    }


    public short getFreeInodesSumInMemory() {
        return block[freeInodesSumInMemory];
    }

    public synchronized void setFreeInodesSumInMemory(short setNum){
        block[freeInodesSumInMemory] = setNum;
    }

    /**
     * @Description: 返回一个关于内存中空闲inode节点的位示图
     * @param: []
     * @return: boolean[][]
     * @auther: Lu Ning
     * @date: 2021/2/21 15:04
     */
    public boolean[][] getFreeInodesNumInMemory() {
        boolean[][] map = new boolean[2][16];
        int index = 0;
        //位图的储存类型为short，一个short16位对应16个inode。false为空闲
        for(int i = 0; i< freeInodesSumInDisk - freeInodesNumInMemory; i++){
            short thisShort = block[i+freeInodesNumInMemory];
            if(thisShort >= 0){                     //根据正负判断最高位
                map[i][15] = false;
            } else {
                map[i][15] = true;
                thisShort += 32768;
            }
            for(int j=0;j<15;j++){
                map[i][j] = (thisShort%2)==1;
                thisShort /= (short)2;
            }
        }
        return map;
    }

    /**
     * @Description: 更新内存中空闲inode位示图
     * @param: [map]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/21 15:06
     */
    public void setFreeInodesNumInMemory(boolean[][] map){
        for (int i = 0; i< freeInodesSumInDisk - freeInodesNumInMemory; i++) {
            short thisShort = 0;
            for (int j = 14; j >= 0; j--) {
                thisShort *= 2;
                thisShort += map[i][j] ? 1 : 0;
            }
            if (map[i][15]) {
                thisShort -= 32768;
            }
            block[i + freeInodesNumInMemory] = thisShort;
        }
    }


    public short getFreeInodesSumInDisk() {
//        System.out.println(block[freeInodesSumInDisk]);
        return block[freeInodesSumInDisk];

    }

    public synchronized void setFreeInodesSumInDisk(short setNum){
        block[freeInodesSumInDisk] = setNum;
    }


    /**
     * @Description: 返回一个关于硬盘中空闲inode节点的位示图
     * @param: []
     * @return: boolean[][]
     * @auther: Lu Ning
     * @date: 2021/2/3 17:44
     */
    public boolean[][] getFreeInodesNumInDisk() {
        boolean[][] map = new boolean[32][16];
        int index = 0;
        //位图的储存类型为short，一个short16位对应16个inode。false为空闲
        for(int i = 0; i< freeBlocksInDiskGroupLink - freeInodesNumInDisk; i++){
            short thisShort = block[i+freeInodesNumInDisk];

            if(thisShort >= 0){                     //根据正负判断最高位
                map[i][15] = false;
            } else {
                map[i][15] = true;
                thisShort += 32768;
            }
            for(int j=0;j<15;j++){
                map[i][j] = (thisShort%2)==1;
                thisShort /= (short)2;
            }
        }
        return map;
    }

    /**
     * @Description: 更新硬盘中空闲inode
     * @param: [map]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/3 17:49
     */
    public void setFreeInodesNumInDisk(boolean[][] map){
        for (int i = 0; i< freeBlocksInDiskGroupLink - freeInodesNumInDisk; i++) {
            short thisShort = 0;
            for (int j = 14; j >= 0; j--) {
                thisShort *= 2;
                thisShort += map[i][j] ? 1 : 0;
            }
            if (map[i][15]) {
                thisShort -= 32768;
            }
            block[i + freeInodesNumInDisk] = thisShort;
        }
    }
    public short getFreeBlocksInDiskGroupLink() {
        return block[freeBlocksInDiskGroupLink];
    }

    public synchronized void setFreeBlocksInDiskGroupLink(short setNum){
        block[freeBlocksInDiskGroupLink] = setNum;
    }

    public short getIfChanged() {
        return block[ifChanged];
    }

    public synchronized void setIfChanged(short setNum){
        block[ifChanged] = setNum;
    }

    public short getFreeBlocksSumInDisk() {
        return block[freeBlocksSumInDisk];
    }

    public synchronized void setFreeBlocksSumInDisk(short setNum){
        block[freeBlocksSumInDisk] = setNum;
    }

    public short getRunTime() {
        return block[runTime];
    }

    public void setRunTime(short setTime){
        block[runTime] = setTime;
    }
}
