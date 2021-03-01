package workManage;


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
    public static LinkedList<Job> jobReadyQueue = new LinkedList<>();                   //后备作业队列

    public static LinkedList<Process> readyQueue = new LinkedList<>();                  //就绪队列
    public static LinkedList<Process> hangUpReadyQueue = new LinkedList<>();            //挂起就绪队列
    public static LinkedList<Process> hangUpBlockedQueue = new LinkedList<>();          //挂起等待队列
    public static LinkedList[] blockedQueue = new LinkedList[8];                        //不同原因的阻塞队列

    /**
     * @Description: 开机时初始化阻塞队列
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/1 10:06
     */
    public static void initQueues(){
        for(int i=0;i<8;i++){
            blockedQueue[i] = new LinkedList<Process>();
        }
    }
}
