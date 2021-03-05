package workManage;

import control.OS;
import deviceManage.DisplayThread;
import deviceManage.KeyBoardThread;
import hardware.CPU;

/**
 * @ClassName: PV
 * @Description: 关于PV的方法集合
 * @Author: luning
 * @Date: 2021/3/3 12:50
 * @Version: v1.0
 */
public class PV {


    private short value;
    private short blockedQueueIndex;
    public static PV keyboard;
    public static PV disk;
    public static PV display;

    public PV(short index){
        value = 1;
        blockedQueueIndex = index;
    }
    /**
     * @Description: 初始化PV信号量
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 12:55
     */
    public static void initPV(){
       keyboard = new PV((short) 0);
       disk = new PV((short)2);
       display = new PV((short)1);
    }

    /**
     * @Description: P操作
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 15:31
     */
    public static void PKeyboard(Process process){
        CPU.switchUserModeToKernelMode();
        keyboard.value--;
        if(keyboard.value >= 0){
            KeyBoardThread.ifKeyboardWork = true;
            KeyBoardThread.lastTime = (short) OS.getTime();
            System.out.println("申请通过,进程"+process.getPcb().getPid()+"获得资源");
        }else{
            System.out.println("申请通过,但进程"+process.getPcb().getPid()+"前有"+(-keyboard.value)+"个进程，已加入对应阻塞队列");
        }
        CPU.switchKernelModeToUserMode();
        Primitives.block(process,keyboard.blockedQueueIndex);
    }

    /**
     * @Description: V操作
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 15:31
     */
    public static void VKeyboard(Process process){
        CPU.switchUserModeToKernelMode();
        keyboard.value++;
        System.out.println("进程"+process.getPcb().getPid()+"释放资源");
        CPU.switchKernelModeToUserMode();
        Primitives.awake(process);
    }

    /**
     * @Description: P操作
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 15:31
     */
    public static void PDisplay(Process process){
        CPU.switchUserModeToKernelMode();
        display.value--;
        if(display.value >= 0){
            DisplayThread.ifDisplayWork = true;
            DisplayThread.lastTime = (short) OS.getTime();
            System.out.println("申请通过,进程"+process.getPcb().getPid()+"获得资源");
        }else{
            System.out.println("申请通过,但进程"+process.getPcb().getPid()+"前有"+(-display.value)+"个进程，已加入对应阻塞队列");
        }
        CPU.switchKernelModeToUserMode();
        Primitives.block(process,display.blockedQueueIndex);
    }

    /**
     * @Description: V操作
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 15:31
     */
    public static void VDisplay(Process process){
        CPU.switchUserModeToKernelMode();
        display.value++;
        System.out.println("进程"+process.getPcb().getPid()+"释放资源");
        CPU.switchKernelModeToUserMode();
        Primitives.awake(process);
    }
    
    /**
     * @Description: P操作
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 15:31
     */
    public static void PDisk(Process process){
        CPU.switchUserModeToKernelMode();
        disk.value--;
        if(disk.value >= 0){
            DisplayThread.ifDisplayWork = true;
            DisplayThread.lastTime = (short) OS.getTime();
            System.out.println("申请通过,进程"+process.getPcb().getPid()+"获得资源");
        }else{
            System.out.println("申请通过,但进程"+process.getPcb().getPid()+"前有"+(-display.value)+"个进程，已加入对应阻塞队列");
        }
        CPU.switchKernelModeToUserMode();
        Primitives.block(process,display.blockedQueueIndex);
    }

    /**
     * @Description: V操作
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 15:31
     */
    public static void VDisk(Process process){
        CPU.switchUserModeToKernelMode();
        display.value++;
        System.out.println("进程"+process.getPcb().getPid()+"释放资源");
        CPU.switchKernelModeToUserMode();
        Primitives.awake(process);
    }


    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public short getBlockedQueueIndex() {
        return blockedQueueIndex;
    }

    public void setBlockedQueueIndex(short blockedQueueIndex) {
        this.blockedQueueIndex = blockedQueueIndex;
    }
}
