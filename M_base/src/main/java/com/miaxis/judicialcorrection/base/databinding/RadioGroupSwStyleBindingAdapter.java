package com.miaxis.judicialcorrection.base.databinding;

import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

/**
 * Test
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class RadioGroupSwStyleBindingAdapter {

    //@BindingAdapter("android:checkedButton")
    public static void setSwitchRgChecked(RadioGroup view, int checked) {
        int id;
        if (checked == 1) {
            id = view.getChildAt(0).getId();
        } else {
            id = view.getChildAt(1).getId();
        }
        view.check(id);
    }

    //@InverseBindingAdapter(attribute = "android:checkedButton",event = "android:checkedButtonAttrChanged")
    public static @IdRes int getSwitchRgChecked(RadioGroup view) {
        int checkedId = view.getCheckedRadioButtonId();
        int id = view.getChildAt(0).getId();
        return id == checkedId ? 1 : 0;
    }

//    @BindingAdapter(value = {"android:onCheckedChanged", "android:checkedButtonAttrChanged"},
//            requireAll = false)
    public static void setListeners(RadioGroup view, final RadioGroup.OnCheckedChangeListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnCheckedChangeListener(listener);
        } else {
            view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (listener != null) {
                        listener.onCheckedChanged(group, checkedId);
                    }
                    attrChange.onChange();
                }
            });
        }
    }
}
