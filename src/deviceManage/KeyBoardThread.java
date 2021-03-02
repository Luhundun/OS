package deviceManage;

import control.OS;
import workManage.Primitives;
import workManage.Process;

/**
 * @ClassName: KeyBoardThread
 * @Description:
 * @Author: luning
 * @Date: 2021/3/3 15:52
 * @Version: v1.0
 */
public class KeyBoardThread extends Thread {
    private static boolean ifKeyboardWork = false; //表示键盘输入线程状态，false为空闲，true为忙碌
    private static Process usingProcess = null;    //正在等待键盘结果的线程
    private static int lastUseTime = 0;            //用来计数等待的线程已经等了多久
    public void run() {
        while(true) {
            OS.baseTimerLock.lock();//请求锁
            try {
                OS.baseTimerCondition.await();        //等到时钟进程发出时钟中断，再开始执行下面操作
//                doWhatKeyBoardDoEverySecond();                   //执行每秒此线程被唤醒后该执行的程序
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
//    private void doWhatKeyBoardDoEverySecond() {
////        OperatingSystemGUI.textList2.setText("");  //每秒清空GUI显示
//        if(ifKeyboardWork && OS.getTime()-lastUseTime == 21) {   //键盘中断需要4秒，如果阻塞队列为空，运行指令那一秒也会计算，因此此处差应为5秒
////            usingProcess.awakeProcess();   //系统调用结束，唤醒此进程
//            Primitives.awake(usingProcess);
//            usingProcess.interruptPlusPCAndCheckIfNeedToCancelTheProcess();   //此进程的pc指向下一条
//            usingProcess = PCB.findProcessWithPCB(PCB.pollBlockedQueue1());  //调入阻塞队列其他进程，如果有返回队头，如果没有返回空地址
//            if(usingProcess != null)                 //如果阻塞队列不空队头得到键盘，空则空闲
//                lastUseTime = CPU.getTime()-1;       //1秒作为阻塞队列后续进程获得键盘的补偿
//            else
//                ifKeyboardWork = false;
//        }
//        if(!ifKeyboardWork){                    //根据键盘的状态输出信息
//            OperatingSystemGUI.textArea1.append("键盘状态：无进程请求" + "\n");
//        }
//        else {
//            OperatingSystemGUI.textArea1.append("键盘状态：进程" + usingProcess.getID() + "请求，阻塞队列还有" + PCB.getBlockedQueue1Length() + "个进程:");
//            PCB.showBlockedQueue1Ids();
//        }
//    }{
}
