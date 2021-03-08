package deviceManage;

import control.GUI;
import hardware.Block;

import java.util.ArrayList;

/**
 * @ClassName: SystemDeviceTable
 * @Description: 系统设备表
 * @Author: luning
 * @Date: 2021/3/6 12:17
 * @Version: v1.0
 */
public class SystemDeviceTable {
    public static ArrayList<SystemDeviceTableItem> systemDeviceTable = new ArrayList<>();

    /**
     * @Description: 系统设备表的表项类
     * @param:
     * @return:
     * @auther: Lu Ning
     * @date: 2021/3/6 15:17
     */
    public static class SystemDeviceTableItem{
        private short deviceType;           //设备类型
        private short deviceAddress;        //中断地址
        private Device devicePoint;         //设备表指针
        
        /**
         * @Description: 系统设备表项的构造函数 
         * @param: [deviceType, deviceAddress, devicePoint]
         * @return: 
         * @auther: Lu Ning
         * @date: 2021/3/6 12:22
         */
        public SystemDeviceTableItem(short deviceType, Device devicePoint) {
            this.deviceType = deviceType;
            this.deviceAddress = (short) (15360 + 64 * devicePoint.getDeviceId() );
            this.devicePoint = devicePoint;
        }

        public short getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(short deviceType) {
            this.deviceType = deviceType;
        }

        public short getDeviceAddress() {
            return deviceAddress;
        }

        public void setDeviceAddress(short deviceAddress) {
            this.deviceAddress = deviceAddress;
        }

        public Device getDevicePoint() {
            return devicePoint;
        }

        public void setDevicePoint(Device devicePoint) {
            this.devicePoint = devicePoint;
        }
    }

    /**
     * @Description: 根据设备号查找设备名
     * @param: [id]
     * @return: java.lang.String
     * @auther: Lu Ning
     * @date: 2021/3/6 15:21
     */
    public static String getDeviceNameById(short id) throws Exception {
        if (id >= systemDeviceTable.size()){
            throw new Exception("找不到此设备");
        }
        for (SystemDeviceTableItem item : systemDeviceTable){
            if(item.getDevicePoint().getDeviceId() == id){
                return item.getDevicePoint().getDeviceName();
            }
        }
        throw new Exception("找不到此设备");
    }

    /**
     * @Description: 在GUI展示设备信息
     * @param: []
     * @return: void
     * @auther: Lu Ning
     * @date: 2021/3/6 16:03
     */
    public static void showSystemDeviceTable(){
        GUI.d01.setText(systemDeviceTable.get(0).devicePoint.getDeviceName());
        GUI.d02.setText(String.valueOf(systemDeviceTable.get(0).devicePoint.getDeviceDealTime()));
        GUI.d03.setText(Block.convertShortToAddress(systemDeviceTable.get(0).getDeviceAddress()));
        GUI.d11.setText(systemDeviceTable.get(1).devicePoint.getDeviceName());
        GUI.d12.setText(String.valueOf(systemDeviceTable.get(1).devicePoint.getDeviceDealTime()));
        GUI.d13.setText(Block.convertShortToAddress(systemDeviceTable.get(2).getDeviceAddress()));
        GUI.d21.setText(systemDeviceTable.get(2).devicePoint.getDeviceName());
        GUI.d22.setText(String.valueOf(systemDeviceTable.get(2).devicePoint.getDeviceDealTime()));
        GUI.d23.setText(Block.convertShortToAddress(systemDeviceTable.get(2).getDeviceAddress()));
        GUI.d31.setText(systemDeviceTable.get(3).devicePoint.getDeviceName());
        GUI.d32.setText(String.valueOf(systemDeviceTable.get(3).devicePoint.getDeviceDealTime()));
        GUI.d33.setText(Block.convertShortToAddress(systemDeviceTable.get(3).getDeviceAddress()));
    }
}
