package com.miaxis.judicialcorrection.base.api.vo;

/**
 * @author yawei
 * @data on 2018/7/16 上午9:59
 * @email zyawei@live.com
 */
public class User {
    private int id;
    private String xm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", xm='" + xm + '\'' +
                '}';
    }
}
