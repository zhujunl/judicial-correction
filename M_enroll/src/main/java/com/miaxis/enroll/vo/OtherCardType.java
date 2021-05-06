package com.miaxis.enroll.vo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.miaxis.enroll.BR;

/**
 * OtherCardType
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
// 1.有，0无
public class OtherCardType extends BaseObservable {
    //有无港澳台通行证
    public int ywgatsfz;
    // 有无台胞证
    public int ywtbz;
    //有无护照
    public int ywhz;
    //有无港澳台通行证
    public int ywgattxz;

    @Bindable
    public int getYwgatsfz() {
        return ywgatsfz;
    }

    public void setYwgatsfz(int ywgatsfz) {
        this.ywgatsfz = ywgatsfz;
        notifyPropertyChanged(BR.ywgatsfz);
    }

    @Bindable
    public int getYwtbz() {
        return ywtbz;
    }

    public void setYwtbz(int ywtbz) {
        this.ywtbz = ywtbz;
        notifyPropertyChanged(BR.ywtbz);
    }

    @Bindable
    public int getYwhz() {
        return ywhz;
    }

    public void setYwhz(int ywhz) {
        this.ywhz = ywhz;
        notifyPropertyChanged(BR.ywhz);
    }

    @Bindable
    public int getYwgattxz() {
        return ywgattxz;
    }

    public void setYwgattxz(int ywgattxz) {
        this.ywgattxz = ywgattxz;
        notifyPropertyChanged(BR.ywgattxz);
    }

    @Override
    public String toString() {
        return "OtherCardType{" +
                "ywgatsfz=" + ywgatsfz +
                ", ywtbz=" + ywtbz +
                ", ywhz=" + ywhz +
                ", ywgattxz=" + ywgattxz +
                '}';
    }
}