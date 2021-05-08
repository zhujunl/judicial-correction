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
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static float getDays(String startTime, String endTime) {
        try {
            long start = dateFormat.parse(startTime).getTime();
            long end = dateFormat.parse(endTime).getTime();
            if (end - start >= 0) {
                return (end - start) / 1000F / 60F / 60F / 24F;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
