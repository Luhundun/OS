package workManage;

import sun.awt.image.ImageWatched;

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
    public static LinkedList<Process> hangUpReadyQueue = new LinkedList<>();            //挂起队列

    public static ArrayList<LinkedList<Process>> blockedQueues = new ArrayList<>();     //因为不同条件阻塞的阻塞队列

}
