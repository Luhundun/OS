package workManage;

import control.OS;

/**
 * @ClassName: ProcessSchduleThread
 * @Description:
 * @Author: luning
 * @Date: 2021/3/2 18:49
 * @Version: v1.0
 */
public class ProcessSchduleThread extends Thread{
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
            //        roundRobinScheduling();       //执行时间片轮转算法的一系列操作
        }
    }
}
