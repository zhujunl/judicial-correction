package com.miaxis.judicialcorrection.leave.progress;

import android.os.Bundle;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.Leave;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.TimeUtils;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.leave.LeaveRepo;
import com.miaxis.judicialcorrection.leave.R;
import com.miaxis.judicialcorrection.leave.databinding.FragmentProgressBinding;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * 查询进度
 */

@AndroidEntryPoint
public class ProgressFragment extends BaseBindingFragment<FragmentProgressBinding> {

    private String title = "进度查询";
    Leave.ListBean item;
    VerifyInfo verifyInfo;
    @Inject
    Lazy<AppHints> appHintsLazy;

    @Inject
    LeaveRepo mLeaveRepo;

    public ProgressFragment(VerifyInfo verifyInfo, Leave.ListBean item) {
        this.verifyInfo = verifyInfo;
        this.item = item;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_progress;
    }

    @Override
    protected void initView(@NonNull FragmentProgressBinding binding, @Nullable Bundle savedInstanceState) {
        ProgressViewModel progressViewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> {
            finish();
        });
        binding.setData(progressViewModel);
        mLeaveRepo.getLeave(item.id).observe(this, leaveResource -> {
            switch (leaveResource.status) {
                case LOADING:
                    showLoading(title, "正在请求" + title + "信息，请稍后");
                    break;
                case ERROR:
                    dismissLoading();
                    appHintsLazy.get().showError("Error:" + leaveResource.errorMessage);
                    break;
                case SUCCESS:
                    dismissLoading();
                    if (leaveResource.data == null) {
                        appHintsLazy.get().showError("没有查询到数据", (dialog, which) -> finish());
                    } else {
                        progressViewModel.progress.set(item.flowStatusName);
                        progressViewModel.name.set(this.verifyInfo.name);
                        progressViewModel.idCardNumber.set(this.verifyInfo.idCardNumber);
                        progressViewModel.startTime.set(TimeUtils.simpleDateFormat.format(item.ksqr));
                        progressViewModel.endTime.set(TimeUtils.simpleDateFormat.format(item.jsrq));
                        progressViewModel.days.set(item.wcts);
                        progressViewModel.reasonType.set(item.wclyName);
                        progressViewModel.reason.set(item.wcly);
                        progressViewModel.cancelTime.set(item.xjsj);
                    }
                    break;
            }
        });
    }

    @Override
    protected void initData(@NonNull FragmentProgressBinding binding, @Nullable Bundle savedInstanceState) {
    }

}
