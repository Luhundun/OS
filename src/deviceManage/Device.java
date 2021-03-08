package deviceManage;

import fileManage.File;

/**
 * @ClassName: Device
 * @Description: 设备类
 * @Author: luning
 * @Date: 2021/3/6 12:00
 * @Version: v1.0
 */
public class Device {
    private String deviceName;
    private short deviceId;      //设备id号（标志符）
    private File deviceFile;     //设备所需要在外存占据的空间，这里用文件表示，实际储存以文件ino形式储存
    private short deviceDealTime;  //设备处理所需的时间（秒），这里按平均花费时间来模拟

    public Device(String name, short id, short time){
        deviceName = name;
        deviceId = id;
        deviceDealTime = time;
        SystemDeviceTable.systemDeviceTable.add(new SystemDeviceTable.SystemDeviceTableItem((short) 0, this));
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public short getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(short deviceId) {
        this.deviceId = deviceId;
    }

    public File getDeviceFile() {
        return deviceFile;
    }

    public void setDeviceFile(File deviceFile) {
        this.deviceFile = deviceFile;
    }

    public short getDeviceDealTime() {
        return deviceDealTime;
    }

    public void setDeviceDealTime(short deviceDealTime) {
        this.deviceDealTime = deviceDealTime;
    }
}
