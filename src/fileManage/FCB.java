package fileManage;

import hardware.Block;

/**
 * @ClassName: FCB
 * @Description: 是虚拟的类，仅在目录文件的数据块中调用，由62B的文件名和2B的inode号组成。
 * @Author: Lu Ning
 * @Date: 2021/2/23 17:25
 * @Version: v1.0
 */
public class FCB {

    private String fileName;         //文件名
    private short ino;              //文件编号

    /**
     * @Description: 初始化一个FCB
     * @param: [fileNme, ino]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/23 17:28
     */
    public FCB(String fileNme, short ino) throws Exception{
        if(fileNme.length() > 31){
           throw new Exception("文件名过长");
        }
        if(fileNme.contains("/")){
           throw new Exception("文件名不合法");
        }
        this.fileName = fileNme;
        this.ino =ino;
    }

    /**
     * @Description: 从硬盘中读取FCB
     * @param: [blockContext]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/24 20:10
     */
    public FCB(short[] blockContext) throws Exception {
        if(blockContext == null || blockContext.length < 32){
            throw new Exception("非法的FCB");
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0;i<31;i++){
            if(blockContext[i] == 0){
                break;
            }
            stringBuffer.append(Block.convertShortToUTF16(blockContext[i]));
        }
        fileName = stringBuffer.toString();
        ino = blockContext[31];

    }

    /**
     * @Description: 将FCB转化成32个short，用于存放
     * @param: []
     * @return: short[]
     * @auther: Lu Ning
     * @date: 2021/2/23 17:31
     */
    public short[] convertFCBToShortArray(){
        short[] shortArray = new short[32];
        fileName = fileName + '\0';
        for(int i=0;i<31&&i<fileName.length();i++){
            if(fileName.charAt(i) == '\0'){
                shortArray[i] = 0;
                break;
            }else {
                shortArray[i] = Block.convertUTF16ToShort(fileName.substring(i,i+1));
            }
        }
        shortArray[31] = ino;
        return shortArray;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public short getIno() {
        return ino;
    }

    public void setIno(short ino) {
        this.ino = ino;
    }

}
