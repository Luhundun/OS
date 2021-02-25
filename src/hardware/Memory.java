package hardware;

import control.OS;

import java.util.ArrayList;

/**
 * @ClassName: memory
 * @Description: 模拟裸机的内存，共32KB，其中系统分配8KB，用户分配24KB
 * @Author: Lu Ning
 * @Date: 2021/1/25 19:42
 * @Version: v1.0
 */
public class Memory {
    private Block[] memory;                //物理块
    private int memoryUsedBytes;           //内存使用量

    /**
     * @Description: 初始化内存,并设置开机要做的事
     * @param: []
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/21 14:25
     */
    public Memory(){
       memory = new Block[64];
       for (int i=0;i<64;i++){
           memory[i] = new Block();
       }
       memory[0] = OS.getSuperBlock().cloneABlock((short)-1);  //将硬盘的超级块深拷贝至内存
       for(int i=1;i<16;i++){
           memory[i].setBlockUsedBytes(512);          //系统部分默认全部占用
       }
    }

    /**
     * @Description: 计算内存用量
     * @param: []
     * @return: int
     * @auther: Lu Ning
     * @date: 2021/2/21 19:56
     */
    public int computeMemoryUsedBytes(){
       int sum = 0;
       for (Block e : memory){
           sum += e.blockUsedBytes;
       }
       return sum;
    }

    /**
     * @Description: 根据在内存的块号定位物理块
     * @param: [num]
     * @return: hardware.Block
     * @auther: Lu Ning
     * @date: 2021/2/22 0:50
     */
    public Block findBlockByNumber(int num){
       return memory[num];
    }


    /**
     * @Description: 从内存读一个字
     * @param: [address]
     * @return: short
     * @auther: Lu Ning
     * @date: 2021/2/22 17:29
     */
    public short readAWordInMemory(short address) throws Exception{
        if(address >= 64 * 256 || address <0){
            throw new Exception("内存位置不合法");
        }
        return OS.memory.findBlockByNumber(address/256).readAWord(address%256);
    }

    /**
     * @Description: 向内存写一个字
     * @param: [address, context]
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/22 17:32
     */
    public void readAWordInMemory(short address, short context) throws Exception{
        if(address >= 64 * 256 || address <0){
            throw new Exception("内存位置不合法");
        }
        OS.memory.findBlockByNumber(address/256).writeAWord(context, address%256);
    }

}
