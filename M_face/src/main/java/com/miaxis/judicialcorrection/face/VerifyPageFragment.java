package com.miaxis.judicialcorrection.face;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.SurfaceHolder;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.xhapimanager.XHApiManager;
import com.bumptech.glide.Glide;
import com.miaxis.camera.CameraConfig;
import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.MXCamera;
import com.miaxis.camera.MXFrame;
import com.miaxis.faceid.FaceConfig;
import com.miaxis.faceid.FaceManager;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.vo.Education;
import com.miaxis.judicialcorrection.base.api.vo.IndividualEducationBean;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.repo.PersonRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.TTsUtils;
import com.miaxis.judicialcorrection.base.utils.numbers.HexStringUtils;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.face.callback.FaceCallback;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.face.databinding.FragmentVerifyBinding;
import com.miaxis.utils.BitmapUtils;
import com.miaxis.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Tank
 * @date 2021/4/26 4:40 PM
 * @des 334
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

    @Inject
    AppExecutors mAppExecutors;
    //用于集中教育
    private Education.ListBean mEducationBean = null;
    //个别教育
    private IndividualEducationBean.ListDTO mIndividual = null;


    public VerifyPageFragment(String title, @NonNull PersonInfo personInfo) {
        this.title = title;
        this.personInfo = personInfo;
    }

    //集中教育
    public VerifyPageFragment(String title, @NonNull PersonInfo personInfo, Education.ListBean bean) {
        this.title = title;
        this.personInfo = personInfo;
        this.mEducationBean = bean;
    }

    //个别教育
    public VerifyPageFragment(String title, @NonNull PersonInfo personInfo, IndividualEducationBean.ListDTO bean) {
        this.title = title;
        this.personInfo = personInfo;
        this.mIndividual = bean;
    }

    private XHApiManager apimanager;
    //人脸或者指纹是否通过
    private boolean isFacePass = false;
    private boolean isFingerPass = false;

    @Override
    protected int initLayout() {
        return R.layout.fragment_verify;
    }

    @Override
    protected void initView(@NonNull FragmentVerifyBinding view, @Nullable Bundle savedInstanceState) {
        mVerifyPageViewModel = new ViewModelProvider(this).get(VerifyPageViewModel.class);
        binding.tvTitle.setText(String.valueOf(title));
        setEducationType();
        if (BuildConfig.EQUIPMENT_TYPE == 3) {
            apimanager = new XHApiManager();
            apimanager.XHSetGpioValue(1, 1);
        }

        mVerifyPageViewModel.faceRect.observe(this, rectF -> binding.frvFace.setRect(rectF, false));
        mVerifyPageViewModel.name.observe(this, s -> {
            if (mEducationBean != null || mIndividual != null) {
                binding.tvNameTitle.setText("");
                binding.tvName.setText("");
            } else {
                binding.tvNameTitle.setText("姓名：");
                binding.tvName.setText(s);
            }
        });
        mVerifyPageViewModel.idCardNumber.observe(this, s -> {
            if (mEducationBean != null || mIndividual != null) {
                binding.tvIdCardTitle.setText("");
                binding.tvIdCard.setText("");
            } else {
                binding.tvIdCardTitle.setText("身份证号：");
                binding.tvIdCard.setText(s);
            }
        });
        mVerifyPageViewModel.faceTips.observe(this, s -> binding.tvFaceTips.setText(s));
        //指纹识别 需要下载图片后进行比对
        fingerInit();

        mVerifyPageViewModel.name.setValue(personInfo.getXm());
        mVerifyPageViewModel.idCardNumber.setValue(personInfo.getIdCardNumber());

        mVerifyPageViewModel.tempFaceFeature.observe(this, bytes -> {
            if (bytes != null) {
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
//                FragmentActivity activity = getActivity();
//                if (activity instanceof VerifyCallback) {
//                    VerifyCallback callback = (VerifyCallback) activity;
////                response.getData().entryMethod="2";
//                    callback.onVerify(ZZResponse.CreateSuccess());
//                    return;
//                }

                //请求成功后拿到图片 解析成bitmap
                ResponseBody responseBody = response.body();
                Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                if (bitmap == null) {
                    appHintsLazy.get().showError("Error:人像解析失败",
                            (dialog, which) -> finish());
                    return;
                }
                try {
                    File filePath = FileUtils.createFileParent(getContext());
                    mAppExecutors.networkIO().execute(() -> {
                        String fileName = System.currentTimeMillis() + ".jpg";
                        File file = new File(filePath, fileName);
                        if (!file.exists()) {
                            File parentFile = file.getParentFile();
                            if (parentFile != null && !parentFile.exists()) {
                                boolean mkdirs = parentFile.mkdirs();
                            }
                        } else {
                            try {
                                boolean newFile = file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        boolean save = BitmapUtils.saveBitmap(bitmap, file.getAbsolutePath());
                        if (!save) {
                            appHintsLazy.get().showError("Error:保存人像失败",
                                    (dialog, which) -> finish());
                            return;
                        }
                        int[] oX = new int[1];
                        int[] oY = new int[1];
                        byte[] rgbFromFile = FaceManager.getInstance().getRgbFromFile(file.getAbsolutePath(), oX, oY);
                        if (rgbFromFile == null) {
                            appHintsLazy.get().showError("Error:人像数据解析失败",
                                    (dialog, which) -> finish());
                            return;
                        }
                        mHandler.post(() -> mVerifyPageViewModel.extractFeatureFromRgb(rgbFromFile, bitmap.getWidth(), bitmap.getHeight()));
                    });
                } catch (Exception e) {
                    e.getStackTrace();
                }
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
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    finish();
                                }));
                    }
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                CameraHelper.getInstance().free();
                try {
                    holder.removeCallback(this);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });
    }

    /**
     * 指纹
     */
    private void fingerInit() {
        TTsUtils.textToSpeechStr("请将人脸置于采集区域");
        mVerifyPageViewModel.getFingerPrint(personInfo.getId()).observe(this, fingerEntityResource -> {
            if (fingerEntityResource.isSuccess()) {
                if (fingerEntityResource.data == null || fingerEntityResource.data.getFingerprints() == null || fingerEntityResource.data.getFingerprints().length == 0) {
                    return;
                }
                mAppExecutors.networkIO().execute(() -> {
                    String finger = fingerEntityResource.data.getFingerprints()[0];
                    byte[] decode = Base64.decode(finger, Base64.NO_WRAP);
                    mHandler.post(() -> {
                        mVerifyPageViewModel.fingerprint1.set(decode);
                        mVerifyPageViewModel.initFingerDevice();
                    });
                });
            }
        });
        mVerifyPageViewModel.resultState.observe(this, aBoolean -> {
            //状态
        });
        //提示
        mVerifyPageViewModel.hint.observe(this, s -> binding.tvHint.setText(s));
        //显示图片
        mVerifyPageViewModel.bitmapFinger.observe(this, bitmap -> {
            if (bitmap!=null) {
                Glide.with(VerifyPageFragment.this).load(bitmap).into(binding.ivFinger);
            }
        });
        //比对结果
        mVerifyPageViewModel.stateLiveData.observe(this, result -> {
            isFingerPass = true;
            if (!isFacePass) {
                verifyComplete(ZZResponse.CreateSuccess());
            }
        });
    }

    /**
     * 设置教育类别显示不同内容
     */
    @SuppressLint("SetTextI18n")
    private void setEducationType() {
        if (mEducationBean != null) {
            binding.inInfo.clContent.setVisibility(View.VISIBLE);
            binding.inInfo.tvName.setText("姓名：" + personInfo.getXm());
            binding.inInfo.tvIdCard.setText("身份证号：" + personInfo.getIdCardNumber());
            String jiaoyuzhutiName = TextUtils.isEmpty(mEducationBean.jiaoyuzhutiName) ? "" : mEducationBean.jiaoyuzhutiName;
            binding.inInfo.tvTheme.setText("教育主题：" + jiaoyuzhutiName);
            String s = HexStringUtils.DateToString(mEducationBean.jyxxkssj);
            binding.inInfo.tvEducationTime.setText("集中教育时间：" + s);
            String jyxxsc = TextUtils.isEmpty(mEducationBean.jyxxsc) ? "" : mEducationBean.jyxxsc;
            binding.inInfo.tvEducationTimeLong.setText("集中教育时长：" + jyxxsc);
        } else if (mIndividual != null) {
            binding.inInfo.clContent.setVisibility(View.VISIBLE);
            binding.inInfo.tvName.setText("姓名：" + personInfo.getXm());
            binding.inInfo.tvIdCard.setText("身份证号：" + personInfo.getIdCardNumber());
            String jyxxfsName = TextUtils.isEmpty(mIndividual.getJyxxfsName()) ? "" : mIndividual.getJyxxfsName();
            binding.inInfo.tvTheme.setText("教育主题：" + jyxxfsName);
            String s = HexStringUtils.DateToString(mIndividual.getJyxxkssj());
            binding.inInfo.tvEducationTime.setText("集中教育时间：" + s);
            String jyxxsc = TextUtils.isEmpty(mIndividual.getJyxxsc()) ? "" : mIndividual.getJyxxsc();
            binding.inInfo.tvEducationTimeLong.setText("集中教育时长：" + jyxxsc);
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
//                response.getData().entryMethod="2";
                callback.onVerify(response);
            }
        }
    }

    private final static Handler mHandler = new Handler();

    @Override
    public void onRgbProcessReady() {
        mHandler.post(() -> {
            ZZResponse<MXCamera> mxCameraNir = CameraHelper.getInstance().createMXCamera(CameraConfig.Camera_NIR);
            if (ZZResponse.isSuccess(mxCameraNir)) {
                //开启NIR近红外视频流
                mxCameraNir.getData().setNextFrameEnable();
            } else {
                appHintsLazy.get().showError("Error:未查询到近红外摄像头",
                        (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        });
            }
        });
    }

    @Override
    public void onLiveReady(MXFrame nirFrame, boolean success) {
        if (success) {
            mVerifyPageViewModel.matchFeature(FaceConfig.threshold, this);
        } else {
            mHandler.post(this::startRgbPreview);
        }
    }

    @Override
    public void onMatchReady(boolean success) {
        mHandler.post(() -> {
            if (success) {
                isFacePass = true;
                if (!isFingerPass) {
                    verifyComplete(ZZResponse.CreateSuccess());
                }
            } else {
                verifyComplete(ZZResponse.CreateFail(-100, "核验失败"));
            }
        });
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
                    (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    });
        }
    }

    @Override
    public void onDestroyView() {
        appHintsLazy.get().close();
        super.onDestroyView();
        mHandler.removeCallbacksAndMessages(null);
        if (BuildConfig.EQUIPMENT_TYPE == 3 && apimanager != null) {
            apimanager.XHSetGpioValue(1, 0);
        }
        TTsUtils.close();
    }
}
