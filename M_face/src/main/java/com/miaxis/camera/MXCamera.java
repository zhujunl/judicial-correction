package com.miaxis.camera;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;

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

    protected int open(int cameraId) {
        if (this.mCamera != null) {
            return -2;
        }
        Camera camera = null;
        try {
            this.mCameraId = cameraId;
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size previewSize = parameters.getPreviewSize();
        this.width = previewSize.width;
        this.height = previewSize.height;
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


}
