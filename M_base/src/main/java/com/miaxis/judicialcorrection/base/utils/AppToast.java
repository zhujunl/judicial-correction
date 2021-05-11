package com.miaxis.judicialcorrection.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
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
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        Point screenWidthAndHeight = DensityUtils.getScreenWidthAndHeight(context);
        int height = (int) (screenWidthAndHeight.y * 0.75);
        toast.setGravity(Gravity.TOP, 0, height);
        toast.show();
        return toast;
    }

}
