package com.miaxis.judicialcorrection.base.db.po;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * MainFuncItem
 *
 * @author zhangyw
 * Created on 4/27/21.
 */
@Entity(tableName = "main_func")
public class MainFunc {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int resId;
    public String title;
    public String targetActivityURI;
    public boolean active = true;

    public MainFunc() {
    }

    public MainFunc(String title, int resId, String targetActivityURI, boolean isActive) {
        this.resId = resId;
        this.title = title;
        this.targetActivityURI = targetActivityURI;
        this.active = isActive;
    }

    @Override
    public String toString() {
        return "MainFunc{" +
                "id=" + id +
                ", resId=" + resId +
                ", title='" + title + '\'' +
                ", targetActivityURI='" + targetActivityURI + '\'' +
                ", active=" + active +
                '}';
    }
}
