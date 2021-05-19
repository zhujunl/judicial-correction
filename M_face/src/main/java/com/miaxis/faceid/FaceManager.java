package com.miaxis.faceid;

import android.content.Context;
import android.graphics.RectF;
import android.util.Size;

import org.zz.api.MXErrorCode;
import org.zz.api.MXFaceAPI;
import org.zz.api.MXFaceInfoEx;
import org.zz.jni.mxImageTool;

import java.io.File;

/**
 * @author Tank
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class FaceManager {

    private MXFaceAPI mMXFaceAPI;
    private mxImageTool mMxImageTool;

    public MXFaceInfoEx[] mFaceInfoExesRgb;
    public MXFaceInfoEx[] mFaceInfoExesNir;

    public int[] mFaceNumberRgb = new int[1];
    public int[] mFaceNumberNir = new int[1];

    private FaceManager() {
    }

    private static class FaceManagerHolder {
        private static final FaceManager faceManager = new FaceManager();
    }

    public static FaceManager getInstance() {
        return FaceManagerHolder.faceManager;
    }

    public void free() {
        if (this.mMXFaceAPI != null) {
            this.mMXFaceAPI.mxFreeAlg();
            this.mMXFaceAPI = null;
        }
        this.mMxImageTool = null;
    }

    public int init(Context context) {
        this.mMXFaceAPI = new MXFaceAPI();
        this.mMxImageTool = new mxImageTool();
        this.mFaceInfoExesRgb = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
        this.mFaceInfoExesNir = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
        for (int i = 0; i < MXFaceInfoEx.iMaxFaceNum; i++) {
            this.mFaceInfoExesRgb[i] = new MXFaceInfoEx();
        }
        System.arraycopy(mFaceInfoExesRgb, 0, mFaceInfoExesNir, 0, MXFaceInfoEx.iMaxFaceNum);
        return this.mMXFaceAPI.mxInitAlg(context, null, null);
    }

    /**
     * 视频流转RGB
     *
     * @param yuv 视频流数据NV21
     */
    public byte[] yuv2Rgb(byte[] yuv, int width, int height) {
        if (this.mMxImageTool == null) {
            return null;
        }
        byte[] pRGBImage = new byte[width * height * 3];
        this.mMxImageTool.YUV2RGB(yuv, width, height, pRGBImage);
        return pRGBImage;
    }

    public RectF getRgbFaceRect() {
        int faceNumberRGB = getFaceNumberRGB();
        if (faceNumberRGB > 0) {
            MXFaceInfoEx faceInfoRGB = getFaceInfoRGB(0);
            return new RectF(faceInfoRGB.x, faceInfoRGB.y, faceInfoRGB.x + faceInfoRGB.width, faceInfoRGB.y + faceInfoRGB.height);
        } else {
            return null;
        }
    }

    /**
     * 获取人脸数量RGB
     */
    public int getFaceNumberRGB() {
        return getFaceNumber(this.mFaceNumberRgb);
    }

    public MXFaceInfoEx getFaceInfoRGB(int index) {
        if (this.mFaceInfoExesRgb == null || index >= this.mFaceInfoExesRgb.length) {
            return null;
        }
        return this.mFaceInfoExesRgb[index];
    }

    //人脸检测RGB
    public int detectFaceRGB(byte[] rgbFrameData, int frameWidth, int frameHeight) {
        return detectFace(rgbFrameData, frameWidth, frameHeight, this.mFaceNumberRgb, this.mFaceInfoExesRgb);
    }

    /**
     * 人脸质量RGB
     */
    public int getFaceQualityRGB(byte[] rgbFrameData, int frameWidth, int frameHeight) {
        int faceNumber = getFaceNumberRGB();
        if (faceNumber < 0) {
            return faceNumber;
        }
        return getFaceQuality(rgbFrameData, frameWidth, frameHeight, faceNumber, this.mFaceInfoExesRgb);
    }

    //提取特征RGB
    public int extractFeatureRgb(byte[] rgbFrameData, int frameWidth, int frameHeight, boolean mask, byte[] pFeatureData) {
        int faceNumber = getFaceNumberRGB();
        if (faceNumber < 0) {
            return faceNumber;
        }
        return extractFeature(rgbFrameData, frameWidth, frameHeight, faceNumber, pFeatureData, this.mFaceInfoExesRgb, mask);
    }

    //口罩检测
    public int detectMaskRGB(byte[] rgbFrameData, int frameWidth, int frameHeight) {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
        }
        if (this.mFaceNumberRgb == null || this.mFaceNumberRgb.length == 0) {
            return -96;
        }
        if (this.mFaceInfoExesRgb == null || this.mFaceInfoExesRgb.length == 0) {
            return -94;
        }
        return this.mMXFaceAPI.mxMaskDetect(rgbFrameData, frameWidth, frameHeight, mFaceNumberRgb[0], this.mFaceInfoExesRgb);
    }

    /**
     * 活体检测
     *
     * @return 10000-Live，10001-no live，others-image quality is not satisfied , negative Number is error code
     */
    public int liveDetect(byte[] rgbFrameData, int frameWidth, int frameHeight,
                          byte[] nirFrameData) {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        if (rgbFrameData == null || rgbFrameData.length == 0) {
            return -98;
        }
        if (nirFrameData == null || nirFrameData.length == 0) {
            return -97;
        }
        if (this.mFaceNumberRgb == null || this.mFaceNumberRgb.length == 0) {
            return -96;
        }
        if (this.mFaceNumberNir == null || this.mFaceNumberNir.length == 0) {
            return -95;
        }
        if (this.mFaceInfoExesRgb == null || this.mFaceInfoExesRgb.length == 0) {
            return -94;
        }
        if (this.mFaceInfoExesNir == null || this.mFaceInfoExesNir.length == 0) {
            return -93;
        }
        return this.mMXFaceAPI.mxDetectLive(rgbFrameData, nirFrameData, frameWidth, frameHeight,
                this.mFaceNumberRgb, this.mFaceInfoExesRgb, this.mFaceNumberNir, this.mFaceInfoExesNir);
    }

    //特征比对
    public int matchFeature(byte[] pFaceFeatureA, byte[] pFaceFeatureB, float[] fScore, boolean mask) {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        if (pFaceFeatureA == null || pFaceFeatureA.length == 0 || pFaceFeatureB == null || pFaceFeatureB.length == 0) {
            return -98;
        }
        if (fScore == null || fScore.length == 0) {
            return -97;
        }
        if (mask) {
            return this.mMXFaceAPI.mxMaskFeatureMatch(pFaceFeatureA, pFaceFeatureB, fScore);
        } else {
            return this.mMXFaceAPI.mxFeatureMatch(pFaceFeatureA, pFaceFeatureB, fScore);
        }
    }

    //    public boolean flip(byte[] pRGBImage, int iRGBWidth, int iRGBHeight) {
    //        if (this.mMxImageTool == null || pRGBImage == null || pRGBImage.length == 0) {
    //            return false;
    //        }
    //        int imageFlip = mMxImageTool.ImageFlip(pRGBImage, iRGBWidth, iRGBHeight, 1, pRGBImage);
    //        return imageFlip == 1;
    //    }

    public Size rotate(byte[] pRGBImage, int iRGBWidth, int iRGBHeight, int rotation, byte[] out) {
        if (this.mMxImageTool == null || pRGBImage == null || pRGBImage.length == 0
                || out == null || out.length == 0) {
            return null;
        }
        int[] width = new int[1];
        int[] height = new int[1];
        int imageFlip = mMxImageTool.ImageRotate(pRGBImage, iRGBWidth, iRGBHeight, rotation, out, width, height);
        if (imageFlip != 1) {
            return null;
        }
        return new Size(width[0], height[0]);
    }

    public byte[] getRgbFromFile(String strPathImgFile, int[] oX, int[] oY) {
        if (mMxImageTool == null) {
            return null;
        }
        // 获取图像大小
        int nRet = mMxImageTool.ImageLoad(strPathImgFile, 3, null, oX, oY);
        if (nRet != 1) {
            return null;
        }
        // 得到图像大小后
        byte[] pRGBBuff = new byte[oX[0] * oY[0] * 3];
        nRet = mMxImageTool.ImageLoad(strPathImgFile, 3, pRGBBuff, oX, oY);
        if (nRet == 1) {
            return pRGBBuff;
        }
        return null;
    }

    public boolean saveRgbTiFile(byte[] rgb, int width, int height, String path) {
        if (mMxImageTool == null) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                boolean mkdirs = parentFile.mkdirs();
            }
        }
        return mMxImageTool.ImageSave(path, rgb, width, height, 3) == 1;
    }

    private int getFaceNumber(int[] mFaceNumber) {
        if (mFaceNumber == null) {
            return -99;
        }
        return mFaceNumber[0];
    }

    //人脸检测
    private int detectFace(byte[] rgbFrameData, int frameWidth, int frameHeight, int[] faceNumber, MXFaceInfoEx[] faceInfoExes) {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
        }
        if (faceNumber == null) {
            return -97;
        }
        if (faceInfoExes == null) {
            return -96;
        }
        int nRet = this.mMXFaceAPI.mxDetectFace(rgbFrameData, frameWidth, frameHeight, faceNumber, faceInfoExes);
        if (nRet != 0 && nRet != 13) {
            return nRet;
        }
        int num = faceNumber[0];
        if (num < 0) {
            faceNumber[0] = 0;
        }
        return 0;
    }

    public int getFeatureSize() {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        return this.mMXFaceAPI.mxGetFeatureSize();
    }

    //人脸质量检测
    private int getFaceQuality(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNum, MXFaceInfoEx[] faceInfo) {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
        }
        if (faceInfo == null) {
            return -97;
        }
        return this.mMXFaceAPI.mxFaceQuality(rgbFrameData, frameWidth, frameHeight, faceNum, faceInfo);
    }

    //提取特征
    private int extractFeature(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNumber, byte[] pFeatureData, MXFaceInfoEx[] faceInfo, boolean mask) {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
        }
        if (mask) {
            return this.mMXFaceAPI.mxMaskFeatureExtract(rgbFrameData, frameWidth, frameHeight, faceNumber, faceInfo, pFeatureData);
        } else {
            return this.mMXFaceAPI.mxFeatureExtract(rgbFrameData, frameWidth, frameHeight, faceNumber, faceInfo, pFeatureData);
        }
    }

    public String getLiveError(int nLiveResult) {
        String strInfo = null;
        switch (nLiveResult) {
            case MXErrorCode.ERR_FACE_LIV_IS_LIVE:
                strInfo = "活体";
                break;
            case MXErrorCode.ERR_FACE_LIV_IS_UNLIVE:
                strInfo = "非活体";
                break;
            case MXErrorCode.ERR_FACE_LIV_VIS_NO_FACE:
                strInfo = "可见光输入没有人脸";
                break;
            case MXErrorCode.ERR_FACE_LIV_NIS_NO_FACE:
                strInfo = "近红外输入没有人脸";
                break;
            case MXErrorCode.ERR_FACE_LIV_SKIN_FAILED:
                strInfo = "人脸肤色检测未通过";
                break;
            case MXErrorCode.ERR_FACE_LIV_DIST_TOO_CLOSE:
                strInfo = "请离远一点";
                break;
            case MXErrorCode.ERR_FACE_LIV_DIST_TOO_FAR:
                strInfo = "请离近一点";
                break;
            case MXErrorCode.ERR_FACE_LIV_POSE_DET_FAIL:
                strInfo = "请正对摄像头";
                break;
            case MXErrorCode.ERR_FACE_LIV_FACE_CLARITY_DET_FAIL:
                strInfo = "模糊";
                break;
            case MXErrorCode.ERR_FACE_LIV_VIS_EYE_CLOSE:
                strInfo = "请勿闭眼";
                break;
            case MXErrorCode.ERR_FACE_LIV_VIS_MOUTH_OPEN:
                strInfo = "请勿张嘴";
                break;
            case MXErrorCode.ERR_FACE_LIV_VIS_BRIGHTNESS_EXC:
                strInfo = "过曝";
                break;
            case MXErrorCode.ERR_FACE_LIV_VIS_BRIGHTNESS_INS:
                strInfo = "欠曝";
                break;
            case MXErrorCode.ERR_FACE_LIV_VIS_OCCLUSION:
                strInfo = "遮挡";
                break;
            default:
                strInfo = "错误";
                break;
        }
        return strInfo;
    }
}
