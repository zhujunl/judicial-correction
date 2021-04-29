package com.miaxis.judicialcorrection.id.inputIdCard;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.dialog.DialogIdCardNotFound;
import com.miaxis.judicialcorrection.id.R;
import com.miaxis.judicialcorrection.id.databinding.FragmentInputIdCardBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author Tank
 * @date 2021/4/25 5:07 PM
 * @des
 * @updateAuthor
 * @updateDes
 */

@Route(path = "/page/inputIDCard")
public class InputIdCardBindingFragment extends BaseBindingFragment<FragmentInputIdCardBinding> {

    @Autowired(name = "Title")
    String title;

    @Autowired(name = "AutoCheckEnable")
    boolean autoCheckEnable;

    @Override
    protected int initLayout() {
        return R.layout.fragment_input_id_card;
    }

    @Override
    protected void initView(@NonNull FragmentInputIdCardBinding view, Bundle savedInstanceState) {
        InputIDCardModel inputIDCardModel = new ViewModelProvider(this).get(InputIDCardModel.class);
        inputIDCardModel.title.observe(this, s -> binding.tvTitle.setText(String.valueOf(s)));
        inputIDCardModel.autoCheckEnable.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                    setResult(ZZResponse.CreateFail(IdCardErrorCode.ErrorArgument, getString(R.string.error_no_title)));
                //                    finish();
                //                    new DialogNotFound(getContext()).show();
                new DialogIdCardNotFound(getContext(), new DialogIdCardNotFound.ClickListener() {
                    @Override
                    public void onTryAgain(AppCompatDialog appCompatDialog) {
                        appCompatDialog.dismiss();
                    }

                    @Override
                    public void onTimeOut(AppCompatDialog appCompatDialog) {
                        appCompatDialog.dismiss();
                        finish();
                    }
                }, "36222653256566255X").show();
            }
        });

        binding.btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inputIDCardModel.title.setValue(title);
        inputIDCardModel.autoCheckEnable.setValue(autoCheckEnable);
    }

    @Override
    protected void initData(@NonNull FragmentInputIdCardBinding binding, @Nullable Bundle savedInstanceState) {

    }

}
