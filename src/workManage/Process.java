package workManage;

import control.OS;
import fileManage.Directory;
import fileManage.File;
import hardware.Block;

import java.util.ArrayList;

/**
 * @ClassName: Process
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/1/28 0:26
 * @Version: v1.0
 */
public class Process implements Comparable<Process>{
    private PCB pcb;                //进程控制块
    private Block[] blocksInMemory; //进程的物理块，包括了代码段、数据段

    /**
     * @Description: 进程概念的构造函数，用在创建进程原语中,只有在PCB池非满时才会被调用
     * @param: [file]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/3/1 11:07
     */
    public Process(File file) throws Exception {
        int index = 0;
        //检查规范性
        short processPriority = file.runnableFileJudgement();
        if (processPriority < 0){
            throw new Exception("不规范的可执行文件");
        }

        //建立PCB和临时交换区文件
        pcb = new PCB(processPriority);
        String fileName = OS.pathDirectory.getNameByFile(file);
        if (fileName.equals("")){
            throw new Exception("目录错误");
        }
        File tempFile = File.createFile("$"+pcb.getPid()+"~"+fileName, 0, 3, file.getfInode().getFileSize() + 1);
//        tempFile.openFileByFile();//这一次是代表进程打开这个文件
        for(int i=0;i<file.getfInode().getFileSize();i++){
            //将运行文件的每一块拷贝给临时交换区文件
            tempFile.getDataBlockList().get(i+1).cloneABlock(file.getDataBlockList().get(i).getDno());
        }
        //PCB中记录临时交换区文件和创建时间
        pcb.setVirtualMemoryInDisk(tempFile.getfInode().getInodeNum());
        pcb.setInTimes((short) OS.getTime());

        //加入就绪队列
        blocksInMemory = null;
        Queues.readyQueue.add(this);


//        blocksInMemory[0] =
    }

    @Override
    public String toString() {
        return String.valueOf(pcb.getPid());
    }


    /**
     * @Description: 根据优先级对进程进行比较
     * @param: [o]
     * @return: int
     * @auther: Lu Ning
     * @date: 2021/3/2 20:19
     */
    @Override
    public int compareTo(Process p) {
        if(this.pcb.getProcessPriority()>p.pcb.getProcessPriority())
            return 1;
        else if(this.pcb.getProcessPriority()<p.pcb.getProcessPriority())
            return -1;
        else
            return 0;
    }

    public PCB getPcb() {
        return pcb;
    }

    public void setPcb(PCB pcb) {
        this.pcb = pcb;
    }

    public Block[] getBlocksInMemory() {
        return blocksInMemory;
    }

    public void setBlocksInMemory(Block[] blocksInMemory) {
        this.blocksInMemory = blocksInMemory;
    }


}
