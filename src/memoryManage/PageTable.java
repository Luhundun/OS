package memoryManage;

import control.OS;
import hardware.Block;

import java.util.ArrayList;

/**
 * @ClassName: PageTable
 * @Description: 有关页表的类，其中模拟MMU的方法大部分在此类中实现
 *               一个页表设定为1块，即512B，一个进程对应一张。在此进程为运行态时加载到系统页表，其他时候驻留外存。
 *               系统级页表常驻系统内存，在内存的第7块
 * @Author: Lu Ning
 * @Date: 2021/2/26 16:13
 * @Version: v1.0
 */
public class PageTable {
    private PageTableItem[] pageTable;
    public static PageTable systemPageTable;

    /**
     * @Description: 页表初始化
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
}
