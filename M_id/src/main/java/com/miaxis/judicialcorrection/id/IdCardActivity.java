package com.miaxis.judicialcorrection.id;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.id.databinding.ActivityIdCardBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Tank
 * @date 2021/4/25 6:43 PM
 * @des
 * @updateAuthor
 * @updateDes
 */

@Route(path = "/activity/readIDCard")
public class IdCardActivity extends BaseBindingActivity<ActivityIdCardBinding> {

    @Autowired(name = "Title")
    String title;

    @Autowired(name = "NoIdCardEnable")
    boolean noIdCardEnable;

    @Autowired(name = "AutoCheckEnable")
    boolean autoCheckEnable;

    @Override
    protected int initLayout() {
        return R.layout.activity_id_card;
    }

    @Override
    protected void initView(@NonNull ActivityIdCardBinding view, @Nullable Bundle savedInstanceState) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.layout_root,
                        (Fragment) ARouter.getInstance()
                                .build("/page/readIDCard")
                                .withString("Title", title)
                                .withBoolean("NoIdCardEnable", noIdCardEnable)
                                .withBoolean("AutoCheckEnable", autoCheckEnable)
                                .navigation()
                )
                .commitNow();
    }

    @Override
    protected void initData(@NonNull ActivityIdCardBinding binding, @Nullable Bundle savedInstanceState) {

    }

}
