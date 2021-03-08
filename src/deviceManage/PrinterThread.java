package deviceManage;

import control.OS;
import workManage.PV;
import workManage.Process;
import workManage.Queues;

/**
 * @ClassName: PrinterThread
 * @Description:   与鼠标键盘不同，借助Spooling请求打印机服务不需要等待打印机相应。
 * @Author: luning
 * @Date: 2021/3/6 13:04
 * @Version: v1.0
 */
public class PrinterThread extends Thread {
    public static short lastTime= 0;
    //    private static Process usingProcess = null;    //正在等待键盘结果的线程
    public void run() {
        while(true) {
            OS.secondTimerLock.lock();//请求锁
            try {
                OS.secondTimerCondition.await();        //等到时钟进程发出时钟中断，再开始执行下面操作
                doWhatPrinterDoEverySecond();                   //执行每秒此线程被唤醒后该执行的程序
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                OS.secondTimerLock.unlock();//释放锁
            }

        }
    }

    /**
     * @Description: 根据不同的状态，执行每次此线程被唤醒后该执行的程序
     * @throws
     */
    private void doWhatPrinterDoEverySecond() throws Exception {
        //spooling井管理程序每秒从缓冲区把数据移到输入井，对输入井信息进行处理，处理后放入输出井
        Spooling.wellManageMent();
        if(lastTime>=OS.PRINTER.getDeviceDealTime()){
            //每5秒将输出井信息缓输出
            lastTime = 0;
            Spooling.slowWrite();
        }
        lastTime ++;
    }
}
