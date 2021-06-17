package com.example.m_common.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.m_common.R;
import com.example.m_common.databinding.DialogToViewBigPictureBinding;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogNoListener;
import com.miaxis.judicialcorrection.dialog.base.BaseNoListenerDialog;

import java.io.File;


/**
 * 查看大图
 */
public class ToViewBigPictureDialog extends BaseNoListenerDialog<DialogToViewBigPictureBinding, ToViewBigPictureDialog.ClickListener> {

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

    public interface ClickListener extends BaseDialogNoListener {

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
