package com.miaxis.enroll.vo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.google.gson.annotations.SerializedName;
import com.miaxis.enroll.BR;

/**
 * Job
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class Job  {

    @SerializedName("pid")
    public String pid;
    @SerializedName("qs")
    public String startTime;
    @SerializedName("zr")
    public String endTime;
    @SerializedName("szdw")
    public String company;
    @SerializedName("zw")
    public String job;

//    社区矫正人员标识	pid	string
//    起时	qs	date
//    止时	zr	date
//    所在单位（所在地）	szdw	string
//    职务	zw	string


    public  ObservableField<String> st = new ObservableField<>();
    public  ObservableField<String> et = new ObservableField<>();
    public  ObservableField<String> cy = new ObservableField<>();
    public  ObservableField<String> jb = new ObservableField<>();
    @Override
    public String toString() {
        return "Job{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", company='" + company + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
