package com.miaxis.judicialcorrection.base.download;

/**
 *  MR990 手持平板 位于land_p分支
 *  MR960-T 立式一体机 位于land分支
 *  MR960-S 海之青双面屏 位于fingerHid分支（指纹模块通信方式为HID，故单独处于一个分支）
 *  MR960-S 老旭辉双面屏 位于land分支
 */
public enum DownLoadConfig {

    MR990("MR-990"),

    MR960_T("MR-960-T"),

    MR960_S("MR-960-S"),

    MR960_S_XH("MR-960-SXH");

    public String type;

    DownLoadConfig(String type) {
        this.type = type;
    }
}
