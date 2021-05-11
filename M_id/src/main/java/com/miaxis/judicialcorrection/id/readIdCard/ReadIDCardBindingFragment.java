package com.miaxis.judicialcorrection.id.readIdCard;

import android.os.Bundle;
import android.view.View;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.dialog.DialogNoButton;
import com.miaxis.judicialcorrection.id.R;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.databinding.FragmentReadIdCardBinding;
import com.miaxis.judicialcorrection.id.inputIdCard.InputIdCardBindingFragment;

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
 * @date 2021/4/25 10:06 AM
 * @des 度身份证界面
 * @updateAuthor tangkai
 * @updateDes
 */
@AndroidEntryPoint
public class ReadIDCardBindingFragment extends BaseBindingFragment<FragmentReadIdCardBinding> {

    String title;

    boolean noIdCardEnable;

    @Inject
    Lazy<AppHints> appHintsLazy;

    ReadIdCardViewModel mReadIdCardViewModel;

    public ReadIDCardBindingFragment(String title, boolean noIdCardEnable) {
        this.title = title;
        this.noIdCardEnable = noIdCardEnable;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_read_id_card;
    }

    @Override
    protected void initView(@NonNull FragmentReadIdCardBinding view, Bundle savedInstanceState) {
        mReadIdCardViewModel = new ViewModelProvider(this).get(ReadIdCardViewModel.class);
        mReadIdCardViewModel.title.observe(this, s -> binding.tvTitle.setText(String.valueOf(s)));
        mReadIdCardViewModel.noIdCardEnable.observe(this, aBoolean -> {
            binding.btnNoIdCardEntry.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            binding.btnNoIdCardEntry.setOnClickListener(v ->
                    getFragmentManager().beginTransaction().replace(
                            R.id.layout_root,
                            new InputIdCardBindingFragment(title)
                    ).commitNow());
        });

        binding.btnBackToHome.setOnClickListener(v -> {
            finish();
        });
        mReadIdCardViewModel.title.setValue(title);
        mReadIdCardViewModel.noIdCardEnable.setValue(noIdCardEnable);

        boolean init = ReadIdCardManager.getInstance().init(getActivity());
        if (init) {
            FragmentActivity activity = getActivity();
            if (activity instanceof ReadIdCardCallback) {
                ReadIdCardCallback readIdCardCallback = (ReadIdCardCallback) activity;
                mReadIdCardViewModel.readIdCard(result -> {
                    readIdCardCallback.onIdCardRead(result);
                    mReadIdCardViewModel.login(result.idCardMsg.id_num).observe(ReadIDCardBindingFragment.this, personInfoResource -> {
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
                                    DialogNoButton.Builder builder = new DialogNoButton.Builder();
                                    builder.success = false;
                                    builder.timeOut = 10;
                                    builder.title = "系统查无此人";
                                    builder.message = "请联系现场工作人员处理\n" +
                                            "（工作人员需确认前期登记的\n" +
                                            "身份证号是否准确）！";
                                    new DialogNoButton(getContext(), new DialogNoButton.ClickListener() {
                                        @Override
                                        public void onTryAgain(AppCompatDialog appCompatDialog) {
                                            appCompatDialog.dismiss();
                                        }

                                        @Override
                                        public void onTimeOut(AppCompatDialog appCompatDialog) {
                                            appCompatDialog.dismiss();
                                            finish();
                                        }
                                    }, builder).show();
                                    return;
                                }
                                personInfoResource.data.setIdCardNumber(result.idCardMsg.id_num);
                                readIdCardCallback.onLogin(personInfoResource.data);
                                break;
                        }
                    });

                });
            }
        } else {
            appHintsLazy.get().showError("初始身份证模块失败");
        }
    }

    @Override
    protected void initData(@NonNull FragmentReadIdCardBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mReadIdCardViewModel.stopRead();
        ReadIdCardManager.getInstance().free(getActivity());
    }

    public interface ReadIdCallback {

        /**
         * 读身份证回调
         */
        void onIdCardRead(IdCard result);
    }
}
