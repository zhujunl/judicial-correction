package com.miaxis.enroll.vo;

/**
 * Job
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class Job {
    public String startTime;
    public String endTime;
    public String company;
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
