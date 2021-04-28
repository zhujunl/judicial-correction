package com.miaxis.judicialcorrection.dialog;

import android.content.Context;

import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.base.databinding.DialogNotFoundBinding;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;
import com.miaxis.judicialcorrection.widget.countdown.CountDownListener;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author Tank
 * @date 2021/4/25 7:38 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class DialogNotFound extends BaseDialog<DialogNotFoundBinding, DialogNotFound.ClickListener> {

    public DialogNotFound(Context context, ClickListener clickListener) {
        super(context,clickListener);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_not_found;
    }

    @Override
    public void initView() {
        binding.cdtvTime.setTime(10);
        binding.cdtvTime.setCountDownListener(new CountDownListener() {
            @Override
            public void onCountDownProgress(int progress) {

            }

            @Override
            public void onCountDownStop() {
                if (listener != null) {
                    listener.onTimeOut(DialogNotFound.this);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    public interface ClickListener extends BaseDialogListener {

    }

    public class ModelNotFound extends ViewModel {

        MutableLiveData<String> title=new MutableLiveData<>();

        MutableLiveData<Boolean> autoCheckEnable=new MutableLiveData<>(false);

    }

}
