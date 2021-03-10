package workManage;

import control.OS;
import fileManage.Directory;
import fileManage.File;

import java.util.ArrayList;

/**
 * @ClassName: JCB
 * @Description:  保存作业信息，在此系统中，JCB占据空间较小，因此没有分到单独物理块
 * @Author: Lu Ning
 * @Date: 2021/1/28 0:26
 * @Version: v1.0
 */
public class JCB implements Comparable<JCB>{


    private short jobID;
    private short jobInTime;

    private short instructionNum;
    private short jobPriority;
    private File jobFile;          //申请这个作业的文件 （实际以这个file的文件号来储存）

    public static short jobNum = 0;

    /**
     * @Description: JCB
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/3/5 12:41
     */
    public JCB(File file, short priority, short num) {
        jobID = jobNum++;
        jobInTime = (short) OS.getTime();
        jobFile = file;
        instructionNum = num;
        jobPriority = priority;
        System.out.println("生成作业"+toString());
    }

    /**
     * @Description: JCB构造函数的另一个重载，可以规定作业创建时间
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/3/5 12:41
     */
    public JCB(File file, short priority, short inTime,short num) {
        jobID = jobNum++;
        jobInTime = inTime;
        jobFile = file;
        jobPriority = priority;
        instructionNum = num;
        System.out.println("生成作业"+toString());
    }

    @Override
    public String toString() {
        return String.valueOf(jobID) + "--" + OS.pathDirectory.getNameByIno(jobFile.getfInode().getInodeNum());
    }

    public short getJobID() {
        return jobID;
    }

    public void setJobID(short jobID) {
        this.jobID = jobID;
    }

    public short getJobInTime() {
        return jobInTime;
    }

    public void setJobInTime(short jobInTime) {
        this.jobInTime = jobInTime;
    }

    public File getJobFile() {
        return jobFile;
    }

    public void setJobFile(File jobFile) {
        this.jobFile = jobFile;
    }

    public short getJobPriority() {
        return jobPriority;
    }

    public void setJobPriority(short jobPriority) {
        this.jobPriority = jobPriority;
    }


    public short getInstructionNum() {
        return instructionNum;
    }

    public void setInstructionNum(short instructionNum) {
        this.instructionNum = instructionNum;
    }

    /**
     * @Description: 作业采用FCFS算法，按进入时间在后备队列进行排序
     * @param: [o]
     * @return: int
     * @auther: Lu Ning
     * @date: 2021/3/5 16:46
     */
    @Override
    public int compareTo(JCB o) {
        return Integer.compare(this.jobInTime, o.jobInTime);
    }
}

