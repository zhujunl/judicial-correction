package com.miaxis.judicialcorrection.dialog.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.app.AppCompatDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @author Tank
 * @date 2021/4/27 9:55 AM
 * @des
 * @updateAuthor
 * @updateDes
 */
public abstract class BaseNoListenerDialog<V extends ViewDataBinding, L extends BaseDialogNoListener> extends AppCompatDialog {

    protected V binding;
    protected L listener;

    public BaseNoListenerDialog(Context context, L listener) {
        super(context);
        Window window = getWindow();
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), initLayout(), (ViewGroup) window.getDecorView(), false);
        setContentView(binding.getRoot());
        window.setBackgroundDrawableResource(android.R.color.transparent);
        this.listener = listener;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initData();
    }

    public abstract int initLayout();

    public abstract void initView();

    public abstract void initData();

}
