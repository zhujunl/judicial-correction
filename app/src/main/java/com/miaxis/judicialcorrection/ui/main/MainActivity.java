package com.miaxis.judicialcorrection.ui.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.miaxis.faceid.FaceManager;
import com.miaxis.finger.FingerManager;
import com.miaxis.finger.FingerStrategy;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.AppToast;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundDiffAdapter;
import com.miaxis.judicialcorrection.databinding.ActivityMainBinding;
import com.miaxis.judicialcorrection.databinding.ItemMainFucBinding;
import com.miaxis.judicialcorrection.dialog.DatePickDialog;
import com.miaxis.judicialcorrection.ui.setting.SettingActivity;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

@AndroidEntryPoint
public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    @Inject
    AppDatabase appDatabase;
    @Inject
    AppExecutors mAppExecutors;

    @Inject
    Lazy<AppHints> appHintsLazy;

    @Inject
    AppToast appToast;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@NonNull ActivityMainBinding view, @Nullable Bundle savedInstanceState) {
        MainAdapter mainAdapter = new MainAdapter(this);
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
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
        };
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
                return;
            }
        }
        init();
    }

    private void init() {
        showLoading(getString(R.string.app_info), getString(R.string.app_init));
        mAppExecutors.networkIO().execute(() -> {
            int init = FaceManager.getInstance().init(MainActivity.this);
            FingerStrategy fingerStrategy = new FingerStrategy(MainActivity.this);
            FingerManager.getInstance().init(fingerStrategy);
            runOnUiThread(() -> {
                dismissLoading();
                if (init != 0) {
                    appHintsLazy.get().showError("初始化失败：" + init);
                }
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PERMISSION_GRANTED) {//选择了“始终允许”
                    appHintsLazy.get().showError("请授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    return;
                }
            }
            init();
        }
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

        Context context;

        protected MainAdapter(Context context) {
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
            this.context = context;
        }

        @Override
        protected ItemMainFucBinding createBinding(ViewGroup parent, int viewType) {
            return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_main_fuc, parent, false);
        }

        @Override
        protected void bind(ItemMainFucBinding binding, MainFunc item) {
            binding.setData(item);
            binding.getRoot().setOnClickListener(v -> {
                //ARouter.getInstance().build(item.targetActivityURI).navigation();
                try {
                    Class<?> aClass = Class.forName(item.targetActivityURI);
                    Intent intent = new Intent(context, aClass);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


}