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
            return date.getTime() <= start.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static float getDays(String startTime, String endTime) {
        try {
            long start = simpleDateFormat.parse(startTime).getTime();
            long end = simpleDateFormat.parse(endTime).getTime();
            if (end - start >= 0) {
                long time = end - start;
                long days = time / (1000 * 60 * 60 * 24);
                long hours = (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
                long seconds = (time % (1000 * 60)) / 1000;
                if (days <= 0 && (hours > 0 || minutes > 0 || seconds > 0)) {
                    return 1;
                } else {
                    if (hours > 0 || minutes > 0 || seconds > 0) {
                        return days + 1;
                    }
                    return days;
                }
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getTime() {
        try {
            return simpleDateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static String dateToString(String time) {
        try {
            Date parse = simpleDateFormat.parse(time);
            return dateFormat.format(parse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
