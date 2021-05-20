package com.miaxis.enroll.vo;

import androidx.databinding.BaseObservable;

import com.miaxis.judicialcorrection.base.databinding.kvsp.KvSpinnerVo;

/**
 * OtherInfo
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class OtherInfo extends BaseObservable {
    // 是否有精神病
    public String sfyjsb;
    // 是否有传染病
    public String sfycrb;
    //sfswry
    public String sfswry;
    /*是否有前科*/
    public String sfyqk;



    public transient KvSpinnerVo hyzkKV;
    // 婚姻状况
    public String hyzk;
    public String hyzkName;

    public transient KvSpinnerVo whcdKV;
    // 文化程度
    public String whcd;
    public String whcdName;

    public transient KvSpinnerVo jyjxqkKV;
    // 就业就学情况
    public String jyjxqk;
    public String jyjxqkName;


    // 个人联系电话
    public String grlxdh;
    public transient KvSpinnerVo xzzmnKV;
    //现政治面貌
    public String xzzmn;
    public String xzzmnName;


    public transient KvSpinnerVo yzzmmKV;
    //原政治面貌
    public String yzzmm;
    public String yzzmmName;

    public transient KvSpinnerVo gjKV;
    //国籍
    public String gj;
    public String gjName;


    //原工作单位
    public String ygzdw;
    //现工作单位中文指
    public String xgzdw;


    //单位联系电话
    public String dwlxdh;


    public KvSpinnerVo getWhcdKV() {
        return whcdKV;
    }

    public void setWhcdKV(KvSpinnerVo whcdKV) {
        this.whcdKV = whcdKV;
        whcd = whcdKV.key;
        whcdName = whcdKV.value;
    }


    public KvSpinnerVo getHyzkKV() {
        return hyzkKV;
    }

    public void setHyzkKV(KvSpinnerVo hyzkKV) {
        this.hyzkKV = hyzkKV;
        this.hyzk = hyzkKV.key;
        this.hyzkName = hyzkKV.value;
    }

    public KvSpinnerVo getJyjxqkKV() {
        return jyjxqkKV;
    }

    public void setJyjxqkKV(KvSpinnerVo jyjxqkKV) {
        this.jyjxqkKV = jyjxqkKV;
        this.jyjxqk = jyjxqkKV.key;
        this.jyjxqkName = jyjxqkKV.value;
    }

    public KvSpinnerVo getXzzmnKV() {
        return xzzmnKV;
    }

    public void setXzzmnKV(KvSpinnerVo xzzmnKV) {
        this.xzzmnKV = xzzmnKV;
        this.xzzmn = xzzmnKV.key;
        this.xzzmnName = xzzmnKV.value;
    }

    public KvSpinnerVo getYzzmmKV() {
        return yzzmmKV;
    }

    public void setYzzmmKV(KvSpinnerVo yzzmmKV) {
        this.yzzmmKV = yzzmmKV;
        this.yzzmm = yzzmmKV.key;
        this.yzzmmName = yzzmmKV.value;
    }

    public KvSpinnerVo getGjKV() {
        return gjKV;
    }

    public void setGjKV(KvSpinnerVo gjKV) {
        this.gjKV = gjKV;
        this.gj = gjKV.key;
        this.gjName = gjKV.value;
    }


    @Override
    public String toString() {
        return "OtherInfo{" +
                "sfyjsb=" + sfyjsb +
                ", sfycrb=" + sfycrb +
                ", hyzkKV=" + hyzkKV +
                ", hyzk='" + hyzk + '\'' +
                ", hyzkName='" + hyzkName + '\'' +
                ", whcdKV=" + whcdKV +
                ", whcd='" + whcd + '\'' +
                ", whcdName='" + whcdName + '\'' +
                ", jyjxqkKV=" + jyjxqkKV +
                ", jyjxqk='" + jyjxqk + '\'' +
                ", jyjxqkName='" + jyjxqkName + '\'' +
                ", grlxdh='" + grlxdh + '\'' +
                ", xzzmnKV=" + xzzmnKV +
                ", xzzmn='" + xzzmn + '\'' +
                ", xzzmnName='" + xzzmnName + '\'' +
                ", yzzmmKV=" + yzzmmKV +
                ", yzzmm='" + yzzmm + '\'' +
                ", yzzmmName='" + yzzmmName + '\'' +
                ", gjKV=" + gjKV +
                ", gj='" + gj + '\'' +
                ", gjName='" + gjName + '\'' +
                ", ygzdw='" + ygzdw + '\'' +
                ", xgzdwName='" + xgzdw + '\'' +
                ", dwlxdh='" + dwlxdh + '\'' +
                ", sfswry=" + sfswry +
                ", sfyqk=" + sfyqk +
                '}';
    }
}
