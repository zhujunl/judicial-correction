package com.miaxis.judicialcorrection.dialog;

import android.content.Context;
import android.view.Window;

import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.widget.countdown.CountDownListener;
import com.miaxis.judicialcorrection.widget.countdown.CountDownTextView;

import androidx.appcompat.app.AppCompatDialog;

/**
 * @author Tank
 * @date 2021/4/25 7:38 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DialogNotFound extends AppCompatDialog {

    public DialogNotFound(Context context) {
        super(context);
        setContentView(initLayout());
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private int initLayout() {
        return R.layout.dialog_not_found;
    }

    @Override
    protected void onStart() {
        super.onStart();
        CountDownTextView cdtv_time = (CountDownTextView) findViewById(R.id.cdtv_time);
        cdtv_time.setTime(10);
        cdtv_time.setCountDownListener(new CountDownListener() {
            @Override
            public void onCountDownProgress(int progress) {

            }

            @Override
            public void onCountDownStop() {
                dismiss();
            }
        });
    }
}
