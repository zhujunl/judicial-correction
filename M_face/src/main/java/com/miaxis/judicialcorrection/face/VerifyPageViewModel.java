package com.miaxis.judicialcorrection.face;

import android.os.SystemClock;

import com.miaxis.camera.MXCamera;
import com.miaxis.faceid.FaceManager;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;

import org.zz.api.MXFaceInfoEx;

import javax.inject.Inject;

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
public class VerifyPageViewModel extends ViewModel {

    MutableLiveData<String> name = new MutableLiveData<>();

    MutableLiveData<String> id = new MutableLiveData<>();

    MutableLiveData<String> idCardNumber = new MutableLiveData<>();

    MutableLiveData<String> faceTips = new MutableLiveData<>();

    MutableLiveData<ZZResponse<VerifyInfo>> verifyStatus = new MutableLiveData<>();

//    MutableLiveData<Bitmap> fingerBitmap = new MutableLiveData<>();

    MutableLiveData<byte[]> tempFaceFeature = new MutableLiveData<>();

    public MXFaceInfoEx[] mFaceInfoExes = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
    public int[] mFaceNumber = new int[1];
    public int[] mFacesData = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];

    AppExecutors mAppExecutors;

    @Inject
    public VerifyPageViewModel(AppExecutors appExecutors) {
        this.mAppExecutors = appExecutors;
        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
            mFaceInfoExes[i] = new MXFaceInfoEx();
        }
    }

//    private OnFingerInitListener mOnFingerInitListener;
//
//    public void initFingerDevice(OnFingerInitListener onFingerInitListener) {
//        this.mOnFingerInitListener = onFingerInitListener;
//        mAppExecutors.networkIO().execute(() -> {
//            FingerManager.getInstance().initDevice(fingerStatusListener);
//        });
//    }

//    public void releaseFingerDevice() {
//        FingerManager.getInstance().setFingerListener(null);
//    }

    public byte[] nv21ToRgb(byte[] frame, int width, int height) {
        return FaceManager.getInstance().yuv2Rgb(frame, width, height);
    }

    public void faceRecognize(byte[] frame, MXCamera camera, int width, int height) {
        mAppExecutors.networkIO().execute(() -> {
            byte[] rgb = nv21ToRgb(frame, width, height);
            //FaceManager.getInstance().flip(rgb, width, height);
            int detectFace = FaceManager.getInstance().detectFace(rgb, width, height, mFaceNumber, mFaceInfoExes);
            Timber.e("face   detectFace:%s", detectFace);
            if (detectFace == 0) {
                int faceNumber = FaceManager.getInstance().getFaceNumber(mFaceNumber);
                Timber.e("face   faceNumber:%s", faceNumber);
                if (faceNumber == 1) {
                    //                    if ((mFaceInfoExes[0].x >= 100 && mFaceInfoExes[0].x <= 150) &&
                    //                            (mFaceInfoExes[0].y >= 100 && mFaceInfoExes[0].y <= 150) &&
                    //                            (mFaceInfoExes[0].width <= 400 && mFaceInfoExes[0].width >= 300) &&
                    //                            (mFaceInfoExes[0].height <= 500 && mFaceInfoExes[0].height >= 300)) {
                    int faceQuality = FaceManager.getInstance().getFaceQuality(rgb, width, height, 1, mFaceInfoExes);
                    Timber.e("face   faceQuality:%s", faceQuality);
                    if (faceQuality == 0) {
                        Timber.e("face   quality:%s", mFaceInfoExes[0].quality);
                        if (mFaceInfoExes[0].quality >= 40) {
                            byte[] feature = new byte[FaceManager.getInstance().getFeatureSize()];
                            int extractFeature = FaceManager.getInstance().extractFeature(rgb, width, height, 1, feature, mFaceInfoExes, false);
                            if (extractFeature == 0) {
                                Timber.e("face   extractFeature:%s", extractFeature);
                                // TODO: 2021/4/28 模拟人脸比对
                                //byte[] temp = new byte[FaceManager.getInstance().getFeatureSize()];
                                float[] floats = new float[1];
                                int matchFeature = FaceManager.getInstance().matchFeature(feature, tempFaceFeature.getValue(), floats, false);
                                Timber.e("face   match:%s", matchFeature);
                                if (matchFeature == 0) {
                                    Timber.e("face   floats:%s", floats[0]);
                                    if (floats[0] >= 0.70F) {
                                        faceTips.postValue("核验通过");
                                        verifyStatus.postValue(ZZResponse.CreateSuccess(new VerifyInfo(id.getValue(), name.getValue(), idCardNumber.getValue())));
                                    } else {
                                        faceTips.postValue("人脸核验未通过");
                                        verifyStatus.postValue(ZZResponse.CreateFail(-1,"人脸核验未通过"));
                                    }
                                    return;
                                }
                            }
                        } else {
                            faceTips.postValue("人脸质量过低，当前：" + mFaceInfoExes[0].quality);
                        }
                    }
                    //                    } else {
                    //                        faceTips.postValue("请将人脸置于框内");
                    //                    }
                } else if (faceNumber <= 0) {
                    faceTips.postValue("未识别到人脸");
                }
            } else {
                faceTips.postValue("未检测到人脸");
            }
            SystemClock.sleep(100);
            if (camera != null) {
                camera.getNextFrame();
            }
        });
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
                            tempFaceFeature.postValue(feature);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tempFaceFeature.postValue(null);
            }
        });
    }


//    private final FingerManager.OnFingerReadListener fingerReadListener = (feature, image) -> {
//        Timber.e("FingerRead:" + (feature == null) + "   " + (image == null));
//        if (feature != null) {
//            //fingerHint.set("获取指纹成功 ");
//            fingerBitmap.postValue(image);
//            FingerManager.getInstance().releaseDevice();
//            FingerManager.getInstance().setFingerListener(null);
//            verifyStatus.postValue(ZZResponse.CreateSuccess(new VerifyInfo(id.getValue(), name.getValue(), idCardNumber.getValue())));
//        } else {
//            SystemClock.sleep(100);
//            try {
//                readFinger();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    private final FingerManager.OnFingerStatusListener fingerStatusListener = result -> {
//        Timber.e("FingerStatus:" + result);
//        if (this.mOnFingerInitListener != null) {
//            this.mOnFingerInitListener.onInit(result);
//        }
//        if (result) {
//            result = readFinger();
//            if (!result) {
//                result = readFinger();
//            }
//        }
//        if (!result) {
//            Timber.e("指纹初始化失败");
//            FingerManager.getInstance().release();
//        }
//    };
//
//    private boolean readFinger() {
//        String device = FingerManager.getInstance().deviceInfo();
//        Timber.e("device:" + device);
//        if (TextUtils.isEmpty(device)) {
//            //fingerHint.set("未找到指纹设备");
//            Timber.e("未找到指纹设备");
//            return false;
//        } else {
//            Timber.e("请在指纹采集仪上按压指纹");
//            //fingerHint.set("请在指纹采集仪上按压指纹");
//            FingerManager.getInstance().setFingerListener(fingerReadListener);
//            mAppExecutors.networkIO().execute(new Runnable() {
//                @Override
//                public void run() {
//                    FingerManager.getInstance().readFinger();
//                }
//            });
//            return true;
//        }
//    }
//
//    public interface OnFingerInitListener {
//        void onInit(boolean result);
//    }


}
