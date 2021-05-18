package com.miaxis.judicialcorrection.face;

import com.miaxis.camera.CameraConfig;
import com.miaxis.camera.MXFrame;
import com.miaxis.faceid.FaceConfig;
import com.miaxis.faceid.FaceManager;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.callback.FaceCallback;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

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

    MutableLiveData<String> idCardNumber = new MutableLiveData<>();

    MutableLiveData<String> faceTips = new MutableLiveData<>();

    MutableLiveData<byte[]> tempFaceFeature = new MutableLiveData<>();

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
                //获取RGB可见光摄像头
                if (!MXFrame.isBufferEmpty(rgbFrame)) {
                    int liveDetect = FaceManager.getInstance().liveDetect(rgbFrame.buffer, rgbFrame.width, rgbFrame.height, nir.buffer);
                    if (liveDetect == 10000) {//活体
                        faceTips.postValue("活体检测完成");
                        byte[] feature = new byte[FaceManager.getInstance().getFeatureSize()];
                        int extractFeatureRgb = FaceManager.getInstance().extractFeatureRgb(rgbFrame.buffer, rgbFrame.width, rgbFrame.height, false, feature);
                        if (extractFeatureRgb == 0) {
                            rgbFrameFaceFeature = feature;
                            captureCallback.onLiveReady(true);
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
                        captureCallback.onLiveReady(false);
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

    public void matchFeature(FaceCallback captureCallback) {
        mAppExecutors.networkIO().execute(() -> {
            byte[] idCardFeature = tempFaceFeature.getValue();
            if (idCardFeature == null || idCardFeature.length == 0) {
                captureCallback.onError(ZZResponse.CreateFail(-90, "身份证数据未处理"));
                return;
            }
            byte[] rgbFeature = rgbFrameFaceFeature;
            if (rgbFeature == null || rgbFeature.length == 0) {
                captureCallback.onError(ZZResponse.CreateFail(-91, "人像特征未提取"));
                return;
            }
            faceTips.postValue("正在比对");
            float[] floats = new float[1];
            int matchFeature = FaceManager.getInstance().matchFeature(idCardFeature, rgbFeature, floats, false);
            if (matchFeature == 0) {
                boolean success = floats[0] > FaceConfig.thresholdIdCard;
                faceTips.postValue(success ? "人证核验通过" : "人证核验未通过");
                captureCallback.onMatchReady(success);
            } else {
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
