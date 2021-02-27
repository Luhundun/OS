package memoryManage;

/**
 * @ClassName: 页表的每一个项
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/2/26 16:15
 * @Version: v1.0
 */
public class PageTableItem {


    private short logicalPageNumber;    //逻辑页号
    private short physicalPageNumber;   //物理页号
    private boolean stateFlag;          //状态位
    private boolean modifyFlag;         //修改位

    /**
     * @Description: 初始化页表项
     * @param: [logicalPageNumber, physicalPageNumber]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/26 16:26
     */
    public PageTableItem(short logicalPageNumber, short physicalPageNumber) throws Exception{
        if(physicalPageNumber > 128){
            throw new Exception("超出寻址范围");
        }
        this.logicalPageNumber = logicalPageNumber;
        this.physicalPageNumber = physicalPageNumber;
        this.stateFlag = false;
        this.modifyFlag = false;
    }

    /**
     * @Description: 新加载页表项
     * @param: [logicalPageNumber, physicalPageNumber]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/26 16:35
     */
    public PageTableItem(short logicalPageNumber, short physicalPageNumber, boolean stateFlag, boolean modifyFlag) throws Exception{
        if(physicalPageNumber > 128){
            throw new Exception("超出寻址范围");
        }
        this.logicalPageNumber = logicalPageNumber;
        this.physicalPageNumber = physicalPageNumber;
        this.stateFlag = stateFlag;
        this.modifyFlag = modifyFlag;
    }


    /**
     * @Description: 将一个页表项内容转化为short存储
     * @param: [item]
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/2/26 16:28
     */
    public static short convertItemToShort(PageTableItem item){
           //逻辑页号主要用于在页表项的定位，与单个页表项转化无关;
        short returnShort = item.physicalPageNumber;  //低8位存放物理地址
        if(item.stateFlag){
            returnShort += 256;    //第9位存放状态位
        }
        if(item.modifyFlag){
            returnShort +=512;      //第10位存放修改位
        }
        return returnShort;
    }


    /**
     * @Description: 将一个short提取为页表项
     * @param: []
     * @return: memoryManage.PageTableItem
     * @auther: Lu Ning
     * @date: 2021/2/26 16:32
     */
    public static PageTableItem convertShortToPageItem(short thisShort) throws Exception {
        boolean modifyFlag = false, stateFlag = false;
        if(thisShort > 512){
            thisShort -= 512;
            modifyFlag = true;
        }
        if(thisShort > 256){
            thisShort -= 256;
            stateFlag = true;
        }
        return new PageTableItem((short) 0,thisShort,stateFlag,modifyFlag);
    }

    /**
     * @Description: 格式化显示页表项
     * @param: []
     * @return: java.lang.String
     * @auther: Lu Ning
     * @date: 2021/2/26 16:37
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        String tempLogical = Integer.toHexString(Short.toUnsignedInt(logicalPageNumber));
        while (tempLogical.length()<4){
            tempLogical = '0' + tempLogical;
        }
        buffer.append(tempLogical);
        buffer.append('\t');

        String tempPhysical = Integer.toHexString(Short.toUnsignedInt(physicalPageNumber));
        while (tempPhysical.length()<4){
            tempPhysical = '0' + tempPhysical;
        }
        buffer.append(tempPhysical);
        buffer.append('\t');

        buffer.append(stateFlag);
        buffer.append('\t');
        buffer.append(modifyFlag);
        return buffer.toString();
    }

    public short getLogicalPageNumber() {
        return logicalPageNumber;
    }

    public void setLogicalPageNumber(short logicalPageNumber) {
        this.logicalPageNumber = logicalPageNumber;
    }

    public short getPhysicalPageNumber() {
        return physicalPageNumber;
    }

    public void setPhysicalPageNumber(short physicalPageNumber) {
        this.physicalPageNumber = physicalPageNumber;
    }

    public boolean isStateFlag() {
        return stateFlag;
    }

    public void setStateFlag(boolean stateFlag) {
        this.stateFlag = stateFlag;
    }

    public boolean isModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(boolean modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(convertShortToPageItem((short) 798).toString());
        System.out.println(convertItemToShort(new PageTableItem((short) 0,(short) 41,false,true)));
    }
}
