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

    //todo 测试 本地测试配置 Camera.CameraInfo.CAMERA_FACING_BACK RGB
//    Camera_RGB(Camera.CameraInfo.CAMERA_FACING_FRONT, 640, 480, 90, 90),
//    Camera_NIR(Camera.CameraInfo.CAMERA_FACING_BACK, 640, 480, 90, 90),
//    Camera_SM(2,640, 480, 90, 90);

    // 正式 小台式 3个 第一个前置 摄像头
    Camera_RGB(2, 640, 480, 0, 0),
    Camera_NIR(0, 640, 480, 0, 0),
    Camera_SM(1,640, 480, 0, 0);


    //todo 现场配置 Camera.CameraInfo.CAMERA_FACING_FRONT RGB
    //todo 现场配置 Camera.CameraInfo.CAMERA_FACING_BACK NIR
      //正式  大台式
//    Camera_RGB(Camera.CameraInfo.CAMERA_FACING_FRONT, 640, 480, 90, 90),
//    Camera_NIR(Camera.CameraInfo.CAMERA_FACING_BACK, 640, 480, 90, 90),
//    Camera_SM(2,640, 480, 90, 90);

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
