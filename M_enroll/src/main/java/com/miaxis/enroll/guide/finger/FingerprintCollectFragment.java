package com.miaxis.enroll.guide.finger;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentFingerprintCollectBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.callback.NavigationCallback;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;


/**
 * 指纹采集
 */
@AndroidEntryPoint
public class FingerprintCollectFragment extends BaseBindingFragment<FragmentFingerprintCollectBinding> {


    private  IdCard mIdCard;
    private  String mPid;

    public  FingerprintCollectFragment(String id, IdCard idCard) {
        this.mPid=id;
        this.mIdCard=idCard;
    }

    private FingerprintCollectModel mFingerprintCollectModel;
    @Inject
    Lazy<AppHints> appHintsLazy;


    @Override
    protected int initLayout() {
        return R.layout.fragment_fingerprint_collect;
    }

    @Override
    protected void initView(@NonNull @NotNull FragmentFingerprintCollectBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding.btnBackToHome.setOnClickListener(v -> finish());
    }

    @Override
    protected void initData(@NonNull @NotNull FragmentFingerprintCollectBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mFingerprintCollectModel = new ViewModelProvider(this).get(FingerprintCollectModel.class);
        mFingerprintCollectModel.filePath = FileUtils.createFileParent(getContext());
        if (mIdCard!=null){
            mFingerprintCollectModel.fingerprint1.set(mIdCard.fp0);
        }
        mFingerprintCollectModel.initFingerDevice(result -> {
        });
        //上传对象
        mFingerprintCollectModel.fingerprintLiveData.observe(this, entity -> {
            entity.id = mPid;
            mFingerprintCollectModel.uploadFingerprint(entity).observe(FingerprintCollectFragment.this, observer -> {
                switch (observer.status) {
                    case LOADING:
                        showLoading();
                        break;
                    case ERROR:
                        dismissLoading();
                        appHintsLazy.get().showError(observer.errorMessage);
                        break;
                    case SUCCESS:
                        dismissLoading();
                        showDialog();
                        break;
                }
            });
        });
    }

    private void showDialog() {
        new DialogResult(getActivity(), new DialogResult.ClickListener() {
            @Override
            public void onBackHome(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
                finish();
            }

            @Override
            public void onTryAgain(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
            }

            @Override
            public void onTimeOut(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
                FragmentActivity activity = getActivity();
                if (activity instanceof NavigationCallback) {
                    NavigationCallback callback = (NavigationCallback) activity;
                    callback.onNavigationCallBack();
                }
            }
        }, new DialogResult.Builder(
                true,
                "采集成功",
                "3s后自动返回信息采集",
                3, false
        ).hideAllHideSucceedInfo(false).hideButton(true)).show();
    }

    @Override
    public void onDestroyView() {
        mFingerprintCollectModel.releaseFingerDevice();
        super.onDestroyView();

    }
}