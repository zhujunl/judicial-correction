package com.miaxis.enroll.guide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentVoiceprintCollectBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.utils.AppHints;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;


/**
 * 声纹采集
 */
@AndroidEntryPoint
public class FingerprintCollectFragment extends BaseBindingFragment<FragmentVoiceprintCollectBinding> {


    public static FingerprintCollectFragment getInstance() {
        FingerprintCollectFragment fragment = new FingerprintCollectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("idCard", "");
        fragment.setArguments(bundle);
        return fragment;
    }

    private FingerprintCollectModel mFingerprintCollectModel;
    private EnrollSharedViewModel mEnrollSharedViewModel;
    @Inject
    Lazy<AppHints> appHintsLazy;

    @Override
    protected int initLayout() {
        return R.layout.fragment_fingerprint_collect;
    }

    @Override
    protected void initView(@NonNull @NotNull FragmentVoiceprintCollectBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initData(@NonNull @NotNull FragmentVoiceprintCollectBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mEnrollSharedViewModel = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);
        mFingerprintCollectModel = new ViewModelProvider(this).get(FingerprintCollectModel.class);
        mFingerprintCollectModel.initFingerDevice(new FingerprintCollectModel.OnFingerInitListener() {
            @Override
            public void onInit(boolean result) {
                    if (!result){
                        appHintsLazy.get().showError("指纹识别初始化模块失败！");
                    }
            }
        });
        //读取指纹模块
        mFingerprintCollectModel.releaseFingerDevice();
        //得到bitmap上传
        mFingerprintCollectModel.fingerBitmap.observe(this,bitmap->{

        });
    }
}