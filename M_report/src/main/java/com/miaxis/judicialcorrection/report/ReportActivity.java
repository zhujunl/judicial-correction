package com.miaxis.judicialcorrection.report;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.User;
import com.miaxis.judicialcorrection.report.databinding.ActivityReportBinding;

import java.util.List;

import javax.inject.Inject;

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
public class ReportActivity extends AppCompatActivity {
    @Inject
    ApiService apiService;

    ActivityReportBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report);
        binding.personList.setOnClickListener(v -> {
            apiService.personList("2019/01/13 12:55:35").observe(this, (ApiResult<List<User>> userApiResult) -> {
                Timber.i("result %s ",userApiResult);
                runOnUiThread(()->{binding.test.setText(userApiResult.toString());});
            });
        });
    }
}
