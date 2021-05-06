package com.miaxis.enroll.guide.infos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentOtherInfoBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * OtherInfoFragment
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@AndroidEntryPoint
public class OtherInfoFragment extends BaseInfoFragment<FragmentOtherInfoBinding> {
    @Override
    protected int initLayout() {
        return R.layout.fragment_other_info;
    }

    @Override
    protected void initView(@NonNull FragmentOtherInfoBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData(@NonNull FragmentOtherInfoBinding binding, @Nullable Bundle savedInstanceState) {

    }
}
