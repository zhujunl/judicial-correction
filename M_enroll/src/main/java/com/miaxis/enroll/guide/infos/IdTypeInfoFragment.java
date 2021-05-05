package com.miaxis.enroll.guide.infos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentIdTypeInfoBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * BaseMsgFragment
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@AndroidEntryPoint
public class IdTypeInfoFragment extends BaseBindingFragment<FragmentIdTypeInfoBinding> {

    @Override
    protected int initLayout() {
        return R.layout.fragment_id_type_info;
    }

    @Override
    protected void initView(@NonNull FragmentIdTypeInfoBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData(@NonNull FragmentIdTypeInfoBinding binding, @Nullable Bundle savedInstanceState) {

    }
}
