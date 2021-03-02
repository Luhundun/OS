package workManage;

import control.OS;
import hardware.CPU;
import java.util.Collections;

/**
 * @ClassName: ProcessSchduleThread
 * @Description:
 * @Author: luning
 * @Date: 2021/3/2 18:49
 * @Version: v1.0
 */
public class ProcessSchduleThread extends Thread{

    /**
     * @Description: 进程调度线程的内容
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/2 23:59
     */
    public void run() {
        while (true) {
            OS.baseTimerLock.lock();//请求锁
            try {
                OS.baseTimerCondition.await();  //唤醒所有等待线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                OS.baseTimerLock.unlock();//释放锁
            }
            try {
                roundRobinScheduling();       //执行时间片轮转算法的一系列操作
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void roundRobinScheduling() throws Exception {
        Collections.sort(Queues.readyQueue);  //按优先级大小对就绪队列进行重新排队
        if(CPU.isCpuWork()) {
            if(CPU.workingProcess.getPcb().getTimeSliceLeft() > 0) {   //如果正在运行的进程时间片还有剩余，那么一个时钟中断周期内此进程继续运行
                CPU.doInstruction();             //根据不同的指令执行对应的操作
            }
            else {                                  //时间片到，将此进程移到就绪队列排队，进行进程上下文切换，再从就绪队列取出优先级最高的进程执行
                CPU.workingProcess.getPcb().setProcessState((short) 1);
                CPU.workingProcess.getPcb().setInQueueTime((short) OS.getTime());
                Queues.readyQueue.add(CPU.workingProcess);                                 //当前进程进入就绪队列
                Collections.sort(Queues.readyQueue);  //按优先级大小对就绪队列进行重新排队
                processContextSwitch(Queues.readyQueue.poll());   //进行进程上下文切换
                CPU.workingProcess.getPcb().setTimeSliceLeft(OS.timeSliceLength);   //重设时间片长度
                CPU.doInstruction();            //根据不同的指令执行对应的操作
            }
        }
        else {
            Process process = Queues.readyQueue.poll();      //如果CPU此刻不工作，就从就绪队列首位取元素,就绪队列为空会返回一个空地址
            if(process == null) {                     //如果就绪队列空，打印CPU空闲状态

            }
            else {                                 //就绪队列不空，进行进程上下文切换，再从就绪队列取出优先级最高的进程执行
                processContextSwitch(process);
                CPU.setCpuWork(true);       //检测到了还有指令没做完，CPU状态设为work
                CPU.workingProcess.getPcb().setTimeSliceLeft(OS.timeSliceLength);   //重设时间片长度
                CPU.doInstruction();            //根据不同的指令执行对应的操作
            }
        }
    }

    /**
     * @Description: 进程上下文切换的内容
     * @param: [newRunProcess]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 00:15
     */
    public void processContextSwitch(Process newRunProcess) {
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
}
