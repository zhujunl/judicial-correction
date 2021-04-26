package com.miaxis.judicialcorrection.base.utils.numbers;

/**
 * @author yawei
 * @data on 2018/7/16 下午1:26
 * @email zyawei@live.com
 */
public class NumberParseUtils {

    public static int tryParseInt(String string) {
        return tryParseInt(string, 0);
    }

    public static int tryParseInt(String string, int defaultValue) {
        try {
            return Integer.parseInt(string);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public static float tryParseFloat(String string) {
        return tryParseFloat(string, 0);
    }

    public static float tryParseFloat(String string, float defaultValue) {
        try {
            return Float.parseFloat(string);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }
}
