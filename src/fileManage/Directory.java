package fileManage;

import control.GUI;
import control.OS;
import hardware.Block;
import workManage.RequestFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @ClassName: Directory
 * @Description: 文件夹类
 * @Author: Lu Ning
 * @Date: 2021/2/2 13:22
 * @Version: v1.0
 */
public class Directory extends File{

    private final ArrayList<FCB> directoryItems;  //实际FCB存储在硬盘的数据块中，这个成员仅仅为了方便GUI展示
    /**
     * @Description: 初始化一个目录文件
     * @param: [saveMode, fileType, size]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/20 15:15
     */
    public Directory(Directory parent,int saveMode) throws Exception {
        super(saveMode, 2 , 1);
        directoryItems = new ArrayList<>();
        directoryItems.add(new FCB("..", parent.fInode.getInodeNum()));
        directoryItems.add(new FCB(".", this.fInode.getInodeNum()));
        fd = openFileByFile();
        flashFileSturctTableInMemory();
        flashDirectory();
    }

    /**
     * @Description: 重装系统时初始化系统的目录文件
     * @param: [root]
     * @return:2
     * @auther: Lu Ning
     * @date: 2021/2/24 0:33
     */
    public Directory(boolean root) throws Exception {
        super(7, 2 , 1,true);
        directoryItems = new ArrayList<>();
        directoryItems.add(new FCB(".", this.fInode.getInodeNum()));
        flashDirectory();
    }

    /**
     * @Description: 从硬盘中读取的目录文件
     * @param: [inode]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/24 20:02
     */
    public Directory(Inode inode) throws Exception {
        super(inode);
        directoryItems = new ArrayList<>();
        for(int i=0;i<inode.getFileSize();i++){
            Block thisBlock = dataBlockList.get(i);
            for(int j=0;j<8;j++){
                short[] temp = new short[32];
                for (int k=0;k<32;k++){
                    temp[k] = thisBlock.readAWord(j*32 + k);
                }
                if(temp[0] == 0){
                    break;
                } else {
                    directoryItems.add(new FCB(temp));
                }
            }
        }
        fd = openFileByFile();
        flashFileSturctTableInMemory();
    }

    /**
     * @Description: 进入下一级的目录(通过..也可以返回上一级)
     * @param: [fileName]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/25 0:37
     */
    public static void getInDirectory(String fileName) throws Exception {
        Directory oldDirectory = OS.pathDirectory;
        File nextDirectory = OS.pathDirectory.findFileInDirectory(fileName);
        if(nextDirectory.fInode.getFileType() != 2){
            throw new Exception("非文件夹");
        }
        //关闭一次父目录下的其他目录项
        for(FCB fcb : OS.pathDirectory.directoryItems){
            if(!fcb.getFileName().equals(".")&&!fcb.getFileName().equals(fileName)){
                short ino = fcb.getIno();
                for(File openFile : fileStructTable) {
                    if (openFile != null && openFile.fInode.getInodeNum() == ino) {
                        closeFileInMemory(openFile.fd);
                    }
                }
            }
        }
        OS.pathDirectory = (Directory) nextDirectory;
        //打开下一级目录下的每个文件的inode
        for(FCB fcb : OS.pathDirectory.directoryItems){
            if(fcb.getFileName().equals(".")){
                continue;
            }
            Inode inode = Inode.findInodeByNumberInMemory(fcb.getIno());
            //在内存情况下
            if (inode != null) {
                getFileFromInode(inode).openFileByFile();
//                inode.loadInodeToMemory();
                flashFileSturctTableInMemory();
            }else {//不在内存情况下
                inode = new Inode(fcb.getIno(), true);
                if(inode.getFileType()==2){
                    Directory directory=new Directory(inode);
                }else {
                    File file = new File(inode);
                }
            }
        }
        //最后关闭上一级的父目录
        if(oldDirectory.getfInode().getInodeNum() != 0 ){
            closeFileInMemory(oldDirectory.getFd());
        }
    }

//    /**
//     * @Description: 真正创建文件的方法，先在当前目录中创建目录项，再分配Inode，新建文件
//     * @param: [fileName, mode, type, sizeForTest]
//     * @return: short
//     * @auther: Lu Ning
//     * @date: 2021/2/24 20:55
//     */
//    public static Directory createDirectory(String fileName,int mode, int type, int sizeForTest) throws Exception {
//        Directory newDirectory = new Directory(OS.pathDirectory,mode);
//        OS.pathDirectory.addToDirectory(new FCB(fileName,newDirectory.fInode.getInodeNum()));
//        return newDirectory;
//    }

    /**
     * @Description: 在当前目录下添加一个FCB，是创建文件的子步骤之一
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/23 19:51
     */
    public void addToDirectory(FCB fcb) throws Exception {
        if(checkRepeatName(fcb.getFileName())){
            throw new Exception("文件已存在");
        }
        directoryItems.add(fcb);
        flashDirectory();
        fInode.setFileAlterTime((short) (OS.getSuperBlock().getRunTime() + OS.getTime()));
    }

    /**
     * @Description: 在当前目录下删去一个FCB，是删除文件的子步骤之一
     * @param: [fileName]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/27 19:48
     */
    public void deleteInDirectory(String fileName) throws Exception{
        for(FCB fcb : directoryItems){
            if(fcb.getFileName().equals(fileName)){
                directoryItems.remove(fcb);
                break;
            }
        }
        flashDirectory();
        fInode.setFileAlterTime((short) (OS.getSuperBlock().getRunTime() + OS.getTime()));
    }

    /**
     * @Description: 在当前文件夹的目录项中根据文件名找到文件
     * @param: [name]
     * @return: fileManage.File
     * @auther: Lu Ning
     * @date: 2021/2/25 0:04
     */
    public File findFileInDirectory(String name) throws Exception {
        short ino = -1;
        for(FCB fcb : directoryItems){
            System.out.println(name + " " + fcb.getFileName()+ " " +Arrays.toString(name.getBytes(StandardCharsets.UTF_8)) +" "+ Arrays.toString(fcb.getFileName().getBytes(StandardCharsets.UTF_8)));
            if(fcb.getFileName().equals(name)){
                ino = fcb.getIno();
                break;
            }
        }
        if(ino == -1){
            throw new Exception("当前目录下无此文件");
        }
        for(File file : fileStructTable){
            if(file != null && file.fInode.getInodeNum() == ino){
                return file;
            }
        }
        throw new Exception("对应文件不存在");
    }

    /**
     * @Description: 更新当前文件夹的内容
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/24 0:07
     */
    public void flashDirectory() throws Exception {
        int FCBNums = directoryItems.size();
        releaseAllBlocks();
        for (int i=0;i<=FCBNums/8;i++){
            Block thisBlock = OS.disk.findBlockByDno(getFileNextBlock());
            for (int j=0;j<8&&i*8+j<FCBNums;j++){
                short[] temp =directoryItems.get(i*8+j).convertFCBToShortArray();
                for(int k=0;k<31;k++){
                    if(temp[k] == 0){
                        break;
                    }
                    thisBlock.writeAWord(temp[k],j*32+k);
                }
                thisBlock.writeAWord(temp[31],j*32+31);
            }
        }
    }

    /**
     * @Description: 检查名称是否重复
     * @param: [name]
     * @return: boolean
     * @auther: Lu Ning
     * @date: 2021/2/24 0:27
     */
    public boolean checkRepeatName(String name){
        for(FCB fcb : directoryItems){
            if(name.equals(fcb.getFileName())){
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 在GUI焦点中打开文件夹
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/24 17:42
     */
    public void openDirectory() throws Exception {
        openFileByFile();
    }

    /**
     * @Description: 在GUI中显示文件
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/24 17:42
     */
    public static void showPathItems(){
        ArrayList<Object> fileNameList = new ArrayList<>();
        ArrayList<Object> fileTypeList = new ArrayList<>();
        for(FCB fcb : OS.pathDirectory.directoryItems){
            fileNameList.add(fcb.getFileName());
            Inode inode = Inode.findInodeByNumberInMemory(fcb.getIno());
            if(inode != null){
                if(inode.getFileType() == 1){
                    fileTypeList.add("普通文件");
                }else if(inode.getFileType() == 2){
                    fileTypeList.add("目录文件");
                }else {
                    fileTypeList.add("特殊文件");
                }
            }
        }
        GUI.fileNameList.setListData(fileNameList.toArray());
        GUI.fileModeList.setListData(fileTypeList.toArray());
    }

    /**
     * @Description: 在当前目录下获取这个文件的文件名
     * @param: []
     * @return: java.lang.String
     * @auther: Lu Ning
     * @date: 2021/3/1 22:38
     */
    public String getNameByIno(short ino){
        for (FCB fcb : directoryItems){
            if(fcb.getIno() == ino){
                return fcb.getFileName();
            }
        }
        return "";
    }


    public ArrayList<FCB> getDirectoryItems() {
        return directoryItems;
    }


}
