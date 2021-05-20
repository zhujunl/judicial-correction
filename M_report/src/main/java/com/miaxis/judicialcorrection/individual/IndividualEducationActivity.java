package com.miaxis.judicialcorrection.individual;

import android.content.DialogInterface;
import android.os.Bundle;

import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.api.vo.Education;
import com.miaxis.judicialcorrection.base.api.vo.IndividualEducationBean;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.TimeUtils;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.VerifyPageFragment;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIDCardBindingFragment;
import com.miaxis.judicialcorrection.report.R;
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
 * FocusActivity
 * 个别教育
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
@AndroidEntryPoint
public class IndividualEducationActivity extends BaseBindingActivity<ActivityReportBinding> implements ReadIdCardCallback, VerifyCallback {

    String title = "个别教育";

    @Inject
    IndividualEducationRepo mIndividualEducationRepo;

    @Inject
    Lazy<AppHints> appHintsLazy;

    private String mListItemId;

    private String mPid;

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

    @Override
    public void onLogin(PersonInfo personInfo) {
        if (personInfo != null) {
            mPid=personInfo.getId();
            getList(personInfo.getId(),personInfo);
        }
    }

    @Override
    public void onVerify(ZZResponse<VerifyInfo> response) {
        if (ZZResponse.isSuccess(response)) {
            mIndividualEducationRepo.individualAdd(mPid,mListItemId).observe(this, new Observer<Resource<Object>>() {
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
                            new DialogResult(IndividualEducationActivity.this, new DialogResult.ClickListener() {
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

    private void getList(String pid,PersonInfo personInfo) {
        mIndividualEducationRepo.getPersonEducation(pid).observe(this, observer -> {
            switch (observer.status) {
                case LOADING:
                    showLoading(title, "正在获取" + title + "信息，请稍后");
                    break;
                case ERROR:
                    dismissLoading();
                    appHintsLazy.get().showError("Error:" + observer.errorMessage);
                    break;
                case SUCCESS:
                    dismissLoading();
                    if (observer.data == null || observer.data.getList() == null ||
                            observer.data.getList().isEmpty()) {
                        appHintsLazy.get().showError("无" + title + "数据", (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        });
                        return;
                    }
                    IndividualEducationBean.ListDTO temp = null;
                    for (IndividualEducationBean.ListDTO listBean : observer.data.getList()) {
                        if (TimeUtils.isInTime(listBean.getJyxxkssj(), listBean.getJyxxjssj())) {
                            temp = listBean;
                            break;
                        }
                    }
                    if (temp == null) {
                        appHintsLazy.get().showError("当前还没有"+title+"，如需签到，请联系工作人员!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        return;
                    }
                    mListItemId=temp.getId();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_root, new VerifyPageFragment(title, personInfo,temp))
                            .commitNow();
                    break;
            }
        });
    }

}
