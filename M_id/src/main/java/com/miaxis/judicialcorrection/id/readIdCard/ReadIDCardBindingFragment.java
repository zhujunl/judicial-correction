package com.miaxis.judicialcorrection.id.readIdCard;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.id.R;
import com.miaxis.judicialcorrection.id.databinding.FragmentReadIdCardBinding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author Tank
 * @date 2021/4/25 10:06 AM
 * @des 度身份证界面
 * @updateAuthor tangkai
 * @updateDes
 */

@Route(path = "/page/readIDCard")
public class ReadIDCardBindingFragment extends BaseBindingFragment<FragmentReadIdCardBinding> {

    @Autowired(name = "Title")
    String title;

    @Autowired(name = "NoIdCardEnable")
    boolean noIdCardEnable;

    @Autowired(name = "AutoCheckEnable")
    boolean autoCheckEnable;

    @Override
    protected int initLayout() {
        return R.layout.fragment_read_id_card;
    }

    @Override
    protected void initView(@NonNull FragmentReadIdCardBinding view, Bundle savedInstanceState) {
        ReadIDCardModel readIDCardModel = new ViewModelProvider(this).get(ReadIDCardModel.class);
        readIDCardModel.title.observe(this, s -> binding.tvTitle.setText(String.valueOf(s)));
        readIDCardModel.noIdCardEnable.observe(this, aBoolean -> {
            binding.btnNoIdCardEntry.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            binding.btnNoIdCardEntry.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(
                    R.id.layout_root,
                    (Fragment) ARouter.getInstance()
                            .build("/page/inputIDCard")
                            .withString("Title", title)
                            .withBoolean("AutoCheckEnable", autoCheckEnable)
                            .navigation()
            ).commitNow());
        });
        readIDCardModel.autoCheckEnable.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });
        binding.btnBackToHome.setOnClickListener(v -> {
            finish();
            //                    ReadIdCardManager.getInstance().read(new ReadIdCardManager.ReadIdCardCallback() {
            //                        @Override
            //                        public void readIdCardCallback(int code, String message, IdCardMsg idCardMsg, Bitmap bitmap) {
            //                            Timber.e("code:" + code + "   message:" + message + "  IdCardMsg:" + idCardMsg + "   Bitmap:" + (bitmap != null));
            //                        }
            //                    });
        });
        readIDCardModel.title.setValue(title);
        readIDCardModel.noIdCardEnable.setValue(noIdCardEnable);
        readIDCardModel.autoCheckEnable.setValue(autoCheckEnable);
        boolean init = ReadIdCardManager.getInstance().init(getActivity());
        Toast.makeText(getContext(), "init:" + init, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ReadIdCardManager.getInstance().free(getActivity());
    }
}
