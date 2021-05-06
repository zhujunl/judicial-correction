package com.miaxis.enroll;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.enroll.databinding.ActivityEnrollBinding;
import com.miaxis.enroll.guide.CaptureFuncFragment;
import com.miaxis.enroll.guide.NvController;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIdCardManager;

import javax.inject.Inject;

import dagger.Lazy;
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


    @Inject
    Lazy<AppHints> appHintsLazy;
    private NvController nvController;
    private EnrollSharedViewModel viewModel;

    @Override
    protected int initLayout() {
        return R.layout.activity_enroll;
    }

    @Override
    protected void initView(@NonNull ActivityEnrollBinding view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData(@NonNull ActivityEnrollBinding binding, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(EnrollSharedViewModel.class);
        nvController = new NvController(getSupportFragmentManager(), R.id.container);
        nvController.nvTo(new ReadIDFragment(), false);
        viewModel.errorMsgLiveData.observe(this, s -> appHintsLazy.get().showError("Error:" + s));
        viewModel.idCardLiveData.observe(this, this::onIdCardRead);
        boolean init = ReadIdCardManager.getInstance().init(this);
        if (init) {
            viewModel.readIdCard();
        } else {
            appHintsLazy.get().showError("初始身份证模块失败");
        }
    }

    public NvController getNvController() {
        return nvController;
    }

    public void onIdCardRead(IdCard result) {
        viewModel.login(result.idCardMsg.id_num).observe(this, personInfoResource -> {
            Timber.i("Login %s", personInfoResource);
            switch (personInfoResource.status) {
                case LOADING:
                    showLoading();
                    break;
                case ERROR:
                    dismissLoading();
                    appHintsLazy.get().showError("Error:" + personInfoResource.errorMessage);
                    break;
                case SUCCESS:
                    dismissLoading();
                    if (personInfoResource.data == null) {
                        nvController.nvTo(new CaptureFuncFragment(), false);
                    } else {
                        nvController.nvTo(new GoHomeFragment(), false);
                    }
                    break;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ReadIdCardManager.getInstance().free(this);
    }
}
