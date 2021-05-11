package com.miaxis.enroll.guide;

import android.os.Bundle;

import com.miaxis.enroll.EnrollActivity;
import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentCaptureFuncBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.face.GetFacePageFragment;
import com.miaxis.judicialcorrection.widget.countdown.DefaultCountDownListener;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * InfoFuncFragment
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
@AndroidEntryPoint
public class CaptureFuncFragment extends BaseBindingFragment<FragmentCaptureFuncBinding> {

    private boolean haveFaceImage;
    private boolean haveIdInfo;

    public CaptureFuncFragment(boolean haveIdInfo, boolean haveFaceImage) {
        this.haveIdInfo = haveIdInfo;
        this.haveFaceImage = haveFaceImage;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_capture_func;
    }

    @Inject
    AppHints appHints;

    @Override
    protected void initView(@NonNull FragmentCaptureFuncBinding binding, @Nullable Bundle savedInstanceState) {
        EnrollSharedViewModel viewModel = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);
        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.tvTimeDown.setCountDownListener(new DefaultCountDownListener() {
            @Override
            public void onCountDownDone() {
                finish();
            }
        });
        binding.groupBase.setOnClickListener(v -> {
            if (haveIdInfo) {
                appHints.toast("您已经采集过基本信息");
            } else {
                navigation(new CaptureBaseInfoFragment());
            }
        });

        binding.groupFace.setOnClickListener(v -> {
            if (haveFaceImage) {
                appHints.toast("您已经采集过人脸");
                return;
            }
            PersonInfo personInfo = viewModel.personInfoLiveData.getValue();
            if (viewModel.personInfoLiveData.getValue() == null) {
                appHints.toast("请先采集基本信息");
                return;
            }
            GetFacePageFragment capturePageFragment = new GetFacePageFragment(personInfo);
            navigation(capturePageFragment);
        });
        binding.groupFinger.setOnClickListener(v -> {
            appHints.toast("暂未开放");
        });
        binding.groupSound.setOnClickListener(v -> {
            appHints.toast("暂未开放");
        });
    }

    protected void navigation(Fragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity instanceof EnrollActivity) {
            ((EnrollActivity) activity).getNvController().nvTo(fragment, true);
        }
    }

    @Override
    protected void initData(@NonNull FragmentCaptureFuncBinding binding, @Nullable Bundle savedInstanceState) {

    }

}
