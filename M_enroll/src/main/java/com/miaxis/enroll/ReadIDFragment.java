package com.miaxis.enroll;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miaxis.enroll.databinding.FragmentReadIdBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * GoHomeFragment
 *
 * @author zhangyw
 * Created on 4/28/21.
 */
@AndroidEntryPoint
public class ReadIDFragment extends BaseBindingFragment<FragmentReadIdBinding> {

    @Override
    protected int initLayout() {
        return R.layout.fragment_read_id;
    }

    @Override
    protected void initView(@NonNull FragmentReadIdBinding binding, @Nullable Bundle savedInstanceState) {
        binding.btnBackToHome.setOnClickListener(v -> finish());
    }

    @Override
    protected void initData(@NonNull FragmentReadIdBinding binding, @Nullable Bundle savedInstanceState) {

    }
}
