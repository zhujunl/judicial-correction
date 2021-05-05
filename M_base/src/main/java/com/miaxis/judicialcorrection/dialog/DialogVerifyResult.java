package com.miaxis.judicialcorrection.dialog;

import android.content.Context;
import android.view.View;

import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.base.databinding.DialogVerifyResultBinding;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;
import com.miaxis.judicialcorrection.widget.countdown.CountDownListener;
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
public class DialogVerifyResult extends BaseDialog<DialogVerifyResultBinding, DialogVerifyResult.ClickListener> {

    private final Builder mBuilder;

    public DialogVerifyResult(Context context, ClickListener clickListener,@NonNull Builder builder) {
        super(context,clickListener);
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
                listener.onTryAgain(DialogVerifyResult.this);
            }
        });

        binding.btnBackHome.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBackHome(DialogVerifyResult.this);
            }
        });

        binding.ivError.setBackgroundResource(this.mBuilder.success ? R.mipmap.mipmap_success : R.mipmap.mipmap_error);
        binding.tvError.setText(this.mBuilder.title);
        binding.tvMessage.setText(this.mBuilder.message);
        binding.cdtvTime.setTime(this.mBuilder.countDownTime);
        binding.cdtvTime.setCountDownListener(new DefaultCountDownListener() {

            @Override
            public void onCountDownDone() {
                if (listener != null) {
                    listener.onTimeOut(DialogVerifyResult.this);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    public interface ClickListener extends BaseDialogListener {

        void onBackHome(AppCompatDialog appCompatDialog);

    }

    public static class Builder {

        boolean success = false;
        String title = "title";
        String message = "message";
        int countDownTime = 10;

        public Builder() {

        }
    }

}
