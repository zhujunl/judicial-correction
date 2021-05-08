package com.miaxis.judicialcorrection.leave;

import android.os.Bundle;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.leave.databinding.FragmentLeaveCancelBinding;

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
public class LeaveCancelFragment extends BaseBindingFragment<FragmentLeaveCancelBinding> {

    private String title = "销假";

    public LeaveCancelFragment() {
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_leave_cancel;
    }

    @Override
    protected void initView(@NonNull FragmentLeaveCancelBinding binding, @Nullable Bundle savedInstanceState) {
        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> finish());



    }

    @Override
    protected void initData(@NonNull FragmentLeaveCancelBinding binding, @Nullable Bundle savedInstanceState) {

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
