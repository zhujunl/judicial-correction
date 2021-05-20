package com.miaxis.judicialcorrection.face;

import android.graphics.RectF;
import android.os.Environment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.camera.CameraConfig;
import com.miaxis.camera.MXFrame;
import com.miaxis.faceid.FaceManager;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.callback.FaceCallback;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

/**
 * @author Tank
 * @date 2021/4/25 2:55 PM
 * @des
 * @updateAuthor
 * @updateDes
 */

@HiltViewModel
public class BaseFaceViewModel extends ViewModel {

    MutableLiveData<String> id = new MutableLiveData<>();

    MutableLiveData<String> name = new MutableLiveData<>();

    MutableLiveData<RectF> faceRect = new MutableLiveData<>();

    MutableLiveData<String> idCardNumber = new MutableLiveData<>();

    MutableLiveData<String> faceTips = new MutableLiveData<>();

    MutableLiveData<byte[]> tempFaceFeature = new MutableLiveData<>();

    private int count=0;

    byte[] rgbFrameFaceFeature;

    MXFrame rgbFrame;

    AppExecutors mAppExecutors;

    @Inject
    public BaseFaceViewModel(AppExecutors appExecutors) {
        this.mAppExecutors = appExecutors;
    }

    public void processRgbFrame(MXFrame frame, FaceCallback captureCallback) {
        mAppExecutors.networkIO().execute(() -> {
            rgbFrame = MXFrame.processFrame(frame, CameraConfig.Camera_RGB.bufferOrientation);
            if (MXFrame.isBufferEmpty(rgbFrame)) {
                captureCallback.onError(ZZResponse.CreateFail(-80, "RGB帧数据处理失败"));
            } else {
                captureCallback.onRgbProcessReady();
            }
        });
    }

    /**
     * @param nirFrame 摄像头视频帧数据
     */
    public void detectLive(MXFrame nirFrame, FaceCallback captureCallback) {
        rgbFrameFaceFeature = null;
        mAppExecutors.networkIO().execute(() -> {
            MXFrame nir = MXFrame.processFrame(nirFrame, CameraConfig.Camera_NIR.bufferOrientation);
            if (nir != null) {

                //测试 保存黑白
//                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/A/" + System.currentTimeMillis() + ".jpg";
//                FaceManager.getInstance().saveRgbTiFile(nir.buffer, nir.width, nir.height, path);
                /*==============================================*/
                //获取RGB可见光摄像头
                if (!MXFrame.isBufferEmpty(rgbFrame)) {
                    int liveDetect = FaceManager.getInstance().liveDetect(rgbFrame.buffer, rgbFrame.width, rgbFrame.height, nir.buffer);

                    try {
                        RectF rgbFaceRect = FaceManager.getInstance().getRgbFaceRect();
                        faceRect.postValue(rgbFaceRect);
                    } catch (Exception e) {
                        e.printStackTrace();
                        faceRect.postValue(null);
                    }
                    if (liveDetect == 10000) {//活体
                        faceTips.postValue("活体检测完成");
                        byte[] feature = new byte[FaceManager.getInstance().getFeatureSize()];
                        int extractFeatureRgb = FaceManager.getInstance().extractFeatureRgb(rgbFrame.buffer, rgbFrame.width, rgbFrame.height, false, feature);
                        if (extractFeatureRgb == 0) {
                            //test
//                            String p = Environment.getExternalStorageDirectory().getAbsolutePath() + "/D/" + System.currentTimeMillis() + ".bmp";
//                            FaceManager.getInstance().saveRgbTiFile(rgbFrame.buffer, rgbFrame.width, rgbFrame.height, p);
//                         /*=========================================================*/
                            rgbFrameFaceFeature = feature;
                            captureCallback.onLiveReady(nirFrame,true);
                        } else {
                            faceTips.postValue("提取特征失败");
                            captureCallback.onError(ZZResponse.CreateFail(-83, "提取特征失败"));
                        }
                    } else if (liveDetect == 100001) {//非活体
                        faceTips.postValue("非活体");
                        captureCallback.onError(ZZResponse.CreateFail(liveDetect, "非活体"));
                    } else if (liveDetect < 0) {
                        faceTips.postValue("活体检测异常");
                        captureCallback.onError(ZZResponse.CreateFail(liveDetect, "活体检测异常"));
                    } else {
                        String liveError = FaceManager.getInstance().getLiveError(liveDetect);
                        faceTips.postValue(liveError);
                        captureCallback.onLiveReady(nirFrame,false);
                    }
                } else {
                    faceTips.postValue("RGB摄像头数据为空");
                    captureCallback.onError(ZZResponse.CreateFail(-82, "RGB摄像头数据为空"));
                }
            } else {
                captureCallback.onError(ZZResponse.CreateFail(-81, "NIR摄像头数据处理异常"));
            }
        });
    }

    public void matchFeature(MXFrame nirFrame,float threshold,FaceCallback captureCallback) {
        mAppExecutors.networkIO().execute(() -> {
            byte[] idCardFeature = tempFaceFeature.getValue();
            if (idCardFeature == null || idCardFeature.length == 0) {
                Timber.e("TAG人像  %s  ","4");
                captureCallback.onError(ZZResponse.CreateFail(-90, "身份证数据未处理"));
                return;
            }
            byte[] rgbFeature = rgbFrameFaceFeature;
            if (rgbFeature == null || rgbFeature.length == 0) {
                Timber.e("TAG人像  %s  ","5");
                captureCallback.onError(ZZResponse.CreateFail(-91, "人像特征未提取"));
                return;
            }
            Timber.e("TAG人像  %s  ","3");
            faceTips.postValue("正在比对");
            float[] floats = new float[1];
            int matchFeature = FaceManager.getInstance().matchFeature(idCardFeature, rgbFeature, floats, false);
            if (matchFeature == 0) {
                boolean success = floats[0] >threshold;
                faceTips.postValue(success ? "人证核验通过" : "人证核验未通过,分值："+floats[0]);
                Timber.e("TAG人像 2  %s  ", floats[0]);
                if (success){
                    count=0;
                    captureCallback.onMatchReady(true);
                    return;
                }
                if (!MXFrame.isNullCamera(nirFrame)){
                    count=count+1;
                    if (count<10){
                        nirFrame.camera.setNextFrameEnable();
                    }else{
                        count=0;
                        captureCallback.onMatchReady(false);
                    }
                }else{
                    count=0;
                    captureCallback.onMatchReady(false);
                }
            } else {
                Timber.e("TAG人像  %s  ","1");
                faceTips.postValue("人证核验失败");
                captureCallback.onError(ZZResponse.CreateFail(-92, "人证核验失败"));
            }
        });
    }

    public void extractFeatureFromRgb(byte[] rgb, int width, int height) {
        mAppExecutors.networkIO().execute(() -> {
            try {
                int detectFace = FaceManager.getInstance().detectFaceRGB(rgb, width, height);
                if (detectFace == 0) {
                    byte[] feature = new byte[FaceManager.getInstance().getFeatureSize()];
                    int extractFeature = FaceManager.getInstance().extractFeatureRgb(rgb, width, height, false, feature);
                    if (extractFeature == 0) {
                        tempFaceFeature.postValue(feature);
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tempFaceFeature.postValue(null);
        });
    }

}
