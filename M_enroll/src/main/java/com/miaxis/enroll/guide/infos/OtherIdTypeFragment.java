package com.miaxis.enroll.guide.infos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentOtherIdTypeBinding;
import com.miaxis.enroll.vo.OtherCardType;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * BaseMsgFragment
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@AndroidEntryPoint
public class OtherIdTypeFragment extends BaseInfoFragment<FragmentOtherIdTypeBinding> {

    @Override
    protected int initLayout() {
        return R.layout.fragment_other_id_type;
    }

    @Override
    protected void initView(@NonNull FragmentOtherIdTypeBinding binding, @Nullable Bundle savedInstanceState) {
        EnrollSharedViewModel vm = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(vm);
    }

    @Override
    protected void initData(@NonNull FragmentOtherIdTypeBinding binding, @Nullable Bundle savedInstanceState) {

    }
}
