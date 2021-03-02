package memoryManage;

import hardware.Block;

/**
 * @ClassName: Page
 * @Description: 与页面有关的数据结构,一个页对应一个物理块
 * @Author: luning
 * @Date: 2021/2/27 16:22
 * @Version: v1.0
 */
public class Page {
    private Block block;                    //页面实际储存的内容
    private short logicalPageNumber;       //逻辑页面号
    private boolean stateFlag;             //状态位，标记此页是否在进程

    /**
     * @Description: 页面构造函数，一般在进程创建时调用
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/27 16:44
     */
    public Page(Block block, short logicalPageNumber){
        this.block = block;
        this.logicalPageNumber = logicalPageNumber;
        this.stateFlag = false;
    }

    /**
     * @Description: 判断此逻辑页是否在内存
     * @param: []
     * @return: boolean
     * @auther: Lu Ning
     * @date: 2021/2/27 17:03
     */
    public boolean isPageInMemory(){
        if (PageTable.systemPageTable.getTable()[logicalPageNumber].isStateFlag()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * @Description: 将一个不在内存的页替换到物理内存
     * @param: [replacedPage, replacingPage]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/27 17:14
     */
    public static void pageReplace(Page replacedPage, Page replacingPage){

    }


    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public short getLogicalPageNumber() {
        return logicalPageNumber;
    }

    public void setLogicalPageNumber(short logicalPageNumber) {
        this.logicalPageNumber = logicalPageNumber;
    }

    public boolean isStateFlag() {
        return stateFlag;
    }

    public void setStateFlag(boolean stateFlag) {
        this.stateFlag = stateFlag;
    }


}
