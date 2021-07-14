package com.miaxis.judicialcorrection.face;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.xhapimanager.XHApiManager;
import com.miaxis.camera.CameraConfig;
import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.MXCamera;
import com.miaxis.camera.MXFrame;
import com.miaxis.faceid.FaceConfig;
import com.miaxis.faceid.FaceManager;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.dialog.PreviewPictureDialog;
import com.miaxis.judicialcorrection.face.callback.FaceCallback;
import com.miaxis.judicialcorrection.face.callback.NavigationCallback;
import com.miaxis.judicialcorrection.face.databinding.FragmentCaptureBinding;
import com.miaxis.utils.BitmapUtils;
import com.miaxis.utils.FileUtils;

import java.io.File;
import java.lang.reflect.Field;

import javax.inject.Inject;

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
public class GetFacePageFragment extends BaseBindingFragment<FragmentCaptureBinding> implements FaceCallback {

    String title = "人像采集";

    PersonInfo personInfo;

    GetFaceViewModel mGetFaceViewModel;

    @Inject
    Lazy<AppHints> appHintsLazy;

    private final Bitmap idCardFace;


    private PreviewPictureDialog dialog;

    public GetFacePageFragment(@NonNull PersonInfo personInfo, Bitmap bitmap) {
        this.personInfo = personInfo;
        this.idCardFace = bitmap;
    }

    XHApiManager apimanager;

    private File filePath;

    @Override
    protected int initLayout() {
        return R.layout.fragment_capture;
    }

    @Override
    protected void initView(@NonNull FragmentCaptureBinding view, @Nullable Bundle savedInstanceState) {
        filePath = FileUtils.createFileParent(getContext());

        mGetFaceViewModel = new ViewModelProvider(this).get(GetFaceViewModel.class);
        if (BuildConfig.EQUIPMENT_TYPE == 3) {
            apimanager = new XHApiManager();
            apimanager.XHSetGpioValue(1, 1);
        }

        binding.tvTitle.setText(String.valueOf(title));
        mGetFaceViewModel.faceRect.observe(this, rectF -> binding.frvFace.setRect(rectF, false));
        mGetFaceViewModel.name.observe(this, s -> binding.tvName.setText(s));
        mGetFaceViewModel.idCardNumber.observe(this, s -> binding.tvIdCard.setText(s));
        mGetFaceViewModel.faceTips.observe(this, s -> binding.tvFaceTips.setText(s));
        mGetFaceViewModel.tempFaceFeature.observe(this, feature -> {
            if (feature == null) {
                appHintsLazy.get().showError("特征提取失败，请退出后重试", (dialog, which) -> finish());
                return;
            }
            startRgbPreview();
        });

        mGetFaceViewModel.id.setValue(personInfo.getId());
        mGetFaceViewModel.name.setValue(personInfo.getXm());
        mGetFaceViewModel.idCardNumber.setValue(personInfo.getIdCardNumber());

        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.svPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                ZZResponse<?> init = CameraHelper.getInstance().init();
                if (ZZResponse.isSuccess(init)) {
                    ZZResponse<MXCamera> mxCameraRgb = CameraHelper.getInstance().createMXCamera(CameraConfig.Camera_RGB);
                    ZZResponse<MXCamera> mxCameraNir = CameraHelper.getInstance().createMXCamera(CameraConfig.Camera_NIR);
                    if (ZZResponse.isSuccess(mxCameraRgb) && ZZResponse.isSuccess(mxCameraNir)) {
                        mxCameraRgb.getData().setOrientation(CameraConfig.Camera_RGB.previewOrientation);
                        mxCameraRgb.getData().setPreviewCallback(frame -> {
                            //可见光人脸检测
                            mGetFaceViewModel.processRgbFrame(frame, GetFacePageFragment.this);
                        });
                        mxCameraRgb.getData().start(holder);

                        mxCameraNir.getData().setOrientation(CameraConfig.Camera_NIR.previewOrientation);
                        mxCameraNir.getData().setPreviewCallback(frame -> {
                            //活体检测
                            mGetFaceViewModel.detectLive(frame, GetFacePageFragment.this);
                        });
                        mxCameraNir.getData().start(null);
                    } else {
                        try {
                            mHandler.post(() -> appHintsLazy.get().showError("Error:打开双目摄像头失败，请联系工作人员", (dialog, which) -> finish()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

        if (idCardFace == null) {
            appHintsLazy.get().showError("Error:人像解析失败,请退出后重新尝试", (dialog, which) -> finish());
            return;
        }
        String path = FileUtils.createFileParent(getContext()) + "/" + this.personInfo.getIdCardNumber() + ".bmp";
        File file = new File(path);
        if (!file.exists()) {
            boolean save = BitmapUtils.saveBitmap(idCardFace, path);
        }
        idCardFace.recycle();
        int[] oX = new int[1];
        int[] oY = new int[1];
        byte[] rgbFromFile = FaceManager.getInstance().getRgbFromFile(path, oX, oY);
        if (rgbFromFile == null) {
            appHintsLazy.get().showError("Error:人像数据解析失败",
                    (dialog, which) -> finish());
            return;
        }
        mGetFaceViewModel.extractFeatureFromRgb(rgbFromFile, oX[0], oY[0]);
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
    public void onRgbProcessReady() {
        mHandler.post(() -> {
            ZZResponse<MXCamera> mxCameraNir = CameraHelper.getInstance().createMXCamera(CameraConfig.Camera_NIR);
            if (ZZResponse.isSuccess(mxCameraNir)) {
                //开启NIR近红外视频流
                mxCameraNir.getData().setNextFrameEnable();
            } else {
                appHintsLazy.get().showError("Error:未查询到近红外摄像头",
                        (dialog, which) -> finish());
            }
        });
    }

    @Override
    public void onLiveReady(MXFrame nirFrame, boolean success) {
        if (success) {
            mGetFaceViewModel.matchFeature(FaceConfig.thresholdIdCard, this);
        } else {
            mHandler.post(this::startRgbPreview);
        }
    }

    @Override
    public void onMatchReady(boolean success) {
        if (success) {
            ZZResponse<MXCamera> mxCameraRgb = CameraHelper.getInstance().find(CameraConfig.Camera_RGB);
            if (ZZResponse.isSuccess(mxCameraRgb)) {
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(filePath, fileName);
                boolean frameImage = mxCameraRgb.getData().saveFrameImage(file.getAbsolutePath());
                if (!frameImage) {
                    mxCameraRgb.getData().saveFrameImage(file.getAbsolutePath());
                }
                String base64Path = "";
                if (frameImage) {
                    base64Path = FileUtils.imageToBase64(file.getAbsolutePath());
                }
                String finalBase64Path = base64Path;
                mHandler.post(() -> {
                    if (frameImage) {
                        setPreviewDialog(file, finalBase64Path, mxCameraRgb);
                    } else {
                        mxCameraRgb.getData().setNextFrameEnable();
                    }
                });
            } else {
                mHandler.post(() -> appHintsLazy.get().showError("Error:未查询到近红外摄像头",
                        (dialog, which) -> finish()));
            }
        } else {
            mHandler.post(() -> {
                new DialogResult(getActivity(), new DialogResult.ClickListener() {
                    @Override
                    public void onBackHome(AppCompatDialog appCompatDialog) {
                        appCompatDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onTryAgain(AppCompatDialog appCompatDialog) {
                        appCompatDialog.dismiss();
                        startRgbPreview();
                    }

                    @Override
                    public void onTimeOut(AppCompatDialog appCompatDialog) {
                        appCompatDialog.dismiss();
                        finish();
                    }
                }, new DialogResult.Builder(
                        false,
                        "验证失败",
                        "请点击“重新验证”重新尝试验证，\n" +
                                "如还是失败，请联系现场工作人员。",
                        10, true
                ).hideAllHideSucceedInfo(false).hideButton(false)).show();
            });
        }
    }

    private void setPreviewDialog(File file, String base64, ZZResponse<MXCamera> cameraZZResponse) {
        dialog = new PreviewPictureDialog(getContext(), new PreviewPictureDialog.ClickListener() {
            @Override
            public void onDetermine() {
                mGetFaceViewModel.uploadPic(personInfo.getId(), base64).observe(GetFacePageFragment.this, observer -> {
                    switch (observer.status) {
                        case LOADING:
                            showLoading();
                            break;
                        case ERROR:
                            dismissLoading();
                            appHintsLazy.get().showError(observer.errorMessage,
                                    (dialog, which) -> finish());
                            break;
                        case SUCCESS:
                            dismissLoading();
                            showDialog();
                            break;
                    }
                });
            }

            @Override
            public void onTryAgain(AppCompatDialog appCompatDialog) {
                cameraZZResponse.getData().setNextFrameEnable();
            }

            @Override
            public void onTimeOut(AppCompatDialog appCompatDialog) {

            }
        }, new PreviewPictureDialog.Builder().setPathFile(file.getAbsolutePath()));
        dialog.show();

    }

    @Override
    public void onError(ZZResponse<?> response) {
        //活体检测出现错误
        mHandler.post(() ->
                appHintsLazy.get().showError("Error:" + response.getCode() + "  " + response.getMsg(),
                        (dialog, which) -> {
                            startRgbPreview();
                        }));
    }

    private void startRgbPreview() {
        ZZResponse<MXCamera> mxCameraRgb = CameraHelper.getInstance().find(CameraConfig.Camera_RGB);
        if (ZZResponse.isSuccess(mxCameraRgb)) {
            mxCameraRgb.getData().setNextFrameEnable();
        } else {
            appHintsLazy.get().showError("查询摄像头失败，请退出后重试", (dialog1, which1) -> finish());
        }
    }

    private final static Handler mHandler = new Handler();

    private void showDialog() {
        mHandler.post(() -> new DialogResult(getActivity(), new DialogResult.ClickListener() {
            @Override
            public void onBackHome(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
                finish();
            }

            @Override
            public void onTryAgain(AppCompatDialog appCompatDialog) {
                appCompatDialog.dismiss();
                startRgbPreview();
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
        ).hideAllHideSucceedInfo(false).hideButton(true)).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacksAndMessages(null);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (BuildConfig.EQUIPMENT_TYPE == 3 && apimanager != null) {
            apimanager.XHSetGpioValue(1, 0);
        }
    }
}
