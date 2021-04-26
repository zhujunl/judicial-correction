package com.miaxis.judicialcorrection.base.utils.numbers;

/**
 * @author yawei
 * @data on 2018/7/16 下午1:25
 * @email zyawei@live.com
 */
public class UnboxUtils {
    public static int safeUnBox(Integer integer) {
        return safeUnBox(integer, 0);
    }

    public static int safeUnBox(Integer integer, int defaultValue) {
        return integer == null ? defaultValue : integer;
    }

}
