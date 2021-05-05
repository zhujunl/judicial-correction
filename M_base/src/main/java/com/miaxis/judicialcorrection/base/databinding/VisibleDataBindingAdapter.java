package com.miaxis.judicialcorrection.base.databinding;

import android.view.View;

import androidx.databinding.BindingAdapter;

/**
 * @author yawei
 * @email zyawei@live.com
 */
public class VisibleDataBindingAdapter {

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("visibleGone")
    public static void visibleGone(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("visibleInvisible")
    public static void visibleInvisible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}
