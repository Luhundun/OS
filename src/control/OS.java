package control;

import deviceManage.*;
import fileManage.*;
import hardware.CPU;
import hardware.Disk;
import hardware.Memory;
import memoryManage.MissingPageInterruptThread;
import memoryManage.PageTable;
import workManage.*;
import workManage.Process;

import java.util.ArrayList;
import java.util.Stack;
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
    public static ReentrantLock secondTimerLock =new ReentrantLock();           //控制0.2秒执行一条cpu指令的锁
    public static Condition secondTimerCondition = secondTimerLock.newCondition();

    public static final String virtualDiskRootPath = "./src/hardware/virtualDisk/";
    public static final String virtualDiskMirrorPath = "./src/hardware/DiskMirror.mir";
    public static final short TIMESLICELENGTH = 5;          //时间片长度
    public static final short PCBPOOLSIZE = 12;             //PCB池大小
    public static final short MAXNUMPROCESSINMEMORY = 8;   //进程在内存的最大数，分配内存块为16 - 16+4*max  max<=12
    public static final short HANGUPWAITTIME = 20;        //在就绪或者阻塞队列等待的时间超过这个值，就有可能被挂起
    public static final short HANGUPCHECKTIME = 25;        //每隔x隔时间片检查一次中级调度
    public static final short JOBCHECKTIME = 25;        //每隔x隔时间片检查作业请求情况
    public static final short BUFFERAREA = 15;       //缓冲区在内存的位置

    //默认加载的设备，创建参数依次是 设备名，设备号，设备处理一条指令所需秒数
    public static final Device DISK = new Device("硬盘",(short) 0, (short) 0);
    public static final Device KEYBOARD = new Device("键盘",(short) 1, (short) 4);
    public static final Device DISPLAY = new Device("显示器",(short) 2, (short) 2);
    public static final Device PRINTER = new Device("打印机",(short) 3, (short) 10);
    public static final short SPOOLINGWORKTABLEMAXSIZE = 20;  //井中作业表最大容量


    //模拟操作系统中的硬件
    public static CPU cpu;
    public static Disk disk = new Disk();
    public static Memory memory;

    private static short time = 0;
    private static SuperBlock superBlock = null;

    public static PageTable systemPageTable = null;

    public static File topFile;
    public static Process chooseProcess;
    public static Directory pathDirectory;
    public static Stack<String> path = new Stack<>();
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

            //初始化spooling的井
            Spooling.initSpooling();
            //启动对应进程
            new TimerThread().start();   //启动时钟进程
            new FlashGUIThread().start(); //启动刷新GUI进程
            new ProcessSchduleThread().start();
            new KeyBoardThread().start();
            new DisplayThread().start();
            new VisitDIskThread().start();
            new JobSchduleThread().start();
            new MissingPageInterruptThread().start();
            new VisitDIskThread().start();
            new PrinterThread().start();
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
    public static short getTime(){
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
