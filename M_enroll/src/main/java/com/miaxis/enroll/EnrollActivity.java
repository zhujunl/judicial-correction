package com.miaxis.enroll;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.miaxis.enroll.databinding.ActivityEnrollBinding;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * EnrollActivity
 *
 * @author zhangyw
 * Created on 4/28/21.
 */
@AndroidEntryPoint
@Route(path = "/enroll/EnrollActivity")
public class EnrollActivity extends BaseBindingActivity<ActivityEnrollBinding> {
    @Override
    protected int initLayout() {
        return R.layout.activity_enroll;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.container,
                        (Fragment) ARouter.getInstance()
                                .build("/page/readIDCard")
                                .withString("Title", "测试")
                                .withBoolean("NoIdCardEnable", true)
                                .withBoolean("AutoCheckEnable", true)
                                .navigation()
                )
                .commitNow();
    }
}
