package com.miaxis.judicialcorrection.id.inputIdCard;

import android.os.Bundle;
import android.text.TextUtils;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.dialog.DialogIdCardNotFound;
import com.miaxis.judicialcorrection.dialog.DialogNoButton;
import com.miaxis.judicialcorrection.id.R;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.bean.IdCardMsg;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.databinding.FragmentInputIdCardBinding;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * @author Tank
 * @date 2021/4/25 5:07 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
@AndroidEntryPoint
public class InputIdCardBindingFragment extends BaseBindingFragment<FragmentInputIdCardBinding> {

    String title;

    @Inject
    Lazy<AppHints> appHintsLazy;

    public InputIdCardBindingFragment(String title) {
        this.title = title;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_input_id_card;
    }

    @Override
    protected void initView(@NonNull FragmentInputIdCardBinding view, Bundle savedInstanceState) {
        InputIDCardViewModel inputIDCardViewModel = new ViewModelProvider(this).get(InputIDCardViewModel.class);
        inputIDCardViewModel.title.observe(this, s -> binding.tvTitle.setText(String.valueOf(s)));

        binding.btnConfirm.setOnClickListener(v -> {
            String idCard = binding.etInputIdCard.getText().toString();
            if (TextUtils.isEmpty(idCard) || idCard.length() != 18) {
                DialogNoButton.Builder builder = new DialogNoButton.Builder();
                builder.title = "请输入正确的身份证号码";
                new DialogNoButton(getContext(), new DialogNoButton.ClickListener() {
                    @Override
                    public void onTryAgain(AppCompatDialog appCompatDialog) {

                    }

                    @Override
                    public void onTimeOut(AppCompatDialog appCompatDialog) {
                        appCompatDialog.dismiss();
                    }

                }, builder).show();
                return;
            }

            FragmentActivity activity = getActivity();
            if (activity instanceof ReadIdCardCallback) {
                ReadIdCardCallback readIdCardCallback = (ReadIdCardCallback) activity;
                IdCard idCardData = new IdCard();
                IdCardMsg idCardMsg = new IdCardMsg();
                idCardMsg.id_num = idCard;
                idCardData.idCardMsg = idCardMsg;
                readIdCardCallback.onIdCardRead(idCardData);
                inputIDCardViewModel.login(idCardData.idCardMsg.id_num).observe(InputIdCardBindingFragment.this, personInfoResource -> {
                    switch (personInfoResource.status) {
                        case LOADING:
                            showLoading();
                            break;
                        case ERROR:
                            dismissLoading();
                            readIdCardCallback.onLogin(null);
                            appHintsLazy.get().showError("Error:" + personInfoResource.errorMessage);
                            break;
                        case SUCCESS:
                            dismissLoading();
                            if (personInfoResource.data == null) {
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
                                }, idCardData.idCardMsg.id_num).show();
                                return;
                            }
                            personInfoResource.data.setIdCardNumber(idCardData.idCardMsg.id_num);
                            readIdCardCallback.onLogin(personInfoResource.data);
                            break;
                    }
                });
            }
        });

        binding.btnBackToHome.setOnClickListener(v -> finish());

        inputIDCardViewModel.title.setValue(title);

    }

    @Override
    protected void initData(@NonNull FragmentInputIdCardBinding binding, @Nullable Bundle savedInstanceState) {

    }

}
