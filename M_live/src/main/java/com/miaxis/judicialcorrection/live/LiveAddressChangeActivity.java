package com.miaxis.judicialcorrection.live;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.guide.LiveAddressFragment;
import com.miaxis.judicialcorrection.guide.LiveListFragment;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIDCardBindingFragment;
import com.miaxis.judicialcorrection.live.databinding.ActivityLiveAddressChangeBinding;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
@Route(path = "/live/LiveAddressChangeActivity")
public class LiveAddressChangeActivity extends BaseBindingActivity<ActivityLiveAddressChangeBinding> implements ReadIdCardCallback, VerifyCallback {

    String title = "居住地变更申请";

    private LiveAddressChangeViewModel model;

    @Override
    protected int initLayout() {
        return R.layout.activity_live_address_change;
    }

    @Override
    protected void initView(@NonNull @NotNull ActivityLiveAddressChangeBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        replaceFragment(new ReadIDCardBindingFragment(title, true));
    }

    @Override
    protected void initData(@NonNull @NotNull ActivityLiveAddressChangeBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(this).get(LiveAddressChangeViewModel.class);
    }

    @Override
    public void onVerify(ZZResponse<VerifyInfo> response) {
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
                if (ZZResponse.isSuccess(response)) {
                    appCompatDialog.dismiss();
                    replaceFragment(new LiveAddressFragment());
                } else {
                    finish();
                }
            }
        }, new DialogResult.Builder(
                ZZResponse.isSuccess(response),
                ZZResponse.isSuccess(response) ?  "校验成功" : "验证失败",
                ZZResponse.isSuccess(response) ? "系统将自动返回" + title + "身份证刷取页面" : "请点击“重新验证”重新尝试验证，\n如还是失败，请联系现场工作人员。",
                ZZResponse.isSuccess(response) ? 3 : 10, true
        )).show();
    }

    @Override
    public void onIdCardRead(IdCard result) {
        Timber.e("读取身份证：result:" + result);
        PersonInfo info = new PersonInfo();
        info.setIdCardNumber(result.idCardMsg.id_num);
        info.setXm(result.idCardMsg.name);
    }

    @Override
    public void onLogin(PersonInfo personInfo) {
        if (personInfo != null) {
            model.personInfoMutableLiveData.postValue(personInfo);
            replaceFragment(new LiveListFragment());
        }
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_root, fragment)
                .commitNow();
    }

}
