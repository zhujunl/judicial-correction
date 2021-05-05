package com.miaxis.judicialcorrection.id.bean;

import android.graphics.Bitmap;

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
