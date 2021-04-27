package com.miaxis.judicialcorrection.base.db.po;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainFunc mainFunc = (MainFunc) o;
        return id == mainFunc.id &&
                resId == mainFunc.resId &&
                active == mainFunc.active &&
                Objects.equals(title, mainFunc.title) &&
                Objects.equals(targetActivityURI, mainFunc.targetActivityURI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, resId, title, targetActivityURI, active);
    }
}
