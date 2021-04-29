package com.miaxis.judicialcorrection.report;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.User;
import com.miaxis.judicialcorrection.report.databinding.ActivityReportBinding;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * TestActivity
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
@AndroidEntryPoint
@Route(path = "/report/ReportActivity")
public class ReportActivity extends BaseBindingActivity<ActivityReportBinding> {

    @Inject
    ApiService apiService;

    ActivityReportBinding binding;

    @Override
    protected int initLayout() {
        return R.layout.activity_report;
    }

    @Override
    protected void initView(@NonNull ActivityReportBinding binding, @Nullable Bundle savedInstanceState) {
        binding.personList.setOnClickListener(v -> {
            apiService.personList("2019/01/13 12:55:35").observe(this, (ApiResult<List<User>> userApiResult) -> {
                Timber.i("result %s ", userApiResult);
                runOnUiThread(() -> {
                    binding.test.setText(userApiResult.toString());
                });
            });
        });
    }

    @Override
    protected void initData(@NonNull ActivityReportBinding binding, @Nullable Bundle savedInstanceState) {

    }
}
