package com.miaxis.judicialcorrection.dialog.base;

import androidx.appcompat.app.AppCompatDialog;

/**
 * @author Tank
 * @date 2021/4/27 11:14 AM
 * @des
 * @updateAuthor
 * @updateDes
 */
public interface BaseDialogListener {

    void onTryAgain(AppCompatDialog appCompatDialog);

    void onTimeOut(AppCompatDialog appCompatDialog);

}