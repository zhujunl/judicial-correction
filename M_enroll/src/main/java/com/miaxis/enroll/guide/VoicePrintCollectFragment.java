package com.miaxis.enroll.guide;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentVoiceprintCollectBinding;
import com.miaxis.enroll.utils.RecordUtils;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.utils.AppToast;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * 声纹采集
 */
@AndroidEntryPoint
public class VoicePrintCollectFragment extends BaseBindingFragment<FragmentVoiceprintCollectBinding> {


    private static final Handler mHandler = new Handler();

    @Inject
    AppToast mAppToast;

    @Override
    protected int initLayout() {
        return R.layout.fragment_voiceprint_collect;
    }

    @Override
    protected void initView(@NonNull @NotNull FragmentVoiceprintCollectBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
      binding.imgVoicePrint.setOnClickListener(v -> {
          if (RecordUtils.isRunning()) {
              RecordUtils.stop();
          }
          //得到文件路径
          File pathFile = RecordUtils.getPathFile();
      });
    }

    @Override
    protected void initData(@NonNull @NotNull FragmentVoiceprintCollectBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        RecordUtils.init();
        mHandler.postDelayed(() -> {
            if (RecordUtils.isRunning()) {
                RecordUtils.stop();
            }
            RecordUtils.start();
            mAppToast.show("开始录制中...");
        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (RecordUtils.isRunning()) {
            RecordUtils.stop();
        }
        mHandler.removeCallbacksAndMessages(null);
    }
}