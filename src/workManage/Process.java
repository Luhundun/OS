package workManage;

import control.GUI;
import control.OS;
import fileManage.File;
import hardware.Block;
import hardware.CPU;
import memoryManage.LRU;
import memoryManage.PageTable;
import memoryManage.missingPageInterrupt;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @ClassName: Process
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/1/28 0:26
 * @Version: v1.0
 */
public class Process implements Comparable<Process>{
    private PCB pcb;                //进程控制块
    private Block[] blocksInMemory; //进程的物理块，包括了代码段、数据段。如果此进程被挂起，或未创建，那么此成员为空。

    public File getTempFile() {
        return tempFile;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }

    private File tempFile;          //创建进程产生的临时交换区文件，此处为了加速定位

    public Stack<Short> getCoreStack() {
        return coreStack;
    }

    public void setCoreStack(Stack<Short> coreStack) {
        this.coreStack = coreStack;
    }

    public Stack<Short> getUserStack() {
        return userStack;
    }

    public void setUserStack(Stack<Short> userStack) {
        this.userStack = userStack;
    }

    private Stack<Short> coreStack; //核心栈，其内容在临时交换区的最后一块或者内存区此进程的第一块，只是一个抽象
    private Stack<Short> userStack; //用户栈，同上

    public PageTable getPageTable() {
        return pageTable;
    }

    public void setPageTable(PageTable pageTable) {
        this.pageTable = pageTable;
    }

    private PageTable pageTable;    //用户级页表，同上

    public static Process[] processInMemory = new Process[12];   //仅仅是内存中进程的一个抽象，会有转换函数将其映射到内存

    /**1
     * @Description: 进程概念的构造函数，用在创建进程原语中,只有在PCB池非满时才会被调用
     * @param: [file]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/3/1 11:07
     */
    public Process(File file, short priority) throws Exception {
//        int index = 0;
//        //检查规范性
//        short processPriority = file.runnableFileJudgement();
//        if (processPriority < 0){
//            throw new Exception("不规范的可执行文件");
//        }

        //建立PCB和临时交换区文件
        pcb = new PCB(priority);
        String fileName = OS.pathDirectory.getNameByIno(file.getfInode().getInodeNum());
        if (fileName.equals("")){
            throw new Exception("目录错误");
        }
        tempFile = File.createFile("$"+pcb.getPid()+"~"+fileName, 0, 3, file.getfInode().getFileSize() + 1);
//        tempFile.openFileByFile();//这一次是代表进程打开这个文件
        for(int i=0;i<file.getfInode().getFileSize();i++){
            //将运行文件的每一块拷贝给临时交换区文件
            Block.cloneABlock(tempFile.getDataBlockList().get(i),file.getDataBlockList().get(i));
        }
        tempFile.getDataBlockList().get(file.getfInode().getFileSize()).setBlockFF();
        //PCB中记录临时交换区文件、当前文件位置和创建时间
        pcb.setVirtualMemoryInDisk(tempFile.getfInode().getInodeNum());
        pcb.setDirectoryIno(OS.pathDirectory.getfInode().getInodeNum());
        pcb.setInTimes((short) OS.getTime());

        blocksInMemory = new Block[4];
        userStack = new Stack<>();
        coreStack = new Stack<>();
        pageTable = new PageTable();
    }

    /**
     * @Description: 指令执行时，获取下一条指令
     * @param: [address]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 21:23
     */
    public Instruction getNextInstruction() throws Exception {
        short instructionNum = (short) (CPU.getPc()/8);
        //每读32个指令就需要通过缺页中断换一块

        Instruction instruction = new Instruction(tempFile.readInstruction(instructionNum));

        CPU.setPc((short) (CPU.getPc() + 8));
        //需要换页的情况
        if(CPU.getPc()%256 == 0){
            short newPage = (short) (CPU.getPc()/256);
            short oldPage = LRU.lru(userStack, newPage);
            if(oldPage > -5){
                //两种情况，都在下面缺页中断相关方法中处理
                //引发缺页中断,在页表进行替换
                //引发缺页中断但内存未满，加载新页即可
                missingPageInterrupt.loadNewPage(newPage,oldPage);
                System.out.println(oldPage);
            }
        }

//        if(!pageTable.isInMemory(CPU.getPc())){
//            //引发缺页中断
//            missingPageInterrupt.loadNewPage((short) (CPU.getPc()/256));
//        }

        CPU.setIr(Instruction.convertInstruction(instruction)[0]);
        return instruction;

    }


    /**
     * @Description: 找到内存中空闲进程的位置
     * @param: []
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/3/3 18:38
     */
    public static short getFreeIndex(){
        for(short i=0;i<12;i++){
            if(processInMemory[i] == null){
                return i;
            }
        }
        return -1;
    }

    /**
     * @Description: gui展示进程信息
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 18:37
     */
    public static void showProcessInfo(){
        GUI.processInMemory.setListData(processInMemory);
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

    /**
     * @Description: 查看内存中进程是否还有空间
     * @param: []
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/3/3 20:50
     */
    public short ifblockInMemoryFull(){
        for (short i=0;i<4;i++){
            if(blocksInMemory[i]==null){
                return i;
            }
        }
        return -1;
    }

    /**
     * @Description: 初始化内存内进程
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 19:47
     */
    public static void initProcess(){
        for (int i=0;i<12;i++){
            processInMemory[i] = null;
        }
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
