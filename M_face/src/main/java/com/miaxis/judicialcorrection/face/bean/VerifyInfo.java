package com.miaxis.judicialcorrection.face.bean;

/**
 * @author Tank
 * @date 2021/5/6 19:53
 * @des
 * @updateAuthor
 * @updateDes
 */
public class VerifyInfo {

    public String pid="";
    public String name="";
    public String idCardNumber="";

    private VerifyInfo() {
    }

    public VerifyInfo(String pid, String name, String idCardNumber) {
        this.pid = pid;
        this.name = name;
        this.idCardNumber = idCardNumber;
    }

}
