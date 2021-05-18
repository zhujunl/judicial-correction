package com.miaxis.judicialcorrection.face;

import android.os.SystemClock;
import android.util.Size;

import com.miaxis.camera.MXCamera;
import com.miaxis.faceid.FaceManager;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;

import org.zz.api.MXFaceInfoEx;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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
public class GetFaceViewModel extends ViewModel {

    MutableLiveData<String> name = new MutableLiveData<>();

    MutableLiveData<String> idCardNumber = new MutableLiveData<>();

    MutableLiveData<String> faceTips = new MutableLiveData<>();

    MutableLiveData<byte[]> idCardFaceFeature = new MutableLiveData<>();

    public MXFaceInfoEx[] mFaceInfoExes = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
    public int[] mFaceNumber = new int[1];

    AppExecutors mAppExecutors;

    CapturePageRepo mCapturePageRepo;

    @Inject
    public GetFaceViewModel(CapturePageRepo capturePageRepo, AppExecutors appExecutors) {
        this.mAppExecutors = appExecutors;
        this.mCapturePageRepo = capturePageRepo;
        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
            mFaceInfoExes[i] = new MXFaceInfoEx();
        }
    }

    public void getFace(byte[] frame, MXCamera camera, int widthO, int heightO, GetFacePageFragment.GetFaceCallback captureCallback) {
        mAppExecutors.networkIO().execute(() -> {
            byte[] buffer = FaceManager.getInstance().yuv2Rgb(frame, widthO, heightO);
            byte[] rgb = new byte[buffer.length];
            Size rotate = FaceManager.getInstance().rotate(buffer, widthO, heightO, 90, rgb);
            if (rotate != null) {
                int width = rotate.getWidth();
                int height = rotate.getHeight();
                int detectFace = FaceManager.getInstance().detectFace(rgb, width, height, mFaceNumber, mFaceInfoExes);
                Timber.e("face   detectFace:%s", detectFace);
                if (detectFace == 0) {
                    int faceNumber = FaceManager.getInstance().getFaceNumber(mFaceNumber);
                    Timber.e("face   faceNumber:%s", faceNumber);
                    if (faceNumber == 1) {
                        if (mFaceInfoExes[0].width >= FaceConfig.minFaceWidth) {
                            int faceQuality = FaceManager.getInstance().getFaceQuality(rgb, width, height, 1, mFaceInfoExes);
                            Timber.e("face   faceQuality:%s", faceQuality);
                            if (faceQuality == 0) {
                                Timber.e("face   quality:%s", mFaceInfoExes[0].quality);
                                if (mFaceInfoExes[0].quality >= FaceConfig.minFaceQuality) {
                                    faceTips.postValue("正在提取特征");
                                    byte[] feature = new byte[FaceManager.getInstance().getFeatureSize()];
                                    int extractFeature = FaceManager.getInstance().extractFeature(rgb, width, height, 1, feature, mFaceInfoExes, false);
                                    if (extractFeature == 0) {
                                        faceTips.postValue("正在进行比对");
                                        Timber.e("face   extractFeature:%s", extractFeature);
                                        float[] floats = new float[1];
                                        int matchFeature = FaceManager.getInstance().matchFeature(feature, idCardFaceFeature.getValue(), floats, false);
                                        Timber.e("face   match:%s", matchFeature);
                                        if (matchFeature == 0) {
                                            Timber.e("face   floats:%s", floats[0]);
                                            if (floats[0] >= FaceConfig.threshold) {
                                                faceTips.postValue("人证核验通过");
                                                captureCallback.onFaceReady(camera);
                                                return;
                                            } else {
                                                faceTips.postValue("人证核验未通过");
                                            }
                                            return;
                                        } else {
                                            faceTips.postValue("比对失败");
                                        }
                                    } else {
                                        faceTips.postValue("特征提取失败");
                                    }
                                } else {
                                    faceTips.postValue("人脸质量过低，当前：" + mFaceInfoExes[0].quality + ",低于" + FaceConfig.minFaceQuality);
                                }
                            }
                        } else {
                            faceTips.postValue("人脸过小");
                        }
                    } else if (faceNumber <= 0) {
                        faceTips.postValue("未检测到人脸");
                    }
                } else {
                    faceTips.postValue("检测人脸失败");
                }
            }
            SystemClock.sleep(100);
            if (camera != null) {
                camera.getNextFrame();
            }
        });
    }


    public LiveData<Resource<Object>> uploadPic(String pid, String base64Str) {
        return mCapturePageRepo.uploadFace(pid, base64Str);
    }


    public void extractFeature(byte[] rgb, int width, int height) {
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int detectFace = FaceManager.getInstance().detectFace(rgb, width, height, mFaceNumber, mFaceInfoExes);
                    if (detectFace == 0) {
                        byte[] feature = new byte[FaceManager.getInstance().getFeatureSize()];
                        int extractFeature = FaceManager.getInstance().extractFeature(rgb, width, height, 1, feature, mFaceInfoExes, false);
                        if (extractFeature == 0) {
                            idCardFaceFeature.postValue(feature);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                idCardFaceFeature.postValue(null);
            }
        });
    }
}
