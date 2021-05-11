package com.miaxis.judicialcorrection.face;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.CameraPreviewCallback;
import com.miaxis.camera.MXCamera;
import com.miaxis.faceid.FaceManager;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.repo.PersonRepo;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.face.callback.VerifyCallback;
import com.miaxis.judicialcorrection.face.databinding.FragmentVerifyBinding;
import com.miaxis.utils.BitmapUtils;
import com.miaxis.utils.FileUtils;

import java.io.File;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
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
public class VerifyPageFragment extends BaseBindingFragment<FragmentVerifyBinding> implements CameraPreviewCallback {

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
        mVerifyPageViewModel.verifyStatus.observe(this, this::verifyComplete);
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
        mVerifyPageViewModel.name.setValue(personInfo.getXm());
        mVerifyPageViewModel.idCardNumber.setValue(personInfo.getIdCardNumber());
        mVerifyPageViewModel.id.setValue(personInfo.getId());

        mVerifyPageViewModel.tempFaceFeature.observe(this, new Observer<byte[]>() {
            @Override
            public void onChanged(byte[] bytes) {
                if (bytes != null) {
                    //Toast.makeText(getContext(), "提取特征成功", Toast.LENGTH_SHORT).show();
                    ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().find(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    if (ZZResponse.isSuccess(mxCamera)) {
                        mxCamera.getData().setPreviewCallback(VerifyPageFragment.this);
                    }
                } else {
                    appHintsLazy.get().showError(
                            "Error:提取人像特征失败",
                            (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-2, "Error:提取人像特征失败")));
                }
            }
        });

        binding.btnBackToHome.setOnClickListener(v -> finish());
        ZZResponse<?> init = CameraHelper.getInstance().init();
        if (ZZResponse.isSuccess(init)) {
            //todo 身份证人脸特征数据
            if (BuildConfig.DEBUG) {
                File fileParent = FileUtils.createFileParent(getContext());
                if (fileParent == null || !fileParent.exists()) {
                    appHintsLazy.get().showError("Error:获取路径失败",
                            (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-3, "Error:获取路径失败")));
                    return;
                }
                File file = new File(fileParent.getAbsolutePath(), personInfo.getId() + ".jpg");
                if (!file.exists()) {
                    appHintsLazy.get().showError("Error:请先录入人脸信息",
                            (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-4, "Error:请先录入人脸信息")));
                    return;
                }
                //                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                //                if (bitmap == null) {
                //                    appHintsLazy.get().showError("Error:人像解析失败",
                //                            (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-5, "Error:人像解析失败")));
                //                    return;
                //                }
                //                byte[] rgb = BitmapUtils.bitmap2RGB(bitmap);
                int[] oX = new int[1];
                int[] oY = new int[1];
                byte[] rgbFromFile = FaceManager.getInstance().getRgbFromFile(file.getAbsolutePath(), oX, oY);
                if (rgbFromFile == null) {
                    appHintsLazy.get().showError("Error:人像解析失败",
                            (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-5, "Error:人像解析失败")));
                    return;
                }
                mVerifyPageViewModel.extractFeature(rgbFromFile, oX[0], oY[0]);
            } else {
                showLoading(title, "正在请求，请稍后");
                mPersonRepo.getFace(personInfo.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dismissLoading();
                        //请求成功后拿到图片 解析成bitmap
                        ResponseBody responseBody = response.body();
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                        if (bitmap == null) {
                            appHintsLazy.get().showError("Error:人像解析失败",
                                    (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-6, "Error:人像解析失败")));
                            return;
                        }
                        //File filePath = FileUtils.createFilePath(getContext());
                        //if (filePath == null || !filePath.exists()) {
                        //    appHintsLazy.get().showError("Error:获取路径失败",
                        //            (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-7, "Error:获取路径失败")));
                        //    return;
                        // }
                        // TODO: 2021/5/11 测试保存文件
                        //boolean saveBitmap = BitmapUtils.saveBitmap(bitmap, filePath.getAbsolutePath() + File.pathSeparator + personInfo.getId() + ".jpg");
                        //if (!saveBitmap) {
                        //    appHintsLazy.get().showError("Error:人像保存失败");
                        //    return;
                        //}
                        byte[] rgb = BitmapUtils.bitmap2RGB(bitmap);
                        mVerifyPageViewModel.extractFeature(rgb, bitmap.getWidth(), bitmap.getHeight());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        dismissLoading();
                        appHintsLazy.get().showError("Error:" + t,
                                (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-8, "Error:" + t)));
                    }
                });
            }

            ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createMXCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
            if (ZZResponse.isSuccess(mxCamera)) {
                binding.svPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(@NonNull SurfaceHolder holder) {
                        ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().find(Camera.CameraInfo.CAMERA_FACING_FRONT);
                        if (ZZResponse.isSuccess(mxCamera)) {
                            mxCamera.getData().setOrientation(90);
                            mxCamera.getData().start(holder);
                            return;
                        }
                        appHintsLazy.get().showError("摄像头初始化失败",
                                (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-9, "Error:摄像头初始化失败")));
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
        appHintsLazy.get().showError("摄像头初始化失败",
                (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-10, "Error:摄像头初始化失败")));

    }

    //    @Override
    //    public void onInit(boolean result) {
    //        if (!result) {
    //            appHintsLazy.get().showError("指纹设备初始化失败",
    //                    (dialog, which) -> verifyComplete(ZZResponse.CreateFail(-11, "Error:指纹设备初始化失败")));
    //        }
    //    }

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
        //byte[] rgb = mVerifyPageViewModel.nv21ToRgb(frame, width, height);
        mVerifyPageViewModel.faceRecognize(frame, camera, width, height);
        //mVerifyPageViewModel.verifyStatus.postValue(ZZResponse.CreateSuccess(new VerifyInfo(personInfo.getId(),personInfo.getXm(), personInfo.getIdCardNumber())));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mVerifyPageViewModel.releaseFingerDevice();
    }

    private int errorTime=0;
    public void verifyComplete(ZZResponse<VerifyInfo> response) {
        if (!ZZResponse.isSuccess(response)){
            errorTime++;
            if (errorTime>=5){
                ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().find(Camera.CameraInfo.CAMERA_FACING_FRONT);
                if (ZZResponse.isSuccess(mxCamera)) {
                    mxCamera.getData().setPreviewCallback(VerifyPageFragment.this);
                }
                errorTime=0;
                return;
            }
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
                    ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().find(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    if (ZZResponse.isSuccess(mxCamera)) {
                        mxCamera.getData().setPreviewCallback(VerifyPageFragment.this);
                    }
                }

                @Override
                public void onTimeOut(AppCompatDialog appCompatDialog) {
                    appCompatDialog.dismiss();
                    finish();
                }
            }, builder).show();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity instanceof VerifyCallback) {
            VerifyCallback callback = (VerifyCallback) activity;
            callback.onVerify(response);
        }
    }


}
