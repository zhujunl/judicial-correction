package com.miaxis.enroll.utils;

import android.text.TextUtils;

public class CardUtils {

    public static String getProvinceStr(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return "";
        }
        String[] firstIdCard = {"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41",
                "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81",
                "82"};

        String[] province = {"北京市", "天津市", "河北省", "山西省", "内蒙古自治区", "辽宁省", "吉林省", "黑龙江省", "上海市", "江苏省", "浙江省", "安徽省", "福建省",
                "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "广西壮族自治区", "海南省", "重庆市", "四川省", "贵州省", "云南省", "西藏自治区",
                "陕西省", "甘肃省", "青海省", "宁夏回族自治区", "新疆维吾尔自治区", "台湾省", "香港特别行政区", "澳门特别行政区"};       //将省份全部放进数组b;
        String pos = (idCard.substring(0, 2));      //id.substring(0, 2)获取身份证的前两位；
        String p = "";
        for (int i = 0; i < firstIdCard.length; i++) {
            if (pos.equals(firstIdCard[i])) {
                p = province[i];
                break;
            }
        }
        return p; //获取b数组中的省份信息且输出省份;
    }

}
