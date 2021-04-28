package com.miaxis.judicialcorrection.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.common.response.ZZResponseCode;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, initLayout(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(this);
        boolean initData = initData(view, savedInstanceState);
        if (initData) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.error_title)
                    .setMessage(R.string.error_no_title).
                    setPositiveButton(R.string.dialog_btn_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            setResult(ZZResponse.CreateFail(ZZResponseCode.CODE_ILLEGAL_PARAMETER, getString(R.string.error_no_title)));
                            finish();
                        }
                    }).create().show();
        } else {
            initView(view, savedInstanceState);
        }
    }

    protected abstract int initLayout();

    protected abstract void initView(@NonNull View view, @Nullable Bundle savedInstanceState);

    protected abstract boolean initData(@NonNull View view, @Nullable Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.unbind();
        }
    }

    protected void setResult(Serializable serializable) {
        FragmentActivity activity = getActivity();
        Intent intent = activity.getIntent();
        intent.putExtra("result", serializable);
        activity.setResult(Activity.RESULT_OK, intent);
    }

    protected void finish() {
        getActivity().finish();
    }

}
