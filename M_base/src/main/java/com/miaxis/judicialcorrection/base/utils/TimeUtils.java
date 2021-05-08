package com.miaxis.judicialcorrection.base.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tank
 * @date 2021/5/8 15:51
 * @des
 * @updateAuthor
 * @updateDes
 */
public class TimeUtils {

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的日期格式，根据实际调整""里面内容

    public static boolean isInTime(Date start, Date end) {
        try {
            Date date = new Date();
            boolean after = date.after(start);
            if (after) {
                return date.before(end);
            }
            return after;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
