package org.zz.mxhidfingerdriver;

import org.zz.jni.*;
import android.os.*;
import android.content.*;
import org.zz.protocol.*;
import java.util.*;
import android.graphics.*;
import org.zz.tool.*;

public class MXFingerDriver
{
    private MXHidFingerComm m_hidFingerComm;
    private Boolean m_bLoadImgRepair;
    private mxImgRepair m_repair;
    private Handler m_fHandler;
    private static final int IMAGE_AREA_THRESHOLD = 5;
    
    public MXFingerDriver(final Context context) {
        this.m_bLoadImgRepair = false;
        this.m_fHandler = null;
        this.m_fHandler = null;
        this.m_hidFingerComm = new MXHidFingerComm(context);
    }
    
    public MXFingerDriver(final Context context, final int iPid, final int iVid) {
        this.m_bLoadImgRepair = false;
        this.m_fHandler = null;
        this.m_fHandler = null;
        this.mxSetPVID(iPid, iVid);
        this.m_bLoadImgRepair = true;
        System.loadLibrary("mxImgRepair");
        this.m_repair = new mxImgRepair();
        this.m_hidFingerComm = new MXHidFingerComm(context);
    }
    
    public MXFingerDriver(final Context context, final int iPid, final int iVid, final Handler bioHandler) {
        this.m_bLoadImgRepair = false;
        this.m_fHandler = null;
        this.m_fHandler = bioHandler;
        this.mxSetPVID(iPid, iVid);
        this.m_bLoadImgRepair = true;
        System.loadLibrary("mxImgRepair");
        this.m_repair = new mxImgRepair();
        this.m_hidFingerComm = new MXHidFingerComm(context);
    }
    
    public MXFingerDriver(final Context context, final Handler bioHandler) {
        this.m_bLoadImgRepair = false;
        this.m_fHandler = null;
        this.m_fHandler = bioHandler;
        this.m_hidFingerComm = new MXHidFingerComm(context, bioHandler);
    }
    
    public MXFingerDriver(final Context context, final Boolean bLoadImgRepair) {
        this.m_bLoadImgRepair = false;
        this.m_fHandler = null;
        this.m_fHandler = null;
        if (bLoadImgRepair) {
            this.m_bLoadImgRepair = bLoadImgRepair;
            System.loadLibrary("mxImgRepair");
            this.m_repair = new mxImgRepair();
        }
        this.m_hidFingerComm = new MXHidFingerComm(context);
    }
    
    public MXFingerDriver(final Context context, final Handler bioHandler, final Boolean bLoadImgRepair) {
        this.m_bLoadImgRepair = false;
        this.m_fHandler = null;
        this.m_fHandler = bioHandler;
        if (bLoadImgRepair) {
            this.m_bLoadImgRepair = bLoadImgRepair;
            System.loadLibrary("mxImgRepair");
            this.m_repair = new mxImgRepair();
        }
        this.m_hidFingerComm = new MXHidFingerComm(context, bioHandler);
    }
    
    public String mxGetDriverVersion() {
        return MXVersion.strVersion;
    }
    
    public int mxSetPVID(final int iPid, final int iVid) {
        MXCommand.VENDORID = iVid;
        MXCommand.PRODUCTID = iPid;
        return 0;
    }
    
    public int mxSetLog(final boolean bLog) {
        MXLog.LOG_MSG = bLog;
        return 0;
    }
    
    public String getDevAttribute() {
        return this.m_hidFingerComm.getDevAttribute();
    }
    
    public int mxGetDevVersion(final byte[] bVersion) {
        return this.m_hidFingerComm.mxGetDevVersion(bVersion);
    }
    
    public int mxGetDevSN(final byte[] bVersion) {
        return this.m_hidFingerComm.mxGetDevSN(bVersion);
    }
    
    public int mxSetDevSN(final byte[] szDevSerial, final int iDevSerialLen) {
        return this.m_hidFingerComm.mxSetDevSN(szDevSerial, iDevSerialLen);
    }
    
    public int mxPlayVoice(final int iVoiceCode1, final int iVoiceCode2) {
        return this.m_hidFingerComm.mxplayvoice(iVoiceCode1, iVoiceCode2);
    }
    
    public int mxDetectFinger() {
        return this.m_hidFingerComm.mxDetectFinger();
    }
    
    public void MySleep(final int iTimeout) {
        long duration = -1L;
        Calendar time2;
        for (Calendar time1 = Calendar.getInstance(); duration <= iTimeout; duration = time2.getTimeInMillis() - time1.getTimeInMillis()) {
            time2 = Calendar.getInstance();
        }
    }
    
    public int mxGetImage(final byte[] bImgBuf, final int iImgLen, final int iTimeOut, final int iFlagLeave) {
        MXLog.SendMsg("mxGetImage");
        int nRet = this.m_hidFingerComm.mxGetImage(bImgBuf, iImgLen, iTimeOut, iFlagLeave);
        if (nRet != 0) {
            if (nRet == -2 || nRet == -3) {
                return nRet;
            }
            if (nRet > -100) {
                MXLog.SendMsg("\u91cd\u53d1mxGetImage");
                this.MySleep(300);
                MXLog.SendMsg("\u91cd\u53d1mxGetImage");
                nRet = this.m_hidFingerComm.mxGetImage(bImgBuf, iImgLen, iTimeOut, iFlagLeave);
            }
        }
        return nRet;
    }
    
    public int mxGetImageB64(final byte[] bImgBufB64, final int iImgB64Len, final int iTimeOut, final int iFlagLeave) {
        int nRet = 0;
        final int iImgLen = 30400;
        final byte[] bImgBuf = new byte[30400];
        nRet = this.mxGetImage(bImgBuf, iImgLen, iTimeOut, iFlagLeave);
        if (nRet == 0) {
            this.JavaBase64Encode(bImgBuf, iImgLen, bImgBufB64, iImgB64Len);
        }
        return nRet;
    }
    
    public int mxGetIsoImage(final byte[] bIsoImgBuf, final int iIsoImgLen, final int iTimeOut, final int iFlagLeave) {
        final int iDpi = 363;
        final int iWidth = 152;
        final int iHeight = 200;
        final int iImgLen = iIsoImgLen - 54;
        final byte[] bImgBuf = new byte[iImgLen];
        final int iRV = this.mxGetImage(bImgBuf, iImgLen, iTimeOut, iFlagLeave);
        if (iRV < 0) {
            return iRV;
        }
        ImgFormatTrans.ImgToIso(bImgBuf, iDpi, iWidth, iHeight, bIsoImgBuf);
        return iRV;
    }
    
    public int mxGetImage256x304(final byte[] bImgBuf, final int iTimeOut, final int iFlagLeave) {
        int nRet = 0;
        final int iSrcWidth = 240;
        final int iSrcHeight = 204;
        final int iDstWidth = 256;
        final int iDstHeight = 304;
        final byte[] tmp = new byte[iDstWidth * iDstHeight];
        nRet = this.m_hidFingerComm.mxGetImage256x304(tmp, iSrcWidth, iSrcHeight, iTimeOut, iFlagLeave);
        if (nRet == 0 && this.m_bLoadImgRepair) {
            final Calendar time1 = Calendar.getInstance();
            this.m_repair.Scale(tmp, 0, 0, iSrcWidth, iSrcHeight, iSrcWidth, iSrcHeight, bImgBuf, iDstWidth, iDstHeight);
            final Calendar time2 = Calendar.getInstance();
            final long n = time2.getTimeInMillis() - time1.getTimeInMillis();
        }
        return nRet;
    }
    
    boolean bytecmp(final byte[] src1, final byte[] src2, final int ilen) {
        boolean iSame = true;
        for (int i = 0; i < ilen; ++i) {
            if (src1[i] != src2[i]) {
                iSame = false;
                break;
            }
        }
        return iSame;
    }
    
    public int mxAutoGetImage(final byte[] bImgBuf, final int[] iImgW, final int[] iImgH, final int iTimeOut, final int iFlagLeave) {
        int nRet = 0;
        final byte[] imgdesc = new byte[8];
        final String strDST = "OPTIC";
        nRet = this.m_hidFingerComm.mxAutoGetImage(bImgBuf, iImgW, iImgH, iTimeOut, iFlagLeave, imgdesc, false, false, false);
        if (nRet == 0) {
            MXLog.SendMsg("\u7c7b\u578b\uff1a" + new String(imgdesc));
            if (this.m_bLoadImgRepair && iImgW[0] == 256 && iImgH[0] == 360) {
                if (this.m_repair.CalcFingerArea256X360(bImgBuf, iImgW[0], iImgH[0]) < 5) {
                    return -9;
                }
                if (this.m_repair.VerificationKeyData(0, bImgBuf) != 0) {
                    return -14;
                }
                if (this.bytecmp(imgdesc, strDST.getBytes(), 5)) {
                    MXLog.SendMsg("\u5149\u5b66");
                    this.m_repair.FP_OPTICReapir(bImgBuf, iImgW[0], iImgH[0]);
                }
                else {
                    MXLog.SendMsg("\u7535\u5bb9");
                    this.m_repair.FP_UPEKReapir(bImgBuf, iImgW[0], iImgH[0]);
                }
                this.m_repair.VerificationKeyData(1, bImgBuf);
            }
        }
        return nRet;
    }
    
    public int mxAutoGetImage_in(final byte[] bImgBuf, final int[] iImgW, final int[] iImgH, final int iTimeOut, final int iFlagLeave) {
        int nRet = 0;
        final byte[] imgdesc = new byte[8];
        final String strDST = "OPTIC";
        nRet = this.m_hidFingerComm.mxAutoGetImage(bImgBuf, iImgW, iImgH, iTimeOut, iFlagLeave, imgdesc, false, false, false);
        if (nRet == 0) {
            MXLog.SendMsg("\u7c7b\u578b\uff1a" + new String(imgdesc));
            if (this.m_bLoadImgRepair && iImgW[0] == 256 && iImgH[0] == 360) {
                if (this.m_repair.CalcFingerArea256X360(bImgBuf, iImgW[0], iImgH[0]) < 5) {
                    return -9;
                }
                if (this.bytecmp(imgdesc, strDST.getBytes(), 5)) {
                    MXLog.SendMsg("\u5149\u5b66");
                    this.m_repair.FP_OPTICReapir(bImgBuf, iImgW[0], iImgH[0]);
                }
                else {
                    MXLog.SendMsg("\u7535\u5bb9");
                    this.m_repair.FP_UPEKReapir(bImgBuf, iImgW[0], iImgH[0]);
                }
                this.m_repair.VerificationKeyData(1, bImgBuf);
            }
        }
        return nRet;
    }
    
    public int mxAutoGetIsoImage(final byte[] bIsoImgBuf, final int[] iWidth, final int[] iHeight, final int iDpi, final int iTimeOut, final int iFlagLeave) {
        final byte[] bImgBuf = new byte[92160];
        final int iRV = this.mxAutoGetImage(bImgBuf, iWidth, iHeight, iTimeOut, iFlagLeave);
        if (iRV < 0) {
            return iRV;
        }
        ImgFormatTrans.ImgToIso(bImgBuf, iDpi, iWidth[0], iHeight[0], bIsoImgBuf);
        return iRV;
    }
    
    public int mxAutoCaptureImage(final byte[] bImgBuf, final int[] iImgW, final int[] iImgH, final int iTimeOut, final int iFlagLeave) {
        int nRet = 0;
        final byte[] imgdesc = new byte[8];
        final String strDST = "OPTIC";
        nRet = this.m_hidFingerComm.mxAutoGetImage(bImgBuf, iImgW, iImgH, iTimeOut, iFlagLeave, imgdesc, false, false, false);
        if (nRet == 0) {
            MXLog.SendMsg("\u7c7b\u578b\uff1a" + new String(imgdesc));
            if (this.m_bLoadImgRepair && iImgW[0] == 256 && iImgH[0] == 360) {
                if (this.m_repair.CalcFingerArea256X360(bImgBuf, iImgW[0], iImgH[0]) < 5) {
                    return -9;
                }
                if (this.m_repair.VerificationKeyData(0, bImgBuf) != 0) {
                    return -14;
                }
                if (this.bytecmp(imgdesc, strDST.getBytes(), 5)) {
                    MXLog.SendMsg("\u5149\u5b66");
                    this.m_repair.FP_OPTICReapir(bImgBuf, iImgW[0], iImgH[0]);
                }
                else {
                    MXLog.SendMsg("\u7535\u5bb9");
                    this.m_repair.FP_UPEKReapir(bImgBuf, iImgW[0], iImgH[0]);
                }
                this.m_repair.VerificationKeyData(1, bImgBuf);
            }
        }
        return nRet;
    }
    
    public void mxCancelGetImage() {
        this.m_hidFingerComm.mxCancelGetImage();
    }
    
    public Bitmap Raw2Bimap(final byte[] imgBuf, final int iImgX, final int iImgY) {
        return BMP.Raw2Bimap(imgBuf, iImgX, iImgY);
    }
    
    public Bitmap Iso2Bimap(final byte[] isoImgBuf) {
        return BMP.Iso2Bimap(isoImgBuf);
    }
    
    public int mxDevUpdate(final byte[] bCodbuffer, final int codbufferlen) {
        return this.m_hidFingerComm.mxDevUpdate(bCodbuffer, codbufferlen);
    }
    
    public int mxSetSleepMode() {
        return this.m_hidFingerComm.mxSetSleepMode();
    }
    
    public int zzOpenDev(final int vid, final int pid) {
        int nRet = 0;
        nRet = this.m_hidFingerComm.zzOpenDev(vid, pid);
        if (nRet != 0) {
            return nRet;
        }
        nRet = this.m_hidFingerComm.zzStartFPC();
        if (nRet != 0) {
            this.m_hidFingerComm.zzCloseDev();
            return nRet;
        }
        return nRet;
    }
    
    public int zzCloseDev() {
        this.m_hidFingerComm.zzStopFPC();
        return this.m_hidFingerComm.zzCloseDev();
    }
    
    public int zzAutoUpImage(final byte[] bImgBuf, final int[] iImgW, final int[] iImgH, final int iTimeOut, final int iFlagLeave) {
        int nRet = 0;
        final byte[] imgdesc = new byte[8];
        final String strDST = "OPTIC";
        nRet = this.m_hidFingerComm.zzUpAutoImage(bImgBuf, iImgW, iImgH, false, imgdesc);
        if (nRet == 0 && this.m_bLoadImgRepair && iImgW[0] == 256 && iImgH[0] == 360) {
            if (this.bytecmp(imgdesc, strDST.getBytes(), 5)) {
                this.m_repair.FP_OPTICReapir(bImgBuf, iImgW[0], iImgH[0]);
            }
            else {
                this.m_repair.FP_UPEKReapir(bImgBuf, iImgW[0], iImgH[0]);
            }
        }
        return nRet;
    }
    
    public int JavaBase64Encode(final byte[] pInput, final int inputLen, final byte[] pOutput, final int outputbufsize) {
        return mxJavaBase64.JavaBase64Encode(pInput, inputLen, pOutput, outputbufsize);
    }
    
    public int JavaBase64Decode(final byte[] pInput, final int inputLen, final byte[] pOutput) {
        return mxJavaBase64.JavaBase64Decode(pInput, inputLen, pOutput);
    }
    
    public void unRegUsbMonitor() {
        this.m_hidFingerComm.unRegUsbMonitor();
    }
    
    public int mxGetDevTzZX(final int iTimeOut, final int iType, final byte[] bTzBuf) {
        return this.m_hidFingerComm.mxGetDevTzZX(iTimeOut, iType, bTzBuf);
    }
    
    public int mxGetDevMbZX(final int iTimeOut, final int iType, final byte[] bMbBuf) {
        return this.m_hidFingerComm.mxGetDevMbZX(iTimeOut, iType, bMbBuf);
    }
    
    public int mxDownRSAPublicKeyZX(final byte[] iPublicKey, final short iPublicKeyLen) {
        return this.m_hidFingerComm.mxDownRSAPublicKeyZX(iPublicKey, iPublicKeyLen);
    }
}
