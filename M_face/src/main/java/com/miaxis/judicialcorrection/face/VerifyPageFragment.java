package com.miaxis.judicialcorrection.face;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;

import com.bumptech.glide.Glide;
import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.CameraPreviewCallback;
import com.miaxis.camera.MXCamera;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.repo.PersonRepo;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.face.databinding.FragmentVerifyBinding;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * @author Tank
 * @date 2021/4/26 4:40 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
@AndroidEntryPoint
public class VerifyPageFragment extends BaseBindingFragment<FragmentVerifyBinding> implements CameraPreviewCallback, VerifyPageViewModel.OnFingerInitListener {

    String title;

    PersonInfo personInfo;

    VerifyPageViewModel mVerifyPageViewModel;

    @Inject
    Lazy<AppHints> appHintsLazy;

    @Inject
    PersonRepo mPersonRepo;

    public VerifyPageFragment(String title, @NonNull PersonInfo personInfo) {
        this.title = title;
        this.personInfo = personInfo;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_verify;
    }

    @Override
    protected void initView(@NonNull FragmentVerifyBinding view, @Nullable Bundle savedInstanceState) {
        mVerifyPageViewModel = new ViewModelProvider(this).get(VerifyPageViewModel.class);
        binding.tvTitle.setText(String.valueOf(title));
        mVerifyPageViewModel.name.observe(this, s -> binding.tvName.setText(s));
        mVerifyPageViewModel.idCardNumber.observe(this, s -> binding.tvIdCard.setText(s));
        mVerifyPageViewModel.faceTips.observe(this, s -> binding.tvFaceTips.setText(s));
        mVerifyPageViewModel.verifyStatus.observe(this, response -> {
            FragmentActivity activity = getActivity();
            if (activity instanceof VerifyCallback) {
                VerifyCallback callback = (VerifyCallback) activity;
                callback.onVerify(response);
            }
        });
        mVerifyPageViewModel.fingerBitmap.observe(this, bitmap -> {
            Glide.with(VerifyPageFragment.this).load(bitmap).error(R.mipmap.mipmap_error).into(binding.ivFinger);
            binding.ivFinger.setOnClickListener(v -> {
                Glide.with(VerifyPageFragment.this).load(R.mipmap.mipmap_bg_finger).into(binding.ivFinger);
                mVerifyPageViewModel.releaseFingerDevice();
                mVerifyPageViewModel.initFingerDevice(VerifyPageFragment.this);
                binding.ivFinger.setOnClickListener(null);
            });
        });
        mVerifyPageViewModel.initFingerDevice(this);

        mVerifyPageViewModel.name.setValue(personInfo.getXm());
        mVerifyPageViewModel.idCardNumber.setValue(personInfo.getIdCardNumber());
        //todo 身份证人脸特征数据
        LiveData<Resource<Object>> mPersonRepoFace = mPersonRepo.getFace(personInfo.getId());
        mPersonRepoFace.observe(this, new Observer<Resource<Object>>() {
            @Override
            public void onChanged(Resource<Object> objectResource) {

            }
        });

        mVerifyPageViewModel.idCardFaceFeature.setValue(new byte[1]);

        binding.btnBackToHome.setOnClickListener(v -> finish());

        ZZResponse<?> init = CameraHelper.getInstance().init();
        if (ZZResponse.isSuccess(init)) {
            ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createMXCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
            if (ZZResponse.isSuccess(mxCamera)) {
                binding.svPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(@NonNull SurfaceHolder holder) {
                        ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().find(Camera.CameraInfo.CAMERA_FACING_FRONT);
                        if (ZZResponse.isSuccess(mxCamera)) {
                            mxCamera.getData().setOrientation(90);
                            mxCamera.getData().setPreviewCallback(VerifyPageFragment.this);
                            mxCamera.getData().start(holder);
                            return;
                        }
                        appHintsLazy.get().showError("摄像头初始化失败");
                    }

                    @Override
                    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

                    }

                    @Override
                    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                        CameraHelper.getInstance().free();
                    }
                });
                return;
            }
        }
        appHintsLazy.get().showError("摄像头初始化失败");
    }

    @Override
    public void onInit(boolean result) {
        if (!result) {
            appHintsLazy.get().showError("指纹设备初始化失败");
        }
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
    protected void initData(@NonNull FragmentVerifyBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onPreview(int cameraId, byte[] frame, MXCamera camera, int width, int height) {
        mVerifyPageViewModel.faceRecognize(cameraId, frame, camera, width, height);
        //        mVerifyPageViewModel.verifyStatus.postValue(ZZResponse.CreateSuccess(new VerifyInfo(personInfo.getId(),personInfo.getXm(), personInfo.getIdCardNumber())));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVerifyPageViewModel.releaseFingerDevice();
    }
}
