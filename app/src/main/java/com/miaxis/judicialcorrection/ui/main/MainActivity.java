package com.miaxis.judicialcorrection.ui.main;

import android.app.Dialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.MainFunc;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundDiffAdapter;
import com.miaxis.judicialcorrection.databinding.ActivityMainBinding;
import com.miaxis.judicialcorrection.databinding.ItemMainFucBinding;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@Route(path = "/main/MainActivity")
@AndroidEntryPoint
public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    @Inject
    AppDatabase appDatabase;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
                ARouter.getInstance().build("/setting/SettingActivity").navigation();
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