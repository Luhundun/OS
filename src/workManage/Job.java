package workManage;

import fileManage.File;

/**
 * @ClassName: Job
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/1/28 0:26
 * @Version: v1.0
 */
public class Job {
    private int jobID;
    private int jobPriority;
//    private int jobFile;
    private int jobInTime;
    private int jobProcessID;       //该作业生成的进程的id，若未生成进程则为0

    private File jobFile;          //产生这个作业的文件

}

