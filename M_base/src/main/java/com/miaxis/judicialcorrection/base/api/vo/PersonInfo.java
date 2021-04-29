package com.miaxis.judicialcorrection.base.api.vo;

/**
 * Person
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
public class PersonInfo {
    private String id;
    private String xm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return "Person{" +
                "id='" + id + '\'' +
                ", xm='" + xm + '\'' +
                '}';
    }
}
