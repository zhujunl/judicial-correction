package com.miaxis.judicialcorrection.base.api.vo;

/**
 * 设备相机参数配置
 * 如后面要不同类型设备摄像头要整合可以用这个操作
 */
public class EquipmentConfigCameraEntity {
    public int cameraRGBId;

    public int cameraNIRId;

    public int cameraSMId;

    public int previewOrientation;

    public int bufferOrientation;

    //高拍仪
    public int previewOrientationSM;

    public int bufferOrientationSM;

    public int savePictureRotationSize;
    //人脸质量
    public int faceQuality=30;
    //比对分数
    public int  faceComparison=75;

}
