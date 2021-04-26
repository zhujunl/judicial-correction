package com.miaxis.judicialcorrection.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Window;

import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.widget.countdown.CountDownListener;
import com.miaxis.judicialcorrection.widget.countdown.CountDownTextView;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author Tank
 * @date 2021/4/25 7:38 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DialogNotFoundWithIdCard extends AppCompatDialog {

    private final String idCard;

    public DialogNotFoundWithIdCard(Context context, String idCard) {
        super(context);
        setContentView(initLayout());
        setCancelable(false);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        this.idCard = idCard;
    }

    private int initLayout() {
        return R.layout.dialog_not_found_with_id_card;
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.btn_input).setOnClickListener(v -> dismiss());
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
        AppCompatTextView tv_message = (AppCompatTextView) findViewById(R.id.tv_message);
        CharSequence charSequence = getContext().getResources().getText(R.string.dialog_error_3);
        String format = String.format(String.valueOf(charSequence), this.idCard);
        int splitLeft = format.indexOf("（");
        int splitRight = format.indexOf("）");
        SpannableStringBuilder style = new SpannableStringBuilder(format);
        style.setSpan(new ForegroundColorSpan(Color.RED), splitLeft + 1, splitRight, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_message.setText(style);
    }
}
