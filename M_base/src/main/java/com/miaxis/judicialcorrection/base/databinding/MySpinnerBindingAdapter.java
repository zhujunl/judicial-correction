package com.miaxis.judicialcorrection.base.databinding;

import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.IdRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.util.Objects;

import timber.log.Timber;

/**
 * Test
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class MySpinnerBindingAdapter {

    /**
     * 文本改变，Spinner自动选中
     */
    @BindingAdapter("selection")
    public static void setSelection(Spinner view, String selectText) {
        int count = view.getCount();
        int selectIndex = 0;
        for (int i = 0; i < count; i++) {
            if (Objects.equals(selectText, view.getItemAtPosition(i))) {
                selectIndex = i;
                break;
            }
        }
        Timber.i("setSelection %s,index:[%d/%d]",selectText,selectIndex,count);
        view.setSelection(selectIndex);
    }

    /**
     * Spinner选中,文本自动更新
     */
    @InverseBindingAdapter(attribute = "selection", event = "selectionAttrChanged")
    public static String getSelection(Spinner view) {
        Object selectedItem = view.getSelectedItem();
        String result = selectedItem == null ? null : selectedItem.toString();
        Timber.i("getSelection %s",result);
        return result;
    }

    @BindingAdapter(value = {"selection", "selectionAttrChanged"},
            requireAll = false)
    public static void setListeners(Spinner view, final AdapterView.OnItemSelectedListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnItemSelectedListener(listener);
        } else {
            view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (listener != null) {
                        listener.onItemSelected(parent, view, position, id);
                    }
                    attrChange.onChange();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    if (listener != null) {
                        listener.onNothingSelected(parent);
                    }
                    attrChange.onChange();
                }
            });
        }
    }
}
