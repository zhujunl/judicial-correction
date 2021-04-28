package com.miaxis.judicialcorrection.face;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.MXCamera;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.databinding.ActivityVerifyBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author Tank
 * @date 2021/4/26 4:40 PM
 * @des
 * @updateAuthor
 * @updateDes
 */

@Route(path = "/activity/verifyPage")
public class VerifyPageActivity extends BaseBindingActivity<ActivityVerifyBinding> {

    @Autowired(name = "Name")
    String title;

    @Autowired(name = "IdCardNumber")
    String idCardNumber;

    @Override
    protected int initLayout() {
        return R.layout.activity_verify;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        VerifyPageModel verifyPageModel = new ViewModelProvider(this).get(VerifyPageModel.class);
        verifyPageModel.name.observe(this, s -> binding.tvName.setText(s));
        verifyPageModel.idCardNumber.observe(this, s -> binding.tvIdCard.setText(s));
        verifyPageModel.name.setValue(title);
        verifyPageModel.idCardNumber.setValue(idCardNumber);

        binding.btnBackToHome.setOnClickListener(v -> finish());

        binding.svPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                ZZResponse<?> init = CameraHelper.getInstance().init();
                ZZResponse<MXCamera> mxCamera2 = CameraHelper.getInstance().createMXCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                if (ZZResponse.isSuccess(mxCamera2)) {
                    mxCamera2.getData().setOrientation(90);
                    mxCamera2.getData().start(holder);
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                CameraHelper.getInstance().free();
            }

        });

    }

    @Override
    protected boolean initData(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //return TextUtils.isEmpty(title) || TextUtils.isEmpty(idCardNumber);
        return false;
    }

}
