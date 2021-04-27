package com.miaxis.judicialcorrection;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundDiffAdapter;
import com.miaxis.judicialcorrection.databinding.ItemMainBinding;
import com.miaxis.judicialcorrection.databinding.MainActivityBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@Route(path = "/main/MainActivity")
@AndroidEntryPoint
public class MainActivity extends BaseBindingActivity<MainActivityBinding> {

    @Inject
    AppDatabase appDatabase;

    @Override
    protected int initLayout() {
        return R.layout.main_activity;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainAdapter mainAdapter = new MainAdapter();
        binding.recyclerView.setAdapter(mainAdapter);
        appDatabase.mainFuncDAO().loadFuncActive().observe(this, mainAdapter::submitList);
    }

    @Override
    protected void initData(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.initData(view, savedInstanceState);

    }

    static class MainAdapter extends BaseDataBoundDiffAdapter<MainFunc, ItemMainBinding> {

        protected MainAdapter() {
            super(new DiffUtil.ItemCallback<MainFunc>() {
                @Override
                public boolean areItemsTheSame(@NonNull MainFunc oldItem, @NonNull MainFunc newItem) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull MainFunc oldItem, @NonNull MainFunc newItem) {
                    return false;
                }
            });
        }

        @Override
        protected ItemMainBinding createBinding(ViewGroup parent, int viewType) {
            return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_main, parent, false);
        }

        @Override
        protected void bind(ItemMainBinding binding, MainFunc item) {
            binding.setBgRes(item.resId);
            binding.setTitle(item.title);
            binding.getRoot().setOnClickListener(v ->
                    ARouter.getInstance().build(item.targetActivityURI).navigation()
            );
        }
    }


}