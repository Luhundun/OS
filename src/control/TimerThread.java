package control;
/**
 * @Title: Timer.java
 * @author Lu Ning
 * @date 2020-10-24 9:52:03
 * @version V1.0
 */

/**
 * @Description 每秒发送一次中断信号的计时器线程
 *
 */
public class TimerThread extends Thread {
	private static boolean ifTimerSuspend = false;   //控制时钟是否运行
	private static int count = 0;

	/**
	 *  线程激活后，每0.2秒发送一次时钟中断信号，其中每5次中断信号发送1次刷新GUI信号
	 */
	public void run() {
		int thisTime;
		int allTime = OS.getSuperBlock().getRunTime();
		while(true) {
			if(ifTimerSuspend){    //如果被GUI的按钮手动暂停了，时钟不发送信号，每一秒检查是否还在被暂停
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}

			OS.secondTimerLock.lock();
			OS.baseTimerLock.lock();//请求锁
			try {
				System.out.println("Time--"+OS.getTime());
				OS.baseTimerCondition.signalAll();//唤醒其他所有加基础锁线程
				if(count == 5){
					OS.secondTimerCondition.signalAll(); //唤醒秒锁
				}
			}
			finally{
				OS.baseTimerLock.unlock();//释放锁
				OS.secondTimerLock.unlock();
			}
			try {
				if(count ==5){
					OS.passTime();// 设置CPU过去了1秒
					thisTime = OS.getTime();
					GUI.timeLabel.setText("当前时间" + thisTime);
					GUI.allTimeLabel.setText("累计时间" + (thisTime+allTime));

					count = 0;
				}
				Thread.sleep(200);//进程休眠0.2秒，模拟时钟过去了0.2秒
				count++;
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * @return the ifTimerSuspend
	 */
	public static boolean isIfTimerSuspend() {
		return ifTimerSuspend;
	}


	/**
	 * @param ifTimerSuspend the ifTimerSuspend to set
	 */
	public static void setIfTimerSuspend(boolean ifTimerSuspend) {
		TimerThread.ifTimerSuspend = ifTimerSuspend;
	}

}