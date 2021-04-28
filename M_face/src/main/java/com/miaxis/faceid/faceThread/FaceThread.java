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
public class FaceThread extends Thread {

    public Lock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();

    public FaceThread() {
    }

    private byte[] frame;
    private int width;
    private int height;
    private boolean isRunning = false;
    private FaceCallback mFaceCallback;

    public MXFaceInfoEx[] mFaceInfoExes = new MXFaceInfoEx[MXFaceInfoEx.iMaxFaceNum];
    public int[] mFaceNumber = new int[1];
    public int[] mFacesData = new int[MXFaceInfoEx.SIZE * MXFaceInfoEx.iMaxFaceNum];


    public FaceThread init(FaceCallback faceCallback) {
        this.mFaceCallback = faceCallback;
        for (int i = 0; i < this.mFaceInfoExes.length; i++) {
            this.mFaceInfoExes[i] = new MXFaceInfoEx();
        }
        return this;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
            this.lock.lock();
            try {
                this.condition.await();
                this.isRunning = true;
                if (isInterrupted() || this.mFaceCallback == null) {
                    return;
                }
                Log.d("人脸检测", "-------------------------------");
                byte[] bytes = FaceManager.getInstance().yuv2Rgb(this.frame, this.width, this.height);
                Log.d("人脸检测", "yuv----rgb");
                int detectFace = FaceManager.getInstance().detectFace(bytes, this.width, this.height, this.mFaceNumber, this.mFacesData, this.mFaceInfoExes);
                Log.d("人脸检测", "检测成功:" + (detectFace == 0));

                if (detectFace == 0 && !isInterrupted()) {
                    if (this.mFaceCallback != null) {
                        this.mFaceCallback.onDetectFaceComplete(FaceManager.getInstance().getFaceNumber(this.mFaceNumber), this.mFaceInfoExes);
                    }
                    if (this.mFaceOperationThread != null) {
                        int pushRGBFaceData = this.mFaceOperationThread.pushRGBFaceData(
                                bytes,
                                FaceManager.getInstance().getFaceNumber(this.mFaceNumber),
                                this.mFacesData,
                                this.mFaceInfoExes,
                                this.width,
                                this.height);
                        if (this.mFaceCallback != null) {
                            this.mFaceCallback.onFaceSubmit(this.mFaceOperationThread, FaceManager.getInstance().getFaceNumber(this.mFaceNumber), pushRGBFaceData);
                        }
                    }
                } else {
                    if (this.mFaceCallback != null) {
                        this.mFaceCallback.onDetectFaceComplete(0, null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("人脸", "异常:" + e);
            } finally {
                try {
                    this.lock.unlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.isRunning = false;
                Log.d("人脸识别", "结束");
            }
        }
    }

    public int pushYuvData(byte[] yuv, int width, int height) {
        Log.d("提交数据", "isRunning-" + this.isRunning);
        if (this.isRunning) {
            Log.d("提交数据", "失败  -1");
            return -1;
        }
        this.frame = yuv;
        this.width = width;
        this.height = height;
        if (this.frame != null) {
            this.lock.lock();
            try {
                Log.d("提交数据", "开始0");
                this.condition.signal();
                Log.d("提交数据", "开始1");
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("提交数据", "开始异常:" + e.getMessage());
            } finally {
                this.lock.unlock();
                Log.d("提交数据", "结束");
            }
            return 0;
        }
        Log.d("提交数据", "失败  -2");
        return -2;
    }

    @Override
    public void interrupt() {
        super.interrupt();
        if (this.mFaceOperationThread != null) {
            this.mFaceOperationThread.interrupt();
            this.mFaceOperationThread = null;
        }
    }

    private FaceOperationThread mFaceOperationThread;

    @Override
    public synchronized void start() {
        super.start();
        if (this.mFaceOperationThread == null) {
            this.mFaceOperationThread = new FaceOperationThread().initData(this.mFaceCallback);
        }
        this.mFaceOperationThread.start();
    }
}
