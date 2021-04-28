package com.miaxis.faceid;

import android.content.Context;
import android.os.Environment;

import org.zz.api.MXFaceInfoEx;
import org.zz.jni.JustouchFaceApi;
import org.zz.jni.mxImageTool;
import org.zz.tool.AssetsUtils;

import java.io.File;

/**
 * @author Tank
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
        return initAlg;
    }

    private String getLicense(String dstPath) {
        if (dstPath == null || dstPath.isEmpty()) {
            return null;
        }
        byte[] szLicenseData = FileDataUtils.ReadData(dstPath);
        if (szLicenseData == null) {
            return null;
        }
        return new String(szLicenseData);
    }

    public int initData(MXFaceInfoEx[] faceInfoExes) {
        if (faceInfoExes != null) {
            for (int i = 0; i < faceInfoExes.length; i++) {
                faceInfoExes[i] = new MXFaceInfoEx();
            }
            return 0;
        }
        return -90;
    }

    public int getFeatureSize() {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        return this.mJustouchFaceApi.getFeatureSize();
    }

    public byte[] yuv2Rgb(byte[] yuv, int width, int height) {
        if (this.mMxImageTool == null) {
            return null;
        }
        byte[] pRGBImage = new byte[width * height * 3];
        this.mMxImageTool.YUV2RGB(yuv, width, height, pRGBImage);
        return pRGBImage;
    }

    public int getFaceNumber(int[] faceNumber) {
        return faceNumber[0];
    }

    //人脸检测
    public int detectFace(byte[] rgbFrameData, int frameWidth, int frameHeight, int[] faceNumber, int[] facesData, MXFaceInfoEx[] faceInfoExes) {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
        }
        int nRet = this.mJustouchFaceApi.detectFace(rgbFrameData, frameWidth, frameHeight, faceNumber, facesData);
        if (nRet != 0) {
            return nRet;
        }
        int num = faceNumber[0];
        if (num < 0) {
            faceNumber[0] = 0;
        }
        MXFaceInfoEx.Int2MXFaceInfoEx(num, facesData, faceInfoExes);
        return 0;
    }

    //人脸质量检测
    public int getFaceQuality(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNum, int[] faces, MXFaceInfoEx[] faceInfo) {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
        }
        int faceQuality = this.mJustouchFaceApi.faceQuality(rgbFrameData, frameWidth, frameHeight, faceNum, faces);
        if (faceQuality == 0) {
            MXFaceInfoEx.Int2MXFaceInfoEx(faceNum, faces, faceInfo);
        }
        return faceQuality;
    }

    //活体检测
    public int detectLiveness(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNumber, int[] facesData, MXFaceInfoEx[] faceInfoExes, boolean nir) {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
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
        if (rgbFrameData == null) {
            return -98;
        }
        if (mask) {
            return this.mJustouchFaceApi.maskFeatureExtract(rgbFrameData, frameWidth, frameHeight, faceNumber, faceData, pFeatureData);
        } else {
            return this.mJustouchFaceApi.featureExtract(rgbFrameData, frameWidth, frameHeight, faceNumber, faceData, pFeatureData);
        }
    }

    //口罩检测
    public int detectMask(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNumber, int[] faceData, MXFaceInfoEx[] faceInfo) {
        if (this.mJustouchFaceApi == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
        }
        int maskDetect = this.mJustouchFaceApi.maskDetect(rgbFrameData, frameWidth, frameHeight, faceNumber, faceData);
        if (maskDetect == 0) {
            MXFaceInfoEx.MXFaceInfoEx2Int(faceNumber, faceData, faceInfo);
        }
        return maskDetect;
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

}
