package com.miaxis.judicialcorrection.ui.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.miaxis.faceid.FaceManager;
import com.miaxis.finger.FingerManager;
import com.miaxis.finger.FingerStrategy;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundDiffAdapter;
import com.miaxis.judicialcorrection.databinding.ActivityMainBinding;
import com.miaxis.judicialcorrection.databinding.ItemMainFucBinding;
import com.miaxis.judicialcorrection.ui.setting.SettingActivity;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    @Inject
    AppDatabase appDatabase;
    @Inject
    AppExecutors mAppExecutors;

    ProgressDialog progressDialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@NonNull ActivityMainBinding view, @Nullable Bundle savedInstanceState) {
        MainAdapter mainAdapter = new MainAdapter();
        binding.recyclerView.setAdapter(mainAdapter);
        appDatabase.mainFuncDAO().loadFuncActive().observe(this, mainAdapter::submitList);
        binding.title.setOnClickListener(v -> {
            long lt = v.getTag(v.getId()) == null ? 0L : (long) v.getTag(v.getId());
            long ct = System.currentTimeMillis();
            if (ct - lt < 500) {
                new PasswordDialog()
                        .show(getSupportFragmentManager(), "pwd");
                v.setTag(v.getId(), 0);
            }
            v.setTag(v.getId(), ct);
        });
    }

    @Override
    protected void initData(@NonNull ActivityMainBinding binding, @Nullable Bundle savedInstanceState) {
        init();
    }

    private void init() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setTitle(getString(R.string.app_info));
        progressDialog.setMessage(getString(R.string.app_init));
        mAppExecutors.networkIO().execute(() -> {
            runOnUiThread(() -> {
                if (progressDialog != null) {
                    progressDialog.show();
                }
            });
            int init = FaceManager.getInstance().init(MainActivity.this);
            FingerStrategy fingerStrategy = new FingerStrategy(MainActivity.this);
            FingerManager.getInstance().init(fingerStrategy);
            runOnUiThread(() -> {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (init != 0) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.app_info)
                            .setMessage("初始化失败：" + String.valueOf(init))
                            .show();
                }
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FaceManager.getInstance().free();
        FingerManager.getInstance().release();
    }

    public static class PasswordDialog extends AppCompatDialogFragment {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_pwd, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            EditText editText = view.findViewById(R.id.etPwd);
            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
                    checkPassword(v.getText().toString().trim());
                    return true;
                }
                return false;
            });
            view.findViewById(R.id.btnSubmit).setOnClickListener(v -> checkPassword(editText.getText().toString().trim()));
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(true);
        }

        void checkPassword(String pwd) {
            dismiss();
            if ("666666".equals(pwd)) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            } else {
                Toast.makeText(getContext(), "密码输入错误!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    static class MainAdapter extends BaseDataBoundDiffAdapter<MainFunc, ItemMainFucBinding> {

        protected MainAdapter() {
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
        protected ItemMainFucBinding createBinding(ViewGroup parent, int viewType) {
            return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_main_fuc, parent, false);
        }

        @Override
        protected void bind(ItemMainFucBinding binding, MainFunc item) {
            binding.setData(item);
            binding.getRoot().setOnClickListener(v ->
                    ARouter.getInstance().build(item.targetActivityURI).navigation()
            );
        }
    }


}