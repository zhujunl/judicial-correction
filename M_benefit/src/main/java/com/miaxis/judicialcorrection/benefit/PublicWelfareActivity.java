package com.miaxis.judicialcorrection.benefit;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.enroll.guide.NvController;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.benefit.databinding.ActivityPublicWelfareBinding;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.VerifyPageFragment;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.guide.ToSignUpFragment;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIDCardBindingFragment;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;


/**
 * 公益活动Page
 */
@AndroidEntryPoint
@Route(path = "/benefit/PublicWelfareActivity")
public class PublicWelfareActivity extends BaseBindingActivity<ActivityPublicWelfareBinding> implements ReadIdCardCallback, VerifyCallback {


    String title = "身份核验";
    private WelfareViewModel mWelfareViewModel;

    @Inject
    AppHints appHints;

    @Override
    protected int initLayout() {
        return R.layout.activity_public_welfare;
    }

    @Override
    protected void initView(@NonNull @NotNull ActivityPublicWelfareBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_root, new ReadIDCardBindingFragment(title, true))
                .commitNow();
    }

    @Override
    protected void initData(@NonNull @NotNull ActivityPublicWelfareBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mWelfareViewModel = new ViewModelProvider(this).get(WelfareViewModel.class);
    }


    @Override
    public void onIdCardRead(IdCard result) {
        mWelfareViewModel.idCard = result;
        Timber.e("读取身份证：result:" + result);
//        PersonInfo info=new PersonInfo();
//        info.setXm(result.idCardMsg.name);
//        info.setIdCardNumber(result.idCardMsg.id_num);
//        replaceFragment(new VerifyPageFragment(title,info));
    }

    @Override
    public void onLogin(PersonInfo personInfo) {
        //成功对象不为空
        if (personInfo != null) {
            if (mWelfareViewModel.idCard != null && TextUtils.isEmpty(mWelfareViewModel.idCard.idCardMsg.name)) {
                mWelfareViewModel.idCard.idCardMsg.name = personInfo.getXm();
            }
            mWelfareViewModel.mStrPid.postValue(personInfo.getId());
            replaceFragment(new ToSignUpFragment());
        }
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_root, fragment)
                .commitNow();
    }

    @Override
    public void onVerify(ZZResponse<VerifyInfo> response) {
        if (ZZResponse.isSuccess(response)) {
            mWelfareViewModel.mVerificationSignUp.postValue(true);
        } else {
            showDialog();
        }
    }

    public void showDialog() {
        new DialogResult(this, new DialogResult.ClickListener() {
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
                finish();
            }
        }, new DialogResult.Builder(
                false,
                "验证失败",
                "请点击“重新验证”重新尝试验证，\n如还是失败，请联系现场工作人员。",
                10, false
        )).show();


    }
}
