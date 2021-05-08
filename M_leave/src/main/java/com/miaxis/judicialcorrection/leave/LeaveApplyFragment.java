package com.miaxis.judicialcorrection.leave;

import android.os.Bundle;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.leave.databinding.FragmentLeaveApplyBinding;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * @author Tank
 * @date 2021/5/7 10:08
 * @des
 * @updateAuthor
 * @updateDes
 */
@AndroidEntryPoint
public class LeaveApplyFragment extends BaseBindingFragment<FragmentLeaveApplyBinding> {

    private String title = "请假申请";

    private VerifyInfo verifyInfo;

    public LeaveApplyFragment(@NotNull VerifyInfo verifyInfo) {
        this.verifyInfo = verifyInfo;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_leave_apply;
    }

    @Override
    protected void initView(@NonNull FragmentLeaveApplyBinding binding, @Nullable Bundle savedInstanceState) {
        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.tvName.setText(verifyInfo.name);
        binding.tvIdCard.setText(verifyInfo.idCardNumber);
    }

    @Override
    protected void initData(@NonNull FragmentLeaveApplyBinding binding, @Nullable Bundle savedInstanceState) {

    }

    private void queryProgress() {
        FragmentActivity activity = getActivity();
        if (activity instanceof LeaveListener) {
            LeaveListener listener = (LeaveListener) activity;
            listener.onQueryProgress();
        }
    }

    private void cancel() {
        FragmentActivity activity = getActivity();
        if (activity instanceof LeaveListener) {
            LeaveListener listener = (LeaveListener) activity;
            listener.onCancel();
        }
    }

    private void apply(VerifyInfo verifyInfo) {
        FragmentActivity activity = getActivity();
        if (activity instanceof LeaveListener) {
            LeaveListener listener = (LeaveListener) activity;
            listener.onApply(verifyInfo);
        }
    }

}
