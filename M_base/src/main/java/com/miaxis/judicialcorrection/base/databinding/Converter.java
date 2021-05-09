package com.miaxis.judicialcorrection.base.databinding;

import android.icu.text.SimpleDateFormat;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.InverseMethod;

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

    public static String convertGMTToLocal(Date source) {
        if (source==null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("English"));
        return sdf.format(source);
    }
}
