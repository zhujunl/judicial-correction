package com.miaxis.judicialcorrection.leave;

import android.os.Bundle;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.leave.databinding.FragmentProgressBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 查询进度
 */
public class ProgressFragment extends BaseBindingFragment<FragmentProgressBinding> {

    private String title = "进度查询";


    public ProgressFragment() {
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_progress;
    }

    @Override
    protected void initView(@NonNull FragmentProgressBinding binding, @Nullable Bundle savedInstanceState) {
        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    protected void initData(@NonNull FragmentProgressBinding binding, @Nullable Bundle savedInstanceState) {

    }
}
