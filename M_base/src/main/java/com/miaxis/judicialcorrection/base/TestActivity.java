package com.miaxis.judicialcorrection.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.User;

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
@Route(path = "/test/TestAct")
public class TestActivity extends AppCompatActivity {
    @Inject
    ApiService apiService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        apiService.login("111","111","111").observe(this, userApiResult -> {
//            Timber.i("result %s ",userApiResult);
//            runOnUiThread(() -> Toast.makeText(this,"???",Toast.LENGTH_SHORT).show());
//        });

    }
}
