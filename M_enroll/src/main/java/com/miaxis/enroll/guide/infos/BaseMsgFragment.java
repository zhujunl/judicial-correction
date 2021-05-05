package com.miaxis.enroll.guide.infos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentBaseMsgBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * BaseMsgFragment
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@AndroidEntryPoint
public class BaseMsgFragment extends BaseBindingFragment<FragmentBaseMsgBinding> {

    @Override
    protected int initLayout() {
        return R.layout.fragment_base_msg;
    }

    @Override
    protected void initView(@NonNull FragmentBaseMsgBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData(@NonNull FragmentBaseMsgBinding binding, @Nullable Bundle savedInstanceState) {

    }
}
