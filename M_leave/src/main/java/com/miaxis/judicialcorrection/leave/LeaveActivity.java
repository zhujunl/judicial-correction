package com.miaxis.judicialcorrection.leave;

import android.os.Bundle;

import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.VerifyPageFragment;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIDCardBindingFragment;
import com.miaxis.judicialcorrection.leave.apply.LeaveApplyFragment;
import com.miaxis.judicialcorrection.leave.databinding.ActivityLeaveBinding;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;


/**
 * 请销假界面
 *
 * @author tangkai
 * Created on 4/25/21.
 */
@AndroidEntryPoint
public class LeaveActivity extends BaseBindingActivity<ActivityLeaveBinding> implements ReadIdCardCallback, VerifyCallback, LeaveListener {

    String title = "请销假";

    @Override
    protected int initLayout() {
        return R.layout.activity_leave;
    }

    @Override
    protected void initView(@NonNull ActivityLeaveBinding binding, @Nullable Bundle savedInstanceState) {
        //readIdCard();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_root, new LeaveApplyFragment(new VerifyInfo("11", "22", "33")))
                .commitNow();
    }

    private void readIdCard() {
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
    }

    @Override
    public void onLogin(PersonInfo personInfo) {
        if (personInfo != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_root, new VerifyPageFragment(title, personInfo))
                    .commitNow();
        }
    }

    @Override
    public void onVerify(ZZResponse<VerifyInfo> response) {
        if (ZZResponse.isSuccess(response)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_root, new LeaveListFragment(response.getData()))
                    .commitNow();
        } else {
            DialogResult.Builder builder = new DialogResult.Builder();
            builder.success = false;
            builder.countDownTime = 10;
            builder.title = "验证失败";
            builder.message = "请联系现场工作人员处理\n" +
                    "（工作人员需确认前期登记的\n" +
                    "身份证号是否准确）！";
            new DialogResult(this, new DialogResult.ClickListener() {
                @Override
                public void onBackHome(AppCompatDialog appCompatDialog) {
                    appCompatDialog.dismiss();
                    finish();
                }

                @Override
                public void onTryAgain(AppCompatDialog appCompatDialog) {
                    appCompatDialog.dismiss();
                    readIdCard();
                }

                @Override
                public void onTimeOut(AppCompatDialog appCompatDialog) {
                    appCompatDialog.dismiss();
                    finish();
                }
            }, builder).show();
        }
    }

    @Override
    public void onApply(@NotNull VerifyInfo verifyInfo) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_root, new LeaveApplyFragment(verifyInfo))
                .commitNow();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onQueryProgress() {

    }

}
