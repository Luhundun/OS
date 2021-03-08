package deviceManage;

import control.OS;
import workManage.PV;
import workManage.Process;
import workManage.Queues;

/**
 * @ClassName: KeyBoardThread
 * @Description:
 * @Author: luning
 * @Date: 2021/3/3 15:52
 * @Version: v1.0
 */
public class KeyBoardThread extends Thread {
    public static boolean ifKeyboardWork = false; //表示键盘输入线程状态，false为空闲，true为忙碌
    public static short lastTime= 0;
//    private static Process usingProcess = null;    //正在等待键盘结果的线程
    public void run() {
        while(true) {
            OS.baseTimerLock.lock();//请求锁
            try {
                OS.baseTimerCondition.await();        //等到时钟进程发出时钟中断，再开始执行下面操作
                doWhatKeyBoardDoEverySecond();                   //执行每秒此线程被唤醒后该执行的程序
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally{
                OS.baseTimerLock.unlock();//释放锁
            }

        }
    }

    /**
     * @Description: 根据不同的状态，执行每次此线程被唤醒后该执行的程序
     * @throws
     */
    private void doWhatKeyBoardDoEverySecond() throws InterruptedException {
        //模拟键盘需要3秒时间来回应
        if(ifKeyboardWork && OS.getTime()-lastTime==OS.KEYBOARD.getDeviceDealTime()){
            //V操作释放资源
            Process usingProcess = Queues.blockedQueue[PV.keyboard.getBlockedQueueIndex()].get(0);
            PV.VKeyboard(usingProcess);

            //检查阻塞队列是否还有其他进程排队
            if(Queues.blockedQueue[PV.keyboard.getBlockedQueueIndex()].size() == 0){
                ifKeyboardWork = false;
            }else {
                lastTime = (short) OS.getTime();
            }
        }

    }
}
