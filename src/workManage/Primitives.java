package workManage;

import fileManage.File;

/**
 * @ClassName: Primitives
 * @Description: 集中了各种原语的静态方法
 * @Author: luning
 * @Date: 2021/3/1 09:37
 * @Version: v1.0
 */
public class Primitives {


    /**
     * @Description: 创建进程原语
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/1 10:40
     */
    public synchronized static void init(File file){
        if(PCB.pcbIsFull()){
//            Queues.jobReadyQueue.add();
        }else {

        }
    }
}
