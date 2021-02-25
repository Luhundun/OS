package hardware;

import fileManage.Inode;
import sun.text.normalizer.UTF16;

import java.math.BigInteger;

/**
 * @ClassName: block
 * @Description: 模拟一个512B的盘块
 * @Author: Lu Ning
 * @Date: 2021/1/25 19:45
 * @Version: v1.0
 */
public class Block {
    protected short[] block;                      //一个物理块的内容,512B
    protected int blockUsedBytes;                 //这个物理块使用的空间
    protected short dno;                          //这个物理块的位置


    /**
     * @Description: 构造一个物理块
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/1/27 16:28
     */
    public Block(){
        block = new short[256];
        blockUsedBytes = 0;
        dno = -1;
    }


    /**
     * @Description: 初始化硬盘的物理块所用的构造函数，确定每个块在硬盘的位置
     * @param: [dno]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/18 16:28
     */
    public Block(int cylinder,int trace,int sector){
        block = new short[256];
        blockUsedBytes = 0;
        dno = (short) (cylinder*32*64 + trace*64 + sector);
    }

    public Block(Block old){
        this.block = new short[256];
        for (int i=0;i<256;i++){
            this.block[i] = old.block[i];
        }
        this.blockUsedBytes = old.blockUsedBytes;
        this.dno = 0;
    }


    /**
     * @Description: 将当前物理块内的数据以16进制形式输出，每16位为一个单位
     * @param: []
     * @return: java.lang.String
     * @auther: Lu Ning
     * @date: 2021/1/29 17:58
     */
    public String showInFile(){
        StringBuffer buffer = new StringBuffer();
        for(short e:block){
            String temp = Integer.toHexString(Short.toUnsignedInt(e));
            while (temp.length()<4){
                temp = '0' + temp;
            }
            buffer.append(temp);
            buffer.append(' ');
        }
        String tempDno = Integer.toHexString(Short.toUnsignedInt(dno));         //末尾存放磁盘号
        while (tempDno.length()<4){
            tempDno = '0' + tempDno;
        }
        buffer.append(tempDno);
        buffer.append(' ');

        String temp = Integer.toHexString(blockUsedBytes);         //末尾存放已使用字节数
        while (temp.length()<4){
            temp = '0' + temp;
        }
        buffer.append(temp);

        return buffer.toString();
    }

    /**
     * @Description: 将单个物理块文件加载到操作系统的模拟硬盘
     * @param: [context]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/29 19:56
     */
    public void loadBlock(String context){
        int i;
        context = context.replaceAll(" ","");
        for(i=0;i<256;i++){
            BigInteger bi = new BigInteger(context.substring(4*i,4*(i+1)),16);
            block[i] = bi.shortValue();
        }
        BigInteger bi = new BigInteger(context.substring(4*i,4*(i+1)),16);
        dno = bi.shortValue();
        bi = new BigInteger(context.substring(4*(i+1)),16);
        blockUsedBytes = bi.intValue();


    }

    /**
     * @Description: 从此块写一个字,只有空闲处才可调用此函数
     * @param: [context, index]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/30 0:05
     */
    public void writeAWord(short context, int index){
        block[index] = context;
    }

    /**
     * @Description: 从此块读取一个字
     * @param: [index]
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/1/30 0:07
     */
    public short readAWord(int index){
        return block[index];
    }

    /**
     * @Description: 写入一个Inode
     * @param: [inode, ino] ino为这个inode在这一块的序号，范围为0-8
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/2/2 14:00
     */
    public synchronized void writeInode(Inode inode, int ino)throws Exception{
        if(ino<0 || ino>7){
            throw new Exception("错误的inode存储位置");
        }
        int startIndex = ino * 32;
        block[startIndex + 0] = inode.inodeNum;
        block[startIndex + 1] = inode.inodeCount;
        block[startIndex + 2] = inode.inodeSaveMode;
        block[startIndex + 3] = inode.inodeLink;
        block[startIndex + 4] = inode.fileType;
        block[startIndex + 5] = inode.fileSize;
        block[startIndex + 6] = inode.fileOwner;
        block[startIndex + 7] = inode.fileDeviceNumber;
        block[startIndex + 8] = inode.getFileCreateTime();
        block[startIndex + 9] = inode.getFileAlterTime();
        block[startIndex + 10] = inode.getInodeIndexInMemory();

        for (int i=0;i<8;i++){
            block[startIndex + 11 + i] = inode.fileAddressDirect[i];
        }
        block[startIndex + 19] = inode.fileAddressIndirect1;
        block[startIndex + 20] = inode.fileAddressIndirect2;
    }

    /**
     * @Description: 获取当前块使用的字节数
     * @param: []
     * @return: int
     * @auther: Lu Ning
     * @date: 2021/2/18 19:37
     */
    public int getBlockUsedBytes() {
        return blockUsedBytes;
    }

    /**
     * @Description: 修改当前块所用字节数
     * @param: [blockUsedBytes]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/18 19:37
     */
    public void setBlockUsedBytes(int blockUsedBytes) {
        this.blockUsedBytes = blockUsedBytes;
    }

    /**
     * @Description: 将物理块所有字设置成0000,
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/18 19:39
     */
    public void setBlock00(){
       for (int i=0;i<256;i++){
           block[i] = 0;
       }
       blockUsedBytes = 0;
    }

    /**
     * @Description: 将物理块所有字设置成1111
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/18 19:40
     */
    public void setBlockFF(){
        for (int i=0;i<256;i++){
            block[i] = -1;
        }
        blockUsedBytes = 512;
    }

    /**
     * @Description: 用于交换硬盘和内存的物理块
     * @param: []
     * @return: hardware.Block
     * @auther: Lu Ning
     * @date: 2021/2/21 14:35
     */
    public Block cloneABlock(short newDno){
        Block newBlock = new Block(this);
        newBlock.setDno(newDno);
        return newBlock;
    }

    /**
     * @Description: 获取物理块在硬盘的位置
     * @param: []
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/2/18 19:38
     */
    public short getDno() {
        return dno;
    }

    /**
     * @Description: 将一个short类型的字转化成UTF16编码
     * @param: [sh]
     * @return: java.lang.String
     * @auther: Lu Ning
     * @date: 2021/2/23 0:34
     */
    public static String convertShortToUTF16(short sh){
        return UTF16.valueOf(Short.toUnsignedInt((sh)));
    }

    /**
     * @Description: 将一个UTF16的字转化成short类型
     * @param: [str]
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/2/23 0:38
     */
    public static short convertUTF16ToShort(String str){
        BigInteger bigInteger = new BigInteger(String.valueOf(UTF16.charAt(str,0)));
        return bigInteger.shortValue();
    }


    public void setDno(short dno) {
        this.dno = dno;
    }



}
