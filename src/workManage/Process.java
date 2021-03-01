package workManage;

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
public class Process {
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
        short processPriority = file.runnableFileJudgement();
        if (processPriority < 0){
            throw new Exception("不规范的可执行文件");
        }


        pcb = new PCB(processPriority);
//        blocksInMemory[0] =
    }
}
