package com.miaxis.judicialcorrection.id.readIdCard;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import com.miaxis.judicialcorrection.id.R;
import com.miaxis.judicialcorrection.id.databinding.FragmentReadIdCardBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * @author Tank
 * @date 2021/4/25 10:06 AM
 * @des 度身份证界面
 * @updateAuthor tangkai
 * @updateDes
 */
@AndroidEntryPoint
@Route(path = "/page/readIDCard")
public class ReadIDCardBindingFragment extends BaseBindingFragment<FragmentReadIdCardBinding> {

    @Autowired(name = "Title")
    String title;

    @Autowired(name = "NoIdCardEnable")
    boolean noIdCardEnable;

    ReadIdCardViewModel mReadIdCardViewModel;

    public ReadIDCardBindingFragment(String title, boolean noIdCardEnable) {
        this.title = title;
        this.noIdCardEnable = noIdCardEnable;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_read_id_card;
    }

    @Override
    protected void initView(@NonNull FragmentReadIdCardBinding view, Bundle savedInstanceState) {
        mReadIdCardViewModel = new ViewModelProvider(this).get(ReadIdCardViewModel.class);
        mReadIdCardViewModel.title.observe(this, s -> binding.tvTitle.setText(String.valueOf(s)));
        mReadIdCardViewModel.noIdCardEnable.observe(this, aBoolean -> {
            binding.btnNoIdCardEntry.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            binding.btnNoIdCardEntry.setOnClickListener(v ->
                    getFragmentManager().beginTransaction().replace(
                            R.id.layout_root,
                            (Fragment) ARouter.getInstance()
                                    .build("/page/inputIDCard")
                                    .withString("Title", title)
                                    .navigation()
                    ).commitNow());
        });

        binding.btnBackToHome.setOnClickListener(v -> {
            finish();
        });
        mReadIdCardViewModel.title.setValue(title);
        mReadIdCardViewModel.noIdCardEnable.setValue(noIdCardEnable);

        boolean init = ReadIdCardManager.getInstance().init(getActivity());
        if (init) {
            FragmentActivity activity = getActivity();
            if (activity instanceof ReadIdCardCallback) {
                mReadIdCardViewModel.readIdCard((ReadIdCardCallback) activity);
            }
        } else {

        }

    }

    @Override
    protected void initData(@NonNull FragmentReadIdCardBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mReadIdCardViewModel.stopRead();
        ReadIdCardManager.getInstance().free(getActivity());
    }
}
