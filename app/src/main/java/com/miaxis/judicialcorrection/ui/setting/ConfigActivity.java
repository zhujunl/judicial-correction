package com.miaxis.judicialcorrection.ui.setting;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.databinding.ActivityCameraConfigBinding;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ConfigActivity extends BaseBindingActivity<ActivityCameraConfigBinding> {

    private ConfigModel model;
    private static final Handler mHandler = new Handler();

    @Override
    protected int initLayout() {
        return R.layout.activity_camera_config;
    }

    @Override
    protected void initView(@NonNull @NotNull ActivityCameraConfigBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding.btnClose.setOnClickListener(v -> finish());
        //清空时间戳
        binding.btnClearTime.setOnClickListener(v -> {

        });
        //更新
        binding.btnCheckForUpdates.setOnClickListener(v -> {

        });
    }

    @Override
    protected void initData(@NonNull @NotNull ActivityCameraConfigBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(this).get(ConfigModel.class);
        binding.setData(model);
        model.init();
        if ("2".equals(model.cameraRGBId.get())) {
            binding.group1.check(R.id.cameraId_2);
        } else {
            binding.group1.check(R.id.cameraId_0);
        }
        if ("2".equals(model.cameraNIRId.get())) {
            binding.group2.check(R.id.cameraNir_2);
        } else {
            binding.group2.check(R.id.cameraNir_0);
        }

        binding.btnSave.setOnClickListener(v -> {
                    if (binding.group1.getCheckedRadioButtonId() == R.id.cameraId_2) {
                        model.cameraRGBId.set("2");
                    } else {
                        model.cameraRGBId.set("0");
                    }
                    if (binding.group2.getCheckedRadioButtonId() == R.id.cameraNir_2) {
                        model.cameraNIRId.set("2");
                    } else {
                        model.cameraNIRId.set("0");
                    }
                    model.save();
//            EquipmentConfigCameraEntity equipmentConfigCameraEntity = model.setCameraInfo(3);
//            BaseApplication.application.setCameraConfig(equipmentConfigCameraEntity);
                    showLoading();
                    mHandler.postDelayed(() -> {
                        dismissLoading();
                        finish();
                    }, 2000);
                }
        );
    }
}