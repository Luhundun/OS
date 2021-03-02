package workManage;

import fileManage.File;

import java.util.Collections;

/**
 * @ClassName: Primitives
 * @Description: 集中了各种原语的静态方法
 * @Author: luning
 * @Date: 2021/3/1 09:37
 * @Version: v1.0
 */
public class Primitives {


    /**
     * @Description: 创建进程原语
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/1 10:40
     */
    public synchronized static void init(File file) throws Exception {
        if(PCB.pcbIsFull()){
//            Queues.jobReadyQueue.add();
        }else {
            new Process(file);
        }
    }

    /**
     * @Description: 撤销进程原语
     * @param: [process]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/2 20:12
     */
    public synchronized static void destroy(Process process) throws Exception {
        //极大部分进程调用撤销原语在就绪态,不过不管它在什么状态都将其从相应队列删除
        switch (process.getPcb().getProcessState()){
            case 1:
                Queues.readyQueue.remove(process);
                Collections.sort(Queues.readyQueue);
                break;
            case 2:
                throw new Exception("正在运行的进程暂时不允许撤销！");
            case 3:
                break;
            default:
                if(process.getPcb().getProcessState() > 10 && process.getPcb().getProcessState() < 19){
                    //先从对应阻塞队列中移除，加入挂起阻塞队列
                    Queues.blockedQueue[process.getPcb().getProcessState() - 11].remove(process);
                }else {
                    throw new Exception("不合法的状态");
                }
        }

        //删除临时交换区
//        File.d


    }

    /**
     * @Description: 阻塞原语,一般运行态的进程才能被阻塞
     * @param: [process, reason]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/2 20:33
     */
    public synchronized static void block(Process process, short reason) {
        //从就绪队列移除
        Queues.readyQueue.remove(process);
        //根据阻塞原因加入不同阻塞队列
        process.getPcb().setProcessState((short) (10+reason));
        Queues.blockedQueue[reason - 1].add(process);
        Collections.sort(Queues.blockedQueue[reason - 1]);
        //进程上下文切换


    }

    /**
     * @Description: 唤醒原语，资源充足时由其他进程唤醒此进程
     * @param: [process]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/2 20:57
     */
    public synchronized static void awake(Process process){
        Queues.blockedQueue[process.getPcb().getProcessState() - 11].remove(process);
        process.getPcb().setProcessState((short) 1);
        Queues.readyQueue.add(process);
        Collections.sort(Queues.readyQueue);
        //进程上下文切换
    }


    /**
     * @Description: 挂起原语，将进程移入挂起队列，并且暂时保存到外存
     * @param: [process]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/2 21:01
     */
    public synchronized static void hangup(Process process) throws Exception {
        switch (process.getPcb().getProcessState()){
            case 1:
                //从等待队列移除，加入挂起等待队列
                Queues.hangUpReadyQueue.add(process);
                process.getPcb().setProcessState((short) 3);
                Collections.sort(Queues.hangUpReadyQueue);
                break;
            case 2:
                throw new Exception("正在运行的进程不能被挂起");
            case 3:
            case 4:
                throw new Exception("已挂起");
            default:
                if(process.getPcb().getProcessState() > 10 && process.getPcb().getProcessState() < 19){
                    //先从对应阻塞队列中移除，加入挂起阻塞队列
                    Queues.blockedQueue[process.getPcb().getProcessState() - 11].remove(process);
                    process.getPcb().setProcessState((short) 4);
                    Queues.hangUpBlockedQueue.add(process);
                    Collections.sort(Queues.hangUpBlockedQueue);
                }else {
                    throw new Exception("不合法的状态");
                }

        }
    }



}
