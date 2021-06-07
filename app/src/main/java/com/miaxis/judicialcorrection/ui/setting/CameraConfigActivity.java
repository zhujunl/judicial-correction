package com.miaxis.judicialcorrection.ui.setting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.databinding.ActivityCameraConfigBinding;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CameraConfigActivity extends BaseBindingActivity<ActivityCameraConfigBinding> {

    private CameraConfigModel model;
    private static Handler mHandler = new Handler();

    @Override
    protected int initLayout() {
        return R.layout.activity_camera_config;
    }

    @Override
    protected void initView(@NonNull @NotNull ActivityCameraConfigBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData(@NonNull @NotNull ActivityCameraConfigBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(this).get(CameraConfigModel.class);
        binding.setData(model);
        model.init();
        binding.btnSave.setOnClickListener(v -> {
                    model.save();
                    showLoading();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoading();
                            finish();
                        }
                    }, 2000);
                }
        );
    }
}