package com.miaxis.judicialcorrection.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

/**
 * ToastUtils
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@Singleton
public class AppToast {
    private final Context context;

    @Inject
    public AppToast(@ApplicationContext Context context) {
        this.context = context;
    }

    Toast toast;

    @SuppressLint("ShowToast")
    public Toast show(String text) {
        if (toast == null) {
            toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
        return toast;
    }
}
