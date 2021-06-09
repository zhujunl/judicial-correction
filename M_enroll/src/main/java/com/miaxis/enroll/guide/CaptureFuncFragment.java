package com.miaxis.enroll.guide;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.miaxis.enroll.EnrollActivity;
import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentCaptureFuncBinding;
import com.miaxis.enroll.guide.voice.VoicePrintCollectFragment;
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

    private Bitmap idCardFace;
    private boolean haveIdInfo;

    public CaptureFuncFragment(boolean haveIdInfo, Bitmap idCardFace) {
        this.haveIdInfo = haveIdInfo;
        this.idCardFace = idCardFace;
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
            PersonInfo personInfo = viewModel.personInfoLiveData.getValue();
            if (viewModel.personInfoLiveData.getValue() == null) {
                appHints.toast("请先采集基本信息");
                return;
            }
            GetFacePageFragment capturePageFragment = new GetFacePageFragment(personInfo,idCardFace);
            navigation(capturePageFragment);
        });
        binding.groupFinger.setOnClickListener(v -> {
            appHints.toast("暂未开放");
//            PersonInfo personInfo = viewModel.personInfoLiveData.getValue();
//            IdCard idcard = viewModel.idCardLiveData.getValue();
//            FingerprintCollectFragment fragment = new FingerprintCollectFragment(personInfo.getId(),idcard);
//            navigation(fragment);

        });
        binding.groupSound.setOnClickListener(v -> {
            appHints.toast("暂未开放");
//            PersonInfo personInfo = viewModel.personInfoLiveData.getValue();
//            VoicePrintCollectFragment fragment = new VoicePrintCollectFragment(personInfo);
//            navigation(fragment);

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
