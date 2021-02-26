package fileManage;

import control.GUI;
import control.OS;
import hardware.Block;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @ClassName: File
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/1/28 0:58
 * @Version: v1.0
 */
public class File {
    protected short fd;                       //文件描述符
    protected short fFlags;
    protected short fCount;
    protected short fPos;
    protected Inode fInode;                    //这个文件的inode节点
    protected ArrayList<Block> dataBlockList;  //这个文件在磁盘的存储块链 (8个直接物理块块 -> 256个一级间址物理块 -> 32k个二级间址物理块)，这个成员仅仅为了方便展示
    public static File[] fileStructTable = new File[32];   //系统打开文件表，存储在内存的第8块。每个元素16B，共可同时打开32个。

    /**
     * @Description: 专门为系统文件设计的构造函数
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/24 0:41
     */
    public File(int mode, int type, int sizeForTest,boolean systemFile) throws Exception {
        short nextBlock;
        //硬盘中分配空间
        if(sizeForTest > 8 + 256 + 65536) {
            throw new Exception("单个文件过大");
        } else {
            fInode = new Inode((short)mode,(short) type,(short) sizeForTest); //权限rwx，普通文件，大小为给定的size
            dataBlockList = new ArrayList<>();
            if(sizeForTest <= 8){
                initDirectBlock(sizeForTest);
            } else if(sizeForTest <= 8 + 256){
                initDirectBlock(8);
                initIndirect1Block(sizeForTest - 8);
            } else {
                initDirectBlock(8);
                initIndirect1Block(256);
                initIndirect2Block(sizeForTest - 8 - 256);
            }
        }
        fInode.saveInodeToDisk();
    }

    /**
     * @Description: 创建一个具有索引结构的空文件
     * @param: [sizeForTest]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/20 11:44
     */
    public File(int mode, int type, int sizeForTest) throws Exception {
        short nextBlock;
        //硬盘中分配空间
        if(sizeForTest > 8 + 256 + 65536) {
            throw new Exception("单个文件过大");
        } else {
            fInode = new Inode((short)mode,(short) type,(short) sizeForTest); //权限rwx，普通文件，大小为给定的size
            dataBlockList = new ArrayList<>();
            if(sizeForTest <= 8){
                initDirectBlock(sizeForTest);
            } else if(sizeForTest <= 8 + 256){
                initDirectBlock(8);
                initIndirect1Block(sizeForTest - 8);
            } else {
                initDirectBlock(8);
                initIndirect1Block(256);
                initIndirect2Block(sizeForTest - 8 - 256);
            }
        }
        //内存中将其打开
        if(fInode.getFileType() != 2){
            fd = openFileByFile();
            flashFileSturctTableInMemory();
        }
    }

    /**
     * @Description: 根据inode打开文件，用于不在内存的inode
     * @param: [inode]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/24 1:19
     */
    public File(Inode inode) throws Exception {
        fInode = inode;
        dataBlockList = new ArrayList<>();
        int blockNum = inode.getFileSize();
        //预加载直接地址
        for(int i=0;i<8&&i<blockNum;i++){
            dataBlockList.add(OS.disk.findBlockByDno(fInode.getFileAddressDirect()[i]));
        }
        //预加载一级间址
        Block indirect1 = OS.disk.findBlockByDno(inode.getFileAddressIndirect1());
        for (int i=8;i<264&&i<blockNum;i++){
            dataBlockList.add(OS.disk.findBlockByDno(indirect1.readAWord(i-8)));
        }
        //预加载二级间址
        Block indirect2 = OS.disk.findBlockByDno(inode.getFileAddressIndirect2());
        int indirect1Num = (blockNum-264)/256 + 1;
        for (int i=0;i<indirect1Num;i++){
            indirect1 = OS.disk.findBlockByDno(indirect2.readAWord(i));
            for (int j=0;j<256&&i*256+j+264<blockNum;j++){
                dataBlockList.add(OS.disk.findBlockByDno(indirect1.readAWord(j)));
            }
        }
        if(fInode.getFileType() != 2){
            fd = openFileByFile();
            flashFileSturctTableInMemory();
        }
    }

    /**
     * @Description: 真正创建文件的方法，先在当前目录中创建目录项，再分配Inode，新建文件
     * @param: [fileName, mode, type, sizeForTest]
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/2/24 20:55
     */
    public static File createFile(String fileName,int mode, int type, int sizeForTest) throws Exception {
        File newFile;
        if(type != 2){
            newFile = new File(mode,type,sizeForTest);
        }else {
            newFile = new Directory(OS.pathDirectory, mode);
        }
        OS.pathDirectory.addToDirectory(new FCB(fileName,newFile.fInode.getInodeNum()));
        newFile.fInode.saveInodeToDisk();
        return newFile;
    }

    /**
     * @Description: 根据文件名将文件打开
     * @param: []
     * @return: fileManage.File
     * @auther: Lu Ning
     * @date: 2021/2/25 0:19
     */
    public static void openFile(String fileName){

    }

    /**
     * @Description: 有File条件下在内存中将这个文件打开
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/22 14:09
     */
    public short openFileByFile() throws Exception {
        fCount++;
        if (fCount ==1){         //如果内存中这个文件打开数目为0，那么把对应的inode和文件在相关表中删去
            fInode.loadInodeToMemory();
            openInFileStructTable();
        }
        flashFileSturctTableInMemory();
        return fd;
    }



    /**
     * @Description: 在内存中将这个文件关掉
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/22 14:09
     */
    public static void closeFileInMemory(short fd) throws Exception {
        File thisFile = fileStructTable[fd];
        thisFile.fCount--;
        if (thisFile.fCount ==0){         //如果内存中这个文件打开数目为0，那么把对应的inode和文件在相关表中删去
            thisFile.closeInFileStructTable();
            thisFile.fInode.saveInodeToDisk();
            thisFile.fInode.deleteInodeInMemory();
        }
        flashFileSturctTableInMemory();
    }

//    public void write

    /**
     * @Description: 构造直接地址块
     * @param: [blockNum]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/20 12:20
     */
    public void initDirectBlock(int blockNum) throws Exception {
        short nextBlock;
        if(blockNum > 8){
            throw new Exception("直接地址块数过多");
        }
        for (int i=0;i<blockNum;i++){
            nextBlock = GroupLink.getAFreeBlock();
            fInode.fileAddressDirect[i] = nextBlock;
            dataBlockList.add(OS.disk.findBlockByDno(nextBlock));
        }
    }

    /**
     * @Description: 构造文件的一级间接地址块
     * @param: [blockNum]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/20 12:16
     */
    public void initIndirect1Block(int blockNum) throws Exception {
        if(blockNum > 256){
            throw new Exception("一级索引块数过多");
        }
        short nextBlock;
        fInode.setFileAddressIndirect1(GroupLink.getAFreeBlock());
        Block indirect1 = OS.disk.findBlockByDno(fInode.getFileAddressIndirect1());
        indirect1.setBlockFF();     //为了便于分别后续的每个字是否有意义
        for (int i=0;i<blockNum;i++){
            nextBlock = GroupLink.getAFreeBlock();
            indirect1.writeAWord(nextBlock, i);
            dataBlockList.add(OS.disk.findBlockByDno(nextBlock));
        }
    }

    /**
     * @Description: 构造文件的二级间接地址块
     * @param: [blockNum]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/20 12:26
     */
    public void initIndirect2Block(int blockNum) throws Exception {
        if(blockNum > 65536){
            throw new Exception("二级索引块数过多");
        }
        short nextBlock;
        short thisIndirect1Block;
        fInode.setFileAddressIndirect2(GroupLink.getAFreeBlock());
        Block indirect2 = OS.disk.findBlockByDno(fInode.getFileAddressIndirect2());
        indirect2.setBlockFF();  //为了便于分别后续的每个字是否有意义

        int index = 0;
        while (blockNum/256 >= 1) {
            blockNum -= 256;
            thisIndirect1Block = GroupLink.getAFreeBlock();
            indirect2.writeAWord(thisIndirect1Block, index++);
            Block indirect1 = OS.disk.findBlockByDno(thisIndirect1Block);
            indirect1.setBlockFF();
            for (int i = 0; i < 256; i++) {
                nextBlock = GroupLink.getAFreeBlock();
                indirect1.writeAWord(nextBlock, i);
                dataBlockList.add(OS.disk.findBlockByDno(nextBlock));
            }
        }

        thisIndirect1Block = GroupLink.getAFreeBlock();
        indirect2.writeAWord(thisIndirect1Block, index);
        Block indirect1 = OS.disk.findBlockByDno(thisIndirect1Block);
        indirect1.setBlockFF();
        for (int i = 0; i < blockNum; i++) {
            nextBlock = GroupLink.getAFreeBlock();
            indirect1.writeAWord(nextBlock, i);
            dataBlockList.add(OS.disk.findBlockByDno(nextBlock));
        }


    }

    /**
     * @Description: 在GUI展示文件信息
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/20 13:20
     */
    public void showFileInfomation(){
        GUI.fileName.setText("");
        if(fInode.getFileType() == 1){
            GUI.fileType.setText("普通文件");
        } else if(fInode.getFileType() ==2){
            GUI.fileType.setText("文件目录");
        } else if(fInode.getFileType() ==3){
            GUI.fileType.setText("系统文件");
        }
        short size = fInode.getFileSize();
        if(size <= 20){
            GUI.fileSize.setText(size*512 + "B");
        }else{
            GUI.fileSize.setText(size/2 + "KB");
        }

        GUI.fileLink.setText(String.valueOf(fInode.getInodeLink()));
        GUI.fileCount.setText(String.valueOf(fCount));
        switch (fInode.getInodeSaveMode()){
            case 0: GUI.fileSaveMode.setText("不可读写运行"); break;
            case 1: GUI.fileSaveMode.setText("不可读写 可运行"); break;
            case 2: GUI.fileSaveMode.setText("不可读运行 可写"); break;
            case 3: GUI.fileSaveMode.setText("不可读 可写运行"); break;
            case 4: GUI.fileSaveMode.setText("只读"); break;
            case 5: GUI.fileSaveMode.setText("可读运行 不可写"); break;
            case 6: GUI.fileSaveMode.setText("可读写 不可运行"); break;
            case 7: GUI.fileSaveMode.setText("可读、写、运行"); break;
        }
        GUI.fileStructureP.setText("索引结构");
        GUI.fileStructureL.setText("流式结构");
        GUI.fileFID.setText(String.valueOf(fInode.getInodeNum()));
        //时间
        short[] directAddress = fInode.getFileAddressDirect();
        GUI.directAddress1.setText(String.valueOf(directAddress[0]));
        GUI.directAddress2.setText(String.valueOf(directAddress[1]));
        GUI.directAddress3.setText(String.valueOf(directAddress[2]));
        GUI.directAddress4.setText(String.valueOf(directAddress[3]));
        GUI.directAddress5.setText(String.valueOf(directAddress[4]));
        GUI.directAddress6.setText(String.valueOf(directAddress[5]));
        GUI.directAddress7.setText(String.valueOf(directAddress[6]));
        GUI.directAddress8.setText(String.valueOf(directAddress[7]));
        GUI.indirectAddress1.setText(String.valueOf(fInode.getFileAddressIndirect1()));
        GUI.indirectAddress2.setText(String.valueOf(fInode.getFileAddressIndirect2()));
    }

    /**
     * @Description: 在系统打开文件表打开一个文件
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/22 13:24
     */
    public void openInFileStructTable() throws Exception{
        for(int i=0;i<32;i++){              //遍历寻找空位
            if(fileStructTable[i] == null){
                fileStructTable[i] = this;
                this.fd = (short) i;
                return;
            }
        }
        throw new Exception("系统打开文件表已满,可能是同时打开文件数目超过32");
    }

    /**
     * @Description: 文件读写操作结束后，释放系统打开文件表的相关内容
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/22 13:29
     */
    public void closeInFileStructTable(){
        fileStructTable[this.fd] = null;
        this.fd = -1;
    }

    /**
     * @Description: 刷新内存中的打开文件表
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/22 13:15
     */
    public static void flashFileSturctTableInMemory(){
        Block thisBlock = OS.memory.findBlockByNumber(8);
        for (int i=0;i<32;i++){
            File thisFile = fileStructTable[i];
            if(thisFile != null){
                thisBlock.writeAWord(thisFile.getFd(), 8*i);
                thisBlock.writeAWord(thisFile.getfFlags(), 8*i + 1);
                thisBlock.writeAWord(thisFile.getfCount(), 8*i + 2);
                thisBlock.writeAWord(thisFile.getfPos(), 8*i + 3);
                thisBlock.writeAWord(thisFile.getfInode().getInodeNum(), 8*i + 4);
            }
        }
    }

    /**
     * @Description: 将文件中的数据内容以UTF16的形式读出
     * @param: []0
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/23 15:21
     */
    public static String readFile(short fd){
        File thisFile = getFileFromFd(fd);
        StringBuffer stringBuffer = new StringBuffer();
        for(Block block : thisFile.dataBlockList){
            for (int i=0;i<256;i++){
                String temp = Block.convertShortToUTF16(block.readAWord(i));
                if(temp.equals("\0")){
                    return stringBuffer.toString();
                }
                stringBuffer.append(temp);
            }
        }
        System.out.println("不正常结束");
        return stringBuffer.toString();
    }

    /**
     * @Description: 写文件，将原先文件覆盖
     * @param: []
     * @return: java.lang.String
     * @auther: Lu Ning
     * @date: 2021/2/23 0:59
     */
    public static void writeFile(short fd, String context) throws Exception {
        File thisFile = getFileFromFd(fd);
        thisFile.releaseAllBlocks();
        short[] directAddress = thisFile.fInode.getFileAddressDirect();
        directAddress[0] = GroupLink.getAFreeBlock();
        thisFile.dataBlockList.add(OS.disk.findBlockByDno(directAddress[0]));
        thisFile.fInode.setFileSize((short) 1);
        thisFile.dataBlockList.get(0).setBlock00();
        thisFile.dataBlockList.get(0).setBlockUsedBytes(512);
        context = context + '\0';
        int length = context.length();
        if(length < 256){
            for (int i = 0;i < length; i++){
                thisFile.dataBlockList.get(0).writeAWord(Block.convertUTF16ToShort(context.substring(i,i+1)),i);
            }
        } else {
            for (int i = 0;i < 256; i++){
                thisFile.dataBlockList.get(0).writeAWord(Block.convertUTF16ToShort(context.substring(i,i+1)),i);
            }
            int blockNums = length/256 + 1;
            for (int i = 1; i <blockNums; i++){
                //获取新的数据块
                Block newBlock = OS.disk.findBlockByDno(thisFile.getFileNextBlock());
                for(int j = 0; j < 256 && i*256+j < length ;j++){
                    newBlock.writeAWord(Block.convertUTF16ToShort(context.substring(i*256+j,i*256+j+1)),j);
                }
            }
        }
        thisFile.fInode.setFileAlterTime((short) (OS.getSuperBlock().getRunTime() + OS.getTime()));
    }

    /**
     * @Description: 在文件末尾追加内容
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/23 17:42
     */
    public static void addFileContext(short fd, String context) throws Exception {
        File thisFile = getFileFromFd(fd);
        Block thisBlock = thisFile.dataBlockList.get(thisFile.dataBlockList.size()-1);
        context = context + '\0';
        int length = context.length();
        int oldEOFPos = 0;
        //确定原先文件结尾位置
        for(int i=0;i<256;i++){
            short temp = thisBlock.readAWord(i);
            if(temp == 0){
                oldEOFPos = i;
                break;
            }
        }
        //判断是否需要额外的块,进行不同的处理
        if(length + oldEOFPos <= 256){
            for (int i = oldEOFPos;i < length+oldEOFPos; i++){
                thisBlock.writeAWord(Block.convertUTF16ToShort(context.substring(i-oldEOFPos,i-oldEOFPos+1)),i);
            }
        }else {
            for (int i = oldEOFPos;i<256;i++){
                thisBlock.writeAWord(Block.convertUTF16ToShort(context.substring(i-oldEOFPos,i-oldEOFPos+1)),i);
            }
            int extraWordsNum = length-256+oldEOFPos;
            int extraBlockNums = extraWordsNum/256 + 1;
            for (int i = 1; i <=extraBlockNums; i++){
                //获取新的数据块
                Block newBlock = OS.disk.findBlockByDno(thisFile.getFileNextBlock());
                for(int j = 0; j < 256 && (i-1)*256+j < extraWordsNum ;j++){
                    newBlock.writeAWord(Block.convertUTF16ToShort(context.substring(i*256+j-oldEOFPos,i*256+j+1-oldEOFPos)),j);
                }
            }
        }
        thisFile.fInode.setFileAlterTime((short) (OS.getSuperBlock().getRunTime() + OS.getTime()));
    }

    /**
     * @Description: 清除所有数据块和间接地址块
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/23 1:07
     */
    public void releaseAllBlocks() {
        dataBlockList.clear();
        short[] directAddress = fInode.getFileAddressDirect();
        //释放直接地址上的物理块(1-7)
        for(int i=1;i<8;i++) {
            if(directAddress[i] > 0) {
                GroupLink.releaseAFreeBlock(directAddress[i]);
                directAddress[i] = 0;
            }
        }
        //释放一级间接地址上的物理块
        if(fInode.getFileAddressIndirect1() > 0){
            Block indirect1 = OS.disk.findBlockByDno(fInode.getFileAddressIndirect1());
            for(int i=0;i<256;i++){
                short blockDno = indirect1.readAWord(i);
                if(blockDno != -1){
                    GroupLink.releaseAFreeBlock(blockDno);
                }else {
                    break;
                }
            }
            GroupLink.releaseAFreeBlock(indirect1.getDno());
            fInode.setFileAddressIndirect1((short) 0);
        }
        //释放二级间接地址上的每个索引块和物理块
        if(fInode.getFileAddressIndirect2() > 0) {
            Block indirect2 = OS.disk.findBlockByDno(fInode.getFileAddressIndirect2());
            for (int i=0;i<256;i++) {
                short indirect1Dno = indirect2.readAWord(i);
                if(indirect1Dno != -1) {
                    Block indirect1 = OS.disk.findBlockByDno(indirect1Dno);
                    for(int j=0;j<256;j++){
                        short blockDno = indirect1.readAWord(j);
                        if(blockDno != -1){
                            GroupLink.releaseAFreeBlock(blockDno);
                        }else {
                            break;
                        }
                    }
                    GroupLink.releaseAFreeBlock(indirect1Dno);
                }else {
                    break;
                }
            }
            GroupLink.releaseAFreeBlock(indirect2.getDno());
            fInode.setFileAddressIndirect2((short) 0);
        }
        //最后释放第0块
        GroupLink.releaseAFreeBlock(directAddress[0]);
        directAddress[0] = 0;
        fInode.setFileSize((short) 0);
    }

    /**
     * @Description: 根据这个文件当前inode中的文件大小信息，找到下一个数据块
     * @param: []
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/2/23 14:46
     */
    public short getFileNextBlock() throws Exception {
        short fileSize = fInode.getFileSize();
        short nextBlock = GroupLink.getAFreeBlock();
        if(fileSize < 8){
           fInode.getFileAddressDirect()[fileSize] = nextBlock;
        }else if(fileSize == 8){
           short indirect1 = GroupLink.getAFreeBlock();
           fInode.setFileAddressIndirect1(indirect1);
           OS.disk.findBlockByDno(indirect1).writeAWord(nextBlock,0);
        }else if(fileSize < 8 + 256){
           OS.disk.findBlockByDno(fInode.getFileAddressIndirect1()).writeAWord(nextBlock,fileSize - 8);
        }else if(fileSize == 8 + 256){
           short indirect2 = GroupLink.getAFreeBlock();
           fInode.setFileAddressIndirect2(indirect2);
           short indirect1 = GroupLink.getAFreeBlock();
           OS.disk.findBlockByDno(indirect2).writeAWord(indirect1,0);
           OS.disk.findBlockByDno(indirect1).writeAWord(nextBlock,0);
        } else{
           short temp = (short) (fileSize - 264);
           short indirect1;
           if(temp % 256 == 0){
               indirect1 = GroupLink.getAFreeBlock();
               OS.disk.findBlockByDno(fInode.getFileAddressIndirect2()).writeAWord(indirect1, temp/256);
               OS.disk.findBlockByDno(indirect1).writeAWord(nextBlock,0);
           } else {
               indirect1 = OS.disk.findBlockByDno(fInode.getFileAddressIndirect2()).readAWord(temp/256);
               OS.disk.findBlockByDno(indirect1).writeAWord(nextBlock,temp%256);
           }
        }
        fInode.setFileSize((short) (fileSize + 1));
        dataBlockList.add(OS.disk.findBlockByDno(nextBlock));
        OS.disk.findBlockByDno(nextBlock).setBlock00();
        OS.disk.findBlockByDno(nextBlock).setBlockUsedBytes(512);
        return nextBlock;
    }

    /**
     * @Description: 根据系统文件打开表中的fd，找到对应文件的信息
     * @param: []
     * @return: fileManage.File
     * @auther: Lu Ning
     * @date: 2021/2/23 20:09
     */
    public static File getFileFromFd(short fd){
        return fileStructTable[fd];
    }

    /**
     * @Description: 根据inode号获取文件，分已在内存和不在内存两种情况
     * @param: []
     * @return: fileManage.File
     * @auther: Lu Ning
     * @date: 2021/2/24 1:33
     */
    public static File getFileFromInode(Inode inode) throws Exception {
        if(ifInodeInMemory(inode)){
            for (File file : fileStructTable){
                if (file != null &&file.getfInode().equals(inode)){
                    return file;
                }
            }
        } else {
            if(inode.getFileType()==2){
                new Directory(inode);
            }else {
                new File(inode);
            }
        }
        return null;
    }

    /**
     * @Description: 检查inode是否在内存中打开
     * @param: [inode]
     * @return: boolean
     * @auther: Lu Ning
     * @date: 2021/2/24 1:34
     */
    public static boolean ifInodeInMemory(Inode inode){
       for (File file : fileStructTable){
           if (file != null &&file.getfInode().equals(inode)){
               return true;
           }
       }
        return false;
    }


    /**
     * @Description: 展示系统打开文件表
     * @param: []
     * @return: java.lang.Object[]
     * @auther: Lu Ning
     * @date: 2021/2/24 1:17
     */
    public static Object[] showSystemOpenFileTable(){
        ArrayList<String> list = new ArrayList<>();
        for(File file : fileStructTable){
            if(file != null && file.fd != -1){
                list.add(file.fd+"--------------------------"+file.fInode.inodeNum);
            }
        }
        return list.toArray();
    }

    /**
     * @Description: 用inode来比较两个文件是否相同；
     * @param: [o]
     * @return: boolean
     * @auther: Lu Ning
     * @date: 2021/2/25 0:55
     */
    public boolean equals(File o) {
        if (this == o) return true;
        if (o == null) return false;
        return Objects.equals(getfInode().getInodeNum(), o.getfInode().getInodeNum());
    }


    public short getFd() {
        return fd;
    }

    public void setFd(short fd) {
        this.fd = fd;
    }

    public short getfFlags() {
        return fFlags;
    }

    public void setfFlags(short fFlags) {
        this.fFlags = fFlags;
    }

    public short getfCount() {
        return fCount;
    }

    public void setfCount(short fCount) {
        this.fCount = fCount;
    }

    public short getfPos() {
        return fPos;
    }

    public void setfPos(short fPos) {
        this.fPos = fPos;
    }

    public Inode getfInode() {
        return fInode;
    }


}
