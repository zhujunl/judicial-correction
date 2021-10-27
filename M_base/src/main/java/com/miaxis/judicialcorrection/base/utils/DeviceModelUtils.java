package com.miaxis.judicialcorrection.base.utils;

import com.miaxis.judicialcorrection.base.BuildConfig;

public class DeviceModelUtils {

    /**
     * 设备类型
     *
     */
    public static int getDeviceType() {
        return BuildConfig.EQUIPMENT_TYPE;
    }

    /**
     * 获取设备上传类型
     * @return
     */
    public static String getDeviceUpdateName() {
        return BuildConfig.CLIENT_NAME;
    }
}
