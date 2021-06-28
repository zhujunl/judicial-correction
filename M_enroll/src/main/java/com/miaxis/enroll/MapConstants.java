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
        jyjxqkKvList.add(new KvSpinnerVo("1","就学"));
        jyjxqkKvList.add(new KvSpinnerVo("2","就业"));
        jyjxqkKvList.add(new KvSpinnerVo("3","务农"));
        jyjxqkKvList.add(new KvSpinnerVo("4","无业"));
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
}
