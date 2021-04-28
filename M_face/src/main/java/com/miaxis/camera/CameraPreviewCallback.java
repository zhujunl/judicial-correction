package com.miaxis.camera;

/**
 * @author Admin
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public interface CameraPreviewCallback {

    void onPreview(int cameraId, byte[] frame, MXCamera camera, int width, int height);

}
