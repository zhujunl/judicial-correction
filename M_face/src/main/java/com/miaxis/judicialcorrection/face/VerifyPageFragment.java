package com.miaxis.judicialcorrection.face;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.miaxis.camera.CameraConfig;
import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.MXCamera;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.repo.PersonRepo;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.face.callback.FaceCallback;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.face.databinding.FragmentVerifyBinding;
import com.miaxis.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Tank
 * @date 2021/4/26 4:40 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
@AndroidEntryPoint
public class VerifyPageFragment extends BaseBindingFragment<FragmentVerifyBinding> implements FaceCallback {

    String title;

    PersonInfo personInfo;

    VerifyPageViewModel mVerifyPageViewModel;

    @Inject
    Lazy<AppHints> appHintsLazy;

    @Inject
    PersonRepo mPersonRepo;

    private Handler mHandler = new Handler();

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
        //mVerifyPageViewModel.fingerBitmap.observe(this, bitmap -> {
        //            Glide.with(VerifyPageFragment.this).load(bitmap).error(R.mipmap.mipmap_error).into(binding.ivFinger);
        //            binding.ivFinger.setOnClickListener(v -> {
        //                Glide.with(VerifyPageFragment.this).load(R.mipmap.mipmap_bg_finger).into(binding.ivFinger);
        //                mVerifyPageViewModel.releaseFingerDevice();
        //                mVerifyPageViewModel.initFingerDevice(VerifyPageFragment.this);
        //                binding.ivFinger.setOnClickListener(null);
        //            });
        //        });
        //mVerifyPageViewModel.initFingerDevice(this);
        mVerifyPageViewModel.id.setValue(personInfo.getId());
        mVerifyPageViewModel.name.setValue(personInfo.getXm());
        mVerifyPageViewModel.idCardNumber.setValue(personInfo.getIdCardNumber());

        mVerifyPageViewModel.tempFaceFeature.observe(this, bytes -> {
            if (bytes != null) {
                //Toast.makeText(getContext(), "提取特征成功", Toast.LENGTH_SHORT).show();
                startRgbPreview();
            } else {
                appHintsLazy.get().showError(
                        "Error:提取人像特征失败",
                        (dialog, which) -> finish());
            }
        });

        binding.btnBackToHome.setOnClickListener(v -> finish());
        showLoading(title, "正在请求，请稍后");
        mPersonRepo.getFace(personInfo.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                dismissLoading();
                //请求成功后拿到图片 解析成bitmap
                ResponseBody responseBody = response.body();
                Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                if (bitmap == null) {
                    appHintsLazy.get().showError("Error:人像解析失败",
                            (dialog, which) -> finish());
                    return;
                }
                byte[] rgb = BitmapUtils.bitmap2RGB(bitmap);
                mVerifyPageViewModel.extractFeatureFromRgb(rgb, bitmap.getWidth(), bitmap.getHeight());
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                dismissLoading();
                appHintsLazy.get().showError("Error:" + t,
                        (dialog, which) -> finish());
            }
        });
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
                            mVerifyPageViewModel.processRgbFrame(frame, VerifyPageFragment.this);
                        });
                        mxCameraRgb.getData().start(holder);

                        mxCameraNir.getData().setOrientation(CameraConfig.Camera_NIR.previewOrientation);
                        mxCameraNir.getData().setPreviewCallback(frame -> {
                            //活体检测
                            mVerifyPageViewModel.detectLive(frame, VerifyPageFragment.this);
                        });
                        mxCameraNir.getData().start(null);
                    } else {
                        mHandler.post(() -> appHintsLazy.get().showError("Error:打开双目摄像头失败，请联系工作人员",
                                (dialog, which) -> finish()));
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

    public void verifyComplete(ZZResponse<VerifyInfo> response) {
        if (!ZZResponse.isSuccess(response)) {
            DialogResult.Builder builder = new DialogResult.Builder();
            builder.success = false;
            builder.countDownTime = 10;
            builder.title = "验证失败";
            builder.message = "请联系现场工作人员处理\n" +
                    "（工作人员需确认前期登记的\n" +
                    "身份证号是否准确）！";
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
            }, builder).show();
        } else {
            FragmentActivity activity = getActivity();
            if (activity instanceof VerifyCallback) {
                VerifyCallback callback = (VerifyCallback) activity;
                callback.onVerify(response);
            }
        }
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
    public void onLiveReady(boolean success) {
        if (success) {
            mVerifyPageViewModel.matchFeature(this);
        } else {
            startRgbPreview();
        }
    }

    @Override
    public void onMatchReady(boolean success) {
        if (success) {
            verifyComplete(ZZResponse.CreateSuccess());
        } else {
            verifyComplete(ZZResponse.CreateFail(-100, "核验失败"));
        }
    }

    @Override
    public void onError(ZZResponse<?> response) {
        //活体检测出现错误
        mHandler.post(() -> appHintsLazy.get().showError("Error:" + response.getCode() + "  " + response.getMsg(),
                (dialog, which) -> {
                    startRgbPreview();
                }));
    }

    private void startRgbPreview() {
        ZZResponse<MXCamera> mxCameraRgb = CameraHelper.getInstance().find(CameraConfig.Camera_RGB);
        if (ZZResponse.isSuccess(mxCameraRgb)) {
            mxCameraRgb.getData().setNextFrameEnable();
        } else {
            appHintsLazy.get().showError("查询摄像头失败，请退出后重试",
                    (dialog, which) -> finish());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mVerifyPageViewModel.releaseFingerDevice();
        mHandler.removeCallbacksAndMessages(null);
    }
}
