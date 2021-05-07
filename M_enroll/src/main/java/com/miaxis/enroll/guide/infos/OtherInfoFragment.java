package com.miaxis.enroll.guide.infos;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentOtherInfoBinding;
import com.miaxis.enroll.vo.OtherInfo;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;


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
        EnrollSharedViewModel vm = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(vm);
    }

    @Override
    protected void initData(@NonNull FragmentOtherInfoBinding binding, @Nullable Bundle savedInstanceState) {

    }
}
