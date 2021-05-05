package com.miaxis.judicialcorrection.id.inputIdCard;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.bean.IdCard;
import com.miaxis.judicialcorrection.bean.IdCardMsg;
import com.miaxis.judicialcorrection.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.dialog.DialogNoButton;
import com.miaxis.judicialcorrection.id.R;
import com.miaxis.judicialcorrection.id.databinding.FragmentInputIdCardBinding;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * @author Tank
 * @date 2021/4/25 5:07 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
@AndroidEntryPoint
@Route(path = "/page/inputIDCard")
public class InputIdCardBindingFragment extends BaseBindingFragment<FragmentInputIdCardBinding> {

    @Autowired(name = "Title")
    String title;

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
            }
        });

        binding.btnBackToHome.setOnClickListener(v -> finish());

        inputIDCardViewModel.title.setValue(title);

    }

    @Override
    protected void initData(@NonNull FragmentInputIdCardBinding binding, @Nullable Bundle savedInstanceState) {

    }

}
