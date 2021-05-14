package com.miaxis.enroll.vo;

import androidx.databinding.BaseObservable;

/**
 * Family
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class Family extends BaseObservable {
    public String name;
    public String relationship;
    public String job;
    public String phone;
    public String pid;

    @Override
    public String toString() {
        return "Family{" +
                "name='" + name + '\'' +
                ", relationship='" + relationship + '\'' +
                ", job='" + job + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
