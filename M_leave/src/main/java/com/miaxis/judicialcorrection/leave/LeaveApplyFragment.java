package com.miaxis.judicialcorrection.leave;

import android.os.Bundle;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.utils.TimeUtils;
import com.miaxis.judicialcorrection.dialog.DatePickDialog;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.leave.bean.ApplyData;
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

    private ApplyData applyData = new ApplyData();

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

        applyData.name = verifyInfo.name;
        applyData.idCardNumber = verifyInfo.idCardNumber;
        binding.setData(applyData);


        binding.tvApplyTime.setOnClickListener(v ->
                new DatePickDialog(getContext(), binding.tvApplyTime::setText).show()
        );

        binding.tvApplyStartTime.setOnClickListener(v -> {
                    new DatePickDialog(getContext(), date -> {
                        applyData.startTime = date;
                        applyData.days = (int) TimeUtils.getDays(applyData.startTime, applyData.endTime)+"";
                    }).show();
                }
        );

        binding.tvApplyEndTime.setOnClickListener(v -> {
                    new DatePickDialog(getContext(), date -> {
                        applyData.endTime = date;
                        applyData.days = (int) TimeUtils.getDays(applyData.startTime, applyData.endTime)+"";
                    }).show();
                }
        );

        binding.tvApplyEndTime.setOnClickListener(v -> {
                    new DatePickDialog(getContext(), date -> {
                        applyData.endTime = date;
                        applyData.days = (int) TimeUtils.getDays(applyData.startTime, applyData.endTime)+"";
                    }).show();
                }
        );


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
