package com.miaxis.judicialcorrection.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.base.databinding.DialogNotFoundBinding;
import com.miaxis.judicialcorrection.base.databinding.DialogPreviewPictureBinding;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;
import com.miaxis.judicialcorrection.widget.countdown.CountDownTextView;
import com.miaxis.judicialcorrection.widget.countdown.DefaultCountDownListener;

import java.io.File;

/**
 * @author Tank
 * @date 2021/4/25 7:38 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class PreviewPictureDialog extends BaseDialog<DialogPreviewPictureBinding, PreviewPictureDialog.ClickListener> {

    private final Builder mBuilder;

    public PreviewPictureDialog(@NonNull Context context, ClickListener clickListener, @NonNull Builder builder) {
        super(context, clickListener);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        this.mBuilder = builder;
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_preview_picture;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        Bitmap bitmap = BitmapFactory.decodeFile(mBuilder.filePath);
        binding.image.setImageBitmap(bitmap);
        binding.btnRetry.setOnClickListener(v -> {
            if (listener!=null){
                dismiss();
                listener.onTryAgain(PreviewPictureDialog.this);
            }
        });
        binding.btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    dismiss();
                    listener.onDetermine();
                }
            }
        });

    }

    public interface ClickListener extends BaseDialogListener {

         void  onDetermine();
    }

    public static class Builder {

        public String  filePath;

        public Builder() {
        }

        public  Builder setPathFile(String filePath){
            this.filePath=filePath;
            return this;
        }

    }

}
