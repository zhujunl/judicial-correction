package com.miaxis.enroll.vo;

import com.google.gson.annotations.SerializedName;

/**
 * Job
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class Job {

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
