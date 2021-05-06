package com.miaxis.enroll.guide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.miaxis.enroll.EnrollActivity;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentCaptureFuncBinding;
import com.miaxis.enroll.guide.CaptureBaseInfoFragment;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.widget.countdown.DefaultCountDownListener;

/**
 * InfoFuncFragment
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
public class CaptureFuncFragment extends BaseBindingFragment<FragmentCaptureFuncBinding> {
    @Override
    protected int initLayout() {
        return R.layout.fragment_capture_func;
    }

    @Override
    protected void initView(@NonNull FragmentCaptureFuncBinding binding, @Nullable Bundle savedInstanceState) {
        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.tvTimeDown.setCountDownListener(new DefaultCountDownListener() {
            @Override
            public void onCountDownDone() {
                finish();
            }
        });
        binding.groupBase.setOnClickListener(v -> {
            navigation(new CaptureBaseInfoFragment());
        });
        binding.groupFace.setOnClickListener(v -> {

        });
        binding.groupFinger.setOnClickListener(v -> {

        });
        binding.groupSound.setOnClickListener(v -> {

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
