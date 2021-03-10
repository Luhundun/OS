package hardware;

import control.OS;
import workManage.Process;

/**
 * @ClassName: MMU
 * @Description:  根据系统内存的页表（占有CPU的那个进程会将自己进程内的页表导入系统页表），进行逻辑地址到物理地址的映射
 * @Author: Lu Ning
 * @Date: 2021/2/26 16:48
 * @Version: v1.0
 */
public class MMU {
    //占位符
    //说明：实际主机中MMU作为硬件，负责地址变化和内存保护等
    //而本系统中，为了保证每一个软件模块的完整性，与MMU有关的方法都实现在了memoryManage包的PageTable和MMUThread

//    public static void loadNewPage(short newPage, short oldPage) throws Exception {
////        checkInterruptValid();
//        //确定交换的位置
//        if(oldPage >= 0){
//            //需要替换页面的情况
//            short physicalIndex = CPU.workingProcess.getPageTable().getTable()[oldPage].getPhysicalPageNumber();
//            short relateIndex = (short) (physicalIndex  - 16 - 4 * CPU.workingProcess.getPcb().getIndexInMemory());
//            //在页表记录
//            CPU.workingProcess.getPageTable().exchangeInPageTable(newPage,oldPage,relateIndex);
//            //在内存交换
//            Block.cloneABlock(OS.memory.findBlockByNumber(physicalIndex),CPU.workingProcess.getTempFile().getDataBlockList().get(newPage));
//        }else {
//            short physicalIndex = (short) (-oldPage + 4*CPU.workingProcess.getPcb().getIndexInMemory() + 16);
//            //不需要替换页面的情况，直接修改页表并且存入内存相应位置
//            CPU.workingProcess.getPageTable().getTable()[newPage].setPhysicalPageNumber(physicalIndex);
//            Block.cloneABlock(OS.memory.findBlockByNumber(physicalIndex),CPU.workingProcess.getTempFile().getDataBlockList().get(newPage));
//
//        }
//
//    public void exchangeInPageTable(short newBlock, short oldBlock,short index){
//        if(oldBlock >= 0){
//            pageTable[oldBlock].setPhysicalPageNumber((short)0);
//            pageTable[newBlock].setPhysicalPageNumber((short) (16 + 4*(CPU.workingProcess.getPcb().getIndexInMemory()) + index));
//        }else {
//            pageTable[newBlock].setPhysicalPageNumber((short) (16 + 4*(CPU.workingProcess.getPcb().getIndexInMemory()) + index));
//        }
//    }
//
//    public void exchangeInPageTable(short newBlock, short oldBlock, short index, Process process){
//        if(oldBlock >= 0){
//            pageTable[oldBlock].setPhysicalPageNumber((short)0);
//            pageTable[newBlock].setPhysicalPageNumber((short) ((16 + 4*process.getPcb().getIndexInMemory()) + index));
//        }else {
//            pageTable[newBlock].setPhysicalPageNumber((short) ((16 + 4*process.getPcb().getIndexInMemory()) + index));
//        }
//    }
    }
