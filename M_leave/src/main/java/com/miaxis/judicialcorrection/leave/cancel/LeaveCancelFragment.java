package com.miaxis.judicialcorrection.leave.cancel;

import android.os.Bundle;
import android.text.TextUtils;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.Leave;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.dialog.DatePickDialog;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.leave.LeaveRepo;
import com.miaxis.judicialcorrection.leave.R;
import com.miaxis.judicialcorrection.leave.databinding.FragmentLeaveCancelBinding;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.ViewModelProvider;
import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * @author Tank
 * @date 2021/5/7 10:08
 * @des
 * @updateAuthor
 * @updateDes
 */
@AndroidEntryPoint
public class LeaveCancelFragment extends BaseBindingFragment<FragmentLeaveCancelBinding> {

    private String title = "销假";

    @Inject
    Lazy<AppHints> appHintsLazy;

    @Inject
    LeaveRepo mLeaveRepo;
    private Leave.ListBean listBean;
    private VerifyInfo verifyInfo;

    public LeaveCancelFragment(VerifyInfo verifyInfo, Leave.ListBean listBean) {
        this.verifyInfo = verifyInfo;
        this.listBean = listBean;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_leave_cancel;
    }

    @Override
    protected void initView(@NonNull FragmentLeaveCancelBinding binding, @Nullable Bundle savedInstanceState) {
        CancelViewModel cancelViewModel = new ViewModelProvider(this).get(CancelViewModel.class);
        binding.setData(cancelViewModel);
        cancelViewModel.name.set(verifyInfo.name);
        cancelViewModel.idCardNumber.set(verifyInfo.idCardNumber);

        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.tvCancelTime.setOnClickListener(v -> new DatePickDialog(getContext(), date -> cancelViewModel.cancelTime.set(date)).show());
        binding.btnSubmit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(cancelViewModel.cancelTime.get())) {
                appHintsLazy.get().showError("请输入销假日期");
                return;
            }
            mLeaveRepo.leaveEnd(listBean.id)
                    .observe(LeaveCancelFragment.this, objectResource -> {
                        switch (objectResource.status) {
                            case LOADING:
                                showLoading(title, "正在提交" + title + "信息，请稍后");
                                break;
                            case ERROR:
                                dismissLoading();
                                appHintsLazy.get().showError("Error:" + objectResource.errorMessage);
                                break;
                            case SUCCESS:
                                dismissLoading();
                                new DialogResult(getContext(), new DialogResult.ClickListener() {
                                    @Override
                                    public void onBackHome(AppCompatDialog appCompatDialog) {
                                        appCompatDialog.dismiss();
                                        finish();
                                    }

                                    @Override
                                    public void onTryAgain(AppCompatDialog appCompatDialog) {
                                        appCompatDialog.dismiss();
                                    }

                                    @Override
                                    public void onTimeOut(AppCompatDialog appCompatDialog) {
                                        appCompatDialog.dismiss();
                                        finish();
                                    }
                                }, new DialogResult.Builder(
                                        true,
                                        "提交成功",
                                        "",
                                        3, false
                                ).hideAllHideSucceedInfo(true)).show();
                                break;
                        }
                    });
        });
    }

    @Override
    protected void initData(@NonNull FragmentLeaveCancelBinding binding, @Nullable Bundle savedInstanceState) {

    }

}
