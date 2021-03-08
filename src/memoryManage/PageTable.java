package memoryManage;

import control.GUI;
import control.OS;
import hardware.Block;
import hardware.CPU;
import workManage.Process;

import java.util.ArrayList;

/**
 * @ClassName: PageTable
 * @Description: 有关页表的类，其中模拟MMU的方法大部分在此类中实现
 *               一个页表设定为1块，即512B，一个进程对应一张。在此进程为运行态时加载到系统页表，其他时候以Block形式驻留外存。
 *               系统级页表常驻系统内存，在内存的第7块.
 * @Author: Lu Ning
 * @Date: 2021/2/26 16:13
 * @Version: v1.0
 */
public class PageTable {

    private PageTableItem[] pageTable;
    public static PageTable systemPageTable;

    /**
     * @Description: 进程被创建或者重新加载到内存时的页表初始化
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/26 16:16
     */
    public PageTable() throws Exception {
       pageTable = new PageTableItem[256];
       for(int i=0;i<256;i++){
           pageTable[i] = new PageTableItem((short) i,(short) 0);
       }
    }

    /**
     * @Description: 从虚存文件的指定物理块加载页表，用于就绪或阻塞态的进程重新获得CPU
     * @param: [infoBlock]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/27 16:58
     */
    public PageTable(Block infoBlock) throws Exception {
        pageTable = new PageTableItem[256];
        for(int i=0;i<256;i++){
            pageTable[i] = PageTableItem.convertShortToPageItem(infoBlock.readAWord((short)i));
        }
    }

    /**
     * @Description: 系统开机时初始化系统页表
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/26 17:13
     */
    public static void initSystemPageTable() throws Exception {
        systemPageTable = new PageTable();
        systemPageTable.loadPageTableToSystem();
    }

    /**
     * @Description: 进程切换时，将新的页表加载到系统页表
     * @param:
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/26 17:17
     */
    public void loadPageTableToSystem(){
        systemPageTable = this;
        Block thisBlock = OS.memory.findBlockByNumber(7);

        //将页表覆盖如内存第7个物理块
        for(int i=0;i<256;i++){
            thisBlock.writeAWord(PageTableItem.convertItemToShort(pageTable[i]), i);
        }
    }

    /**
     * @Description: 当这个进程失去CPU后，从系统页表写回到外存
     * @param: [Block]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/27 16:54
     */
    public static void writeBackToVirtualMemory(Block infoBlock){
        for(int i=0;i<256;i++){
            infoBlock.writeAWord(PageTableItem.convertItemToShort(systemPageTable.pageTable[i]),i);
        }
    }

    public PageTableItem[] getTable() {
        return pageTable;
    }

    /**
     * @Description: 缺页中断会调用的换页，如果不需要换出则old < 0
     * @param: [oldBlock, newBlock ,index]  index表示这个是当前进程在内存的第index块
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/4 00:11
     */
    public void exchangeInPageTable(short newBlock, short oldBlock,short index){
        if(oldBlock >= 0){
            pageTable[oldBlock].setPhysicalPageNumber((short)0);
            pageTable[newBlock].setPhysicalPageNumber((short) (16 + 4*(CPU.workingProcess.getPcb().getIndexInMemory()) + index));
        }else {
            pageTable[newBlock].setPhysicalPageNumber((short) (16 + 4*(CPU.workingProcess.getPcb().getIndexInMemory()) + index));
        }
    }

    public void exchangeInPageTable(short newBlock, short oldBlock,short index,Process process){
        if(oldBlock >= 0){
            pageTable[oldBlock].setPhysicalPageNumber((short)0);
            pageTable[newBlock].setPhysicalPageNumber((short) ((16 + 4*process.getPcb().getIndexInMemory()) + index));
        }else {
            pageTable[newBlock].setPhysicalPageNumber((short) ((16 + 4*process.getPcb().getIndexInMemory()) + index));
        }
    }

    /**
     * @Description:判断是否在内存
     * @param: [address]
     * @return: boolean
     * @auther: Lu Ning
     * @date: 2021/3/4 00:25
     */
    public boolean isInMemory(short address){
        if(pageTable[address/256].getPhysicalPageNumber() <= 0){
            return false;
        }else return true;
    }

//    public Object[] showWhichPageInMemory(){
//        ArrayList<String> temp = new ArrayList<>();
//        for()
//        for(int i=0;i<256;i++){
//            if(pageTable[i].getPhysicalPageNumber() > 0){
//                temp.add(pageTable[i].getLogicalPageNumber()+"-----------------"+pageTable[i].getPhysicalPageNumber());
//            }
//        }
//        return temp.toArray();
//    }

    public static void showSystemPageTableInfo(){
        if(CPU.workingProcess == null){
            return;
        }
        systemPageTable = CPU.workingProcess.getPageTable();
        ArrayList<String> temp = new ArrayList<>();
        for(short number : CPU.workingProcess.getUserStack()){
            temp.add(systemPageTable.pageTable[number].getLogicalPageNumber()+"-----------------"+systemPageTable.pageTable[number].getPhysicalPageNumber());
        }
        GUI.systemPageTable.setListData(temp.toArray());
    }
}
