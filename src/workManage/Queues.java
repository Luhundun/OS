package workManage;


import control.GUI;
import control.OS;
import fileManage.File;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @ClassName: Queues
 * @Description: 各种需要用到的队列，都在这个类中统一管理
 * @Author: Lu Ning
 * @Date: 2021/1/28 1:01
 * @Version: v1.0
 */
public class Queues {
    public static LinkedList<JCB> jobReadyQueue = new LinkedList<>();                   //后备作业队列

    public static LinkedList<Process> readyQueue = new LinkedList<>();                  //就绪队列
    public static LinkedList<Process> hangUpReadyQueue = new LinkedList<>();            //挂起就绪队列
    public static LinkedList<Process> hangUpBlockedQueue = new LinkedList<>();          //挂起等待队列
    public static LinkedList<Process>[] blockedQueue = new LinkedList[8];                        //不同原因的阻塞队列

    /**
     * @Description: 开机时初始化阻塞队列
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/1 10:06
     */
    public static void initQueues() throws Exception {
        for(int i=0;i<8;i++){
            blockedQueue[i] = new LinkedList<Process>();
        }
    }

    /**
     * @Description: 根据优先级对队列进行重排序
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/2 20:25
     */
    public void resortQueue(){

    }

    /**
     * @Description: 在GUI显示队列详细信息
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/2 15:08
     */
    public static void showQueuesInformation(){
        GUI.readyQueue.setListData(readyQueue.toArray());
        GUI.hangupBlockQueue.setListData(hangUpBlockedQueue.toArray());
        GUI.hangupReadyQueue.setListData(hangUpReadyQueue.toArray());
//        GUI.initQueue.setListData(jobReadyQueue.toArray());
        GUI.blockedQueue1.setListData(blockedQueue[0].toArray());
        GUI.blockedQueue2.setListData(blockedQueue[1].toArray());
        GUI.blockedQueue3.setListData(blockedQueue[2].toArray());
        GUI.blockedQueue4.setListData(blockedQueue[3].toArray());
//        GUI.blockedQueue5.setListData(blockedQueue[4].toArray());
//        GUI.blockedQueue6.setListData(blockedQueue[5].toArray());
//        GUI.blockedQueue7.setListData(blockedQueue[6].toArray());
//        GUI.blockedQueue8.setListData(blockedQueue[7].toArray());
        GUI.jobReadyQueue.setListData(jobReadyQueue.toArray());
    }

    /**
     * @Description: 加入一个进程队列的静态方法
     * @param: [queue, process]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 00:27
     */
    public static void joinAQueue(LinkedList<Process> queue, Process process){
        queue.offer(process);
        process.getPcb().setInQueueTime((short) OS.getTime());
    }

    public static Process pollFromAQueue(LinkedList<Process> queue){
        return queue.poll();
    }
}
