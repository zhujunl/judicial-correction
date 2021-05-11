package com.miaxis.judicialcorrection.face;

import android.os.SystemClock;

import com.miaxis.camera.MXCamera;
import com.miaxis.faceid.FaceManager;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;

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

    //    MutableLiveData<Bitmap> fingerBitmap = new MutableLiveData<>();
    MutableLiveData<File> faceFile = new MutableLiveData<>();

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
            int detectFace = FaceManager.getInstance().detectFace(rgb, width, height, mFaceNumber, mFaceInfoExes);
            Timber.e("face   detectFace:%s", detectFace);
            if (detectFace == 0) {
                int faceNumber = FaceManager.getInstance().getFaceNumber(mFaceNumber);
                Timber.e("face   faceNumber:%s", faceNumber);
                if (faceNumber == 1) {
                    if (mFaceInfoExes[0].width >= 200) {
                        int faceQuality = FaceManager.getInstance().reg(rgb, width, height, 1, mFaceInfoExes);
                        Timber.e("face   faceQuality:%s", faceQuality);
                        if (faceQuality == 0) {
                            Timber.e("face   quality:%s", mFaceInfoExes[0].quality);
                            if (mFaceInfoExes[0].quality >= 45) {
                                captureCallback.onFaceReady(camera);
                                return;
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

    public LiveData<Resource<Object>> uploadPic(String pid, File file) {
        return mCapturePageRepo.uploadFace(pid, file);
    }

}
