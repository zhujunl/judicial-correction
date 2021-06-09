package com.miaxis.judicialcorrection.id.bean;

import android.graphics.Bitmap;
import android.util.Base64;

/**
 * @author Tank
 * @date 2021/4/29 10:53 AM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class IdCard {

    public IdCardMsg idCardMsg;

    public Bitmap face;

    public byte[] fp0;

    public byte[] fp1;

    public String fingerprint0;
    public  String fingerprintPosition0;


    public String fingerprint1;
    public  String fingerprintPosition1;
    public IdCard() {
    }

    @Override
    public String toString() {
        return "IdCard{" +
                "idCardMsg=" + idCardMsg +
                ", face=" + face +
                '}';
    }
}
