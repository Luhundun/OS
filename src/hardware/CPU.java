package hardware;

import control.GUI;
import control.OS;
import workManage.Instruction;
import workManage.Process;

/**
 * @ClassName: cpu
 * @Description: 模拟裸机的cpu
 * @Author: Lu Ning
 * @Date: 2021/1/25 19:41
 * @Version: v1.0
 */
public class CPU {

    private static short ir;  //ir寄存器
    private static short pc;  //pc寄存器
    private static short psw;  //状态字寄存器
    private static short r0;  //普通寄存器
    private static short r1;  //普通寄存器
    private static short r2;  //普通寄存器
    private static short r3;  //普通寄存器

    private static boolean ifCpuWork = false;  // cpu是否工作
    private static boolean ifCpuCloseInterrupt = false;  //关中断标志位，为了简化模拟，可以当作false时cpu处于用户态，true时处于核心态
    public static Process workingProcess = null;

    public static void doInstruction() throws Exception {
        workingProcess.getPcb().setTimeSliceLeft((short) (workingProcess.getPcb().getTimeSliceLeft() - 1));
        workingProcess.getPcb().setRunTimes((short) (workingProcess.getPcb().getRunTimes() + 1));
        System.out.print(workingProcess.getPcb().getPid()+"号进程");

        //获取指令 执行指令 输出信息
        System.out.println(Instruction.executeInstruction(workingProcess.getNextInstruction()));
//        workingProcess.plusProcessRunTime();
//        workingProcess.useTimeSlice();
//        workingProcess.setIRNewInstructionState();  //确保每次执行指令时，当前指令都是最新的
//        ir = workingProcess.getIR();
//        OperatingSystemGUI.textField2.setText(String.valueOf(workingProcess.getID()));
//        OperatingSystemGUI.textField3.setText(String.valueOf(workingProcess.getPC()));
//        OperatingSystemGUI.textField4.setText(String.valueOf(workingProcess.getIR()));
//        OperatingSystemGUI.textArea1.append("CPU状态：用户态，正在执行进程" + workingProcess.getID() + "的" + workingProcess.getCurrentInstructionID() + "号指令，类型为" + ir +"\n");
//        if(ir == 0) {              //正常执行指令
//            CPU.setCpuWorkState(true);
//            OperatingSystemGUI.textField1.setText("用户态");
//            workingProcess.cpuPlusPCAndCheckIfNeedToCancelTheProcess();                 	  //进程PCB指向下一条指令。
//        }
//        else if(ir == 1 ) {                                                             	 //系统调用键盘
//            switchUserModeToKernelMode();     												//CPU用户态转化核心态
//            if(KeyBoard.getKeyBoardState())
//                workingProcess.blockProcess(); 											    //用阻塞原语加入对应的阻塞队列排队
//            else
//                KeyBoard.setKeyBoardWorkForAProcess(workingProcess);
//            switchKernelModeToUserMode();   												 //核心态转化为用户态
//            CPU.ifCpuWork = false;             												 //设置false更多为了强制用完剩余时间片，并且方便判断指令是否全部执行完
//        }else if(ir == 3) {       															 //系统调用显示器
//            switchUserModeToKernelMode();    												//CPU用户态转化核心态
//            if(Display.getDisplayState())
//                workingProcess.blockProcess();  											 //用阻塞原语加入对应的阻塞队列排队
//            else
//                Display.setDisplayWork(workingProcess);
//            switchKernelModeToUserMode();   												//核心态转化为用户态
//            CPU.ifCpuWork = false;
//        }
//        else if(ir == 2) {        														    //系统调用PV通信线程
//            if(PV.getPVState())
//                workingProcess.blockProcess();
//            else
//                PV.setPVWork(workingProcess);
//            CPU.ifCpuWork = false;
//        }
//        OperatingSystemGUI.textArea1.append("就绪队列有" + PCB.getReadyQueueLength() + "个进程:");
//        PCB.showReadyQueueIds();
    }


    public static boolean isCpuWork() {
        return ifCpuWork;
    }

    public static void setCpuWork(boolean ifCpuWork) {
        CPU.ifCpuWork = ifCpuWork;
    }

    public static boolean isCpuCloseInterrupt() {
        return ifCpuCloseInterrupt;
    }

    public static void setCpuCloseInterrupt(boolean ifCpuCloseInterrupt) {
        CPU.ifCpuCloseInterrupt = ifCpuCloseInterrupt;
    }

    /**
     * @Description: 进程上下文切换的内容
     * @param: [newRunProcess]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 00:15
     */
    public static void processContextSwitch(Process newRunProcess) {
        if(CPU.workingProcess != null){
            CPU.workingProcess.getPcb().setPc(CPU.getPc());
            CPU.workingProcess.getPcb().setIr(CPU.getIr());
            CPU.workingProcess.getPcb().setPsw(CPU.getPsw());
            CPU.workingProcess.getPcb().setR0(CPU.getR0());
            CPU.workingProcess.getPcb().setR1(CPU.getR1());
            CPU.workingProcess.getPcb().setR2(CPU.getR2());
            CPU.workingProcess.getPcb().setR3(CPU.getR3());
        }
        CPU.workingProcess = newRunProcess;
        CPU.switchUserModeToKernelMode();       //进程上下文切换是要在CPU核心态下实现的
        newRunProcess.getPcb().setProcessState((short) 1);
        CPU.workingProcess = newRunProcess;
        CPU.switchKernelModeToUserMode();
        CPU.setPc(newRunProcess.getPcb().getPc());
        CPU.setIr(newRunProcess.getPcb().getIr());
        CPU.setPsw(newRunProcess.getPcb().getPsw());
        CPU.setR0(newRunProcess.getPcb().getR0());
        CPU.setR1(newRunProcess.getPcb().getR1());
        CPU.setR2(newRunProcess.getPcb().getR2());
        CPU.setR3(newRunProcess.getPcb().getR3());

    }

    public static void switchUserModeToKernelMode() {
        CPU.ifCpuCloseInterrupt = true;  //关中断
//        workingProcess.inCoreStack(pc);    //模拟现场保护
//        workingProcess.inCoreStack(ir);
//        workingProcess.inCoreStack(psw);
//        OperatingSystemGUI.textField1.setText("内核态");
    }

    /**
     * @Description: CPU内核态转用户态
     * @throws
     */
    public static void switchKernelModeToUserMode() {     //CPU内核态转用户态
//        psw = workingProcess.outCoreStack();
//        ir = workingProcess.outCoreStack();     //模拟返回现场
//        pc = workingProcess.outCoreStack();
//        OperatingSystemGUI.textField1.setText("用户态");
        CPU.ifCpuCloseInterrupt = false;      //模拟开中断

    }


    /**
     * @Description: 在GUI展示CPU的信息
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 16:06
     */
    public static void showCPUInfo(){
        if(workingProcess != null){
            GUI.workingProcessNum.setText(String.valueOf(workingProcess.getPcb().getPid()));
            GUI.systemTime.setText(String.valueOf(OS.getTime()));
            GUI.IR.setText(String.valueOf(ir));
            GUI.PC.setText(String.valueOf(pc));
            GUI.PSW.setText(String.valueOf(psw));
            GUI.R0.setText(String.valueOf(r0));
            GUI.R1.setText(String.valueOf(r1));
            GUI.R2.setText(String.valueOf(r2));
            GUI.R3.setText(String.valueOf(r3));
            if(CPU.ifCpuCloseInterrupt){
                GUI.cpuState.setText("内核态");
            }else {
                GUI.cpuState.setText("用户态");
            }
        }else {
            GUI.systemTime.setText(String.valueOf(OS.getTime()));
            GUI.workingProcessNum.setText("null");
        }
        if(OS.chooseProcess != null){
            GUI.chosenProcess.setText(OS.chooseProcess.getPcb().toString());
        }

    }


    public static short getIr() {
        return ir;
    }

    public static void setIr(short ir) {
        CPU.ir = ir;
    }

    public static short getPc() {
        return pc;
    }

    public static void setPc(short pc) {
        CPU.pc = pc;
    }

    public static short getPsw() {
        return psw;
    }

    public static void setPsw(short psw) {
        CPU.psw = psw;
    }

    public static short getR0() {
        return r0;
    }

    public static void setR0(short r0) {
        CPU.r0 = r0;
    }

    public static short getR1() {
        return r1;
    }

    public static void setR1(short r1) {
        CPU.r1 = r1;
    }

    public static short getR2() {
        return r2;
    }

    public static void setR2(short r2) {
        CPU.r2 = r2;
    }

    public static short getR3() {
        return r3;
    }

    public static void setR3(short r3) {
        CPU.r3 = r3;
    }

}
