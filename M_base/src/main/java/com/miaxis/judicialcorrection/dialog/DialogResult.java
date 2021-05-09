package com.miaxis.judicialcorrection.dialog;

import android.content.Context;
import android.view.View;

import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.base.databinding.DialogVerifyResultBinding;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;
import com.miaxis.judicialcorrection.widget.countdown.DefaultCountDownListener;

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
        if (this.mBuilder.countDownTime!=0) {
            binding.cdtvTime.setVisibility(View.VISIBLE);
            binding.cdtvTime.setTime(this.mBuilder.countDownTime);
            binding.cdtvTime.setCountDownListener(new DefaultCountDownListener() {

                @Override
                public void onCountDownDone() {
                    if (listener != null) {
                        listener.onTimeOut(DialogResult.this);
                    }
                }
            });
        }else{
            binding.cdtvTime.setVisibility(View.GONE);
        }

        if (mBuilder.isHideAllShowSucceed){
            binding.tvMessage.setVisibility(View.GONE);
            binding.btnTryAgain.setVisibility(View.INVISIBLE);
            binding.btnBackHome.setVisibility(View.GONE);
        }
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
        public boolean isHideAllShowSucceed = false;

        public Builder() {

        }

        public Builder(boolean success, String title, String message, int countDownTime, boolean enableBackHome) {
            this.success = success;
            this.title = title;
            this.message = message;
            this.countDownTime = countDownTime;
            this.enableBackHome = enableBackHome;
        }

        public Builder hideAllHideSucceedInfo(boolean isHideAllShowSucceed) {
            this.isHideAllShowSucceed = isHideAllShowSucceed;
            return this;
        }
    }

}
