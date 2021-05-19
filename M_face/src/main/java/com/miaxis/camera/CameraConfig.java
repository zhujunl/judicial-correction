package com.miaxis.camera;

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
    Camera_RGB(Camera.CameraInfo.CAMERA_FACING_BACK, 640, 480, 90, 270),
    Camera_NIR(Camera.CameraInfo.CAMERA_FACING_FRONT, 640, 480, 90, 270);

    //todo 现场配置 Camera.CameraInfo.CAMERA_FACING_FRONT RGB
    //todo 现场配置 Camera.CameraInfo.CAMERA_FACING_BACK NIR
    //Camera_RGB(Camera.CameraInfo.CAMERA_FACING_FRONT, 640, 480, 90, 90),
    //Camera_NIR(Camera.CameraInfo.CAMERA_FACING_BACK, 640, 480, 90, 90);

    public int CameraId;
    public int width;
    public int height;
    public int previewOrientation;
    public int bufferOrientation;

    CameraConfig(int cameraId, int width, int height, int previewOrientation, int bufferOrientation) {
        this.CameraId = cameraId;
        this.width = width;
        this.height = height;
        this.previewOrientation = previewOrientation;
        this.bufferOrientation = bufferOrientation;
    }

}
