package com.miaxis.judicialcorrection.ui.cloud;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.api.TokenResult;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.api.vo.bean.TokenBean;
import com.miaxis.judicialcorrection.base.api.vo.bean.TokenRep;
import com.miaxis.judicialcorrection.databinding.ActivityCloudBinding;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIDCardBindingFragment;
import com.tencent.mmkv.MMKV;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class CloudActivity extends BaseBindingActivity<ActivityCloudBinding>  implements ReadIdCardCallback{

    String title = "云端查询";

    private CloudModel viewModel;

    @Inject
    TokenRep tokenRep;


    @Override
    protected int initLayout() {
        return R.layout.activity_cloud;
    }

    @Override
    protected void initView(@NonNull ActivityCloudBinding binding, @Nullable Bundle savedInstanceState) {
//        viewModel = new ViewModelProvider(this).get(CloudModel.class);
        readIdCard();
    }

    @Override
    protected void initData(@NonNull ActivityCloudBinding binding, @Nullable Bundle savedInstanceState) {
    }

    private void readIdCard() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_root, new ReadIDCardBindingFragment(title, true))
                .commit();
    }

    @Override
    public void onIdCardRead(IdCard result) {
        Timber.e("读取身份证：result:" + result);
    }

    String id = "";

    @Override
    public void onLogin(PersonInfo personInfo) {
        if (personInfo != null) {
            LiveData<TokenResult<TokenBean>> getreport =tokenRep.getToken();
            getreport.observe(this, tokenBeanResource -> {
                MMKV.defaultMMKV().encode("cloudtoken","Bearer "+tokenBeanResource.getAccess_token());
                id = personInfo.getId();
                MMKV.defaultMMKV().encode("pid",id);
                replaceFragment(new CloudFragment(personInfo));
            });
        }
    }


    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(com.miaxis.judicialcorrection.benefit.R.id.layout_root, fragment)
                .commit();
    }
}