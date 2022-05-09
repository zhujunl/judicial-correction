package com.miaxis.utils;

/**
 * @author ZJL
 * @date 2022/5/9 13:58
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DeviceUtils {

    /**
     * 获取手机型号
     */
    public static String getDeviceModel() {
        return android.os.Build.MODEL;//CB005-HZQ2
    }

    /**
     * 获取手机主板名
     */
    public static String getDeviceBoard() {
        return android.os.Build.BOARD;
    }

    /**
     * 设备名
     * **/
    public static String getDeviceDevice() {
        return android.os.Build.DEVICE;
    }

}
