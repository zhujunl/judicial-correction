package com.miaxis.judicialcorrection.face.bean;

/**
 * @author Tank
 * @date 2021/5/6 19:53
 * @des
 * @updateAuthor
 * @updateDes
 */
public class VerifyInfo {

    public String name;
    public String idCardNumber;

    private VerifyInfo() {
    }

    public VerifyInfo(String name, String idCardNumber) {
        this.name = name;
        this.idCardNumber = idCardNumber;
    }

}
