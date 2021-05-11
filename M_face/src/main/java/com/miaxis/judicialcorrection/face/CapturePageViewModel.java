package com.miaxis.judicialcorrection.face;

import android.os.SystemClock;

import com.miaxis.camera.MXCamera;
import com.miaxis.faceid.FaceManager;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;

import org.zz.api.MXFaceInfoEx;

import java.io.File;

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
public class CapturePageViewModel extends ViewModel {

    MutableLiveData<String> name = new MutableLiveData<>();

    MutableLiveData<String> idCardNumber = new MutableLiveData<>();

    MutableLiveData<String> faceTips = new MutableLiveData<>();

    MutableLiveData<ZZResponse<VerifyInfo>> verifyStatus = new MutableLiveData<>();

    //    MutableLiveData<Bitmap> fingerBitmap = new MutableLiveData<>();
    MutableLiveData<File> faceFile = new MutableLiveData<>();

    public MXFaceInfoEx[] mFaceInfoExes = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
    public int[] mFaceNumber = new int[1];
    public int[] mFacesData = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];

    AppExecutors mAppExecutors;

    @Inject
    public CapturePageViewModel(AppExecutors appExecutors) {
        this.mAppExecutors = appExecutors;
        FaceManager.getInstance().initData(mFaceInfoExes);
    }

    public void getFace(int cameraId, byte[] frame, MXCamera camera, int width, int height, GetFacePageFragment.GetFaceCallback captureCallback) {
        mAppExecutors.networkIO().execute(() -> {
            byte[] rgb = FaceManager.getInstance().yuv2Rgb(frame, width, height);
            int detectFace = FaceManager.getInstance().detectFace(rgb, width, height, mFaceNumber, mFacesData, mFaceInfoExes);
            if (detectFace == 0) {
                int faceNumber = FaceManager.getInstance().getFaceNumber(mFaceNumber);
                if (faceNumber == 1) {
                    //                    if ((mFaceInfoExes[0].x >= 100 && mFaceInfoExes[0].x <= 150) &&
                    //                            (mFaceInfoExes[0].y >= 100 && mFaceInfoExes[0].y <= 150) &&
                    //                            (mFaceInfoExes[0].width <= 400 && mFaceInfoExes[0].width >= 300) &&
                    //                            (mFaceInfoExes[0].height <= 500 && mFaceInfoExes[0].height >= 300)) {
                    int faceQuality = FaceManager.getInstance().getFaceQuality(rgb, width, height, 1, mFacesData, mFaceInfoExes);
                    if (faceQuality == 0) {
                        if (mFaceInfoExes[0].quality >= 30) {
                            int detectMask = FaceManager.getInstance().detectMask(rgb, width, height, 1, mFacesData, mFaceInfoExes);
                            if (detectMask == 0) {
                                byte[] feature = new byte[FaceManager.getInstance().getFeatureSize()];
                                boolean mask = mFaceInfoExes[0].mask >= 40;
                            }
                            captureCallback.onFaceReady(camera);

                            return;
                        } else {
                            faceTips.postValue("人脸质量过低，当前：" + mFaceInfoExes[0].quality);
                        }
                    }
                    //                    } else {
                    //                        faceTips.postValue("请将人脸置于框内");
                    //                    }
                } else if (faceNumber <= 0) {
                    faceTips.postValue("未检测到人脸");
                }
            } else {
                faceTips.postValue("未检测到人脸");
            }
            SystemClock.sleep(200);
            camera.getNextFrame();
        });
    }

}
