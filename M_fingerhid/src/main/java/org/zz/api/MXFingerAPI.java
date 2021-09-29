package org.zz.api;

import org.zz.mxhidfingerdriver.*;
import org.zz.jni.*;
import android.content.*;
import com.fingerprint.*;
import android.graphics.*;

public class MXFingerAPI
{
    zzFingerAlg fingeralgzh;
    MXFingerDriver fingerDriver;
    mxImgRepair mxImgRepair;
    private final int FingerArea256X360 = 40;
    
    public MXFingerAPI(final Context context, final int iPID, final int iVID) {
        this.fingeralgzh = new zzFingerAlg();
        this.mxImgRepair = new mxImgRepair();
        this.fingerDriver = new MXFingerDriver(context, iPID, iVID);
    }
    
    public String mxGetJarVersion() {
        return "SM80FingerAPI V2.2.1.20190314";
    }
    
    public String mxGetDriverVersion() {
        return this.fingerDriver.mxGetDriverVersion();
    }
    
    public int mxGetDevVersion(final byte[] bVersion) {
        return this.fingerDriver.mxGetDevVersion(bVersion);
    }
    
    public int mxPlayVoice(final int iVoiceCode1, final int iVoiceCode2) {
        return this.fingerDriver.mxPlayVoice(iVoiceCode1, iVoiceCode2);
    }
    
    public int mxGetDevSN(final byte[] bVersion) {
        return this.fingerDriver.mxGetDevSN(bVersion);
    }
    
    public int mxGetAlgVersionID(final byte[] version) {
        return zzFingerAlgID.mxGetVersion(version);
    }
    
    public int mxGetAlgVersionJH(final byte[] version) {
        return this.fingeralgzh.mxGetVersion(version);
    }
    
    public int mxDetectFinger() {
        return this.fingerDriver.mxDetectFinger();
    }
    
    public int mxCaptueFingerprint(final byte[] bImgBuf, final int iTimeOut, final int iFlagLeave) {
        final int[] iImgW = { 0 };
        final int[] iImgH = { 0 };
        final int mxAutoGetImage = this.fingerDriver.mxAutoGetImage(bImgBuf, iImgW, iImgH, iTimeOut, iFlagLeave);
        if (mxAutoGetImage == 0) {
            final int calcFingerArea256X360 = this.mxImgRepair.CalcFingerArea256X360(bImgBuf, iImgW[0], iImgH[0]);
            return (calcFingerArea256X360 >= 40) ? mxAutoGetImage : -5;
        }
        return mxAutoGetImage;
    }
    
    public void mxCancelCaptue() {
        this.fingerDriver.mxCancelGetImage();
    }
    
    public Bitmap Raw2Bimap(final byte[] imgBuf, final int iImgX, final int iImgY) {
        return this.fingerDriver.Raw2Bimap(imgBuf, iImgX, iImgY);
    }
    
    public int mxExtractFeatureID(final byte[] bImgBuf, final int iTimeOut, final int iFlagLeave, final byte[] bTzBuf) {
        int nRet = -1;
        final int[] iImgW = { 0 };
        final int[] iImgH = { 0 };
        nRet = this.fingerDriver.mxAutoGetImage(bImgBuf, iImgW, iImgH, iTimeOut, iFlagLeave);
        if (nRet != 0) {
            return nRet;
        }
        nRet = zzFingerAlgID.mxGetTz512(bImgBuf, bTzBuf);
        if (nRet == 1) {
            nRet = 0;
        }
        else {
            nRet = -6;
        }
        return nRet;
    }
    
    public int mxExtractFeatureJH(final byte[] bImgBuf, final int iTimeOut, final int iFlagLeave, final byte[] bTzBuf) {
        int nRet = -1;
        final int[] iImgW = { 0 };
        final int[] iImgH = { 0 };
        nRet = this.fingerDriver.mxAutoGetImage(bImgBuf, iImgW, iImgH, iTimeOut, iFlagLeave);
        if (nRet != 0) {
            return nRet;
        }
        nRet = this.fingeralgzh.mxGetTz512From256X360(bImgBuf, bTzBuf);
        if (nRet == 1) {
            nRet = 0;
        }
        else {
            nRet = -6;
        }
        return nRet;
    }
    
    public int mxMatchFeatureID(final byte[] bTzBuf1, final byte[] bTzBuf2, final int level) {
        return zzFingerAlgID.mxFingerMatch512(bTzBuf1, bTzBuf2, 3);
    }
    
    public int mxMatchFeatureScoreID(final byte[] bTzBuf1, final byte[] bTzBuf2, final int[] iSimilarity) {
        return zzFingerAlgID.mxIDFingerMatch512Score(bTzBuf1, bTzBuf2, iSimilarity);
    }
    
    public int mxMatchFeatureListID(final byte[] usersfingerdata, final int usersNum, final byte[] fingerdata, final int level) {
        return zzFingerAlgID.mxFingerUsersMatch512(usersfingerdata, usersNum, fingerdata, level);
    }
    
    public int mxMatchFeatureJH(final byte[] bTzBuf1, final byte[] bTzBuf2, final int level) {
        return this.fingeralgzh.mxFingerMatch512(bTzBuf1, bTzBuf2, 3);
    }
    
    public int mxTzToMb(final byte[] tzBuf1, final byte[] tzBuf2, final byte[] tzBuf3, final byte[] mbBuf) {
        return this.fingeralgzh.mxGetMB512(tzBuf1, tzBuf2, tzBuf3, mbBuf);
    }
    
    public void unRegUsbMonitor() {
        this.fingerDriver.unRegUsbMonitor();
    }
    
    static {
        System.loadLibrary("mxFingerAlgId");
        System.loadLibrary("FingerAlg");
    }
}
