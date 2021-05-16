package com.miaxis.judicialcorrection.face;

import android.hardware.Camera;

/**
 * @author Tank
 * @date 2021/5/16 13:19
 * @des
 * @updateAuthor
 * @updateDes
 */
public enum CameraConfig {

    //todo 本地测试配置 Camera.CameraInfo.CAMERA_FACING_BACK RGB
    //Camera_RGB(Camera.CameraInfo.CAMERA_FACING_BACK, 640, 480),
    //Camera_NIR(Camera.CameraInfo.CAMERA_FACING_FRONT, 640, 480);


    //todo 现场配置 Camera.CameraInfo.CAMERA_FACING_FRONT RGB
    //todo 现场配置 Camera.CameraInfo.CAMERA_FACING_BACK NIR
    Camera_RGB(Camera.CameraInfo.CAMERA_FACING_FRONT, 640, 480),
    Camera_NIR(Camera.CameraInfo.CAMERA_FACING_BACK, 640, 480);

    int CameraId;
    int width;
    int height;

    CameraConfig(int cameraId, int width, int height) {
        this.CameraId = cameraId;
        this.width = width;
        this.height = height;
    }

    public int getCameraId() {
        return CameraId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
