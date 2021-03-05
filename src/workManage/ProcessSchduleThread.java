package workManage;

import control.OS;
import hardware.CPU;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @ClassName: ProcessSchduleThread
 * @Description:
 * @Author: luning
 * @Date: 2021/3/2 18:49
 * @Version: v1.0
 */
public class ProcessSchduleThread extends Thread{
    int num = 0;
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

                if(num == OS.HANGUPCHECKTIME){
                    hangupAndActiveSchedule();
                    num = 0;
                }
                num ++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description: 时间片轮转算法和时间片内CPU执行的指令
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 13:19
     */
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
                CPU.processContextSwitch(Queues.readyQueue.poll());   //进行进程上下文切换
                CPU.workingProcess.getPcb().setTimeSliceLeft(OS.TIMESLICELENGTH);   //重设时间片长度
                CPU.doInstruction();            //根据不同的指令执行对应的操作
            }
        }
        else {
            Process process = Queues.readyQueue.poll();      //如果CPU此刻不工作，就从就绪队列首位取元素,就绪队列为空会返回一个空地址
            if(process == null) {                     //如果就绪队列空，打印CPU空闲状态

            }
            else {                                 //就绪队列不空，进行进程上下文切换，再从就绪队列取出优先级最高的进程执行
                CPU.processContextSwitch(process);
                CPU.setCpuWork(true);       //检测到了还有指令没做完，CPU状态设为work
                CPU.workingProcess.getPcb().setTimeSliceLeft(OS.TIMESLICELENGTH);   //重设时间片长度
                CPU.doInstruction();            //根据不同的指令执行对应的操作
            }
        }
    }

    /**
     * @Description: 每隔一段时间进行一次的挂起和激活检测
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/5 17:39
     */
    public void hangupAndActiveSchedule() throws Exception {
        short processNumInMemory = Process.getProcessInMemorySum();

        if (processNumInMemory <= OS.MAXNUMPROCESSINMEMORY/2 ) {
            //如果当前内存中进程数少于等于规定的一半，激活一个被挂起时间最长的进程
            LinkedList<Process> tempList = new LinkedList<>();
            tempList.addAll(Queues.hangUpBlockedQueue);
            tempList.addAll(Queues.hangUpReadyQueue);
            Process thisProcess = null;
            short longestTime = 32767;
            for (Process process : tempList) {
                if (process.getPcb().getInTimes() < longestTime) {
                    longestTime = process.getPcb().getInTimes();
                    thisProcess = process;
                }
            }
            if (thisProcess != null) {
                Primitives.activate(thisProcess);
            }
        }else {
            //如果当前内存进程数大于规定的一半，且有进程在相应队列等待时间超过某个值（在OS.java中规定），挂起超过这个值的进程中优先级最低的
            Process thisProcess = null;
            for (Process process : Queues.readyQueue){
                if(OS.getTime() - process.getPcb().getInTimes() > OS.HANGUPWAITTIME && (thisProcess == null ||
                        thisProcess.getPcb().getProcessPriority() < process.getPcb().getProcessPriority()) ){
                    thisProcess = process;
                }
            }
            for(LinkedList<Process> list : Queues.blockedQueue){
                for(Process process : list){
                    if(OS.getTime() - process.getPcb().getInTimes() > OS.HANGUPWAITTIME && (thisProcess == null ||
                            thisProcess.getPcb().getProcessPriority() < process.getPcb().getProcessPriority()) ){
                        thisProcess = process;
                    }
                }
            }
            if(thisProcess != null){
                Primitives.hangup(thisProcess);
            }
        }
    }


}
