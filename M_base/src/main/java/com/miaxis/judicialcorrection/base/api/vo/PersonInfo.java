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
    private String idCardNumber;

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

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    @Override
    public String toString() {
        return "PersonInfo{" +
                "id='" + id + '\'' +
                ", xm='" + xm + '\'' +
                ", idCardNumber='" + idCardNumber + '\'' +
                '}';
    }
}
