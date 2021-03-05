package workManage;

import control.OS;

import java.util.Collections;
import java.util.LinkedList;

/**
 * @ClassName: JobSchduleThread
 * @Description:
 * @Author: luning
 * @Date: 2021/3/5 13:16
 * @Version: v1.0
 */
public class JobSchduleThread extends Thread{
    int num = 0;
    /**
     * @Description: 作业调度线程的内容
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
                if(num == OS.JOBCHECKTIME){
                    checkJob();       //执行时间片轮转算法的一系列操作
                    num = 0;
                }
                num++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description: 检查是否允许后备队列进入
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/5 13:55
     */
    public static void checkJob() throws Exception {
        //当后备作业队列非空且有空闲PCB池时且内存充足时分配作业
        Collections.sort(Queues.jobReadyQueue);
        while (!Queues.jobReadyQueue.isEmpty() && PCB.getFreeFromPCBPool() != -1 && Process.getFreeIndex() != -1 ){
            JCB firstJob = Queues.jobReadyQueue.pollFirst();
            //作业队列按照进入时间排序了，有些作业事先规定了进入时间，如果最早的请求都没达到要求时间，那么没有后备作业可以被创建
            if(firstJob.getJobInTime() < OS.getTime()){
                break;
            }
            Primitives.init(firstJob.getJobFile(), firstJob.getJobPriority());
        }
    }

}
