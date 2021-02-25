package fileManage;

import control.OS;
import hardware.Block;

import java.util.ArrayList;

/**
 * @ClassName: GroupLink
 * @Description: 成组空闲块链接，用于空闲块管理,每个块可以存放254个空闲块地址
 * @Author: Lu Ning
 * @Date: 2021/1/29 23:30
 * @Version: v1.0
 *
 * 系统开机时
 */
public class GroupLink {

    public static Block topGroupLinkBlock = null;

    /**
     * @Description: 仅在重装操作系统时使用，初始化成组空闲块链
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/30 12:44
     */
    public static void installGroupLink(){
        short freeBlockNum = 148;
        topGroupLinkBlock = OS.disk.findBlockByDno((short)66);
        for(short i=66;i<147;i++){                                //设定操作系统刚安装时磁盘66-147号块存放成组空闲块链，随着后续系统使用发生变化
            int k = 2;                                             //指向空闲块号的从2号字节开始
            Block tempBlock = OS.disk.findBlockByDno(i);
            if(i==146){
                tempBlock.writeAWord((short)0,1);
            }else{
                tempBlock.writeAWord((short) (i+1),1);  //除了最后一个块其它块的1号字指向下一块的块号
            }
            while (k<256&&freeBlockNum<20480){
                tempBlock.writeAWord(freeBlockNum++,k++);
            }
            tempBlock.writeAWord((short) (k-2),0);      //每个块的第0号字表示空闲块计数
            tempBlock.setBlockUsedBytes(512);
        }
        runGroupLink();
    }

    /**
     * @Description: 每次开机时找到成组空闲块链的第一块
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/18 15:47
     */
    public static void runGroupLink(){
        topGroupLinkBlock = OS.disk.findBlockByDno(OS.getSuperBlock().getFreeBlocksInDiskGroupLink());
    }

    /**
     * @Description: 释放一个有数据的数据块
     * @param: [dno]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/18 19:43
     */
    public static void releaseAFreeBlock(short dno){
        short index = topGroupLinkBlock.readAWord(0);
        if(index < 254){            //将被释放块标记为空闲，放在成组空闲块链顶的空闲部分
            topGroupLinkBlock.writeAWord(dno,255 - index);
            topGroupLinkBlock.writeAWord((short) (index + 1), 0);
        } else {                    //将这块作为新的成组空闲块链顶，指向旧的链顶
            Block thisBlock = OS.disk.findBlockByDno(dno);
            thisBlock.setBlockFF();
            thisBlock.writeAWord((short) 0, 0);
            thisBlock.writeAWord(topGroupLinkBlock.getDno(), 1);
            changeTopGroupLinkBlock(dno);
        }
        OS.disk.findBlockByDno(dno).setBlockUsedBytes(0);
        System.out.println("release" + dno);
    }

    /**
     * @Description: 获取一个空的数据块
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/18 15:45
     */
    public static short getAFreeBlock() throws Exception {
        short index = topGroupLinkBlock.readAWord(0);
        if(index > 0){            //取出一个空闲块号，记为-1，总数减少1
            topGroupLinkBlock.writeAWord((short) (index - 1),0);
            short dno = topGroupLinkBlock.readAWord(256 - index);
            topGroupLinkBlock.writeAWord((short) (-1),256 - index);
            OS.disk.findBlockByDno(dno).setBlockUsedBytes(512);
            return dno;

        } else {                //将当前链顶当做空闲物理块，将这个物理块连接的下一个块当做空闲块链顶
            short lastBlockDno = topGroupLinkBlock.getDno();
            short nextBlockDno = topGroupLinkBlock.readAWord(1);
            if(nextBlockDno == 0){
                throw new Exception("物理空间不足");
            }
            changeTopGroupLinkBlock(nextBlockDno);
            return lastBlockDno;

//            index = topGroupLinkBlock.readAWord(0);
//            topGroupLinkBlock.writeAWord((short) (index - 1),0);
//            short dno = topGroupLinkBlock.readAWord(index + 1);
//            topGroupLinkBlock.writeAWord((short) (-1),index + 1);
//            OS.disk.findBlockByDno(dno).setBlockUsedBytes(512);
//            return dno;
            //释放
        }
    }

    /**
     * @Description: 遇到当前空闲块链顶满了或者空了，需要更改链顶
     * @param: [dno]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/18 19:46
     */
    public static void changeTopGroupLinkBlock(short dno){
        OS.getSuperBlock().setFreeBlocksInDiskGroupLink(dno);
        topGroupLinkBlock = OS.disk.findBlockByDno(dno);
    }

    /**
     * @Description: 在GUI中展示承租空闲块链信息
     * @param: []
     * @return: short[]
     * @auther: Lu Ning
     * @date: 2021/2/19 12:34
     */
    public static Object[] showGroupLinkList(){
        ArrayList<Short> groupLinkList = new ArrayList<>();
        groupLinkList.add(topGroupLinkBlock.getDno());
        short blockNum = topGroupLinkBlock.readAWord(1);
        Block temp = topGroupLinkBlock;
        while (blockNum != 0){
            groupLinkList.add(blockNum);
            temp = OS.disk.findBlockByDno(blockNum);
            blockNum = temp.readAWord(1);
        }
        return groupLinkList.toArray();
    }
}
