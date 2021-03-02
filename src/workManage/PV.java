package workManage;

/**
 * @ClassName: PV
 * @Description: 关于PV的方法集合
 * @Author: luning
 * @Date: 2021/3/3 12:50
 * @Version: v1.0
 */
public class PV {
    short value = 1;
    public static PV keyboard;
    public static PV mouse;
    public static PV screen;

    /**
     * @Description: 初始化PV信号量
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 12:55
     */
    public static void initPV(){
       keyboard = new PV();
       mouse = new PV();
       screen = new PV();
    }

    /**
     * @Description: P操作
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 15:31
     */
    public static void P(PV value, Process process){

    }

    /**
     * @Description: V操作
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/3 15:31
     */
    public static void V(PV value, Process process){

    }

}
