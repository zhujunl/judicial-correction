package com.miaxis.faceid;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.zz.api.MXFaceInfoEx;
import org.zz.jni.JustouchFaceApi;
import org.zz.jni.mxImageTool;
import org.zz.tool.AssetsUtils;

import java.io.File;

/**
 * @author Admin
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class FaceManager {

    //    public static final String LicensePath = "/sdcard/miaxis/FaceId_ST/st_lic.txt";
    public static final String LicensePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "miaxis" + File.separator + "FaceId_ST" + File.separator + "st_lic.txt";
    //    private MXFaceAPI mMxFaceAPI;
    private mxImageTool mMxImageTool;
    private JustouchFaceApi mJustouchFaceApi = new JustouchFaceApi();

    private FaceManager() {
    }

    private static class FaceManagerHolder {
        private static FaceManager faceManager = new FaceManager();
    }

    public static FaceManager getInstance() {
        return FaceManagerHolder.faceManager;
    }

    public void free() {
        if (this.mJustouchFaceApi != null) {
            this.mJustouchFaceApi.freeAlg();
            this.mJustouchFaceApi = null;
        }
        if (mMxImageTool != null) {
            mMxImageTool = null;
        }
    }

    public int init(Context context) {
        String dstPath = context.getFilesDir().getAbsolutePath() + File.separator + "MIAXISModelsV5";
        if (!FileDataUtils.isExist(dstPath)) {
            FileDataUtils.AddDirectory(dstPath);
            AssetsUtils.copyFolderFromAssets(context, "MIAXISModelsV5", dstPath);
        }
        String license = getLicense(LicensePath);
        if (license == null) {
            return -1000;
        }
        this.mJustouchFaceApi = new JustouchFaceApi();
        int initAlg = this.mJustouchFaceApi.initAlg(context, dstPath, license);
        if (initAlg == 0) {
            this.mMxImageTool = new mxImageTool();
        }
        //        for (int i = 0; i < mFaceInfoExes.length; i++) {
        //            mFaceInfoExes[i] = new MXFaceInfoEx();
        //        }
        return initAlg;
    }

    public String getLicense(String dstPath) {
        if (dstPath == null || dstPath.isEmpty()) {
            return null;
        }
        byte[] szLicenseData = FileDataUtils.ReadData(dstPath);
        if (szLicenseData == null) {
            return null;
        }
        return new String(szLicenseData);
    }

    public int getFeatureSize() {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        return this.mJustouchFaceApi.getFeatureSize();
    }

    public byte[] yuv2Rgb(byte[] yuv, int width, int height) {
        byte[] pRGBImage = new byte[width * height * 3];
        this.mMxImageTool.YUV2RGB(yuv, width, height, pRGBImage);
        return pRGBImage;
    }


    //    public MXFaceInfoEx[] mFaceInfoExes = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
    //    public int[] mFaceNumber = new int[1];
    //    public int[] mFacesData = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];

    //    public MXFaceInfoEx[] getFaceInfoExes() {
    //        return mFaceInfoExes;
    //    }

    public int getFaceNumber(int[] faceNumber) {
        return faceNumber[0];
    }

    //    public int[] getFacesData() {
    //        return mFacesData;
    //    }

    //人脸检测
    public int detectFace(byte[] rgbFrameData, int frameWidth, int frameHeight, int[] faceNumber, int[] facesData, MXFaceInfoEx[] faceInfoExes) {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        int nRet = this.mJustouchFaceApi.detectFace(rgbFrameData, frameWidth, frameHeight, faceNumber, facesData);
        if (nRet != 0) {
            return nRet;
        }
        int num = faceNumber[0];
        Log.d("人脸检测", "detectFace:size" + num);

        if (num <= 0) {
            return -1;
        }
        MXFaceInfoEx.Int2MXFaceInfoEx(num, facesData, faceInfoExes);
        return 0;
    }

    //人脸质量检测
    public int getFaceQuality(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNum, MXFaceInfoEx[] faceInfo) {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        int[] faces = new int[MXFaceInfoEx.SIZE * faceNum];
        MXFaceInfoEx.MXFaceInfoEx2Int(faceNum, faces, faceInfo);
        return this.mJustouchFaceApi.faceQuality(rgbFrameData, frameWidth, frameHeight, faceNum, faces);
    }

    //活体检测
    public int detectLiveness(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNumber, int[] facesData, MXFaceInfoEx[] faceInfoExes, boolean nir) {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        int livenessDetect;
        if (nir) {
            livenessDetect = this.mJustouchFaceApi.nirLivenessDetect(rgbFrameData, frameWidth, frameHeight, faceNumber, facesData);
        } else {
            livenessDetect = this.mJustouchFaceApi.visLivenessDetect(rgbFrameData, frameWidth, frameHeight, faceNumber, facesData);
        }
        if (livenessDetect == 0) {
            MXFaceInfoEx.Int2MXFaceInfoEx(faceNumber, facesData, faceInfoExes);
        }
        return livenessDetect;
    }

    //提取特征
    public int extractFeature(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNumber, int[] faceData, byte[] pFeatureData, boolean mask) {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        if (mask) {
            return this.mJustouchFaceApi.maskFeatureExtract(rgbFrameData, frameWidth, frameHeight, faceNumber, faceData, pFeatureData);
        } else {
            return this.mJustouchFaceApi.featureExtract(rgbFrameData, frameWidth, frameHeight, faceNumber, faceData, pFeatureData);
        }
    }

    //口罩检测
    public int detectMask(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNumber, int[] faceData) {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        return this.mJustouchFaceApi.maskDetect(rgbFrameData, frameWidth, frameHeight, faceNumber, faceData);
    }

    //特征比对
    public int matchFeature(byte[] pFaceFeatureA, byte[] pFaceFeatureB, float[] fScore, boolean mask) {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        if (mask) {
            return this.mJustouchFaceApi.maskFeatureMatch(pFaceFeatureA, pFaceFeatureB, fScore);
        } else {
            return this.mJustouchFaceApi.featureMatch(pFaceFeatureA, pFaceFeatureB, fScore);
        }
    }

    //人员注册
    //    public int register(byte[] rgbFrameData, int nWidth, int nHeight) {
    //        if (this.mJustouchFaceApi == null) {
    //            return -99;
    //        }
    //        return this.mJustouchFaceApi.faceQuality4Reg(rgbFrameData, nWidth, nHeight, mFacesData);
    //    }

    //    //人脸追踪
    //    public int trackFace(byte[] rgbFrameData, int frameWidth, int frameHeight, MXFaceInfoEx[] faceInfo, int[] faceSize) {
    //        if (this.mJustouchFaceApi == null) {
    //            return -99;
    //        }
    //        if (faceInfo == null) {
    //            return -98;
    //        }
    //
    //        if (faceSize == null) {
    //            return -97;
    //        }
    //        if (rgbFrameData == null) {
    //            return -96;
    //        }
    //        int[] faceNumber = new int[]{0};
    //        int nRet = this.mJustouchFaceApi.trackFace(rgbFrameData, frameWidth, frameHeight, faceNumber, faces);
    //        if (nRet != 0) {
    //            return nRet;
    //        }
    //        int num = faceNumber[0];
    //        faceSize[0] = num;
    //        if (num <= 0) {
    //            return -1;
    //        }
    //        Arrays.fill(faceInfo, 0);
    //        MXFaceInfoEx.Int2MXFaceInfoEx(num, faces, faceInfo);
    //        return 0;
    //    }

}
