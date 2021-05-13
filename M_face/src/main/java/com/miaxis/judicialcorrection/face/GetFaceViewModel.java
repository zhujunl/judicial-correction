package com.miaxis.judicialcorrection.face;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.SystemClock;

import com.miaxis.camera.MXCamera;
import com.miaxis.faceid.FaceManager;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.utils.FileUtils;

import org.zz.api.MXFaceInfoEx;

import java.io.File;

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

    MutableLiveData<ZZResponse<VerifyInfo>> verifyStatus = new MutableLiveData<>();

    MutableLiveData<byte[]> idCardFaceFeature = new MutableLiveData<>();
    //MutableLiveData<File> faceFile = new MutableLiveData<>();

    public MXFaceInfoEx[] mFaceInfoExes = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
    public int[] mFaceNumber = new int[1];
    public int[] mFacesData = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];

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

    public void getFace(byte[] frame, MXCamera camera, int width, int height, GetFacePageFragment.GetFaceCallback captureCallback) {
        mAppExecutors.networkIO().execute(() -> {
            byte[] rgb = FaceManager.getInstance().yuv2Rgb(frame, width, height);
            //FaceManager.getInstance().flip(rgb, width, height);
            // TODO: 2021/5/12 测试阶段保存视频流图片，用于排查人脸算法实用性差原因
//            boolean b = FaceManager.getInstance().saveRgbTiFile(rgb, width, height,
//                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/Miaxis/" + System.currentTimeMillis() + ".jpeg"
//            );

            int detectFace = FaceManager.getInstance().detectFace(rgb, width, height, mFaceNumber, mFaceInfoExes);
            Timber.e("face   detectFace:%s", detectFace);
            if (detectFace == 0) {
                int faceNumber = FaceManager.getInstance().getFaceNumber(mFaceNumber);
                Timber.e("face   faceNumber:%s", faceNumber);
                if (faceNumber == 1) {
                    if (mFaceInfoExes[0].width >= 200) {
                        int faceQuality = FaceManager.getInstance().getFaceQuality(rgb, width, height, 1, mFaceInfoExes);
                        Timber.e("face   faceQuality:%s", faceQuality);
                        if (faceQuality == 0) {
                            Timber.e("face   quality:%s", mFaceInfoExes[0].quality);
                            if (mFaceInfoExes[0].quality >= 45) {
                                byte[] feature = new byte[FaceManager.getInstance().getFeatureSize()];
                                int extractFeature = FaceManager.getInstance().extractFeature(rgb, width, height, 1, feature, mFaceInfoExes, false);
                                if (extractFeature == 0) {
                                    Timber.e("face   extractFeature:%s", extractFeature);
                                    //byte[] temp = new byte[FaceManager.getInstance().getFeatureSize()];
                                    float[] floats = new float[1];
                                    int matchFeature = FaceManager.getInstance().matchFeature(feature, idCardFaceFeature.getValue(), floats, false);
                                    Timber.e("face   match:%s", matchFeature);
                                    if (matchFeature == 0) {
                                        Timber.e("face   floats:%s", floats[0]);
                                        if (floats[0] >= 0.45F) {
                                            faceTips.postValue("人证核验通过");
                                            captureCallback.onFaceReady(camera);
                                            return;
                                        } else {
                                            faceTips.postValue("人证核验未通过");
                                            verifyStatus.postValue(ZZResponse.CreateFail(-1,"人证核验未通过"));
                                        }
                                    }
                                }
                            } else {
                                faceTips.postValue("人脸质量过低，当前：" + mFaceInfoExes[0].quality);
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
