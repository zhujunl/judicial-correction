package com.miaxis.judicialcorrection.face;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;

import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.CameraPreviewCallback;
import com.miaxis.camera.MXCamera;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.callback.NavigationCallback;
import com.miaxis.judicialcorrection.face.databinding.FragmentCaptureBinding;
import com.miaxis.utils.FileUtils;

import java.io.File;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.FragmentActivity;
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
public class GetFacePageFragment extends BaseBindingFragment<FragmentCaptureBinding> implements CameraPreviewCallback {

    String title = "人像采集";

    PersonInfo personInfo;

    GetFaceViewModel mGetFaceViewModel;

    @Inject
    Lazy<AppHints> appHintsLazy;

    public GetFacePageFragment(@NonNull PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_capture;
    }

    @Override
    protected void initView(@NonNull FragmentCaptureBinding view, @Nullable Bundle savedInstanceState) {
        mGetFaceViewModel = new ViewModelProvider(this).get(GetFaceViewModel.class);
        binding.tvTitle.setText(String.valueOf(title));
        mGetFaceViewModel.name.observe(this, s -> binding.tvName.setText(s));
        mGetFaceViewModel.idCardNumber.observe(this, s -> binding.tvIdCard.setText(s));
        mGetFaceViewModel.faceTips.observe(this, s -> binding.tvFaceTips.setText(s));
        //mGetFaceViewModel.faceFile.observe(this, this::onFaceGot);

        mGetFaceViewModel.name.setValue(personInfo.getXm());
        mGetFaceViewModel.idCardNumber.setValue(personInfo.getIdCardNumber());

        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.svPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                ZZResponse<?> init = CameraHelper.getInstance().init();
                if (ZZResponse.isSuccess(init)) {
                    ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createMXCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    if (ZZResponse.isSuccess(mxCamera)) {
                        mxCamera.getData().setOrientation(90);
                        mxCamera.getData().setPreviewCallback(GetFacePageFragment.this);
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
        mGetFaceViewModel.getFace(frame, camera, width, height, new GetFaceCallback() {
            @Override
            public void onFaceReady(MXCamera camera) {
                File filePath = FileUtils.createFileParent(getContext());
                File file = new File(filePath, personInfo.getId() + ".jpg");
                boolean frameImage = camera.getFrameImage(frame, file.getAbsolutePath());
                if (frameImage) {
                    if (!BuildConfig.DEBUG) {
                        mGetFaceViewModel.uploadPic(personInfo.getId(), file).observe(GetFacePageFragment.this, observer -> {
                            switch (observer.status) {
                                case LOADING:
                                    showLoading();
                                    break;
                                case ERROR:
                                    dismissLoading();
                                    appHintsLazy.get().showError(observer.errorMessage);
                                    break;
                                case SUCCESS:
                                    dismissLoading();
                                    showDialog();
                                    break;
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(() -> showDialog());
                    }
                } else {
                    getActivity().runOnUiThread(() -> appHintsLazy.get().showError("图片保存上传失败"));
                }
            }
        });
    }

    private void showDialog() {
        new DialogResult(getActivity(), new DialogResult.ClickListener() {
            @Override
            public void onBackHome(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
                finish();
            }

            @Override
            public void onTryAgain(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
            }

            @Override
            public void onTimeOut(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
                FragmentActivity activity = getActivity();
                if (activity instanceof NavigationCallback) {
                    NavigationCallback callback = (NavigationCallback) activity;
                    callback.onNavigationCallBack();
                }
            }
        }, new DialogResult.Builder(
                true,
                "采集成功",
                "3s后自动返回信息采集",
                3, false
        ).hideAllHideSucceedInfo(false).hideButton(true)).show();
    }

    public interface GetFaceCallback {

        /**
         * 人脸质量检测通过
         */
        void onFaceReady(MXCamera camera);

    }
}
