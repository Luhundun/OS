package memoryManage;

import control.OS;
import fileManage.SuperBlock;

import java.util.ArrayList;

/**
 * @ClassName: FreeBlockManage
 * @Description:
 * @Author: Lu Ning
 * @Date: 2021/2/21 20:01
 * @Version: v1.0
 */
public class FreeBlockManage {

    /**
     * @Description: 用户进程获取内存中的空闲物理块是四个四个的。
     * @param:
     * @return:
     * @auther: Lu Ning
     * @date: 2021/2/21 20:03
     */
    public static short getAUserBlock() throws Exception{
        SuperBlock superBlock = OS.getSuperBlock();
        int i;
        if(superBlock.getFreeBlocksSumInMemory() > 0){
            boolean[][] map = superBlock.getFreeBlocksInMemoryMap();
            for(i=0; i<48 ;i++) {
                if(!map[i/16][i%16]){
                    map[i/16][i%16] = true;
                    superBlock.setFreeBlocksInMemoryMap(map);
                    superBlock.setFreeBlocksSumInMemory((short) (superBlock.getFreeBlocksSumInMemory() - 1));
                    break;
                }
            }
        }else {
            throw new Exception("内存物理空间");
        }
        return (short)(i);
    }

    /**
     * @Description: 在GUI展示内存中的空闲块
     * @param: []
     * @return: Object[]
     * @auther: Lu Ning
     * @date: 2021/2/21 20:46
     */
    public static Object[] showFreeBlockList(){
        ArrayList<Short> returnList = new ArrayList<>();
        boolean[][] map = OS.getSuperBlock().getFreeBlocksInMemoryMap();
        for (int i=0;i<3;i++){
            for (int j=0;j<16;j++){
                if(!map[i][j]){
                    returnList.add((short) (i*16+j+16));
                }
            }
        }
        return returnList.toArray();
    }
}
