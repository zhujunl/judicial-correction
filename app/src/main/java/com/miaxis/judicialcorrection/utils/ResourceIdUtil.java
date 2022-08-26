package com.miaxis.judicialcorrection.utils;



import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.ui.Const;

public class ResourceIdUtil {
    public static int getResourceId(String title) {
        if (title == null) {
            return R.mipmap.main_enroll;
        }
        if (Const.MAIN_ENROLL.equals(title)) {
            return R.mipmap.main_enroll;
        }
        if (Const.MAIN_REPORT.equals(title)) {
            return R.mipmap.main_report;
        }
        if (Const.MAIN_EDU_ALL.equals(title)) {
            return R.mipmap.main_edu_all;
        }
        if (Const.MAIN_DEU_ONE.equals(title)) {
            return R.mipmap.main_edu_one;
        }

        if (Const.MAIN_ATTEN.equals(title)) {
            return R.mipmap.main_atten;
        }

        if (Const.MAIN_LOVE.equals(title)) {
            return R.mipmap.main_love;
        }

        if (Const.MAIN_ADDR.equals(title)) {
            return R.mipmap.main_addr;
        }

        if (Const.MAIN_CLOUD.equals(title)) {
            return R.mipmap.main_cloud;
        }
        return R.mipmap.main_enroll;

    }
}
