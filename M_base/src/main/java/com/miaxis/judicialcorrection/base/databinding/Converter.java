package com.miaxis.judicialcorrection.base.databinding;

import android.icu.text.SimpleDateFormat;
import android.text.TextUtils;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.InverseMethod;
import androidx.lifecycle.MutableLiveData;

import com.miaxis.judicialcorrection.base.api.vo.LiveAddressChangeDetailsBean;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

/**
 * RadioGroupDataBindingAdapter
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class Converter {

    /**
     * @param checked 是否选择,1-是，0-否
     * @return 选中的id
     */
    @InverseMethod("checkedIdToChecked")
    public static int checkedToCheckId(RadioGroup view, int checked) {
        int checkedId;
        if (checked == 1) {
            checkedId = view.getChildAt(0).getId();
        } else {
            checkedId = view.getChildAt(1).getId();
        }
        return checkedId;
    }

    /**
     * @param checkedId 选中的id
     * @return 是否选中 1-选中 , 0-没选中
     */
    public static int checkedIdToChecked(RadioGroup view, int checkedId) {
        int id0 = view.getChildAt(0).getId();
        return (checkedId == id0) ? 1 : 0;
    }

    public static String convertGMTToLocal(String source) {
        if (source == null) {
            return "";
        }
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);//输入的被转化的时间格式
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//需要转化成的时间格式
        Date date1 = null;
        try {
            date1 = dff.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df1.format(date1);
    }
}
