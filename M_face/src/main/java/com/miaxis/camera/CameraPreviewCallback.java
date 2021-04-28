package com.miaxis.camera;

/**
 * @author Admin
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public interface CameraPreviewCallback {

    void onPreview(int cameraId, byte[] frame, int width, int height);

}
