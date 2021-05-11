package com.miaxis.enroll.vo;

/**
 * Family
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class Family {
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
