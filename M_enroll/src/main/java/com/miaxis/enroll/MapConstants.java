package com.miaxis.enroll;

import com.miaxis.judicialcorrection.base.databinding.kvsp.KvSpinnerVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Constants
 *
 * @author zhangyw
 * Created on 5/17/21.
 */
public class MapConstants {
    public static final List<KvSpinnerVo> whcdKvList = new ArrayList<>();

    static {
        whcdKvList.add(new KvSpinnerVo("1", "文盲"));
        whcdKvList.add(new KvSpinnerVo("2", "小学"));
        whcdKvList.add(new KvSpinnerVo("3", "初中"));
        whcdKvList.add(new KvSpinnerVo("4", "高中"));
        whcdKvList.add(new KvSpinnerVo("5", "大专"));
        whcdKvList.add(new KvSpinnerVo("6", "本科"));
        whcdKvList.add(new KvSpinnerVo("7", "硕士"));
        whcdKvList.add(new KvSpinnerVo("8", "博士及以上（包含博士）"));
        whcdKvList.add(new KvSpinnerVo("9", "中专和中技"));
        whcdKvList.add(new KvSpinnerVo("99", "其他"));
    }

    public static final List<KvSpinnerVo> jyjxqkKvList = new ArrayList<>();

    static {
        jyjxqkKvList.add(new KvSpinnerVo("1", "就学"));
        jyjxqkKvList.add(new KvSpinnerVo("2", "就业"));
        jyjxqkKvList.add(new KvSpinnerVo("3", "务农"));
        jyjxqkKvList.add(new KvSpinnerVo("4", "无业"));
    }

    public static final List<KvSpinnerVo> hyzkKvList = new ArrayList<>();

    static {
        hyzkKvList.add(new KvSpinnerVo("1", "未婚"));
        hyzkKvList.add(new KvSpinnerVo("2", "已婚"));
        hyzkKvList.add(new KvSpinnerVo("3", "离异"));
        hyzkKvList.add(new KvSpinnerVo("4", "丧偶"));
    }

    public static final List<KvSpinnerVo> zzmnKvList = new ArrayList<>();

    static {
        zzmnKvList.add(new KvSpinnerVo("1", "中共党员"));
//        zzmnKvList.add(new KvSpinnerVo("2", "共青团员"));
        zzmnKvList.add(new KvSpinnerVo("3", "共青团员"));
        zzmnKvList.add(new KvSpinnerVo("4", "民盟盟员"));
        zzmnKvList.add(new KvSpinnerVo("5", "民建会员"));
        zzmnKvList.add(new KvSpinnerVo("6", "民进会员"));
        zzmnKvList.add(new KvSpinnerVo("7", "农工党党员"));
        zzmnKvList.add(new KvSpinnerVo("8", "致公党党员"));
        zzmnKvList.add(new KvSpinnerVo("9", "农工党党员"));
        zzmnKvList.add(new KvSpinnerVo("10", "九三学社社员"));
        zzmnKvList.add(new KvSpinnerVo("11", "台盟盟员"));
        zzmnKvList.add(new KvSpinnerVo("12", "无党派民主人士"));
        zzmnKvList.add(new KvSpinnerVo("13", "群众"));
    }


    public static final List<KvSpinnerVo> gjList = new ArrayList<>();

    static {
        gjList.add(new KvSpinnerVo("01", "中国籍"));
        gjList.add(new KvSpinnerVo("02", "外国籍"));
        gjList.add(new KvSpinnerVo("03", "无国籍"));

        gjList.add(new KvSpinnerVo("10", "大陆"));
        gjList.add(new KvSpinnerVo("11", "城镇户籍"));
        gjList.add(new KvSpinnerVo("12", "农村户籍"));
        gjList.add(new KvSpinnerVo("13", "其他"));
        gjList.add(new KvSpinnerVo("14", "澳门"));
        gjList.add(new KvSpinnerVo("15", "台湾"));
        gjList.add(new KvSpinnerVo("16", "香港"));
    }


    public static final List<KvSpinnerVo> mzList = new ArrayList<>();

    static {
        mzList.add(new KvSpinnerVo("01", "汉族"));
        mzList.add(new KvSpinnerVo("02","蒙古族"));
        mzList.add(new KvSpinnerVo("03", "回族"));
        mzList.add(new KvSpinnerVo("04", "藏族"));
        mzList.add(new KvSpinnerVo("05", "维吾尔族"));
        mzList.add(new KvSpinnerVo("06", "苗族"));
        mzList.add(new KvSpinnerVo("07", "彝族"));
        mzList.add(new KvSpinnerVo("08", "壮族"));
        mzList.add(new KvSpinnerVo("09", "布依族"));
        mzList.add(new KvSpinnerVo("10","朝鲜族"));
        mzList.add(new KvSpinnerVo("11","满族"));
        mzList.add(new KvSpinnerVo("12","侗族"));
        mzList.add(new KvSpinnerVo("13","瑶族"));
        mzList.add(new KvSpinnerVo("14","白族"));
        mzList.add(new KvSpinnerVo("15","土家族"));
        mzList.add(new KvSpinnerVo("16","哈尼族"));
        mzList.add(new KvSpinnerVo("17","哈萨克族"));
        mzList.add(new KvSpinnerVo("18","傣族"));
        mzList.add(new KvSpinnerVo("19","黎族"));
        mzList.add(new KvSpinnerVo("20","傈僳族"));
        mzList.add(new KvSpinnerVo("21","佤族"));
        mzList.add(new KvSpinnerVo("22","畲族"));
        mzList.add(new KvSpinnerVo("23","高山族"));
        mzList.add(new KvSpinnerVo("24","拉祜族"));
        mzList.add(new KvSpinnerVo("25","水族"));
        mzList.add(new KvSpinnerVo("26","东乡族"));
        mzList.add(new KvSpinnerVo("27","纳西族"));
        mzList.add(new KvSpinnerVo("28","景颇族"));
        mzList.add(new KvSpinnerVo("29","柯尔克孜族"));
        mzList.add(new KvSpinnerVo("30","土族"));
        mzList.add(new KvSpinnerVo("31","达斡尔族"));
        mzList.add(new KvSpinnerVo("32","仫佬族"));
        mzList.add(new KvSpinnerVo("33","羌族"));
        mzList.add(new KvSpinnerVo("34","布朗族"));
        mzList.add(new KvSpinnerVo("35","撒拉族"));
        mzList.add(new KvSpinnerVo("36","毛难族"));
        mzList.add(new KvSpinnerVo("37","仡佬族"));
        mzList.add(new KvSpinnerVo("38","锡伯族"));
        mzList.add(new KvSpinnerVo("39","阿昌族"));
        mzList.add(new KvSpinnerVo("40","普米族"));
        mzList.add(new KvSpinnerVo("41","塔吉克族"));
        mzList.add(new KvSpinnerVo("42","怒族"));
        mzList.add(new KvSpinnerVo("43","乌孜别克族"));
        mzList.add(new KvSpinnerVo("44","俄罗斯族"));
        mzList.add(new KvSpinnerVo("45","鄂温克族"));
        mzList.add(new KvSpinnerVo("46","崩龙族"));
        mzList.add(new KvSpinnerVo("47","保安族"));
        mzList.add(new KvSpinnerVo("48","裕固族"));
        mzList.add(new KvSpinnerVo("49","京族"));
        mzList.add(new KvSpinnerVo("50","塔塔尔族"));
        mzList.add(new KvSpinnerVo("51","独龙族"));
        mzList.add(new KvSpinnerVo("52","鄂伦春族"));
        mzList.add(new KvSpinnerVo("53","赫哲族"));
        mzList.add(new KvSpinnerVo("54","门巴族"));
        mzList.add(new KvSpinnerVo("55","珞巴族"));
        mzList.add(new KvSpinnerVo("56","基诺族"));
        mzList.add(new KvSpinnerVo("97","其他"));
        mzList.add(new KvSpinnerVo("98","外国血统中国籍人士"));

    }

}
