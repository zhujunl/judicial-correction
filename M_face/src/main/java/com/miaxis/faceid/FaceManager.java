package com.miaxis.faceid;

import android.content.Context;
import android.util.Size;

import org.zz.api.MXFaceAPI;
import org.zz.api.MXFaceInfoEx;
import org.zz.jni.mxImageTool;

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

    private FaceManager() {
    }

    private static class FaceManagerHolder {
        private static FaceManager faceManager = new FaceManager();
    }

    public static FaceManager getInstance() {
        return FaceManagerHolder.faceManager;
    }

    public void free() {
        if (this.mMXFaceAPI != null) {
            this.mMXFaceAPI.mxFreeAlg();
            this.mMXFaceAPI = null;
        }
    }

    public int init(Context context) {
        this.mMXFaceAPI = new MXFaceAPI();
        this.mMxImageTool = new mxImageTool();
        return this.mMXFaceAPI.mxInitAlg(context, null, null);
    }


    public int getFeatureSize() {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        return this.mMXFaceAPI.mxGetFeatureSize();
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
    public int detectFace(byte[] rgbFrameData, int frameWidth, int frameHeight, int[] faceNumber, MXFaceInfoEx[] faceInfoExes) {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
        }
        int nRet = this.mMXFaceAPI.mxDetectFace(rgbFrameData, frameWidth, frameHeight, faceNumber, faceInfoExes);
        if (nRet != 0) {
            return nRet;
        }
        int num = faceNumber[0];
        if (num < 0) {
            faceNumber[0] = 0;
        }
        return 0;
    }

    //人脸质量检测
    public int getFaceQuality(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNum, MXFaceInfoEx[] faceInfo) {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
        }
        return this.mMXFaceAPI.mxFaceQuality(rgbFrameData, frameWidth, frameHeight, faceNum, faceInfo);
    }

    //提取特征
    public int extractFeature(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNumber, byte[] pFeatureData, MXFaceInfoEx[] faceInfo, boolean mask) {
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

    //口罩检测
    public int detectMask(byte[] rgbFrameData, int frameWidth, int frameHeight, int faceNumber, MXFaceInfoEx[] faceInfo) {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        if (rgbFrameData == null) {
            return -98;
        }
        return this.mMXFaceAPI.mxMaskDetect(rgbFrameData, frameWidth, frameHeight, faceNumber, faceInfo);
    }

    //特征比对
    public int matchFeature(byte[] pFaceFeatureA, byte[] pFaceFeatureB, float[] fScore, boolean mask) {
        if (this.mMXFaceAPI == null) {
            return -99;
        }
        if (pFaceFeatureA == null || pFaceFeatureA.length == 0 || pFaceFeatureB == null || pFaceFeatureB.length == 0) {
            return -98;
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
        if (this.mMxImageTool == null || pRGBImage == null || out == null || out.length == 0 ||
                out.length != pRGBImage.length) {
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
        return mMxImageTool.ImageSave(path, rgb, width, height, 3) == 1;
    }

}
