package com.miaxis.judicialcorrection.ui.setting;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JAuthInfo;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;
import com.miaxis.judicialcorrection.base.repo.JusticeBureauRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.base.utils.AppToast;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundDiffAdapter;
import com.miaxis.judicialcorrection.databinding.ActivitySettingBinding;
import com.miaxis.judicialcorrection.databinding.ItemSettingFuncBinding;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class SettingActivity extends BaseBindingActivity<ActivitySettingBinding> {

    @Inject
    AppDatabase appDatabase;
    @Inject
    MainAdapter mainAdapter;
    @Inject
    AppExecutors appExecutors;
    @Inject
    JusticeBureauRepo justiceBureauRepo;
    @Inject
    AppToast appToast;
    JusticeBureau mJusticeBureau;

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
        appDatabase.tokenAuthInfoDAO().loadAuthInfo().observe(this, (JAuthInfo info) -> {
            if (info != null && info.activationCode != null) {
                Timber.i(" Query JAuthInfo == null ");
                binding.etActiveCode.setText(info.activationCode);
            }
        });
        binding.etActiveCode.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
                syncActiveCode();
                return true;
            }
            return false;
        });
        justiceBureauRepo.getMyJusticeBureau().observe(this, justiceBureau -> {
            // 暂时先这样
            mJusticeBureau = justiceBureau;
            Timber.i("getMyJusticeBureau %s", justiceBureau);
        });
        justiceBureauRepo.getAllJusticeBureau().observe(this, (Resource<List<JusticeBureau>> listResource) -> {
            if (listResource.isError()) {
                appToast.show("Error:" + listResource.errorMessage);
            } else if (listResource.isSuccess()) {
                int select = -1;
                String[] mItems = new String[listResource.data.size() + 1];
                mItems[0]="请选择";
                for (int i = 0; i < listResource.data.size(); i++) {
                    JusticeBureau justiceBureau = listResource.data.get(i);
                    mItems[i+1] = justiceBureau.getTeamName();
                    if (mJusticeBureau != null && Objects.equals(justiceBureau.getTeamId(), mJusticeBureau.getTeamId())) {
                        select = i;
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinner.setAdapter(adapter);
                binding.spinner.setSelection(select);
                binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        JusticeBureau justiceBureau = listResource.data.get(position);
                        Timber.i("onItemSelected %d ,[%s]", position, justiceBureau);
                        if (position != 0) {
                            appExecutors.diskIO().execute(() -> justiceBureauRepo.setMyJusticeBureau(justiceBureau));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        syncActiveCode();
    }

    void syncActiveCode() {
        String aCode = binding.etActiveCode.getText().toString();
        appExecutors.diskIO().execute(() -> {
            JAuthInfo jAuthInfo = appDatabase.tokenAuthInfoDAO().loadAuthInfoSync();
            if (jAuthInfo == null) {
                Timber.i(" Change JAuthInfo == null ");
                jAuthInfo = new JAuthInfo();
            }
            jAuthInfo.activationCode = aCode;
            appDatabase.tokenAuthInfoDAO().insert(jAuthInfo);
        });
    }

    @Override
    protected void initData(@NonNull ActivitySettingBinding binding, @Nullable Bundle savedInstanceState) {

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