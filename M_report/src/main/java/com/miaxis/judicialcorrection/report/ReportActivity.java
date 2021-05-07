package com.miaxis.judicialcorrection.report;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.databinding.ActivityReportBinding;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.VerifyPageFragment;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIDCardBindingFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * ReportActivity
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
@AndroidEntryPoint
@Route(path = "/report/ReportActivity")
public class ReportActivity extends BaseBindingActivity<ActivityReportBinding> implements ReadIdCardCallback, VerifyCallback {

    String title = "日常报告";

    @Override
    protected int initLayout() {
        return R.layout.activity_report;
    }

    @Override
    protected void initView(@NonNull ActivityReportBinding binding, @Nullable Bundle savedInstanceState) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_root, new ReadIDCardBindingFragment(title, true))
                .commitNow();
    }

    @Override
    protected void initData(@NonNull ActivityReportBinding binding, @Nullable Bundle savedInstanceState) {

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
        new DialogResult(this, new DialogResult.ClickListener() {
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
                finish();
            }
        }, new DialogResult.Builder(
                ZZResponse.isSuccess(response),
                ZZResponse.isSuccess(response) ? title + "成功" : "验证失败",
                ZZResponse.isSuccess(response) ? "系统将自动返回" + title + "身份证刷取页面" : "请点击“重新验证”重新尝试验证，\n如还是失败，请联系现场工作人员。",
                10, true
        )).show();
    }
}
