package deviceManage;

import control.GUI;
import control.OS;
import fileManage.Directory;
import fileManage.File;
import workManage.Process;

import java.util.ArrayList;

/**
 * @ClassName: Spooling
 * @Description: 存放与spooling相关的方法
 * @Author: luning
 * @Date: 2021/3/6 12:34
 * @Version: v1.0
 */
public class Spooling {
    public static File inputWell;  //输入井
    public static File outputWell;  //井
    public static ArrayList<workTableItem> workTable = new ArrayList<>(); //作业表
    /**
     * @Description: 作业表项
     * @param:
     * @return:
     * @auther: Lu Ning
     * @date: 2021/3/6 15:14
     */
    static class workTableItem{
        short processID;    //申请输入的进程号
        short inTime;       //申请的时间
        short deviceID;     //申请的设备号
        short state;        //作业状态
        String result;      //申请的信息

        /**
         * @Description: 作业表项的构造函数
         * @param: [processID, inTime, deviceID, result]
         * @return:
         * @auther: Lu Ning
         * @date: 2021/3/6 15:16
         */
        public workTableItem(short processID, short inTime, short deviceID, String result){
            this.processID = processID;
            this.inTime = inTime;
            this.deviceID = deviceID;
            this.result = result;
            state = 0;
        }

        /**
         * @Description: 格式化输出作业表
         * @param: []
         * @return: java.lang.String
         * @auther: Lu Ning
         * @date: 2021/3/6 15:26
         */
        @Override
        public String toString() {
            try {
                return "申请的进程号" + processID +
                        ", 申请设备名：" + SystemDeviceTable.getDeviceNameById(deviceID) +
                        ", 进入时间：" + inTime +
                        ", 输出内容：" + result + '\n';
            } catch (Exception e) {
                return "申请的进程号" + processID +
                        ", 申请设备名：" + "未知设备名" +
                        ", 进入时间：" + inTime +
                        ", 输出内容：" + result + '\n';
            }
        }
    }

    /**
     * @Description: 初始化输入井和输出井
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/6 13:11
     */
    public static void initSpooling() throws Exception {
        //从跟目录进入设备管理目录，如果不存在井文件则创建，存在则读取
//        File.createFile("设备文件目录",7,2,1);
        Directory.getInDirectory("设备文件目录");
        try{
            inputWell = OS.pathDirectory.findFileInDirectory("输入井");
        }catch (Exception e){
            System.out.println(1);
            inputWell = File.createFile("输入井",6,3,8);
        }
        try{
            outputWell = OS.pathDirectory.findFileInDirectory("输出井");
        }catch (Exception e){
            e.printStackTrace();
            outputWell = File.createFile("输出井",6,3,8);
        }
        //再额外打开一次，防止退出当前目录后inode被回收
        inputWell.openFileByFile();
        outputWell.openFileByFile();
        //退回根目录
        Directory.getInDirectory("..");
    }

    /**
     * @Description: 向井中预输入
     * @param: [context]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/6 14:57
     */
    public synchronized static void typeAHead(Process process, short deviceId, String context) throws Exception {
        //预输入时，在作业表中登记
//        if(workTable.size() >= OS.SPOOLINGWORKTABLEMAXSIZE){
//            //作业表达到上限
//            return;
//        }
        workTableItem item = new workTableItem(process.getPcb().getPid(),OS.getTime(),deviceId,context);
        workTable.add(new workTableItem(process.getPcb().getPid(),OS.getTime(),deviceId,context));
        File.addFileContext(inputWell.getFd(),item.toString());
    }

    /**
     * @Description: 管理井中作业表和数据
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/6 15:37
     */
    public static void wellManageMent() throws Exception {
        //将输入井信息经过处理加载到输出井，并从输入井清除
        File.addFileContext(outputWell.getFd(), File.readFile(inputWell.getFd()));
        File.writeFile(inputWell.getFd()," ");
        workTable.clear();


    }
    /**
     * @Description: 从井中到目标设备缓输出
     * @param: []
     * @return: java.lang.String
     * @auther: Lu Ning
     * @date: 2021/3/6 15:04
     */
    public synchronized static String slowWrite() throws Exception {
        String context = File.readFile(outputWell.getFd());
        File.writeFile(outputWell.getFd(),"");
        return context;
    }

    /**
     * @Description: 在GUI展示Spooling井的信息
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/6 15:07
     */
    public static void showSpoolingInfo()  {
        GUI.inputWell.setText(File.readFile(inputWell.getFd()));
        GUI.outputWell.setText(File.readFile(outputWell.getFd()));
    }
}
