package com.miaxis.enroll.vo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * OtherInfo
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class Addr extends BaseObservable {


    public String hjszsName;
    public String hjszdsName;
    public String hjszxqName;
    public String hjszdName;
    public String hjszdmx;


    public String gdjzdszsName;
    public String gdjzdszdsName;
    public String gdjzdszxqName;
    public String gdjzdName;
    public String gdjzdmx;


    public String getHjszsName() {
        return hjszsName;
    }

    public void setHjszsName(String hjszsName) {
        this.hjszsName = hjszsName;
    }

    public String getHjszdsName() {
        return hjszdsName;
    }

    public void setHjszdsName(String hjszdsName) {
        this.hjszdsName = hjszdsName;
    }

    public String getHjszxqName() {
        return hjszxqName;
    }

    public void setHjszxqName(String hjszxqName) {
        this.hjszxqName = hjszxqName;
    }

    public String getHjszdName() {
        return hjszdName;
    }

    public void setHjszdName(String hjszdName) {
        this.hjszdName = hjszdName;
    }

    public String getHjszdmx() {
        return hjszdmx;
    }

    public void setHjszdmx(String hjszdmx) {
        this.hjszdmx = hjszdmx;
    }

    public String getGdjzdszsName() {
        return gdjzdszsName;
    }

    public void setGdjzdszsName(String gdjzdszsName) {
        this.gdjzdszsName = gdjzdszsName;
    }

    public String getGdjzdszdsName() {
        return gdjzdszdsName;
    }

    public void setGdjzdszdsName(String gdjzdszdsName) {
        this.gdjzdszdsName = gdjzdszdsName;
    }

    public String getGdjzdszxqName() {
        return gdjzdszxqName;
    }

    public void setGdjzdszxqName(String gdjzdszxqName) {
        this.gdjzdszxqName = gdjzdszxqName;
    }

    public String getGdjzdName() {
        return gdjzdName;
    }

    public void setGdjzdName(String gdjzdName) {
        this.gdjzdName = gdjzdName;
    }

    public String getGdjzdmx() {
        return gdjzdmx;
    }

    public void setGdjzdmx(String gdjzdmx) {
        this.gdjzdmx = gdjzdmx;
    }

    @Override
    public String toString() {
        return "Addr{" +
                "hjszsName='" + hjszsName + '\'' +
                ", hjszdsName='" + hjszdsName + '\'' +
                ", hjszxqName='" + hjszxqName + '\'' +
                ", hjszdName='" + hjszdName + '\'' +
                ", hjszdmx='" + hjszdmx + '\'' +
                ", gdjzdszsName='" + gdjzdszsName + '\'' +
                ", gdjzdszdsName='" + gdjzdszdsName + '\'' +
                ", gdjzdszxqName='" + gdjzdszxqName + '\'' +
                ", gdjzdName='" + gdjzdName + '\'' +
                ", gdjzdmx='" + gdjzdmx + '\'' +
                '}';
    }
}
