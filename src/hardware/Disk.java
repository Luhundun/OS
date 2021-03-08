package hardware;

import control.OS;

import java.io.*;

/**
 * @ClassName: Disk
 * @Description: 模拟裸机的硬盘
 * @Author: Lu Ning
 * @Date: 2021/1/25 19:43
 * @Version: v1.0
 */
public class Disk {
    private Block[][][] disk;

    private int diskUsedBytes;

//    public static void main(String[] args) {
//        Disk disk = new Disk();
//        disk.loadDisk();
//        System.out.println(disk.disk[0][0][0].showInFile());
//    }

    public Disk(){
        disk = new Block[10][32][64];
        for(int i = 0; i < 10; i++){
            for (int j = 0; j < 32; j++){
                for (int k = 0; k < 64; k++){
                    disk[i][j][k] = new Block(i,j,k);
                }
            }
        }
        findBlockByDno((short) 0).setBlockUsedBytes(512);
        findBlockByDno((short) 1).setBlockUsedBytes(512);  //前两块初始占用
    }


    /**
     * @Description:  制作硬盘镜像文件
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/29 17:27
     */
    public void saveDisk() {
        try {
            File mirror = new File(OS.virtualDiskMirrorPath);
            mirror.createNewFile();
            //清空原镜像
            FileWriter tempWrite = new FileWriter(mirror);
            tempWrite.write("");
            tempWrite.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(mirror, true));
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 32; j++) {
                    for (int k = 0; k < 64; k++) {
                        writer.write("cylinder" + i + " track" + j + " sector" + k +"\n");
                        writer.write(disk[i][j][k].showInFile()+"\n");
                    }
                }
            }
            System.out.println(findBlockByDno((short)148).showInFile());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    /**
//     * @Description:  将模拟的硬盘以文件形式展示
//     * @param: []
//     * @return: void
//     * @auther: Lu Ning
//     * @date: 2021/1/29 17:27
//     */
//    public void saveDisk() {
//        try {
//            for (int i = 0; i < 10; i++) {
//                File cylinder = new File(OS.virtualDiskRootPath + "clinder_" + i);
//                if (!cylinder.exists()) {
//                    cylinder.mkdir();
//                }
//                for (int j = 0; j < 32; j++) {
//                    File track = new File(OS.virtualDiskRootPath + "clinder_" + i + File.separator + "track" + j);
//                    if (!track.exists()) {
//                        track.mkdir();
//                    }
//                    for (int k = 0; k < 64; k++) {
//                        File sector = new File(OS.virtualDiskRootPath + "clinder_" + i + File.separator + "track" + j + File.separator + "sector" + k + ".txt");
//                        if (!sector.exists()) {
//                            sector.createNewFile();
//                        }
//                        FileWriter fileWriter = new FileWriter(sector);
//                        fileWriter.write(disk[i][j][k].showInFile());
//                        fileWriter.close();
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    /**
     * @Description:  将镜像加载到操作系统
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/1/29 19:53
     */
    public void loadDisk() throws Exception{
        File mirror = new File(OS.virtualDiskMirrorPath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(mirror));
        if (!mirror.exists()){
            throw new Exception("镜像文件不存在");
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 32; j++) {
                for (int k = 0; k < 64; k++) {
                    bufferedReader.readLine();
                    disk[i][j][k].loadBlock(bufferedReader.readLine());
                }
            }
        }
    }

//    /**
//     * @Description:  将模拟硬盘文件加载到操作系统
//     * @param: []
//     * @return: void
//     * @auther: Lu Ning
//     * @date: 2021/1/29 19:53
//     */
//    public void loadDisk() {
//        try {
//            for (int i = 0; i < 10; i++) {
//                File cylinder = new File(OS.virtualDiskRootPath + "clinder_" + i);
//                if (!cylinder.exists()) {
//                    throw new IOException("路径错误");
//                }
//                for (int j = 0; j < 32; j++) {
//                    File track = new File(OS.virtualDiskRootPath + "clinder_" + i + File.separator + "track" + j);
//                    if (!track.exists()) {
//                        throw new IOException("路径错误");                    }
//                    for (int k = 0; k < 64; k++) {
//                        File sector = new File(OS.virtualDiskRootPath + "clinder_" + i + File.separator + "track" + j + File.separator + "sector" + k + ".txt");
//                        if (!sector.exists()) {
//                            throw new IOException("路径错误");                        }
//                        BufferedReader bufferedReader = new BufferedReader(new FileReader(sector));
//                        disk[i][j][k].loadBlock(bufferedReader.readLine());
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * @Description: 通过盘块号定位到对应的磁盘
     * @param: [dno]
     * @return: hardware.Block
     * @auther: Lu Ning
     * @date: 2021/1/29 23:47
     */
    public Block findBlockByDno(short dno){
        return disk[dno/(32*64)][dno/64%32][dno%64];
    }


    /**
     * @Description: 写磁盘某一块的内容
     * @param: [dno, block]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/1 17:34
     */
    public void writeWholeBlock(short dno, Block block){
        disk[dno/(32*64)][dno/64%32][dno%64] = block;
    }


    /**
     * @Description: 计算磁盘使用空间
     * @param: []
     * @return: int
     * @auther: Lu Ning
     * @date: 2021/2/17 14:10
     */
    public int computeDiskUsedBytes(){
        int sum = 0;
        for(Block[][] cylinder : disk){
            for (Block[] track : cylinder){
                for (Block sector : track){
                    sum += sector.blockUsedBytes;
                }
            }
        }
        diskUsedBytes = sum;
        return sum;
    }

    /**
     * @Description: 逻辑意义上的交换一个块
     * @param: [block]
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/2/17 14:11
     */
    public void exchangeBlock(int dno,Block block){
        disk[dno/(32*64)][dno/64%32][dno%64] = block;
    }
}







//public class Disk {
//    Track[] disk = new Track[10];
//    int diskUsedBytes = 0;
//}

///**
// * @ClassName: Track
// * @Description: 模拟裸机的一个柱面
// * @Author: Lu Ning
// * @Date: 2021/1/27 17:02
// * @Version: v1.0
// */
//class Track {//
//    Sector[] track = new Sector[32];
//    int freeNumber = 32;
//    int trackUsedBytes = 0;
//}
//
///**
// * @ClassName: Sector
// * @Description: 模拟裸机的一个磁道
// * @Author: Lu Ning
// * @Date: 2021/1/27 17:21
// * @Version: v1.0
// */
//class Sector {
//    Block[] sector = new Block[32];
//    int freeNumber = 32;
//    int sectorUsedBytes = 0;
//
//    public void swapAsBlock(Block block){
//        ;
//    }
//}
