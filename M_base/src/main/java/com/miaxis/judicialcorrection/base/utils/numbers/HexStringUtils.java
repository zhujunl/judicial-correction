package com.miaxis.judicialcorrection.base.utils.numbers;


import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * @date: 2018/12/28 8:34
 * @author: zhang.yw
 */
public class HexStringUtils {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    private static final char[] CHAR_ARRAY = new char[128];

    static {
        for (int i = 0; i < HEX_ARRAY.length; i++) {
            char c = HEX_ARRAY[i];
            CHAR_ARRAY[c] = (char) i;
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        return bytesToHexString(bytes, bytes.length);
    }

    /**
     * 将byte[] 数组转换为十六进制字符串，每个byte以空格分割
     * 使用{@link #hexStringToBytes(String)}可还原此byte[]
     *
     * @param bytes  byte array
     * @param length length
     * @return HexString
     * @see #hexStringToBytes(String)
     */
    public static String bytesToHexString(byte[] bytes, int length) {
        char[] hexChars = new char[length * 3];
        for (int j = 0; j < length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = HEX_ARRAY[v >>> 4];
            hexChars[j * 3 + 1] = HEX_ARRAY[v & 0xF];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    /**
     * 将{@link #bytesToHexString(byte[])}转换的String 还原
     *
     * @param hexString HexString
     * @return byte array
     * @see #bytesToHexString(byte[])
     */
    public static byte[] hexStringToBytes(String hexString) {
        byte[] bytes = new byte[hexString.length() / 3];
        for (int i = 0, len = bytes.length; i < len; i++) {
            int i0 = CHAR_ARRAY[hexString.charAt(i * 3)];
            int i1 = CHAR_ARRAY[hexString.charAt(i * 3 + 1)];
            bytes[i] = (byte) ((i0 << 4) | (i1 & 0xf));
        }
        return bytes;
    }

    public static String convertGMTToLocal(String source) {
        if (source==null){
            return "";
        }
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS",Locale.ENGLISH);//输入的被转化的时间格式
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//需要转化成的时间格式
        Date date1 = null;
        try {
            date1 = dff.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df1.format(date1);
    }

    public static String convertCurrentGMT(){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return df.format(date);
    }
}
