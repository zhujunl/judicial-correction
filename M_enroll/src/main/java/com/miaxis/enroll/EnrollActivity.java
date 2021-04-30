package com.miaxis.enroll;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.enroll.databinding.ActivityEnrollBinding;
import com.miaxis.enroll.guide.GuideNavigationController;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIDCardBindingFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * EnrollActivity
 *
 * @author zhangyw
 * Created on 4/28/21.
 */
@AndroidEntryPoint
@Route(path = "/enroll/EnrollActivity")
public class EnrollActivity extends BaseBindingActivity<ActivityEnrollBinding> {


    private GuideNavigationController nvController;
    private EnrollViewModel viewModel;

    @Override
    protected int initLayout() {
        return R.layout.activity_enroll;
    }

    @Override
    protected void initView(@NonNull ActivityEnrollBinding view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initData(@NonNull ActivityEnrollBinding binding, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(EnrollViewModel.class);
        nvController = new GuideNavigationController(getSupportFragmentManager(), R.id.container);
        nvController.nvTo(new ReadIDFragment(), false);
        new Handler().postDelayed(() -> onCardRead("412822199109222410"), 1500);
    }


    public GuideNavigationController getNvController() {
        return nvController;
    }

    public void onCardRead(String cardNumber) {
        viewModel.login(cardNumber).observe(this, personInfoResource -> {
            Timber.i("Login %s", personInfoResource);
            switch (personInfoResource.status) {
                case LOADING:
                    showLoading();
                    break;
                case ERROR:
                    dismissLoading();
                    showErrorDialog("Error:" + personInfoResource.errorMessage);
                    break;
                case SUCCESS:
                    dismissLoading();
                    if (personInfoResource.data == null) {
                        nvController.nvTo(new InfoFuncFragment(), false);
                    } else {
                        nvController.nvTo(new GoHomeFragment(), false);
                    }
                    break;
            }
        });
    }


    void showErrorDialog(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("错误")
                .setMessage(msg)
                .setPositiveButton("好的", (dialog, which) -> {

                })
                .setCancelable(false)
                .show();
    }

}
