package com.miaxis.judicialcorrection.face;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;

import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.CameraPreviewCallback;
import com.miaxis.camera.MXCamera;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.callback.CaptureCallback;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.face.databinding.FragmentCaptureBinding;
import com.miaxis.judicialcorrection.widget.countdown.CountDownListener;

import java.io.File;
import java.io.FileOutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author Tank
 * @date 2021/4/26 4:40 PM
 * @des
 * @updateAuthor
 * @updateDes
 */

public class CapturePageFragment extends BaseBindingFragment<FragmentCaptureBinding> implements CameraPreviewCallback, CaptureCallback {

    String title = "人像采集";

    PersonInfo personInfo;

    CapturePageViewModel mCapturePageViewModel;

    public CapturePageFragment(@NonNull PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_capture;
    }

    @Override
    protected void initView(@NonNull FragmentCaptureBinding view, @Nullable Bundle savedInstanceState) {
        mCapturePageViewModel = new ViewModelProvider(this).get(CapturePageViewModel.class);
        binding.tvTitle.setText(String.valueOf(title));
        mCapturePageViewModel.name.observe(this, s -> binding.tvName.setText(s));
        mCapturePageViewModel.idCardNumber.observe(this, s -> binding.tvIdCard.setText(s));
        mCapturePageViewModel.faceTips.observe(this, s -> binding.tvFaceTips.setText(s));
        mCapturePageViewModel.verifyStatus.observe(this, response -> {
            FragmentActivity activity = getActivity();
            if (activity instanceof VerifyCallback) {
                VerifyCallback callback = (VerifyCallback) activity;
                callback.onVerify(response);
            }
        });
        mCapturePageViewModel.fingerBitmap.observe(this, bitmap -> {

        });

        mCapturePageViewModel.name.setValue(personInfo.getXm());
        mCapturePageViewModel.idCardNumber.setValue(personInfo.getIdCardNumber());

        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.svPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                ZZResponse<?> init = CameraHelper.getInstance().init();
                if (ZZResponse.isSuccess(init)) {
                    ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createMXCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    if (ZZResponse.isSuccess(mxCamera)) {
                        mxCamera.getData().setOrientation(90);
                        mxCamera.getData().setPreviewCallback(CapturePageFragment.this);
                        mxCamera.getData().start(holder);
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
    public void onPause() {
        super.onPause();
        CameraHelper.getInstance().pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        CameraHelper.getInstance().resume();
    }

    @Override
    protected void initData(@NonNull FragmentCaptureBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onPreview(int cameraId, byte[] frame, MXCamera camera, int width, int height) {
        mCapturePageViewModel.getFace(cameraId, frame, camera, width, height, this);
    }

    @Override
    public void onFaceReady(MXCamera mxCamera) {
        binding.tvTips.setTime(3);
        binding.tvTips.setCountDownListener(new CountDownListener() {
            @Override
            public void onCountDownStart() {

            }

            @Override
            public void onCountDownProgress(int progress) {

            }

            @Override
            public void onCountDownDone() {
                mxCamera.takePicture(new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        File file = new File("路径");
                        try {
                            if (!file.exists()) {
                                File parentFile = file.getParentFile();
                                if (parentFile != null) {
                                    boolean mkdirs = parentFile.mkdirs();
                                }
                            }
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(data);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
