package fileManage;

import control.OS;
import hardware.Block;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @ClassName: Inode
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/1/28 0:59
 * @Version: v1.0
 * 设定1个inode 64B
 */
public class Inode {
    public short inodeNum;         //inode序号，可以由序号计算出在inode区的位置
    public short inodeCount;       //inode引用数,不在内存则为0
    public short inodeSaveMode;        //文件存储权限
    public short inodeLink;       //文件硬链接数
    public short fileType;          //文件类型 1普通文件 2目录文件 3特殊文件
    public short fileSize;         //文件数据大小（数据块数量）
    public short fileOwner;             //文件属主
    public short fileDeviceNumber;      //文件设备号

    public short inodeIndexInMemory;    //inode在内存的位置，不在内存则为0

    public short fileCreateTime;        //文件创建时间
    public short fileAlterTime;         //文件修改时间
    public short[] fileAddressDirect; //文件直接索引块8块  共8*512B=4KB
    public short fileAddressIndirect1; //文件一级间接索引块   可以存放256*512B=128KB
    public short fileAddressIndirect2; //文件二级间接索引块   可以存放256*128KB=32MB

    public static Inode[] activeInodeInMemoryTable = new Inode[32];  //内存活动inode表，其实质已经模拟在内存的物理块中，此处列出单纯为了展示
                                                        //其存放在内存的 9、10块

    /**
     * @Description: 在硬盘初始化Inode
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/16 16:09
     */
    public Inode() throws Exception {
        inodeNum = getFreeInodeNum();
        inodeCount = 0;
        inodeSaveMode = 7;
        inodeLink = 1;
        fileType = 1;
        fileSize = 1;
        fileAddressDirect = new short[8];
        fileCreateTime = (short) (OS.getSuperBlock().getRunTime() + OS.getTime());
        fileAlterTime = fileCreateTime;
        OS.disk.findBlockByDno((short) (inodeNum/8+2)).writeInode(this,inodeNum%8);
    }

    /**
     * @Description: 初始化特定条件下的的Inode
     * @param: [fileType]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/19 19:41
     */
    public Inode(short inodeSaveMode, short fileType, short fileSize) throws Exception {
        inodeNum = getFreeInodeNum();
        inodeCount = 0;
        this.inodeSaveMode = inodeSaveMode;
        inodeLink = 1;
        this.fileType = fileType;
        this.fileSize = fileSize;
        fileAddressDirect = new short[8];
        OS.disk.findBlockByDno((short) (inodeNum/8+2)).writeInode(this,inodeNum%8);
    }

    /**
     * @Description: 构造函数形式读取完全储存在硬盘的inode
     * @param: [ino, alreadyExit]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/24 1:43
     */
    public Inode(int ino, boolean alreadyExit){
        Block inodeBlock = OS.disk.findBlockByDno((short) (ino/8+2));
        int startIndex = ino % 8 * 32;
        inodeNum = inodeBlock.readAWord(startIndex);
        inodeCount = inodeBlock.readAWord(startIndex + 1);
        inodeSaveMode = inodeBlock.readAWord(startIndex + 2);
        inodeLink = inodeBlock.readAWord(startIndex + 3);
        fileType = inodeBlock.readAWord(startIndex + 4);
        fileSize = inodeBlock.readAWord(startIndex + 5);
        fileOwner = inodeBlock.readAWord(startIndex + 6);
        fileDeviceNumber = inodeBlock.readAWord(startIndex + 7);
        fileCreateTime = inodeBlock.readAWord(startIndex + 8);
        fileAlterTime = inodeBlock.readAWord(startIndex + 9);
        inodeIndexInMemory = inodeBlock.readAWord(startIndex + 10);
        fileAddressDirect = new short[8];
        for (int i=0;i<8;i++){
            fileAddressDirect[i] = inodeBlock.readAWord(startIndex + 11 + i);
        }
        fileAddressIndirect1= inodeBlock.readAWord(startIndex + 19);
        fileAddressIndirect2 = inodeBlock.readAWord(startIndex + 20);

    }


    /**
     * @Description: 找到硬盘里一个空闲的inode编号
     * @param: []
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/2/3 18:05
     */
    public short getFreeInodeNum() throws Exception{
        SuperBlock superBlock = OS.getSuperBlock();
        int i;
        if(superBlock.getFreeInodesSumInDisk() > 0){
            boolean[][] map = superBlock.getFreeInodesNumInDisk();
            for(i=0; i<512 ;i++) {
                if(!map[i/16][i%16]){
                    map[i/16][i%16] = true;
                    superBlock.setFreeInodesNumInDisk(map);
                    superBlock.setFreeInodesSumInDisk((short) (superBlock.getFreeInodesSumInDisk() - 1));
                    break;
                }
            }
        }else {
            throw new Exception("Inode在硬盘的数目不足");
        }
        return (short)(i);
    }

    /**
     * @Description: 初始化内存中的Inode管理，存放在内存的4-7块.这一段也可视为内存活动inode表
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/22 0:43
     */
    public static void initInodeInMemory(){
//        OS.memory.findBlockByNumber(4).setBlock00();
//        OS.memory.findBlockByNumber(5).setBlock00();
//        OS.memory.findBlockByNumber(6).setBlock00();
//        OS.memory.findBlockByNumber(7).setBlock00();
    }

    /**
     * @Description: 将要打开的文件inode从磁盘中转移到内存中
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/22 0:57
     */
    public void loadInodeToMemory() throws Exception{
        SuperBlock superBlock = OS.getSuperBlock();
        int i;
        if(superBlock.getFreeInodesSumInMemory() > 0){
            boolean[][] map = superBlock.getFreeInodesNumInMemory();
            for(i=0; i<32 ;i++) {
                if(!map[i/16][i%16]){
                    map[i/16][i%16] = true;
                    superBlock.setFreeInodesNumInMemory(map);
                    superBlock.setFreeInodesSumInMemory((short) (superBlock.getFreeInodesSumInMemory()  - 1));
                    break;
                }
            }
        }else {
            throw new Exception("内存中存储Inode位置不足");
        }
        this.setInodeIndexInMemory((short) i);
        Block inodeBlock = OS.memory.findBlockByNumber(i / 8 +4);
        inodeBlock.writeInode(this, i % 8);
        activeInodeInMemoryTable[i] = this;
    }

    /**
     * @Description: 将不活跃的inode从内存inode表中删除
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/22 1:22
     */
    public void deleteInodeInMemory() throws Exception {
        //在超级块中删去
        SuperBlock superBlock = OS.getSuperBlock();
        superBlock.setFreeInodesSumInMemory((short) (superBlock.getFreeInodesSumInMemory()  + 1));
        boolean[][] map = superBlock.getFreeInodesNumInMemory();
        short inodeIndex = this.getInodeIndexInMemory();
        map[inodeIndex/16][inodeIndex%16] = false;
        superBlock.setFreeInodesNumInMemory(map);
        activeInodeInMemoryTable[inodeIndex] = null;
        inodeCount = 0;
        saveInodeToDisk();
    }

    /**
     * @Description: 删除硬盘里的Inode
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/27 19:55
     */
    public void deleteInodeInDisk(){
        //在超级块中删去
        SuperBlock superBlock = OS.getSuperBlock();
        superBlock.setFreeInodesSumInDisk((short) (superBlock.getFreeInodesSumInDisk()  + 1));
        boolean[][] map = superBlock.getFreeInodesNumInDisk();
        short inodeIndex = this.inodeNum;
        map[inodeIndex/16][inodeIndex%16] = false;
        superBlock.setFreeInodesNumInDisk(map);
        //在物理硬盘中删去
        OS.disk.findBlockByDno((short) (inodeNum/8+2)).deleteInode(inodeNum%8);
    }

    /**
     * @Description: 定期将内存中已被更改的inode写回硬盘
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/22 2:41
     */
    public void saveInodeToDisk() throws Exception {
        OS.disk.findBlockByDno((short) (inodeNum/8+2)).writeInode(this,inodeNum%8);
    }


    /**
     * @Description: 展示内存中的inode
     * @param: []
     * @return: java.lang.Object[]
     * @auther: Lu Ning
     * @date: 2021/2/22 1:28
     */
    public static Object[] showInodeListInMemory(){
        ArrayList<Short> inodeList = new ArrayList<>();
        for (Inode inode : activeInodeInMemoryTable){
            if(inode != null){
                inodeList.add(inode.getInodeNum());
            }
        }
        return inodeList.toArray();
    }

    /**
     * @Description: 在内存中的inode根据ino号找到inode，找不到返回null
     * @param: []
     * @return: fileManage.Inode
     * @auther: Lu Ning
     * @date: 2021/2/22 1:46
     */
    public static Inode findInodeByNumberInMemory(short ino){
        for(Inode inode : activeInodeInMemoryTable){
            if(inode != null && inode.getInodeNum() == ino){
                return inode;
            }
        }
        return null;
    }


    /**
     * @Description: 根据ino来比较是否是同一个inode
     * @param: [o]
     * @return: boolean
     * @auther: Lu Ning
     * @date: 2021/2/24 1:37
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inode inode = (Inode) o;
        return getInodeNum() == inode.getInodeNum();
    }


    public short getInodeNum() {
        return inodeNum;
    }

    public void setInodeNum(short inodeNum) {
        this.inodeNum = inodeNum;
    }

    public short getInodeCount() {
        return inodeCount;
    }

    public void setInodeCount(short inodeCount) {
        this.inodeCount = inodeCount;
    }

    public short getInodeSaveMode() {
        return inodeSaveMode;
    }

    public void setInodeSaveMode(short inodeSaveMode) {
        this.inodeSaveMode = inodeSaveMode;
    }

    public short getInodeLink() {
        return inodeLink;
    }

    public void setInodeLink(short inodeLink) {
        this.inodeLink = inodeLink;
    }

    public short getFileType() {
        return fileType;
    }

    public void setFileType(short fileType) {
        this.fileType = fileType;
    }

    public short getFileSize() {
        return fileSize;
    }

    public void setFileSize(short fileSize) {
        this.fileSize = fileSize;
    }


    public short getFileOwner() {
        return fileOwner;
    }

    public void setFileOwner(short fileOwner) {
        this.fileOwner = fileOwner;
    }

    public short getFileDeviceNumber() {
        return fileDeviceNumber;
    }

    public void setFileDeviceNumber(short fileDeviceNumber) {
        this.fileDeviceNumber = fileDeviceNumber;
    }

    public short getFileCreateTime() {
        return fileCreateTime;
    }

    public void setFileCreateTime(short fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
    }

    public short getFileAlterTime() {
        return fileAlterTime;
    }

    public void setFileAlterTime(short fileAlterTime) {
        this.fileAlterTime = fileAlterTime;
    }


    public short getInodeIndexInMemory() {
        return inodeIndexInMemory;
    }

    public void setInodeIndexInMemory(short inodeIndexInMemory) {
        this.inodeIndexInMemory = inodeIndexInMemory;
    }

    public short[] getFileAddressDirect() {
        return fileAddressDirect;
    }

    public void setFileAddressDirect(short[] fileAddressDirect) {
        this.fileAddressDirect = fileAddressDirect;
    }


    public short getFileAddressIndirect1() {
        return fileAddressIndirect1;
    }

    public void setFileAddressIndirect1(short fileAddressIndirect1) {
        this.fileAddressIndirect1 = fileAddressIndirect1;
    }

    public short getFileAddressIndirect2() {
        return fileAddressIndirect2;
    }

    public void setFileAddressIndirect2(short fileAddressIndirect2) {
        this.fileAddressIndirect2 = fileAddressIndirect2;
    }




//    public void r(boolean[][] temp){
//        System.out.println("start");
//        for (boolean[]b:temp){
//            for (boolean e : b){
//                System.out.print(e);
//            }
//            System.out.println();
//        }
//    }

}

