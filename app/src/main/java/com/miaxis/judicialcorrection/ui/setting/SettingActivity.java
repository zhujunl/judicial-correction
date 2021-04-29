package com.miaxis.judicialcorrection.ui.setting;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundDiffAdapter;
import com.miaxis.judicialcorrection.databinding.ActivitySettingBinding;
import com.miaxis.judicialcorrection.databinding.ItemSettingFuncBinding;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
@Route(path = "/setting/SettingActivity")
public class SettingActivity extends BaseBindingActivity<ActivitySettingBinding> {

    @Inject
    AppDatabase appDatabase;
    @Inject
    MainAdapter mainAdapter;

    @Override
    protected int initLayout() {
        return R.layout.activity_setting;
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void initView(@NonNull ActivitySettingBinding view, @Nullable Bundle savedInstanceState) {
        binding.recyclerView.setAdapter(mainAdapter);
        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.setImei(Build.SERIAL);
        appDatabase.mainFuncDAO().loadFuncAll().observe(this, mainAdapter::submitList);
        // TODO: 4/28/21 司法局设置，正在沟通
    }

    public static class MainAdapter extends BaseDataBoundDiffAdapter<MainFunc, ItemSettingFuncBinding> {

        @Inject
        AppDatabase appDatabase;
        @Inject
        AppExecutors appExecutors;

        @Inject
        public MainAdapter() {
            super(new DiffUtil.ItemCallback<MainFunc>() {
                @Override
                public boolean areItemsTheSame(@NonNull MainFunc oldItem, @NonNull MainFunc newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull MainFunc oldItem, @NonNull MainFunc newItem) {
                    return Objects.equals(oldItem, newItem);
                }
            });
        }

        @Override
        protected ItemSettingFuncBinding createBinding(ViewGroup parent, int viewType) {
            return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_setting_func, parent, false);
        }

        @Override
        protected void bind(ItemSettingFuncBinding binding, MainFunc item) {
            binding.setData(item);
            binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Timber.i("item %s,isCheck %s", item, isChecked);
                if (item.active != isChecked) {
                    item.active = isChecked;
                    appExecutors.diskIO().execute(() -> appDatabase.mainFuncDAO().insertFunc(item));
                }
            });
        }
    }
}