package control;
/**
 * @Title: Timer.java
 * @author Lu Ning
 * @date 2020-10-24 9:52:03
 * @version V1.0
 */

import hardware.Disk;

/**
 * @Description 每秒发送一次中断信号的计时器线程
 *
 */
public class TimerThread extends Thread {
	private static boolean ifTimerSuspend = false;   //控制时钟是否运行

	/**
	 *  线程激活后，每秒发送一次中断信号
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

			OS.timerLock.lock();//请求锁
			try {
				thisTime = OS.getTime();
				GUI.timeLabel.setText("当前时间" + thisTime);
				GUI.allTimeLabel.setText("累计时间" + (thisTime+allTime));
//				GUI.textArea1.append("CPU时间：" + CPU.getTime() + "\n");
				OS.timerCondition.signalAll();//唤醒其他所有加锁线程

			}
			finally{
				OS.timerLock.unlock();//释放锁
			}

			try {
				Thread.sleep(1000);//进程休眠1秒，模拟时钟过去了1秒
//				GUI.textArea1.setCaretPosition(GUI.textArea1.getText().length());   //为了让GUI的文本框滚动起来
				OS.passTime();// 设置CPU过去了1秒

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