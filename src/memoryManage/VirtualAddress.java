package memoryManage;

import hardware.Block;

/**
 * @ClassName: VirtualAddress
 * @Description: 每个进程所对应的虚地址，裸机地址总线为16位，因此最大虚地址空间位64k。一个物理块512B，因此大小为128个物理块
 * @Author: Lu Ning
 * @Date: 2021/1/28 0:50
 * @Version: v1.0
 */
public class VirtualAddress {
    Block[] blocks = new Block[128];
}
