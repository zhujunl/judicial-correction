package com.miaxis.judicialcorrection.base.databinding.kvsp;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

/**
 * Test
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class KVSpinnerBindingAdapter {


    @BindingAdapter("kvSpinnerList")
    public static void spinnerData(Spinner spinner, List<KvSpinnerVo> data) {
        SpinnerAdapter adapter = spinner.getAdapter();
        if (!(adapter instanceof KvSpinnerAdapter)) {
            adapter = new KvSpinnerAdapter();
            Timber.i("spinnerData KvSpinnerAdapter");
            spinner.setAdapter(adapter);
        }
        Timber.i("spinnerData %s",data);
        ((KvSpinnerAdapter) adapter).submitList(data);
    }


    /**
     * 文本改变，Spinner自动选中
     */
    @BindingAdapter("kvSpinnerSelection")
    public static void setKvSelection(Spinner view, KvSpinnerVo vo) {
        int count = view.getCount();
        int selectIndex = 0;
        for (int i = 0; i < count; i++) {
            if (Objects.equals(vo, view.getItemAtPosition(i))) {
                selectIndex = i;
                break;
            }
        }
        Timber.i("setSelection %s,index:[%d/%d]", vo, selectIndex, count);
        view.setSelection(selectIndex);
    }

    /**
     * Spinner选中,文本自动更新
     */
    @InverseBindingAdapter(attribute = "kvSpinnerSelection", event = "kvSpinnerSelectionAttrChanged")
    public static KvSpinnerVo getKvSelection(Spinner view) {
        Object selectedItem = view.getSelectedItem();
        Timber.i("getSelection %s", selectedItem);
        if (selectedItem instanceof KvSpinnerVo){
            return (KvSpinnerVo) selectedItem;
        }else {
            return null;
        }
    }

    @BindingAdapter(value = {"kvSpinnerSelection", "kvSpinnerSelectionAttrChanged"},
            requireAll = false)
    public static void setKvListeners(Spinner view, final AdapterView.OnItemSelectedListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            Timber.v("attrChange null" );
            view.setOnItemSelectedListener(listener);
        } else {
            view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Timber.v("onItemSelected " +position);
                    if (listener != null) {
                        listener.onItemSelected(parent, view, position, id);
                    }
                    attrChange.onChange();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Timber.v("onNothingSelected " );
                    if (listener != null) {
                        listener.onNothingSelected(parent);
                    }
                    attrChange.onChange();
                }
            });
        }
    }
}
