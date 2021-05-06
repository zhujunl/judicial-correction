package com.miaxis.judicialcorrection.base.databinding;

import android.widget.RadioGroup;

import androidx.databinding.InverseMethod;

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
            checkedId= view.getChildAt(0).getId();
        } else {
            checkedId= view.getChildAt(1).getId();
        }
        return checkedId;
    }

    /**
     * @param checkedId 选中的id
     * @return 是否选中 1-选中 , 0-没选中
     */
    public static int checkedIdToChecked(RadioGroup view, int checkedId) {
        int id0 = view.getChildAt(0).getId();
        return (checkedId == id0 )? 1 : 0;
    }

}
