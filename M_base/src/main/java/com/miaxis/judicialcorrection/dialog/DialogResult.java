package com.miaxis.judicialcorrection.dialog;

import android.content.Context;
import android.view.View;

import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.base.databinding.DialogVerifyResultBinding;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;
import com.miaxis.judicialcorrection.widget.countdown.CountDownListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

/**
 * @author Tank
 * @date 2021/4/25 7:38 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DialogResult extends BaseDialog<DialogVerifyResultBinding, DialogResult.ClickListener> {

    private final Builder mBuilder;

    public DialogResult(Context context, ClickListener clickListener, @NonNull Builder builder) {
        super(context, clickListener);
        setCancelable(false);
        this.mBuilder = builder;
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_verify_result;
    }

    @Override
    public void initView() {
        binding.btnTryAgain.setVisibility(this.mBuilder.success ? View.GONE : View.VISIBLE);
        binding.btnTryAgain.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTryAgain(DialogResult.this);
            }
        });

        binding.btnBackHome.setVisibility(this.mBuilder.enableBackHome ? View.VISIBLE : View.GONE);
        binding.btnBackHome.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBackHome(DialogResult.this);
            }
        });

        binding.ivError.setBackgroundResource(this.mBuilder.success ? R.mipmap.mipmap_success : R.mipmap.mipmap_error);
        binding.tvError.setText(this.mBuilder.title);
        binding.tvMessage.setText(this.mBuilder.message);

        binding.cdtvTime.setTime(this.mBuilder.countDownTime);
        binding.cdtvTime.setCountDownListener(new CountDownListener() {
            @Override
            public void onCountDownProgress(int progress) {

            }

            @Override
            public void onCountDownStop() {
                if (listener != null) {
                    listener.onTimeOut(DialogResult.this);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    public interface ClickListener extends BaseDialogListener {

        /**
         * 返回首页回调
         */
        void onBackHome(AppCompatDialog appCompatDialog);

    }

    public static class Builder {

        public boolean success = false;
        public String title = "title";
        public String message = "message";
        public int countDownTime = 10;
        public boolean enableBackHome = true;

        public Builder() {

        }
    }

}
