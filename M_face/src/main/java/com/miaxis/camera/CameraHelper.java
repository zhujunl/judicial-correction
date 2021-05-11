package com.miaxis.camera;

import android.hardware.Camera;

import com.miaxis.judicialcorrection.common.response.ZZResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class CameraHelper {

    private List<MXCamera> mMXCameras;

    private CameraHelper() {
        this.mMXCameras = new ArrayList<>();
    }

    private static class CameraHelperHolder {
        static CameraHelper mCameraHelper = new CameraHelper();
    }

    public synchronized static CameraHelper getInstance() {
        return CameraHelperHolder.mCameraHelper;
    }

    public ZZResponse<?> init() {
        free();
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras <= 0) {
            return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_NO_CAMERA, MXCameraErrorCode.MSG_FAIL_NO_CAMERA);
        }
        if (numberOfCameras < 2) {
            return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_CAMERA_COUNTS_LESS, MXCameraErrorCode.MSG_FAIL_CAMERA_COUNTS_LESS + ":" + numberOfCameras);
        }
        return ZZResponse.CreateSuccess();
    }

    public void free() {
        if (this.mMXCameras != null && !this.mMXCameras.isEmpty()) {
            this.mMXCameras.clear();
        }
    }

    public ZZResponse<MXCamera> createMXCamera(int cameraId) {
        MXCamera mxCamera = new MXCamera();
        int init = mxCamera.init();
        if (init == 0) {
            int open = mxCamera.open(cameraId);
            if (open == 0) {
                addMXCamera(mxCamera);
                return ZZResponse.CreateSuccess(mxCamera);
            } else if (open == -2) {
                return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_PARAMETERS, MXCameraErrorCode.MSG_FAIL_PARAMETERS);
            } else {
                return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_CAMERA_OPEN, MXCameraErrorCode.MSG_FAIL_CAMERA_OPEN);
            }
        } else {
            return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_NO_CAMERA, MXCameraErrorCode.MSG_FAIL_NO_CAMERA);
        }
    }

    //    public ZZResponse<MXCamera> createMXCamera(int cameraId, int width, int height) {
    //        MXCamera mxCamera = new MXCamera();
    //        int init = mxCamera.init(width, height);
    //        if (init == 0) {
    //            int open = mxCamera.open(cameraId);
    //            if (open == 0) {
    //                addMXCamera(mxCamera);
    //                return ZZResponse.CreateSuccess(mxCamera);
    //            } else if (open == -2) {
    //                return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_PARAMETERS, MXCameraErrorCode.MSG_FAIL_PARAMETERS);
    //            } else {
    //                return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_CAMERA_OPEN, MXCameraErrorCode.MSG_FAIL_CAMERA_OPEN);
    //            }
    //        } else {
    //            return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_NO_CAMERA, MXCameraErrorCode.MSG_FAIL_NO_CAMERA);
    //        }
    //    }

    public synchronized ZZResponse<MXCamera> find(int cameraId) {
        if (cameraId < 0) {
            return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_CAMERA_ID, null);
        }
        if (this.mMXCameras != null) {
            for (MXCamera mxCamera : this.mMXCameras) {
                if (mxCamera.getCameraId() == cameraId) {
                    return ZZResponse.CreateSuccess(mxCamera);
                }
            }
        }
        return ZZResponse.CreateFail(MXCameraErrorCode.CODE_FAIL_CAMERA_ID_NOT_FOUND, null);
    }

    private synchronized void addMXCamera(MXCamera mxCamera) {
        if (mxCamera == null) {
            return;
        }
        if (this.mMXCameras != null) {
            this.mMXCameras.add(mxCamera);
        }
    }

    public synchronized int resume() {
        if (this.mMXCameras != null && !this.mMXCameras.isEmpty()) {
            for (MXCamera mxCamera : this.mMXCameras) {
                if (mxCamera != null) {
                    mxCamera.resume();
                }
            }
        }
        return 0;
    }

    public synchronized int pause() {
        if (this.mMXCameras != null && !this.mMXCameras.isEmpty()) {
            for (MXCamera mxCamera : this.mMXCameras) {
                if (mxCamera != null) {
                    mxCamera.pause();
                }
            }
        }
        return 0;
    }

    public synchronized int stop() {
        if (this.mMXCameras != null && !this.mMXCameras.isEmpty()) {
            for (MXCamera mxCamera : this.mMXCameras) {
                if (mxCamera != null) {
                    mxCamera.stop();
                }
            }
        }
        return 0;
    }


}
