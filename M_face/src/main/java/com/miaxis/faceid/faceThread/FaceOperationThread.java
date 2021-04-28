package com.miaxis.faceid.faceThread;

import android.util.Log;

import com.miaxis.faceid.FaceManager;

import org.zz.api.MXFaceInfoEx;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Admin
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class FaceOperationThread extends Thread {

    public Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();

    public FaceOperationThread() {
    }

    private byte[] rgbFrame;
    private int faceNumber;
    private int[] faceData;
    private int width;
    private int height;
    private boolean isRunning = false;
    private FaceCallback mFaceCallback;
    public MXFaceInfoEx[] faceInfoExes = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];

    public FaceOperationThread initData(FaceCallback faceCallback) {
        this.mFaceCallback = faceCallback;
        return this;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
            try {
                lock.lock();
                condition.await();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("人脸", "异常:" + e);
            }
            if (isInterrupted()) {
                return;
            }
            boolean success = false;
            if (!isRunning) {
                isRunning = true;
                if (mFaceCallback != null) {
                    mFaceCallback.onRecognizeStart();
                }
                int detectMask = FaceManager.getInstance().detectMask(rgbFrame, width, height, faceNumber, faceData);
                if (detectMask == 0) {
                    boolean mask = false;
                    for (int i = 0; i < this.faceNumber; i++) {
                        if (faceInfoExes[i].mask >= 40) {
                            mask = true;
                            break;
                        }
                    }
                    Log.d("人脸识别 口罩检测", "是否口罩:" + mask);
                    int detectLiveness = FaceManager.getInstance().detectLiveness(rgbFrame, width, height, faceNumber, faceData, faceInfoExes, false);
                    Log.e("人脸识别 活体检测", "detectLiveness:" + detectLiveness);
                    if (detectLiveness == 0) {
                        boolean live = false;
                        if (faceNumber > 0) {
                            MXFaceInfoEx faceInfoEx = faceInfoExes[0];
                            live = faceInfoEx.liveness >= 80;
                            Log.e("人脸识别 活体检测", "活体:" + faceInfoEx.liveness);
                        }
                        if (live) {
                            byte[] features = new byte[FaceManager.getInstance().getFeatureSize() * faceNumber];
                            int extractFeature = FaceManager.getInstance().extractFeature(rgbFrame, width, height, faceNumber, faceData, features, mask);
                            Log.e("人脸识别 人脸提取特征", "提取特征:" + extractFeature);
                            if (extractFeature == 0) {
                                if (mFaceCallback != null) {
                                    success = mFaceCallback.onRecognize(FaceOperationThread.this, features);
                                }
                            }
                        }
                    }
                }
            }
            if (mFaceCallback != null) {
                mFaceCallback.onRecognizeComplete(success, FaceOperationThread.this);
            }
        }
        if (mFaceCallback != null) {
            mFaceCallback.onRecognizeStop();
        }
    }

    public void setPushDataStatusEnable() {
        try {
            lock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isRunning = false;
        Log.d("人脸识别", "结束   等待新数据");
    }

    public int pushRGBFaceData(byte[] rgb, int faceNumber, int[] faceData, MXFaceInfoEx[] faceInfoExes, int width, int height) {
        Log.d("提交数据", "isRunning-" + isRunning);
        if (isRunning) {
            Log.d("提交数据", "失败  -1");
            return -1;
        }
        if (faceNumber != 1) {
            return -2;
        }
        if (rgb != null && faceData != null) {

            this.rgbFrame = new byte[rgb.length];
            System.arraycopy(rgb, 0, this.rgbFrame, 0, this.rgbFrame.length);

            this.faceNumber = faceNumber;

            this.faceData = new int[faceData.length];
            System.arraycopy(faceData, 0, this.faceData, 0, this.faceData.length);

            this.width = width;
            this.height = height;
            lock.lock();
            try {
                Log.d("提交数据", "开始0");
                condition.signal();
                Log.d("提交数据", "开始1");
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("提交数据", "开始异常:" + e.getMessage());
            } finally {
                lock.unlock();
                Log.d("提交数据", "结束");
            }
            //            return 0;
            return -1;
        }
        Log.d("提交数据", "失败  -2");
        return -3;
    }

}
