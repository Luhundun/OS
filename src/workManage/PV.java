package workManage;

import control.GUI;
import control.OS;
import deviceManage.DisplayThread;
import deviceManage.KeyBoardThread;
import deviceManage.PrinterThread;
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
//    public static PV printer;

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
        disk = new PV((short)0);
        keyboard = new PV((short) 1);
        display = new PV((short)2);
//        printer = new PV((short)3);
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
            GUI.outInfoArea.append("请求键盘成功,进程"+process.getPcb().getPid()+"获得资源\n");
        }else{
            System.out.println("申请通过,但进程"+process.getPcb().getPid()+"前有"+(-keyboard.value)+"个进程，已加入对应阻塞队列");
            GUI.outInfoArea.append("申请键盘通过,但进程"+process.getPcb().getPid()+"前有"+(-keyboard.value)+"个进程已请求，已加入对应阻塞队列\n");
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
        GUI.outInfoArea.append("进程"+process.getPcb().getPid()+"请求键盘服务结束，释放资源\n");

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
            GUI.outInfoArea.append("请求显示器成功,进程"+process.getPcb().getPid()+"获得资源\n");
        }else{
            System.out.println("申请通过,但进程"+process.getPcb().getPid()+"前有"+(-display.value)+"个进程，已加入对应阻塞队列");
            GUI.outInfoArea.append("申请显示器通过,但进程"+process.getPcb().getPid()+"前有"+(-keyboard.value)+"个进程已请求，已加入对应阻塞队列\n");

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
        GUI.outInfoArea.append("进程"+process.getPcb().getPid()+"请求显示器服务结束，释放资源\n");
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
            VisitDIskThread.ifRequestDisk = true;
            VisitDIskThread.lastTime = (short) OS.getTime();
            System.out.println("申请通过,进程"+process.getPcb().getPid()+"获得资源");
            GUI.outInfoArea.append("请求访问磁盘文件系统成功,进程"+process.getPcb().getPid()+"获得资源\n");
        }else{
            System.out.println("申请通过,但进程"+process.getPcb().getPid()+"前有"+(-disk.value)+"个进程，已加入对应阻塞队列");
            GUI.outInfoArea.append("申请磁盘通过,但进程"+process.getPcb().getPid()+"前有"+(-keyboard.value)+"个进程已请求，已加入对应阻塞队列\n");

        }
        CPU.switchKernelModeToUserMode();
        Primitives.block(process,disk.blockedQueueIndex);
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
        disk.value++;
        System.out.println("进程"+process.getPcb().getPid()+"释放资源");
        GUI.outInfoArea.append("进程"+process.getPcb().getPid()+"请求硬盘服务结束，释放资源\n");

        CPU.switchKernelModeToUserMode();
        Primitives.awake(process);
    }


//    /**
//     * @Description: P操作
//     * @param: []
//     * @return: void
//     * @auther: Lu Ning
//     * @date: 2021/3/3 15:31
//     */
//    public static void PPrinter(Process process){
//        CPU.switchUserModeToKernelMode();
//        printer.value--;
//        if(printer.value >= 0){
//            PrinterThread.ifPrinterWork = true;
//            PrinterThread.lastTime = (short) OS.getTime();
//            System.out.println("申请通过,进程"+process.getPcb().getPid()+"获得资源");
//        }else{
//            System.out.println("申请通过,但进程"+process.getPcb().getPid()+"前有"+(-printer.value)+"个进程，已加入对应阻塞队列");
//        }
//        CPU.switchKernelModeToUserMode();
//        Primitives.block(process,printer.blockedQueueIndex);
//    }
//
//    /**
//     * @Description: V操作
//     * @param: []
//     * @return: void
//     * @auther: Lu Ning
//     * @date: 2021/3/3 15:31
//     */
//    public static void VPrinter(Process process){
//        CPU.switchUserModeToKernelMode();
//        printer.value++;
//        System.out.println("进程"+process.getPcb().getPid()+"释放资源");
//        CPU.switchKernelModeToUserMode();
//        Primitives.awake(process);
//    }

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
