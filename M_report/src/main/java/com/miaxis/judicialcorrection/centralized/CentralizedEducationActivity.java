package com.miaxis.judicialcorrection.centralized;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.api.vo.Education;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.TimeUtils;
import com.miaxis.judicialcorrection.base.utils.numbers.HexStringUtils;
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
 * 集中教育
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
@AndroidEntryPoint
public class CentralizedEducationActivity extends BaseBindingActivity<ActivityReportBinding> implements ReadIdCardCallback, VerifyCallback {

    String title = "集中教育";
    @Inject
    CentralizedEducationRepo mCentralizedEducationRepo;

    @Inject
    Lazy<AppHints> appHintsLazy;

    private String mTempId;

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
            mPid = personInfo.getId();
            getList(personInfo);
        }
    }

    private void getList(PersonInfo personInfo) {
        mCentralizedEducationRepo.getEducation(1, 20).observe(this, new Observer<Resource<Education>>() {
            @Override
            public void onChanged(Resource<Education> objectResource) {
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
                        if (objectResource.data == null || objectResource.data.list == null ||
                                objectResource.data.list.isEmpty()) {
                            appHintsLazy.get().showError("无" + title + "数据", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            return;
                        }
                        Education.ListBean temp = null;
                        for (Education.ListBean listBean : objectResource.data.list) {
                            if (TimeUtils.isInTime(listBean.jyxxkssj, listBean.jyxxjssj)) {
                                temp = listBean;
                                break;
                            }
                        }
                        if (temp == null) {
                            appHintsLazy.get().showError("当前还没有" + title + "，如需签到，请联系工作人员!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            return;
                        }
                        mTempId = temp.id;
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.layout_root, new VerifyPageFragment(title, personInfo, temp))
                                .commitNow();
                        break;
                }
            }
        });
    }

    @Override
    public void onVerify(ZZResponse<VerifyInfo> response) {
        if (ZZResponse.isSuccess(response)) {
            addEducation(mTempId, mPid);
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

    private void addEducation(String id, String pid) {
        mCentralizedEducationRepo.educationAdd(id, pid).observe(this, new Observer<Resource<Object>>() {
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
                        builder.countDownTime = 10;
                        builder.title = title + "成功！";
                        builder.message = "系统将自动返回" + title + "身份证刷取页面";
                        new DialogResult(CentralizedEducationActivity.this, new DialogResult.ClickListener() {
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
    }
}
