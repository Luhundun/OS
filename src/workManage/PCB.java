package workManage;

import control.OS;

/**
 * @ClassName: PCB
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/1/28 0:25
 * @Version: v1.0
 */
public class PCB implements Comparable<PCB>{
    private short pid;          //进程编号

    private short processPriority;    //进程优先数
    private short indexInMemory;      //进程在内存位置，如果不在则为负数
    private short virtualMemoryInDisk;//进程在外存的映像的ino
    private short inTimes;            //进程创建时间
    private short runTimes;           //进程运行时间
    private short turnTimes;          //进程周转时间
    private short timeSliceLeft;      //当前进程在cpu运行的剩余时间片，若非运行态则为0
    private short psw;                //进程状态 0为未知 1为运行 2为就绪 3为阻塞 4为中止 5为挂起就绪 6为挂起等待
    private short pc;    	            //程序计数器信息，记录下一条指令地址
    private short ir;                 //指令计数器信息，记录当前执行的指令类型d
    private short r0;                 //普通寄存器
    private short r1;                 //普通寄存器
    private short r2;                 //普通寄存器
    private short r3;                 //普通寄存器

    public static PCB[] pcbPool;        //PCB池
    public static short pcbNumberIndex;         //出现过的PCB数

    public PCB(short processPriority){
        this.pid = pcbNumberIndex++;
        this.processPriority = processPriority;
        this.inTimes = (short) OS.getTime();
        this.runTimes = 0;
        this.turnTimes = 0;
        this.timeSliceLeft = 0;
        this.psw = 0;
        this.pc = 0;
        this.ir = 0;
        this.r0 = 0;
        this.r1 = 0;
        this.r2 = 0;
        this.r3 = 0;
    }


    /**
     * @Description: 重写比较接口，用于队列中PCB的排序
     * @param: [o]
     * @return: int
     * @auther: Lu Ning
     * @date: 2021/1/28 0:30
     */
    @Override
    public int compareTo(PCB o) {
        return Integer.compare(this.processPriority, o.processPriority);
    }

    /**
     * @Description: 开机时初始化PCB
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/1 12:40
     */
    public static void initPCBManagment(){
       pcbPool = new PCB[32];
       pcbNumberIndex = 0;
    }
    
    /**
     * @Description: 判断PCB池是否满 
     * @param: []
     * @return: boolean
     * @auther: Lu Ning
     * @date: 2021/3/1 11:02
     */
    public static boolean pcbIsFull(){
        for(int i=0 ;i<32 ;i++){
            if(pcbPool[i] == null){
                return false;
            }
        }
        return true;
    }
}
