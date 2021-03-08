package workManage;

import control.GUI;
import control.OS;

/**
 * @ClassName: PCB
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/1/28 0:25
 * @Version: v1.0
 */
public class PCB implements Comparable<PCB>{

    private short pid;                //进程编号
    private short processPriority;    //进程优先数
    private short indexInMemory;      //进程在内存位置，如果不在则为负数
    private short virtualMemoryInDisk;//进程在外存的映像的ino
    private short inTimes;            //进程创建时间
    private short runTimes;           //进程运行时间
    private short turnTimes;          //进程周转时间
    private short timeSliceLeft;      //当前进程在cpu运行的剩余时间片，若非运行态则为0
    private short directoryIno;         //当前进程所在目录
    private short processState;        //进程状态 0为未创建 1为就绪 2为运行 3挂起就绪 4为挂起等待 11-18为不同原因的阻塞
    private short inQueueTime;          //设置进入相应队列时间
    private short psw;                  //状态字寄存器
    private short pc;    	            //程序计数器信息，记录下一条指令地址
    private short ir;                 //指令计数器信息，记录当前执行的指令类型d
    private short r0;                 //通用寄存器r0
    private short r1;                 //通用寄存器r1
    private short r2;                 //通用寄存器r2
    private short r3;                 //通用寄存器r3

    public static PCB[] pcbPool;        //PCB池
    public static short pcbNumberIndex;         //出现过的PCB数


    /**
     * @Description: PCB
     * @param: [processPriority]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/3/1 23:56
     */
    public PCB(short processPriority, short inTime){
        this.pid = pcbNumberIndex++;
        this.processPriority = processPriority;
        this.inTimes = inTime;
        this.runTimes = 0;
        this.turnTimes = 0;
        this.timeSliceLeft = 0;
        this.indexInMemory = -1;
        this.processState = 1;
        this.psw = 0;
        this.pc = 0;
        this.ir = 0;
        this.r0 = 0;
        this.r1 = 0;
        this.r2 = 0;
        this.r3 = 0;
        this.directoryIno = 0;
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
       pcbPool = new PCB[OS.PCBPOOLSIZE];
       pcbNumberIndex = 1;
    }
    
    /**
     * @Description: 从PCB池获取空闲PCB，若未满返回下标，满了返回-1
     * @param: []
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/3/1 11:02
     */
    public synchronized static short getFreeFromPCBPool(){
        for(short i=0 ;i<OS.PCBPOOLSIZE ;i++){
            if(pcbPool[i] == null){
                return i;
            }
        }
        return -1;
    }

    /**
     * @Description: 从PCB池中找到PCB，如果不存在则返回-1
     * @param: []
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/3/4 23:01
     */
    public synchronized static short getPCBIndexIfInPool(PCB pcb){
        for(short i=0 ;i<OS.PCBPOOLSIZE ;i++){
            if(pcbPool[i] == pcb){
                return i;
            }
        }
        return -1;
    }

    /**
     * @Description: 在GUI展示PCB池
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/4 22:55
     */
    public static void showPCBPoll() {
        GUI.pcbPool.setListData(pcbPool);
    }


    @Override
    public String toString() {
        return String.valueOf(pid);
    }

    public short getPid() {
        return pid;
    }

    public void setPid(short pid) {
        this.pid = pid;
    }

    public short getProcessPriority() {
        return processPriority;
    }

    public void setProcessPriority(short processPriority) {
        this.processPriority = processPriority;
    }

    public short getIndexInMemory() {
        return indexInMemory;
    }

    public void setIndexInMemory(short indexInMemory) {
        this.indexInMemory = indexInMemory;
    }

    public short getVirtualMemoryInDisk() {
        return virtualMemoryInDisk;
    }

    public void setVirtualMemoryInDisk(short virtualMemoryInDisk) {
        this.virtualMemoryInDisk = virtualMemoryInDisk;
    }

    public short getInTimes() {
        return inTimes;
    }

    public void setInTimes(short inTimes) {
        this.inTimes = inTimes;
    }

    public short getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(short runTimes) {
        this.runTimes = runTimes;
    }

    public short getTurnTimes() {
        return turnTimes;
    }

    public void setTurnTimes(short turnTimes) {
        this.turnTimes = turnTimes;
    }

    public short getTimeSliceLeft() {
        return timeSliceLeft;
    }

    public void setTimeSliceLeft(short timeSliceLeft) {
        this.timeSliceLeft = timeSliceLeft;
    }

    public short getProcessState() {
        return processState;
    }

    public void setProcessState(short processState) {
        this.processState = processState;
    }

    public short getPsw() {
        return psw;
    }

    public void setPsw(short psw) {
        this.psw = psw;
    }

    public short getPc() {
        return pc;
    }

    public void setPc(short pc) {
        this.pc = pc;
    }

    public short getIr() {
        return ir;
    }

    public void setIr(short ir) {
        this.ir = ir;
    }

    public short getR0() {
        return r0;
    }

    public void setR0(short r0) {
        this.r0 = r0;
    }

    public short getR1() {
        return r1;
    }

    public void setR1(short r1) {
        this.r1 = r1;
    }

    public short getR2() {
        return r2;
    }

    public void setR2(short r2) {
        this.r2 = r2;
    }

    public short getR3() {
        return r3;
    }

    public void setR3(short r3) {
        this.r3 = r3;
    }

    public short getDirectoryIno() {
        return directoryIno;
    }

    public void setDirectoryIno(short directoryIno) {
        this.directoryIno = directoryIno;
    }

    public short getInQueueTime() {
        return inQueueTime;
    }

    public void setInQueueTime(short inQueueTime) {
        this.inQueueTime = inQueueTime;
    }

}
