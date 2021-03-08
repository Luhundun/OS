package memoryManage;

import control.GUI;
import control.OS;
import hardware.Block;
import hardware.CPU;

/**
 * @ClassName: missingPageInterrupt
 * @Description: 缺页中断相关的算法，能引起缺页中断必定是运行态的进程
 * @Author: luning
 * @Date: 2021/3/3 18:26
 * @Version: v1.0
 */
public class MissingPageInterrupt extends Throwable{

    /**
     * @Description: 加载新页(逻辑页)至内存
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 18:28
     */
    public static void loadNewPage(short newPage, short oldPage) throws Exception {
//        checkInterruptValid();
        //确定交换的位置
        if(oldPage >= 0){
            //需要替换页面的情况
            short physicalIndex = CPU.workingProcess.getPageTable().getTable()[oldPage].getPhysicalPageNumber();
            short relateIndex = (short) (physicalIndex  - 16 - 4 * CPU.workingProcess.getPcb().getIndexInMemory());
            //在页表记录
            CPU.workingProcess.getPageTable().exchangeInPageTable(newPage,oldPage,relateIndex);
            //在内存交换
            Block.cloneABlock(OS.memory.findBlockByNumber(physicalIndex),CPU.workingProcess.getTempFile().getDataBlockList().get(newPage));
            GUI.outInfoArea.append("逻辑页面"+oldPage+"被换出，新页面"+newPage+"换入到物理页号"+physicalIndex+"\n");
        }else {
            short physicalIndex = (short) (-oldPage + 4*CPU.workingProcess.getPcb().getIndexInMemory() + 16);
            //不需要替换页面的情况，直接修改页表并且存入内存相应位置
            CPU.workingProcess.getPageTable().getTable()[newPage].setPhysicalPageNumber(physicalIndex);
            Block.cloneABlock(OS.memory.findBlockByNumber(physicalIndex),CPU.workingProcess.getTempFile().getDataBlockList().get(newPage));
            GUI.outInfoArea.append("新页面"+newPage+"载入到物理页号"+physicalIndex+"\n");
        }

    }

    /**
     * @Description: 检查缺页中断合法性
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 18:30
     */
    public static void checkInterruptValid() throws Exception {
        if(CPU.workingProcess == null){
            throw new Exception("不存在正在运行的进程");
        }
//        if(CPU.workingProcess.getPcb().getProcessState() != 2){
//            throw new Exception("非正在运行进程申请缺页中断");
//        }
    }
}
