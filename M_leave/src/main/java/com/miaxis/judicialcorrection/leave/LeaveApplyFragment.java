package com.miaxis.judicialcorrection.leave;

import android.os.Bundle;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.leave.databinding.FragmentLeaveListBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

/**
 * @author Tank
 * @date 2021/5/7 10:08
 * @des
 * @updateAuthor
 * @updateDes
 */
public class LeaveApplyFragment extends BaseBindingFragment<FragmentLeaveListBinding> {

    private String title = "请假申请";

    public LeaveApplyFragment() {
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_leave_apply;
    }

    @Override
    protected void initView(@NonNull FragmentLeaveListBinding binding, @Nullable Bundle savedInstanceState) {
        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> finish());




    }

    @Override
    protected void initData(@NonNull FragmentLeaveListBinding binding, @Nullable Bundle savedInstanceState) {

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

    private void apply() {
        FragmentActivity activity = getActivity();
        if (activity instanceof LeaveListener) {
            LeaveListener listener = (LeaveListener) activity;
            listener.onApply();
        }
    }

}
