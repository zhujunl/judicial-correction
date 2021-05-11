package com.miaxis.judicialcorrection.base.db.po;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Place
 * 全国行政区实体类
 *
 * @author tangkai
 * Created on 4/29/21.
 */
@Entity(tableName = "PLACE")
public class Place {
    /**
     * 主键(ID)
     * 省市区街道名称(VALUE)
     * 所属父级ID(VALUE)   null:表示省/直辖市
     * 地区级别
     */

    @PrimaryKey(autoGenerate = true)
    public int ID;
    public String VALUE;
    public int PARENT_ID;
    public int LEVEL;
    public int ZXS;
    public int ZGX;

    @Override
    public String toString() {
        return "Place{" +
                "ID=" + ID +
                ", VALUE='" + VALUE + '\'' +
                ", PARENT_ID=" + PARENT_ID +
                ", LEVEL=" + LEVEL +
                ", ZXS=" + ZXS +
                ", ZGX=" + ZGX +
                '}';
    }

}
