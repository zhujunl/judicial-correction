package com.miaxis.faceid.faceThread;


import org.zz.api.MXFaceInfoEx;

/**
 * @author Admin
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public interface FaceCallback {

    void onDetectFaceStart();

    void onDetectFaceComplete(int faceNumber, MXFaceInfoEx[] mFaceInfoExes);

    //    void onLiveDetectStart();
    //
    //    void onLiveDetectComplete(Exception e);

    void onFaceSubmit(FaceOperationThread faceOperationThread, int faceNumber, int code);

    void onRecognizeStart();

    boolean onRecognize( FaceOperationThread faceOperationThread, byte[] features);

    void onRecognizeComplete(boolean success,FaceOperationThread faceOperationThread);

    void onRecognizeStop();

}
