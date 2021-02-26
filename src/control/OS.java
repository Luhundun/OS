package control;

import fileManage.*;
import hardware.CPU;
import hardware.Disk;
import hardware.Memory;
import memoryManage.PageTable;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: system
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/1/25 19:30
 * @Version: v1.0
 */
public class OS {
    public static ReentrantLock timerLock=new ReentrantLock();           //重入锁，主要用于控制时钟进程与其他进程的通信
    public static Condition timerCondition =timerLock.newCondition();
    public static String virtualDiskRootPath = "./src/hardware/virtualDisk/";
    public static String virtualDiskMirrorPath = "./src/hardware/DiskMirror.mir";

    //模拟操作系统中的硬件
    public static CPU cpu = new CPU();
    public static Disk disk = new Disk();
    public static Memory memory;

    private static int time = 0;
    private static SuperBlock superBlock = null;

    public static PageTable systemPageTable = null;

    public static File topFile;
    public static Directory pathDirectory;
    public static ArrayList<String> path = new ArrayList<>();
    public static String selectedString;

    /**
     * @Description: 开机相关的操作
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/30 17:10
     */
    public static void runSystem() {
        try {

            //加载硬盘
            disk.loadDisk();

            //加载内存
            memory = new Memory();
            //加载超级块
            superBlock = new SuperBlock(false);
            disk.exchangeBlock(1,superBlock);


            //加载成组空闲块链
            GroupLink.runGroupLink();

            //初始化系统页表
            PageTable.initSystemPageTable();

            //打开根目录，加载根目录内文件
            Inode parentPathInode = new Inode(0,true);
            pathDirectory = new Directory(parentPathInode);
            Directory.getInDirectory(".");

            //启动对应进程
            new TimerThread().start();   //启动时钟进程
            new FlashGUIThread().start(); //启动刷新GUI进程
        } catch (Exception exception) {
            exception.printStackTrace();
        }



    }

    /**
     * @Description: 在硬盘上重装操作系统，仅执行一次
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/30 12:44
     */
    public static void reinstallOperatingSystem(){
        disk = new Disk();
        superBlock =new SuperBlock();               //初始化超级块
        GroupLink.installGroupLink();               //初始化成组链表
        try {
            new Directory(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        disk.saveDisk();

    }


    /**
     * @Description: 关机
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/29 17:14
     */
    public static void showDown(){
        superBlock.setRunTime((short) (superBlock.getRunTime() + OS.getTime()));
        OS.disk.saveDisk();
        System.exit(0);
    }


    /**
     * @Description: 获取当前时间
     * @param: []
     * @return: int
     * @auther: Lu Ning
     * @date: 2021/1/27 19:29
     */
    public static int getTime(){
        return time;
    }

    /**
     * @Description: 时钟中断引起时间+1
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/27 19:29
     */
    public static void passTime(){
        time++;
    }

    /**
     * @Description: 提供超级块的接口
     * @param: []
     * @return: fileManage.SuperBlock
     * @auther: Lu Ning
     * @date: 2021/2/3 17:58
     */
    public static SuperBlock getSuperBlock(){
        return superBlock;
    }

}
