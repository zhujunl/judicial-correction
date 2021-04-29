package com.miaxis.judicialcorrection.face;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.CameraPreviewCallback;
import com.miaxis.camera.MXCamera;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.databinding.ActivityVerifyBinding;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * @author Tank
 * @date 2021/4/26 4:40 PM
 * @des
 * @updateAuthor
 * @updateDes
 */

@AndroidEntryPoint
@Route(path = "/activity/verifyPage")
public class VerifyPageActivity extends BaseBindingActivity<ActivityVerifyBinding> implements CameraPreviewCallback {

    @Autowired(name = "Name")
    String title;

    @Autowired(name = "IdCardNumber")
    String idCardNumber;

    VerifyPageModel mVerifyPageModel;

    @Inject
    AppExecutors mAppExecutors;

    @Override
    protected int initLayout() {
        return R.layout.activity_verify;
    }

    @Override
    protected void initView(@NonNull ActivityVerifyBinding view, @Nullable Bundle savedInstanceState) {
        mVerifyPageModel = new ViewModelProvider(this).get(VerifyPageModel.class);
        mVerifyPageModel.name.observe(this, s -> binding.tvName.setText(s));
        mVerifyPageModel.idCardNumber.observe(this, s -> binding.tvIdCard.setText(s));
        mVerifyPageModel.faceTips.observe(this, s -> binding.tvFaceTips.setText(s));
        mVerifyPageModel.fingerBitmap.observe(this, bitmap -> {
            Glide.with(VerifyPageActivity.this).load(bitmap).error(R.mipmap.mipmap_error).into(binding.ivFinger);
            binding.ivFinger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(VerifyPageActivity.this).load(R.mipmap.mipmap_bg_finger).into(binding.ivFinger);
                    mVerifyPageModel.releaseFingerDevice();
                    mVerifyPageModel.initFingerDevice(mAppExecutors);
                    binding.ivFinger.setOnClickListener(null);
                }
            });
        });
        mVerifyPageModel.initFingerDevice(mAppExecutors);

        mVerifyPageModel.name.setValue(title);
        mVerifyPageModel.idCardNumber.setValue(idCardNumber);
        //todo 身份证人脸特征数据
        mVerifyPageModel.idCardFaceFeature.setValue(new byte[1]);

        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.svPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                ZZResponse<?> init = CameraHelper.getInstance().init();
                if (ZZResponse.isSuccess(init)) {
                    ZZResponse<MXCamera> mxCamera2 = CameraHelper.getInstance().createMXCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    if (ZZResponse.isSuccess(mxCamera2)) {
                        mxCamera2.getData().setOrientation(90);
                        mxCamera2.getData().setPreviewCallback(VerifyPageActivity.this);
                        mxCamera2.getData().start(holder);
                    }
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
    public void onPreview(int cameraId, byte[] frame, MXCamera camera, int width, int height) {
        mVerifyPageModel.faceRecognize(cameraId, frame, camera, width, height);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVerifyPageModel.releaseFingerDevice();
    }
}
