package com.miaxis.judicialcorrection.base.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.hilt.android.qualifiers.ActivityContext;

/**
 * AppDialogs
 *
 * @author zhangyw
 * Created on 5/5/21.
 */
public class AppHints {

    private final Context context;
    @Inject
    Lazy<AppToast> appToastLazy;

    @Inject
    public AppHints(@ActivityContext Context context) {
        this.context = context;
    }

    public void toast(String msg) {
        appToastLazy.get().show(msg);
    }

    public void showError(String errMsg) {
        new AlertDialog.Builder(context)
                .setTitle("错误")
                .setMessage(errMsg)
                .setPositiveButton("好的", (dialog, which) -> {

                })
                .setCancelable(false)
                .show();
    }

    public void showError(String errMsg, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle("错误")
                .setMessage(errMsg)
                .setPositiveButton("好的",listener)
                .setCancelable(false)
                .show();
    }

    public void showHint(String errMsg) {
        new AlertDialog.Builder(context)
                .setTitle("注意")
                .setMessage(errMsg)
                .setPositiveButton("好的", (dialog, which) -> {

                })
                .setCancelable(false)
                .show();
    }

}
