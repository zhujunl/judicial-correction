package com.miaxis.enroll.guide.voice;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentVoiceprintCollectBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Status;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.AppToast;
import com.miaxis.judicialcorrection.face.utils.FileUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


/**
 * 声纹采集
 */
@AndroidEntryPoint
public class VoicePrintCollectFragment extends BaseBindingFragment<FragmentVoiceprintCollectBinding> {


    private static final Handler mHandler = new Handler();

    private PersonInfo mPersonInfo;

    public VoicePrintCollectFragment(PersonInfo personInfo) {
        this.mPersonInfo = personInfo;
    }

    @Inject
    AppToast mAppToast;

    @Inject
    AppHints mAppHints;

    private   VoicePrintModel model;


    @Override
    protected int initLayout() {
        return R.layout.fragment_voiceprint_collect;
    }

    @Override
    protected void initView(@NonNull @NotNull FragmentVoiceprintCollectBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding.imgVoicePrint.setOnClickListener(v -> {
            if(model.isIdle()){
                model.start();
                mAppToast.show("开始录制中...");
            }else{
                model.stop();
            }
            mAppToast.show("停止录制...");
        });
        binding.btnBackToHome.setOnClickListener(v -> finish());
    }

    @Override
    protected void initData(@NonNull @NotNull FragmentVoiceprintCollectBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
         model = new ViewModelProvider(this).get(VoicePrintModel.class);

        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
                return;
            }
        }
        model.init();
        mHandler.postDelayed(() -> {
            if (model.isRunning()) {
                model.stop();
            }
            model.start();
            mAppToast.show("开始录制中...");
        }, 1000);
        model.observableFile.observe(this, file -> {
            if (file == null) {
                mAppHints.showError("采集失败,请点击重新录制");
                return;
            }
            String s = FileUtil.fileToBase64(file);
            if (TextUtils.isEmpty(s)) {
                model.uploadVoicePrint(mPersonInfo.getId(), s).observe(VoicePrintCollectFragment.this, objectResource -> {
                    if (objectResource.status == Status.LOADING) {
                        showLoading("提示", "提交中");
                    } else if (objectResource.status == Status.SUCCESS) {
                        dismissLoading();
                        mAppToast.show("采集成功...");
                    } else if (objectResource.status == Status.ERROR) {
                        mAppHints.showError(objectResource.errorMessage);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacksAndMessages(null);
    }
}