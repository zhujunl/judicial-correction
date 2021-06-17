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

    private AlertDialog dialog;
    private AlertDialog dialog2;
    private AlertDialog dialog3;
    private AlertDialog dialog4;

    public void showError(String errMsg) {
        dialog = new AlertDialog.Builder(context)
                .setTitle("错误")
                .setMessage(errMsg)
                .setPositiveButton("好的", (dialog, which) -> {

                })
                .setCancelable(false)
                .show();
    }

    public void showError(String errMsg, DialogInterface.OnClickListener listener) {
        dialog2 = new AlertDialog.Builder(context)
                .setTitle("错误")
                .setMessage(errMsg)
                .setPositiveButton("好的", listener)
                .setCancelable(false)
                .show();
    }

    public void showHint(String errMsg) {
        dialog3 = new AlertDialog.Builder(context)
                .setTitle("注意")
                .setMessage(errMsg)
                .setPositiveButton("好的", (dialog, which) -> {

                })
                .setCancelable(false)
                .show();
    }


    public void showHint(String errMsg, DialogInterface.OnClickListener listener) {
        dialog4 = new AlertDialog.Builder(context)
                .setTitle("注意")
                .setMessage(errMsg)
                .setPositiveButton("好的", listener)
                .setNegativeButton("取消", (dialog, which) -> {

                })
                .setCancelable(false)
                .show();
    }

    public void close() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (dialog2 != null && dialog2.isShowing()) {
            dialog2.dismiss();
        }
        if (dialog3 != null && dialog3.isShowing()) {
            dialog3.dismiss();
        }
        if (dialog4 != null && dialog4.isShowing()) {
            dialog4.dismiss();
        }
    }
}
