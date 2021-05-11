package com.miaxis.judicialcorrection.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * @author Tank
 * @date 2021/4/25 3:59 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public abstract class BaseBindingFragment<V extends ViewDataBinding> extends Fragment {

    protected V binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, initLayout(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        initView(binding, savedInstanceState);
        initData(binding, savedInstanceState);
    }

    protected abstract int initLayout();

    protected abstract void initView(@NonNull V binding, @Nullable Bundle savedInstanceState);

    protected abstract void initData(@NonNull V binding, @Nullable Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.unbind();
        }
       Activity activity= getActivity();
        if (activity instanceof BaseBindingActivity){
            BaseBindingActivity baseBindingActivity= (BaseBindingActivity)activity;
            if (baseBindingActivity!=null){
                baseBindingActivity.hideInputMethod();
            }
        }

    }

    protected void finish() {
        getActivity().finish();
    }

    protected void showLoading() {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseBindingActivity) {
            ((BaseBindingActivity<?>) activity).showLoading();
        }
    }

    protected void showLoading(String title, String message) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseBindingActivity) {
            ((BaseBindingActivity<?>) activity).showLoading(title, message);
        }
    }

    protected void dismissLoading() {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseBindingActivity) {
            ((BaseBindingActivity<?>) activity).dismissLoading();
        }
    }
}
