package com.miaxis.judicialcorrection.base;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

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
    }

    protected void finish() {
        getActivity().finish();
    }
}
