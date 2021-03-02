package control;

import fileManage.*;
import hardware.CPU;
import hardware.Disk;
import hardware.Memory;
import memoryManage.PageTable;
import workManage.PCB;
import workManage.PV;
import workManage.Process;
import workManage.ProcessSchduleThread;
import workManage.Queues;

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
    //重入锁，主要用于控制时钟进程与其他进程的通信
    public static ReentrantLock baseTimerLock =new ReentrantLock();           //控制0.2秒执行一条cpu指令的锁
    public static Condition baseTimerCondition = baseTimerLock.newCondition();
    public static ReentrantLock guiFlashTimerLock =new ReentrantLock();           //控制0.2秒执行一条cpu指令的锁
    public static Condition guiFlashTimerCondition = guiFlashTimerLock.newCondition();

    public static final String virtualDiskRootPath = "./src/hardware/virtualDisk/";
    public static final String virtualDiskMirrorPath = "./src/hardware/DiskMirror.mir";
    public static final short timeSliceLength = 5;      //时间片长度
    public static final int pcbPoolSize = 32;



    //模拟操作系统中的硬件
    public static CPU cpu;
    public static Disk disk = new Disk();
    public static Memory memory;

    private static int time = 0;
    private static SuperBlock superBlock = null;

    public static PageTable systemPageTable = null;

    public static File topFile;
    public static Process chooseProcess;
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

            //加载超级块
            superBlock = new SuperBlock(false);
            disk.exchangeBlock(1,superBlock);

            //加载内存
            memory = new Memory();

            //加载硬盘上成组空闲块链，并将链顶置入内存指定块
            GroupLink.runGroupLink();

            //初始化信号量
            PV.initPV();

            //初始化PCB池
            PCB.initPCBManagment();

            //初始化进程队列
            Queues.initQueues();
            Process.initProcess();

            //初始化系统页表
            PageTable.initSystemPageTable();

            //打开根目录，加载根目录内文件inode
            Inode parentPathInode = new Inode(0,true);
            pathDirectory = new Directory(parentPathInode);
            Directory.getInDirectory(".");

            //启动对应进程
            new TimerThread().start();   //启动时钟进程
            new FlashGUIThread().start(); //启动刷新GUI进程
            new ProcessSchduleThread().start();
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
     * @Description: 保存操作系统镜像文件
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/28 00:12
     */
    public static void saveOSMirror() throws Exception {
        //保存在内存中的inode
       for(Inode inode : Inode.activeInodeInMemoryTable){
           if(inode != null){
               inode.deleteInodeInMemory();
           }
       }
       OS.disk.saveDisk();
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
