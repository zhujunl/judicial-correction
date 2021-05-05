package com.miaxis.judicialcorrection.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Window;

import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.base.databinding.DialogNotFoundWithIdCardBinding;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;
import com.miaxis.judicialcorrection.widget.countdown.CountDownListener;
import com.miaxis.judicialcorrection.widget.countdown.DefaultCountDownListener;

/**
 * @author Tank
 * @date 2021/4/25 7:38 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DialogIdCardNotFound extends BaseDialog<DialogNotFoundWithIdCardBinding, DialogIdCardNotFound.ClickListener> {

    private final String idCard;

    public DialogIdCardNotFound(Context context, ClickListener clickListener, String idCard) {
        super(context, clickListener);
        setContentView(initLayout());
        setCancelable(false);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        this.idCard = idCard;
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_not_found_with_id_card;
    }

    @Override
    public void initView() {
        binding.btnInput.setOnClickListener(v -> {
            if (listener!=null){
                listener.onTryAgain(DialogIdCardNotFound.this);
            }
        });
        binding.cdtvTime.setTime(10);
        binding.cdtvTime.setCountDownListener(new DefaultCountDownListener() {
            @Override
            public void onCountDownDone() {
                if (listener!=null){
                    listener.onTimeOut(DialogIdCardNotFound.this);
                }
            }
        });
        CharSequence charSequence = getContext().getResources().getText(R.string.dialog_error_3);
        String format = String.format(String.valueOf(charSequence), this.idCard);
        int splitLeft = format.indexOf("（");
        int splitRight = format.indexOf("）");
        SpannableStringBuilder style = new SpannableStringBuilder(format);
        style.setSpan(new ForegroundColorSpan(Color.RED), splitLeft + 1, splitRight, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvMessage.setText(style);
    }

    @Override
    public void initData() {

    }

    public interface ClickListener extends BaseDialogListener {

    }

}
