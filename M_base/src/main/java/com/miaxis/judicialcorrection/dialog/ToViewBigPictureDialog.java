package com.miaxis.judicialcorrection.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.base.databinding.DialogToViewBigPictureBinding;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;

import java.io.File;


/**
 * 查看大图
 */
public class ToViewBigPictureDialog extends BaseDialog<DialogToViewBigPictureBinding, ToViewBigPictureDialog.ClickListener> {

    private final Builder mBuilder;


    public ToViewBigPictureDialog(@NonNull Context context, ClickListener clickListener, @NonNull Builder builder) {
        super(context, clickListener);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        this.mBuilder = builder;
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_to_view_big_picture;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        Glide.with(getContext()).load(new File(mBuilder.filePath)).into(binding.img);
    }

    public interface ClickListener extends BaseDialogListener {


    }

    public static class Builder {

        public String filePath;

        public Builder() {
        }

        public Builder setPathFile(String filePath) {
            this.filePath = filePath;
            return this;
        }
    }
}
