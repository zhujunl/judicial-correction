package com.miaxis.judicialcorrection.face.callback;


import com.miaxis.camera.MXCamera;

/**
 * @author Tank
 * @date 2021/4/29 10:33 AM
 * @des
 * @updateAuthor
 * @updateDes
 */

public interface CaptureCallback {

    /**
     * 人脸质量检测通过
     */
    void onFaceReady( MXCamera camera);

}
