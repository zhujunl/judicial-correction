package com.miaxis.enroll.guide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentVoiceprintCollectBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * 声纹采集
 */
@AndroidEntryPoint
public class VoicePrintCollectFragment extends BaseBindingFragment<FragmentVoiceprintCollectBinding> {




    @Override
    protected int initLayout() {
        return R.layout.fragment_voiceprint_collect;
    }

    @Override
    protected void initView(@NonNull @NotNull FragmentVoiceprintCollectBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initData(@NonNull @NotNull FragmentVoiceprintCollectBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

    }
}