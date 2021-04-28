package com.miaxis.judicialcorrection.face;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;

import com.miaxis.camera.MXCamera;
import com.miaxis.faceid.FaceManager;
import com.miaxis.finger.FingerManager;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;

import org.zz.api.MXFaceInfoEx;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

/**
 * @author Tank
 * @date 2021/4/25 2:55 PM
 * @des
 * @updateAuthor
 * @updateDes
 */

public class VerifyPageModel extends ViewModel {

    MutableLiveData<String> name = new MutableLiveData<>();

    MutableLiveData<String> idCardNumber = new MutableLiveData<>();

    MutableLiveData<String> faceTips = new MutableLiveData<>();

    MutableLiveData<Bitmap> fingerBitmap = new MutableLiveData<>();

    MutableLiveData<byte[]> idCardFaceFeature = new MutableLiveData<>();


    public MXFaceInfoEx[] mFaceInfoExes = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
    public int[] mFaceNumber = new int[1];
    public int[] mFacesData = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];

    AppExecutors mAppExecutors;

    public void initFingerDevice(AppExecutors executorService) {
        mAppExecutors = executorService;
        mAppExecutors.networkIO().execute(() -> {
            FingerManager.getInstance().initDevice(fingerStatusListener);
        });
        FaceManager.getInstance().initData(mFaceInfoExes);
    }

    public void releaseFingerDevice() {
        FingerManager.getInstance().setFingerListener(null);
    }

    public void faceRecognize(int cameraId, byte[] frame, MXCamera camera, int width, int height) {
        mAppExecutors.networkIO().execute(() -> {
            byte[] rgb = FaceManager.getInstance().yuv2Rgb(frame, width, height);
            int detectFace = FaceManager.getInstance().detectFace(rgb, width, height, mFaceNumber, mFacesData, mFaceInfoExes);
            if (detectFace == 0) {
                int faceNumber = FaceManager.getInstance().getFaceNumber(mFaceNumber);
                if (faceNumber == 1) {
                    if ((mFaceInfoExes[0].x >= 100 && mFaceInfoExes[0].x <= 150) &&
                            (mFaceInfoExes[0].y >= 100 && mFaceInfoExes[0].y <= 150) &&
                            (mFaceInfoExes[0].width <= 400 && mFaceInfoExes[0].width >= 300) &&
                            (mFaceInfoExes[0].height <= 500 && mFaceInfoExes[0].height >= 300)) {
                        int faceQuality = FaceManager.getInstance().getFaceQuality(rgb, width, height, 1, mFacesData, mFaceInfoExes);
                        if (faceQuality == 0) {
                            if (mFaceInfoExes[0].quality >= 60) {
                                int detectMask = FaceManager.getInstance().detectMask(rgb, width, height, 1, mFacesData, mFaceInfoExes);
                                if (detectMask == 0) {
                                    byte[] feature = new byte[FaceManager.getInstance().getFeatureSize()];
                                    boolean mask = mFaceInfoExes[0].mask >= 40;
                                    int extractFeature = FaceManager.getInstance().extractFeature(rgb, width, height, 1, mFacesData, feature, mask);
                                    if (extractFeature == 0) {
                                        // TODO: 2021/4/28 模拟人脸比对
                                        byte[] temp = new byte[FaceManager.getInstance().getFeatureSize()];
                                        float[] floats = new float[1];
                                        int matchFeature = FaceManager.getInstance().matchFeature(feature, temp, floats, mask);
                                        if (matchFeature == 0) {
                                            if (floats[0] >= 0.77) {


                                            } else {
                                                faceTips.postValue("人脸核验未通过");
                                            }
                                        }
                                    }
                                }
                            } else {
                                faceTips.postValue("人脸质量过低，当前：" + mFaceInfoExes[0].quality);
                            }
                        }
                    } else {
                        faceTips.postValue("请将人脸置于框内");
                    }
                } else if (faceNumber <= 0) {
                    faceTips.postValue("未检测到人脸");
                }
            }
            camera.getNextFrame();
        });
    }

    private final FingerManager.OnFingerReadListener fingerReadListener = (feature, image) -> {
        Timber.e("FingerRead:" + (feature == null) + "   " + (image == null));
        if (feature != null) {
            //fingerHint.set("获取指纹成功 ");
            fingerBitmap.postValue(image);
            FingerManager.getInstance().releaseDevice();
            FingerManager.getInstance().setFingerListener(null);
        } else {
            SystemClock.sleep(100);
            readFinger();
        }
    };

    private final FingerManager.OnFingerStatusListener fingerStatusListener = result -> {
        Timber.e("FingerStatus:" + result);
        if (result) {
            String device = FingerManager.getInstance().deviceInfo();
            Timber.e("device:" + device);
            if (TextUtils.isEmpty(device)) {
                //fingerHint.set("未找到指纹设备");
                Timber.e("未找到指纹设备");
            } else {
                Timber.e("请在指纹采集仪上按压指纹");
                //fingerHint.set("请在指纹采集仪上按压指纹");
                FingerManager.getInstance().setFingerListener(fingerReadListener);
                readFinger();
            }
        } else {
            //fingerHint.set("指纹初始化失败");
            Timber.e("指纹初始化失败");
            FingerManager.getInstance().release();
        }
    };

    private void readFinger() {
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                FingerManager.getInstance().readFinger();
            }
        });
    }
}
