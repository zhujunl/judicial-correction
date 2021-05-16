package com.miaxis.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.miaxis.utils.BitmapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author Tank
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class MXCamera implements Camera.AutoFocusCallback, Camera.PreviewCallback {

    private int width = 640;
    private int height = 480;
    private boolean isPreview = false;
    private CameraPreviewCallback mCameraPreviewCallback;

    private Camera mCamera;
    private int mCameraId;
    private int mCameraStatus;


    protected MXCamera() {
    }

    public int init() {
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras <= 0) {
            return -1;
        }
        return 0;
    }

    public int getCameraId() {
        return this.mCameraId;
    }

    protected int open(int cameraId, int width, int height) {
        if (this.mCamera != null) {
            return -2;
        }
        if (cameraId >= Camera.getNumberOfCameras()) {
            return -4;
        }
        Camera camera = null;
        try {
            this.mCameraId = cameraId;
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        try {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size previewSize = parameters.getPreviewSize();
            previewSize.width = width;
            previewSize.height = height;
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return -3;
        }
        this.buffer = new byte[((this.width * this.height) * ImageFormat.getBitsPerPixel(ImageFormat.NV21)) / 8];
        this.mCamera = camera;
        return 0;
    }

    public int setOrientation(int orientation) {
        if (this.mCamera == null) {
            return -1;
        }
        this.mCamera.setDisplayOrientation(orientation);
        return 0;
    }

    private byte[] buffer;

    public int setPreviewCallback(CameraPreviewCallback frame) {
        if (this.mCamera == null) {
            return -1;
        }
        this.mCamera.setPreviewCallbackWithBuffer(this);
        this.mCameraPreviewCallback = frame;
        getNextFrame();
        return 0;
    }

    public int takePicture(Camera.PictureCallback jpeg) {
        if (this.mCamera == null) {
            return -1;
        }

        if (!this.isPreview) {
            return -2;
        }
        this.mCamera.takePicture(() -> {
        }, (data, camera) -> {
        }, jpeg);
        return 0;
    }


    public int getNextFrame() {
        if (this.mCamera == null) {
            return -1;
        }
        this.mCamera.addCallbackBuffer(this.buffer);
        return 0;
    }

    public int setFocus(boolean focus) {
        if (this.mCamera == null) {
            return -1;
        }
        Camera.Parameters parameters = this.mCamera.getParameters();
        boolean support = false;
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        for (String focusMode : supportedFocusModes) {
            if (Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO.equals(focusMode)) {
                support = true;
                break;
            }
        }
        if (!support) {
            return -2;
        }
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        this.mCamera.setParameters(parameters);
        this.mCamera.cancelAutoFocus();
        if (focus) {
            this.mCamera.autoFocus(this);
        }
        return 0;
    }

    public int start(SurfaceHolder holder) {
        if (this.mCamera == null) {
            return -1;
        }
        try {
            this.mCamera.setPreviewDisplay(holder);
            this.mCamera.startPreview();
            this.isPreview = true;
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    public int resume() {
        if (this.mCamera == null) {
            return -1;
        }
        if (this.isPreview) {
            this.mCamera.startPreview();
        }
        return 0;
    }

    public int pause() {
        if (this.mCamera == null) {
            return -1;
        }
        this.mCamera.stopPreview();
        return 0;
    }

    public int stop() {
        if (this.mCamera == null) {
            return -1;
        }
        try {
            this.mCamera.stopPreview();
            this.mCamera.setPreviewCallback(null);
            this.mCamera.setPreviewCallbackWithBuffer(null);
            this.mCamera.release();
            this.mCamera = null;
            this.isPreview = false;
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mCameraPreviewCallback != null) {
            mCameraPreviewCallback.onPreview(mCameraId, data, this, this.width, this.height);
        }
    }

    /**
     * @param data 摄像头视频流，NV21
     */
    public boolean getFrameImage(byte[] data, String savePath) {
        try {

            File file = new File(savePath);
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (parentFile != null && !parentFile.exists()) {
                    boolean mkdirs = parentFile.mkdirs();
                }
            } else {
                boolean newFile = file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
            //图像压缩
            boolean success = image.compressToJpeg(
                    new Rect(0, 0, image.getWidth(), image.getHeight()),
                    90, fileOutputStream);   // 将NV21格式图片，以质量70压缩成Jpeg，并得到JPEG数据流
            if (!success) {
                return false;
            }
            //将得到的照片进行270°旋转，使其竖直
            Bitmap bitmap = BitmapFactory.decodeFile(savePath);
            Matrix matrix = new Matrix();
            matrix.preRotate(270);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return BitmapUtils.saveBitmap(bitmap, savePath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
