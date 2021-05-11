package com.miaxis.enroll.vo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.miaxis.enroll.BR;

import timber.log.Timber;

/**
 * OtherInfo
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class OtherInfo  extends BaseObservable {
    public  int sfyjsb;         public  int sfycrb;

    public  String whcd;        public  String hyzk;

    public  String grlxdh;      public  String jyjxqk;

    public  String xzzmn;       public  String yzzmm;


    public  String ygzdw;       public  String xgzdwName;


    public  String dwlxdh;      public  String gjName;

    public  int sfswry;         public  int sfyqk;/*是否有前科*/

    //@Bindable
    public int getSfyjsb() {
        return sfyjsb;
    }

    public void setSfyjsb(int sfyjsb) {
        this.sfyjsb = sfyjsb;
        //notifyPropertyChanged(BR.sfyjsb);
        // 这里配套添加bindable可以实现livedata的观察者自动更新，但是要手动实现一下livedata
    }

    //@Bindable
    public int getSfycrb() {
        return sfycrb;
    }

    public void setSfycrb(int sfycrb) {
        this.sfycrb = sfycrb;
        //notifyPropertyChanged(BR.sfycrb);
    }

    public String getWhcd() {
        Timber.i("getWhcd %s",whcd);
        return whcd;
    }

    public void setWhcd(String whcd) {
        this.whcd = whcd;
        Timber.i("setWhcd %s",whcd);
    }

    public String getHyzk() {
        return hyzk;
    }

    public void setHyzk(String hyzk) {
        this.hyzk = hyzk;
    }

    public String getGrlxdh() {
        return grlxdh;
    }

    public void setGrlxdh(String grlxdh) {
        this.grlxdh = grlxdh;
    }

    public String getJyjxqk() {
        return jyjxqk;
    }

    public void setJyjxqk(String jyjxqk) {
        this.jyjxqk = jyjxqk;
    }

    public String getXzzmn() {
        return xzzmn;
    }

    public void setXzzmn(String xzzmn) {
        this.xzzmn = xzzmn;
    }

    public String getYzzmm() {
        return yzzmm;
    }

    public void setYzzmm(String yzzmm) {
        this.yzzmm = yzzmm;
    }

    public String getYgzdw() {
        return ygzdw;
    }

    public void setYgzdw(String ygzdw) {
        this.ygzdw = ygzdw;
    }

    public String getXgzdwName() {
        return xgzdwName;
    }

    public void setXgzdwName(String xgzdwName) {
        this.xgzdwName = xgzdwName;
    }

    public String getDwlxdh() {
        return dwlxdh;
    }

    public void setDwlxdh(String dwlxdh) {
        this.dwlxdh = dwlxdh;
    }

    public String getGjName() {
        return gjName;
    }

    public void setGjName(String gjName) {
        this.gjName = gjName;
    }

//    @Bindable
    public int getSfswry() {
        return sfswry;
    }

    public void setSfswry(int sfswry) {
        this.sfswry = sfswry;
        //notifyPropertyChanged(BR.sfswry);
    }
//    @Bindable
    public int getSfyqk() {
        return sfyqk;
    }

    public void setSfyqk(int sfyqk) {
        this.sfyqk = sfyqk;
        //notifyPropertyChanged(BR.sfyqk);
    }

    @Override
    public String toString() {
        return "OtherInfo{" +
                "sfyjsb=" + sfyjsb +
                ", sfycrb=" + sfycrb +
                ", whcd='" + whcd + '\'' +
                ", hyzk='" + hyzk + '\'' +
                ", grlxdh='" + grlxdh + '\'' +
                ", jyjxqk='" + jyjxqk + '\'' +
                ", xzzmn='" + xzzmn + '\'' +
                ", yzzmm='" + yzzmm + '\'' +
                ", ygzdw='" + ygzdw + '\'' +
                ", xgzdwName='" + xgzdwName + '\'' +
                ", dwlxdh='" + dwlxdh + '\'' +
                ", gjName='" + gjName + '\'' +
                ", sfswry=" + sfswry +
                ", sfyqk=" + sfyqk +
                '}';
    }
}
