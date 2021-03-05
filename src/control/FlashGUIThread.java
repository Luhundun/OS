package control;

import fileManage.Directory;
import fileManage.File;
import fileManage.GroupLink;
import fileManage.Inode;
import hardware.CPU;
import memoryManage.FreeBlockManage;
import memoryManage.PageTable;
import workManage.PCB;
import workManage.Process;
import workManage.Queues;

/**
 * @ClassName: FlashGUIThread
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/2/19 12:27
 * @Version: v1.0
 */
public class FlashGUIThread extends Thread {
    public void run() {
        while(true) {
            OS.baseTimerLock.lock();//请求锁
            try {
                OS.baseTimerCondition.await();        //等到时钟进程发出时钟中断，再开始执行下面操作
                flashGUI();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally{
                OS.baseTimerLock.unlock();//释放锁
            }
        }
    }

    /**
     * @Description: 在时钟线程控制下，每秒刷新一次GUI的硬件和软件展示信息
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/19 12:33
     */
    public void flashGUI(){

        int freeInodeSumInDisk = OS.getSuperBlock().getFreeInodesSumInDisk();
        GUI.inodeBarInDisk.setValue(freeInodeSumInDisk);
        GUI.inodeInDiskLabel.setText(freeInodeSumInDisk+"/512");
        //展示成组空闲块链
        GUI.groupLinkList.setListData(GroupLink.showGroupLinkList());
        //计算硬盘使用量
        int tempUsedByte = OS.disk.computeDiskUsedBytes();
        GUI.diskBar.setValue(tempUsedByte);
        GUI.diskUsedSituation.setText("共10485760B,已使用"+tempUsedByte+"B");

        //更新内存空闲块链
        GUI.freeBlockList.setListData(FreeBlockManage.showFreeBlockList());

        //更新内存中空闲inode
        GUI.inodeInMemoryList.setListData(Inode.showInodeListInMemory());
        int freeInodeSumInMemory = OS.getSuperBlock().getFreeInodesSumInMemory();
        GUI.inodeBarInMemory.setValue(freeInodeSumInMemory);
        GUI.inodeInMemoryLabel.setText(freeInodeSumInMemory +"/32");

        //更新系统打开文件表
        GUI.systemOpenFileTable.setListData(File.showSystemOpenFileTable());
//        if(OS.topFile!=null){
//            OS.topFile.showFileInfomation();
//        }

        //更新文件系统
        StringBuffer path = new StringBuffer("/");
        for(String subPath : OS.path){
            path.append(subPath);
            path.append("/");
        }
        GUI.filePathLabel.setText(path.toString());
        Directory.showPathItems();

        //更新进程信息
        Queues.showQueuesInformation();
        Process.showProcessInfo();
        CPU.showCPUInfo();
        PCB.showPCBPoll();

        //更新页表信息
        PageTable.showSystemPageTableInfo();

    }
}
