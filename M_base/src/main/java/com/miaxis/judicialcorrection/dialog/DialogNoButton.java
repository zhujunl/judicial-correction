package com.miaxis.judicialcorrection.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.base.databinding.DialogNotFoundBinding;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;
import com.miaxis.judicialcorrection.widget.countdown.CountDownListener;

import androidx.annotation.NonNull;

/**
 * @author Tank
 * @date 2021/4/25 7:38 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DialogNoButton extends BaseDialog<DialogNotFoundBinding, DialogNoButton.ClickListener> {

    private final Builder mBuilder;

    public DialogNoButton(@NonNull Context context, ClickListener clickListener, @NonNull Builder builder) {
        super(context, clickListener);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        this.mBuilder = builder;
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_not_found;
    }

    @Override
    public void initView() {
        binding.cdtvTime.setTime(mBuilder.timeOut);
        binding.cdtvTime.setCountDownListener(new CountDownListener() {
            @Override
            public void onCountDownProgress(int progress) {

            }

            @Override
            public void onCountDownStop() {
                if (listener != null) {
                    listener.onTimeOut(DialogNoButton.this);
                }
            }
        });
        binding.ivError.setImageResource(mBuilder.success ? R.mipmap.mipmap_success : R.mipmap.mipmap_error);
        binding.tvTitle.setText(mBuilder.title);
        binding.tvMessage.setVisibility(TextUtils.isEmpty(mBuilder.message) ? View.GONE : View.VISIBLE);
        binding.tvMessage.setText(String.valueOf(mBuilder.message));
    }

    @Override
    public void initData() {

    }

    public interface ClickListener extends BaseDialogListener {

    }

    public static class Builder {

        public boolean success = false;
        public String title = "title";
        public String message;
        public int timeOut = 10;

        public Builder() {
        }
    }

}
