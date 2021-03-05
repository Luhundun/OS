package hardware;

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
    //而本系统中，为了保证每一个软件模块的完整性，与MMU有关的方法都实现在了memoryManage包中
}
