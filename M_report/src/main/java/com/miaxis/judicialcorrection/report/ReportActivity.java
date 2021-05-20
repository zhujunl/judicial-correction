package com.miaxis.judicialcorrection.report;

import android.os.Bundle;

import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.VerifyPageFragment;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIDCardBindingFragment;
import com.miaxis.judicialcorrection.report.databinding.ActivityReportBinding;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.Observer;
import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * ReportActivity
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
@AndroidEntryPoint
public class ReportActivity extends BaseBindingActivity<ActivityReportBinding> implements ReadIdCardCallback, VerifyCallback {

    String title = "日常报告";
    @Inject
    ReportRepo mReportRepo;

    @Inject
    Lazy<AppHints> appHintsLazy;

    @Override
    protected int initLayout() {
        return R.layout.activity_report;
    }

    @Override
    protected void initView(@NonNull ActivityReportBinding binding, @Nullable Bundle savedInstanceState) {
        readIdCard();
    }

    private void readIdCard() {
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
    }

    String id = "";

    @Override
    public void onLogin(PersonInfo personInfo) {
        if (personInfo != null) {
            id = personInfo.getId();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_root, new VerifyPageFragment(title, personInfo))
                    .commitNow();
        }
    }

    @Override
    public void onVerify(ZZResponse<VerifyInfo> response) {
        if (ZZResponse.isSuccess(response)) {
            //默认人脸 如果有指纹再传指纹相对应的
            String entryMethod="2";
//            if (response.getData()!=null){
//                entryMethod=response.getData().entryMethod;
//            }
            mReportRepo.reportAdd(id,entryMethod).observe(this, new Observer<Resource<Object>>() {
                @Override
                public void onChanged(Resource<Object> objectResource) {
                    switch (objectResource.status) {
                        case LOADING:
                            showLoading(title, "正在获取" + title + "信息，请稍后");
                            break;
                        case ERROR:
                            dismissLoading();
                            appHintsLazy.get().showError("Error:" + objectResource.errorMessage);
                            break;
                        case SUCCESS:
                            dismissLoading();
                            DialogResult.Builder builder = new DialogResult.Builder();
                            builder.success = true;
                            builder.countDownTime = 3;
                            builder.title = title + "成功！";
                            builder.message = "系统将自动返回" + title + "身份证刷取页面";
                            new DialogResult(ReportActivity.this, new DialogResult.ClickListener() {
                                @Override
                                public void onBackHome(AppCompatDialog appCompatDialog) {
                                    appCompatDialog.dismiss();
                                    finish();
                                }

                                @Override
                                public void onTryAgain(AppCompatDialog appCompatDialog) {
                                }

                                @Override
                                public void onTimeOut(AppCompatDialog appCompatDialog) {
                                    appCompatDialog.dismiss();
                                    finish();
                                }
                            }, builder).show();
                            break;
                    }
                }
            });
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
}
