package workManage;

import control.OS;
import fileManage.File;
import fileManage.Inode;
import hardware.Block;
import hardware.CPU;
import memoryManage.PageTable;
import memoryManage.missingPageInterrupt;

import java.util.Collections;
import java.util.Queue;

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
    public synchronized static void init(File file, short priority) throws Exception {
        if(PCB.pcbIsFull()){
//            Queues.jobReadyQueue.add();
        }else {
            //创建进程
            Process newProcess = new Process(file, priority);
            //查看内存情况，如果没有空间直接挂起
            short index = Process.getFreeIndex();
            System.out.println(index);
            if(index >= 0){
                //加入就绪队列
                Queues.readyQueue.add(newProcess);
                Collections.sort(Queues.readyQueue);
                //加载至内存，
                Process.processInMemory[index] = newProcess;
                for(int i=0;i<4;i++){
                    newProcess.getBlocksInMemory()[i] = OS.memory.findBlockByNumber(16+4*index+i);
                }                //进程的第一块(交换区文件的最后一块)常驻内存，其他三块通过调度
                Block.cloneABlock(newProcess.getBlocksInMemory()[0],
                        newProcess.getTempFile().getDataBlockList().get(file.getfInode().getFileSize()));
                //交换区文件inode读入内存
                newProcess.getPcb().setIndexInMemory(index);
                //虚存第一块直接导入内存
                newProcess.getUserStack().push((short)0);
                //更新页表
//                missingPageInterrupt.loadNewPage((short) 0,(short) -1);
                newProcess.getPageTable().exchangeInPageTable((short) 0, (short) -1, (short) 1, newProcess);
                Block.cloneABlock(OS.memory.findBlockByNumber(newProcess.getPcb().getIndexInMemory()*4+16+1),
                                                                newProcess.getTempFile().getDataBlockList().get(0));

//                newProcess.getPcb().setPc((short) (256*(16+index*4+1)));


            }else {
                Queues.hangUpReadyQueue.add(newProcess);
                newProcess.getPcb().setProcessState((short) 3);
                Collections.sort(Queues.hangUpReadyQueue);
                newProcess.getUserStack().clear();
                newProcess.setPageTable(new PageTable());
            }
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
                Queues.hangUpReadyQueue.remove(process);
                break;
            case 4:
                Queues.hangUpBlockedQueue.remove(process);
                break;
            default:
                if(process.getPcb().getProcessState() > 10 && process.getPcb().getProcessState() < 19){
                    //先从对应阻塞队列中移除，加入挂起阻塞队列
                    Queues.blockedQueue[process.getPcb().getProcessState() - 11].remove(process);
                }else {
                    throw new Exception("不合法的状态");
                }
        }

        //从内存删除
        if (process.getPcb().getIndexInMemory() >= 0){
            Process.processInMemory[process.getPcb().getIndexInMemory()] = null;
        }


        //删除临时交换区
        if(OS.pathDirectory.getfInode().getInodeNum() == process.getPcb().getDirectoryIno()){
            File.deleteFile(OS.pathDirectory.getNameByIno(process.getPcb().getVirtualMemoryInDisk()));
        }else {
            Inode inode = Inode.findInodeByNumberInMemory(process.getPcb().getVirtualMemoryInDisk());
            if(inode != null){
                inode.setInodeCount((short) 1);
                File.closeFileInMemory(File.getFileFromInode(inode).getFd());
                inode.deleteInodeInDisk();
            }else {
                inode = new Inode(process.getPcb().getVirtualMemoryInDisk(), true);
                inode.deleteInodeInDisk();
            }
        }

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
                Queues.readyQueue.remove(process);
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
        process.setBlocksInMemory(new Block[4]);  //
        Process.processInMemory[process.getPcb().getIndexInMemory()] = null;
        process.getUserStack().clear();
        process.setPageTable(new PageTable());
    }

    public synchronized static void activate(Process process) throws Exception{
        //从挂起队列加载到相应队列
        if(process.getPcb().getProcessState() == 1){
            process.getPcb().setProcessState((short) 3);
            Queues.readyQueue.remove(process);
            Queues.hangUpReadyQueue.add(process);
            Collections.sort(Queues.hangUpReadyQueue);
        }else if(process.getPcb().getProcessState() >10){
            Queues.blockedQueue[process.getPcb().getProcessState()].remove(process);
            Queues.hangUpBlockedQueue.add(process);
            Collections.sort(Queues.hangUpBlockedQueue);
        }
        short index = Process.getFreeIndex();
        Process.processInMemory[index] = process;
        process.setBlocksInMemory(new Block[4]);
        for(int i=0;i<=4;i++){
            process.getBlocksInMemory()[i] = OS.memory.findBlockByNumber(16+4*index+i);
        }
        //进程的第一块常驻内存，其他三块通过调度
        Block.cloneABlock(process.getBlocksInMemory()[0],
                process.getTempFile().getDataBlockList().get(process.getTempFile().getfInode().getFileSize()-1));
        process.getPcb().setIndexInMemory(index);
    }
}
