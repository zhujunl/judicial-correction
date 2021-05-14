package com.miaxis.judicialcorrection.ui.setting;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;

import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
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
//    JusticeBureau mJusticeBureau;

    @Override
    protected int initLayout() {
        return R.layout.activity_setting;
    }

    private static final String TAG = "MxSettingActivity";
    SettingViewModel viewModel;

    @SuppressLint("HardwareIds")
    @Override
    protected void initView(@NonNull ActivitySettingBinding view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);
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
//        justiceBureauRepo.getMyJusticeBureau().observe(this, justiceBureau -> {
//            // 暂时先这样
////            mJusticeBureau = justiceBureau;
//            Timber.i("getMyJusticeBureau %s", justiceBureau);
//        });

        binding.spinnerShi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object itemAtPosition = parent.getItemAtPosition(position);
                Log.i(TAG, "市 选择: " + position + " - " + itemAtPosition);
                if (itemAtPosition instanceof JusticeBureau) {
                    viewModel.setShi((JusticeBureau) itemAtPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "市 选择: null");
                viewModel.setShi(null);
            }
        });

        binding.spinnerXian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object itemAtPosition = parent.getItemAtPosition(position);
                Log.i(TAG, "县选择: " + position + " - " + itemAtPosition);
                if (itemAtPosition instanceof JusticeBureau) {
                    viewModel.setXian((JusticeBureau) itemAtPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.setXian(null);
                Log.i(TAG, "县选择: null");
            }
        });
        binding.spinnerJiedao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object itemAtPosition = parent.getItemAtPosition(position);
                Log.i(TAG, "街道 选择: " + position + " - " + itemAtPosition);
                if (itemAtPosition instanceof JusticeBureau) {
                    viewModel.setJiedao((JusticeBureau) itemAtPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.setJiedao(null);
                Log.i(TAG, "街道 选择: null");
            }
        });

        viewModel.shiListLiveData.observe(this, listResource -> {
            if (listResource.isSuccess()) {
                Log.i(TAG, "市 列表: " + listResource.data);
                SpAdapter adapter = new SpAdapter();
                adapter.submitList(listResource.data);
                binding.spinnerShi.setAdapter(adapter);
                binding.spinnerShi.setSelection(getCheckedPosition(listResource.data,viewModel.shiChecked));

            }
        });
        viewModel.xianListLiveData.observe(this, listResource -> {
            if (listResource.isSuccess()) {
                Log.i(TAG, "县 列表: " + listResource.data);
                SpAdapter adapter = new SpAdapter();
                adapter.submitList(listResource.data);
                binding.spinnerXian.setAdapter(adapter);
                binding.spinnerXian.setSelection(getCheckedPosition(listResource.data,viewModel.xianChecked));
            }
        });

        viewModel.jiedaoListLiveDataWithUncheck.observe(this, listResource -> {
            if (listResource.isSuccess()) {
                Log.i(TAG, "街道 列表: " + listResource.data);
                SpAdapter adapter = new SpAdapter();
                adapter.submitList(listResource.data);
                binding.spinnerJiedao.setAdapter(adapter);
                binding.spinnerJiedao.setSelection(getCheckedPosition(listResource.data,viewModel.jiedaoChecked));
                if (listResource.data == null) {
                    viewModel.setJiedao(null);
                }
            }
        });
        viewModel.setSheng(null);
    }

    private int getCheckedPosition(List<JusticeBureau> justiceBureaus, JusticeBureau total) {
        if (justiceBureaus == null || justiceBureaus.size() == 0 || total == null) {
            return 0;
        }
        for (int i = 0; i < justiceBureaus.size(); i++) {
            if (Objects.equals(justiceBureaus.get(i).getTeamId(), total.getTeamId())) {
                return i;
            }
        }
        return 0;
    }


    @Override
    protected void onPause() {
        super.onPause();
        syncActiveCode();
        syncJustice();
    }

    private void syncJustice() {
        JusticeBureau shi = (JusticeBureau) binding.spinnerShi.getSelectedItem();
        JusticeBureau xian = (JusticeBureau) binding.spinnerXian.getSelectedItem();
        JusticeBureau jeidao = (JusticeBureau) binding.spinnerJiedao.getSelectedItem();
        if (TextUtils.isEmpty(jeidao.getTeamId())){
            jeidao = null;
        }
        viewModel.addBureau(shi, xian, jeidao);
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


    public static class SpAdapter extends BaseAdapter {

        private List<JusticeBureau> data;

        public void submitList(List<JusticeBureau> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
            view.setText(data.get(position).getTeamName());
            return view;
        }
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