package com.miaxis.enroll.guide.infos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentAddressBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * AddressFragment
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@AndroidEntryPoint
public class AddressFragment extends BaseBindingFragment<FragmentAddressBinding> {
    @Override
    protected int initLayout() {
        return R.layout.fragment_address;
    }

    @Override
    protected void initView(@NonNull FragmentAddressBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData(@NonNull FragmentAddressBinding binding, @Nullable Bundle savedInstanceState) {

    }
}
