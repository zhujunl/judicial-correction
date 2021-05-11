package com.miaxis.judicialcorrection.guide;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressChangeDetailsBean;
import com.miaxis.judicialcorrection.live.LiveAddressChangeActivity;
import com.miaxis.judicialcorrection.live.LiveAddressChangeViewModel;
import com.miaxis.judicialcorrection.live.R;
import com.miaxis.judicialcorrection.live.databinding.FragmentToLiveProgressBinding;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * 查询进度
 */
@AndroidEntryPoint
public class ToLiveProgressFragment extends BaseBindingFragment<FragmentToLiveProgressBinding> {


    private LiveAddressChangeViewModel model;

    @Override
    protected int initLayout() {
        return R.layout.fragment_to_live_progress;
    }

    @Override
    protected void initView(@NonNull @NotNull FragmentToLiveProgressBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding.btnBackToHome.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((LiveAddressChangeActivity) getActivity()).replaceFragment(new LiveListFragment());
            }
        });
    }

    @Override
    protected void initData(@NonNull @NotNull FragmentToLiveProgressBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(getActivity()).get(LiveAddressChangeViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(model);
        model.getLiveAddressChangeDetails(model.mId).observe(this, obServer -> {
            if (obServer.isSuccess()&&obServer.data!=null) {
                model.LiveDetailsBean.postValue(obServer.data);
                binding.tvMoveInPlace.setText(combineStrings(obServer.data));
            }
        });
    }

    public  String combineStrings(LiveAddressChangeDetailsBean vm) {
        String qrdszsName = vm.qrdszsName==null ? "" : vm.qrdszsName;
        String qrdszdName = vm.qrdszdName==null? "" : vm.qrdszdName;
        if (qrdszsName.equals(qrdszdName)){
            qrdszdName="";
        }
        String qrdszxName = vm.qrdszxName==null?"" : vm.qrdszxName;
        String qrdxzName = vm.qrdxzName==null ? "" : vm.qrdxzName;
        String qrdmx = vm.qrdmx==null ? "" : vm.qrdmx;
        return "迁入地：" + qrdszsName + qrdszdName + qrdszxName + qrdxzName + qrdmx;
    }
}
