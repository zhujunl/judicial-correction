package com.miaxis.judicialcorrection.leave;

import android.os.Bundle;

import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.VerifyPageFragment;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIDCardBindingFragment;
import com.miaxis.judicialcorrection.leave.databinding.ActivityLeaveBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import timber.log.Timber;


/**
 * 请销假界面
 *
 * @author tangkai
 * Created on 4/25/21.
 */

public class LeaveActivity extends BaseBindingActivity<ActivityLeaveBinding> implements ReadIdCardCallback, VerifyCallback, LeaveListener {

    String title = "请销假";

    @Override
    protected int initLayout() {
        return R.layout.activity_leave;
    }

    @Override
    protected void initView(@NonNull ActivityLeaveBinding binding, @Nullable Bundle savedInstanceState) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_root, new ReadIDCardBindingFragment(title, true))
                .commitNow();
    }

    @Override
    protected void initData(@NonNull ActivityLeaveBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onIdCardRead(IdCard result) {
        Timber.e("读取身份证：result:" + result);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_root, new VerifyPageFragment(title, result.idCardMsg.name, result.idCardMsg.id_num))
                .commitNow();
    }

    @Override
    public void onVerify(ZZResponse<VerifyInfo> response) {
        if (ZZResponse.isSuccess(response)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_root, new LeaveListFragment())
                    .commitNow();
        }
    }

    @Override
    public void onApply() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_root, new LeaveApplyFragment())
                .commitNow();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onQueryProgress() {

    }

}
