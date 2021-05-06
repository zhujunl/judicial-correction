package com.miaxis.judicialcorrection.base.databinding;

import android.widget.RadioGroup;

import androidx.databinding.InverseMethod;

/**
 * RadioGroupDataBindingAdapter
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class Converter {

    @InverseMethod("checkedIdToChecked")
    public static int checkedToCheckId(RadioGroup view, int check) {
        if (check == 1) {
            return view.getChildAt(0).getId();
        } else {
            return view.getChildAt(1).getId();
        }
    }

    public static int checkedIdToChecked(RadioGroup view, int checkedId) {
        int id = view.getChildAt(0).getId();
        return id == checkedId ? 1 : 0;
    }

}
