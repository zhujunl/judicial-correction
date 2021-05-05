package com.miaxis.judicialcorrection.report;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.bean.IdCard;
import com.miaxis.judicialcorrection.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.report.databinding.ActivityReportBinding;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
public class ReportActivity extends BaseBindingActivity<ActivityReportBinding> implements ReadIdCardCallback {

    String title = "日常报告";

    @Override
    protected int initLayout() {
        return R.layout.activity_report;
    }

    @Override
    protected void initView(@NonNull ActivityReportBinding binding, @Nullable Bundle savedInstanceState) {
        Fragment navigation = (Fragment) ARouter.getInstance()
                .build("/page/readIDCard")
                .withString("Title", title)
                .withBoolean("NoIdCardEnable", true)
                .navigation();
        if (navigation != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_root, navigation)
                    .commitNow();
        }
    }

    @Override
    protected void initData(@NonNull ActivityReportBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onIdCardRead(IdCard result) {
        Timber.e("读取身份证：result:" + result);
        Fragment navigation = (Fragment) ARouter.getInstance()
                .build("/page/verifyPage")
                .withString("Title", title)
                .withString("Name", result.idCardMsg.name)
                .withString("IdCardNumber", result.idCardMsg.id_num)
                .navigation();
        if (navigation != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_root, navigation)
                    .commitNow();
        }
    }

}
