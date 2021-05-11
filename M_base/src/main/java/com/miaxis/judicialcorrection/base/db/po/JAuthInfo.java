package com.miaxis.judicialcorrection.base.db.po;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * JAuthInfo
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
@Entity(tableName = "auth_info")
public class JAuthInfo {
    /**
     * 必填字段：激活码（activationCode，激活码见文档）
     * 设备所在地联系人（contact），
     * 设备所在地联系电话（contactInformation），
     * vendor（设备厂商），
     * 设备类型（deviceType），
     * 设备所在地市Id（dishiId），
     * 设备所在地市Name（dishiName），
     * 设备所在区县Id（quxianId），
     * 设备所在区县Name（quxianName），
     * 设备所在街道Id（jiedaoId）,
     * 设备所在街道Name（jiedaoName），
     * 设备名称（clientName），
     * 设备所在详细地址（loc）,
     * 当前应用 版本（currentVersion）
     * <p>
     * 注:若设备属于区县司法局，则jiedaoId与jiedaoName需设为空字符串
     */

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String vendor = "zkja";
    public String deviceType = "台式";

    public String activationCode;
    public String contact = "测试";
    public String contactInformation = "13300000000";

    public String dishiId;
    public String dishiName;
    public String quxianId;
    public String quxianName;

    public String jiedaoId;
    public String jiedaoName;


    @Override
    public String toString() {
        return "JAuthInfo{" +
                "id=" + id +
                ", vendor='" + vendor + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", activationCode='" + activationCode + '\'' +
                ", contact='" + contact + '\'' +
                ", contactInformation='" + contactInformation + '\'' +
                ", dishiId='" + dishiId + '\'' +
                ", dishiName='" + dishiName + '\'' +
                ", quxianId='" + quxianId + '\'' +
                ", quxianName='" + quxianName + '\'' +
                ", jiedaoId='" + jiedaoId + '\'' +
                ", jiedaoName='" + jiedaoName + '\'' +
                '}';
    }
}
