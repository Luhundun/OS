package workManage;

/**
 * @ClassName: PCB
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/1/28 0:25
 * @Version: v1.0
 */
public class PCB implements Comparable<PCB>{
    private int processID;          //进程编号
    private int processPriority;    //进程优先数
    private int inTimes;            //进程创建时间
    private int runTimes;           //进程运行时间
    private int turnTimes;          //进程周转时间
    private int timeSliceLeft;      //当前进程在cpu运行的剩余时间片，若非运行态则为0
    private int instructionNum;     //进程中包含的指令数目
    private int psw;                //进程状态 0为未知 1为运行 2为就绪 3为阻塞 4为中止 5为挂起就绪 6为挂起等待
    private int pc;    	            //程序计数器信息，记录下一条指令地址
    private int ir;                 //指令计数器信息，记录当前执行的指令类型
    private int blockNumber;        //在内存的地址，若不在内存则为0


    /**
     * @Description: 重写比较接口，用于队列中PCB的排序
     * @param: [o]
     * @return: int
     * @auther: Lu Ning
     * @date: 2021/1/28 0:30
     */
    @Override
    public int compareTo(PCB o) {
        return Integer.compare(this.processPriority, o.processPriority);
    }
}
