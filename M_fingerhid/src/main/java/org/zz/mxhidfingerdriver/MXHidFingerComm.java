package org.zz.mxhidfingerdriver;

import android.content.*;
import org.zz.protocol.*;
import java.util.*;
import android.os.*;
import android.util.*;
import org.zz.tool.*;

public class MXHidFingerComm
{
    private boolean m_bCancelGetImage;
    private UsbBase m_usbBase;
    private Handler m_fHandler;
    
    public MXHidFingerComm(final Context context) {
        this.m_bCancelGetImage = false;
        this.m_fHandler = null;
        this.m_fHandler = null;
        this.m_usbBase = new UsbBase(context);
    }
    
    public MXHidFingerComm(final Context context, final Handler bioHandler) {
        this.m_bCancelGetImage = false;
        this.m_fHandler = null;
        MXLog.SetHandler(this.m_fHandler = bioHandler);
        this.m_usbBase = new UsbBase(context, bioHandler);
    }
    
    public String getDevAttribute() {
        return this.m_usbBase.getDevAttribute();
    }
    
    public int mxGetDevNum() {
        return this.m_usbBase.getDevNum(MXCommand.VENDORID, MXCommand.PRODUCTID);
    }
    
    public int mxGetDevVersion(final byte[] bVersion) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_READ_VERSION;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        this.m_usbBase.closeDev();
        for (int i = 0; i < iRecvDataLen[0]; ++i) {
            bVersion[i] = bRecvBuf[i];
        }
        return 0;
    }
    
    public int mxGetDevSN(final byte[] bVersion) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_GET_SERIAL_NO;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        this.m_usbBase.closeDev();
        for (int i = 0; i < iRecvDataLen[0]; ++i) {
            bVersion[i] = bRecvBuf[i];
        }
        return 0;
    }
    
    public int mxSetDevSN(final byte[] szDevSerial, final int iDevSerialLen) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_SET_SERIAL_NO;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, szDevSerial, iDevSerialLen);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        this.m_usbBase.closeDev();
        return 0;
    }
    
    public int mxDetectFinger() {
        int iRet = 0;
        iRet = this.zzOpenDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        iRet = this.zzDetectFinger();
        this.zzCloseDev();
        return iRet;
    }
    
    public int mxGetImage(final byte[] bImgBuf, final int iImgLen, final int iTimeOut, final int iFlagLeave) {
        int iRet = 0;
        MXLog.SendMsg("\u6253\u5f00\u8bbe\u5907");
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        iRet = this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_ON);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        iRet = this.zzStartFPC();
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        this.m_bCancelGetImage = false;
        long duration = -1L;
        Calendar time1 = Calendar.getInstance();
        MXLog.SendMsg("\u63a2\u6d4b\u624b\u6307");
        Calendar time2;
        for (iRet = this.zzDetectFinger(); iRet == 1 && duration <= iTimeOut && !this.m_bCancelGetImage; duration = time2.getTimeInMillis() - time1.getTimeInMillis()) {
            iRet = this.zzDetectFinger();
            SystemClock.sleep(10L);
            time2 = Calendar.getInstance();
            if (0 != iTimeOut) {}
        }
        if (this.m_bCancelGetImage) {
            this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            this.zzStopFPC();
            this.m_usbBase.closeDev();
            iRet = -2;
            return iRet;
        }
        if (duration > iTimeOut) {
            this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            this.zzStopFPC();
            this.m_usbBase.closeDev();
            iRet = -3;
            return iRet;
        }
        iRet = this.zzReadImage();
        if (0 != iRet) {
            this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            this.zzStopFPC();
            this.m_usbBase.closeDev();
            iRet = -4;
            return iRet;
        }
        MXLog.SendMsg("\u4e0a\u4f20\u56fe\u50cf");
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = this.zzUpPackAndroid(bRecvBuf, 0);
        if (iRet == 0) {
            MXLog.SendMsg("zzUpPackAndroid ok");
            final int[] iImgWidth = { 0 };
            final int[] iImgHeight = { 0 };
            final byte[] imgdesc = new byte[8];
            iRet = this.zzUpImageAndroid(bImgBuf, iImgWidth, iImgHeight, false, imgdesc);
        }
        else {
            MXLog.SendMsg("zzUpPackAndroid error,iRet=" + iRet);
            iRet = this.zzUpImage(bImgBuf, iImgLen);
        }
        if (0 != iRet) {
            iRet = this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            iRet = this.zzStopFPC();
            iRet = this.m_usbBase.closeDev();
            iRet = -5;
            return iRet;
        }
        this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
        if (iFlagLeave == 1) {
            this.zzLampOnOff(MXCommand.LED_GREEN_LIGHT, MXCommand.LED_ON);
            for (duration = -1L, time1 = Calendar.getInstance(), iRet = this.zzDetectFinger(); iRet == 0 && duration <= iTimeOut && !this.m_bCancelGetImage; duration = time2.getTimeInMillis() - time1.getTimeInMillis()) {
                iRet = this.zzDetectFinger();
                SystemClock.sleep(10L);
                time2 = Calendar.getInstance();
                if (0 != iTimeOut) {}
            }
            this.zzLampOnOff(MXCommand.LED_GREEN_LIGHT, MXCommand.LED_OFF);
            if (this.m_bCancelGetImage) {
                this.zzStopFPC();
                this.m_usbBase.closeDev();
                iRet = -2;
                return iRet;
            }
            if (duration > iTimeOut) {
                this.zzStopFPC();
                this.m_usbBase.closeDev();
                iRet = -3;
                return iRet;
            }
        }
        this.zzStopFPC();
        MXLog.SendMsg("\u5173\u95ed\u8bbe\u5907");
        this.m_usbBase.closeDev();
        return 0;
    }
    
    public int mxGetImage256x304(final byte[] bImgBuf, final int iImgW, final int iImgH, final int iTimeOut, final int iFlagLeave) {
        int iRet = 0;
        MXLog.SendMsg("\u6253\u5f00\u8bbe\u5907");
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        MXLog.SendMsg("\u63a2\u6d4b\u624b\u6307\uff0c\u7ea2\u706f\u4eae");
        iRet = this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_ON);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        this.m_bCancelGetImage = false;
        long duration = -1L;
        Calendar time1 = Calendar.getInstance();
        MXLog.SendMsg("\u63a2\u6d4b\u624b\u6307");
        Calendar time2;
        for (iRet = this.zzDetectFinger(); iRet == 1 && duration <= iTimeOut && !this.m_bCancelGetImage; duration = time2.getTimeInMillis() - time1.getTimeInMillis()) {
            iRet = this.zzDetectFinger();
            SystemClock.sleep(10L);
            time2 = Calendar.getInstance();
            if (0 != iTimeOut) {}
        }
        if (this.m_bCancelGetImage) {
            this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            this.zzStopFPC();
            this.m_usbBase.closeDev();
            iRet = -2;
            return iRet;
        }
        if (duration > iTimeOut) {
            this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            this.zzStopFPC();
            this.m_usbBase.closeDev();
            iRet = -3;
            return iRet;
        }
        MXLog.SendMsg("\u63a2\u6d4b\u5230\u624b\u6307\uff0c\u7ea2\u706f\u706d");
        this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
        MXLog.SendMsg("\u4e0a\u4f20\u56fe\u50cf");
        final byte[] imgdesc = new byte[8];
        final int[] ImgW = { 0 };
        final int[] ImgH = { 0 };
        iRet = this.zzUpAutoImage(bImgBuf, ImgW, ImgH, false, imgdesc);
        if (0 != iRet) {
            this.m_usbBase.closeDev();
            iRet = -5;
            return iRet;
        }
        this.zzLampOnOff(MXCommand.LED_GREEN_LIGHT, MXCommand.LED_ON);
        if (iFlagLeave == 1) {
            for (duration = -1L, time1 = Calendar.getInstance(), iRet = this.zzDetectFinger(); iRet == 0 && duration <= iTimeOut && !this.m_bCancelGetImage; duration = time2.getTimeInMillis() - time1.getTimeInMillis()) {
                iRet = this.zzDetectFinger();
                SystemClock.sleep(10L);
                time2 = Calendar.getInstance();
                if (0 != iTimeOut) {}
            }
            if (this.m_bCancelGetImage) {
                this.m_usbBase.closeDev();
                iRet = -2;
                return iRet;
            }
            if (duration > iTimeOut) {
                this.m_usbBase.closeDev();
                iRet = -3;
                return iRet;
            }
        }
        this.zzLampOnOff(MXCommand.LED_GREEN_LIGHT, MXCommand.LED_OFF);
        MXLog.SendMsg("\u5173\u95ed\u8bbe\u5907");
        this.m_usbBase.closeDev();
        return 0;
    }
    
    public int mxAutoGetImage(final byte[] bImgBuf, final int[] iImgW, final int[] iImgH, final int iTimeOut, final int iFlagLeave, final byte[] imgdesc, final boolean bLamp, final boolean bSensor, final boolean bHandshake) {
        int iRet = 0;
        MXLog.SendMsg("\u6253\u5f00\u8bbe\u5907");
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        if (bLamp) {
            MXLog.SendMsg("\u63a2\u6d4b\u624b\u6307\uff0c\u7ea2\u706f\u4eae");
            iRet = this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_ON);
            if (iRet != 0) {
                this.m_usbBase.closeDev();
                return iRet;
            }
        }
        if (bSensor) {
            MXLog.SendMsg("\u5f00\u542fFPC\u4f20\u611f\u5668");
            iRet = this.zzStartFPC();
            if (iRet != 0) {
                this.m_usbBase.closeDev();
                return iRet;
            }
        }
        this.m_bCancelGetImage = false;
        long duration = -1L;
        Calendar time1 = Calendar.getInstance();
        MXLog.SendMsg("\u63a2\u6d4b\u624b\u6307");
        Calendar time2;
        for (iRet = this.zzDetectFinger(); iRet == 1 && duration <= iTimeOut && !this.m_bCancelGetImage; duration = time2.getTimeInMillis() - time1.getTimeInMillis()) {
            iRet = this.zzDetectFinger();
            SystemClock.sleep(10L);
            time2 = Calendar.getInstance();
            if (0 != iTimeOut) {}
        }
        if (this.m_bCancelGetImage) {
            if (bLamp) {
                this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            }
            if (bSensor) {
                this.zzStopFPC();
            }
            this.m_usbBase.closeDev();
            iRet = -2;
            return iRet;
        }
        if (duration > iTimeOut) {
            if (bLamp) {
                this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            }
            if (bSensor) {
                this.zzStopFPC();
            }
            this.m_usbBase.closeDev();
            iRet = -3;
            return iRet;
        }
        MXLog.SendMsg("\u4e0a\u4f20\u56fe\u50cf");
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = this.zzUpPackAndroid(bRecvBuf, 10);
        if (iRet == 0) {
            MXLog.SendMsg("zzUpPackAndroid ok");
            iRet = this.zzUpImageAndroid(bImgBuf, iImgW, iImgH, bHandshake, imgdesc);
        }
        else {
            MXLog.SendMsg("zzUpPackAndroid error,iRet=" + iRet);
            iRet = this.zzUpAutoImage(bImgBuf, iImgW, iImgH, true, imgdesc);
        }
        if (0 != iRet) {
            if (bLamp) {
                this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            }
            if (bSensor) {
                this.zzStopFPC();
            }
            this.m_usbBase.closeDev();
            iRet = -5;
            return iRet;
        }
        MXLog.SendMsg("\u4e0a\u4f20\u56fe\u50cf\u5b8c\u6210");
        if (bLamp) {
            MXLog.SendMsg("\u63a2\u6d4b\u5230\u624b\u6307\uff0c\u7ea2\u706f\u706d");
            this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
        }
        if (iFlagLeave == 1) {
            if (bLamp) {
                this.zzLampOnOff(MXCommand.LED_GREEN_LIGHT, MXCommand.LED_ON);
            }
            for (duration = -1L, time1 = Calendar.getInstance(), iRet = this.zzDetectFinger(); iRet == 0 && duration <= iTimeOut && !this.m_bCancelGetImage; duration = time2.getTimeInMillis() - time1.getTimeInMillis()) {
                iRet = this.zzDetectFinger();
                SystemClock.sleep(10L);
                time2 = Calendar.getInstance();
                if (0 != iTimeOut) {}
            }
            if (bLamp) {
                this.zzLampOnOff(MXCommand.LED_GREEN_LIGHT, MXCommand.LED_OFF);
            }
            if (this.m_bCancelGetImage) {
                if (bSensor) {
                    this.zzStopFPC();
                }
                this.m_usbBase.closeDev();
                iRet = -2;
                return iRet;
            }
            if (duration > iTimeOut) {
                if (bSensor) {
                    this.zzStopFPC();
                }
                this.m_usbBase.closeDev();
                iRet = -3;
                return iRet;
            }
        }
        if (bSensor) {
            this.zzStopFPC();
        }
        MXLog.SendMsg("\u5173\u95ed\u8bbe\u5907");
        this.m_usbBase.closeDev();
        return 0;
    }
    
    public int mxDevUpdate(final byte[] bCodbuffer, final int codbufferlen) {
        int iRet = 0;
        final byte[] checkStr = { 77, 46, 73, 46, 88, 46 };
        final byte[] checkcodStr = { 77, 105, 97, 120, 105, 115, 73, 68 };
        for (int i = 0; i < 8; ++i) {
            if (bCodbuffer[i] != checkcodStr[i]) {
                return -14;
            }
        }
        final byte bCmd = MXCommand.CMD_DEV_UPDATE;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int transsize;
        final int iSendPackageSize = transsize = this.m_usbBase.sendPacketSize();
        final int packsize = transsize - 16;
        int packnum = 0;
        int lasepacksize = 0;
        packnum = (codbufferlen - 8) / packsize;
        lasepacksize = (codbufferlen - 8) % packsize;
        if (lasepacksize != 0) {
            ++packnum;
        }
        for (int j = 0; j < packnum; ++j) {
            int iSendLen = 0;
            final byte[] bSendBuf = new byte[iSendPackageSize];
            final short num = (short)(j + 1);
            int offsize = 0;
            bSendBuf[offsize++] = (byte)(num & 0xFF);
            bSendBuf[offsize++] = (byte)(num >> 8);
            for (int k = 0; k < checkStr.length; ++k) {
                bSendBuf[offsize + k] = checkStr[k];
            }
            offsize += checkStr.length;
            iSendLen = offsize + packsize;
            if (j == packnum - 1) {
                for (int k = 0; k < lasepacksize; ++k) {
                    bSendBuf[offsize + k] = bCodbuffer[8 + j * packsize + k];
                }
            }
            else {
                for (int k = 0; k < packsize; ++k) {
                    bSendBuf[offsize + k] = bCodbuffer[8 + j * packsize + k];
                }
            }
            iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
            if (iRet != 0) {
                this.m_usbBase.closeDev();
                return iRet;
            }
            final byte[] bResult = { 0 };
            final int[] iRecvDataLen = { 0 };
            final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
            final byte[] bRecvBuf = new byte[iRecvPackageSize];
            iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT * 3);
            if (iRet != 0) {
                this.m_usbBase.closeDev();
                return iRet;
            }
        }
        iRet = UsbPacket.sendPacket(this.m_usbBase, MXCommand.CMD_DEV_RESET, new byte[iSendPackageSize], 0);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        Log.i("MX-", "DevUpdate: send reset !");
        final byte[] bResult2 = { 0 };
        final int[] iRecvDataLen2 = { 0 };
        final int iRecvPackageSize2 = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf2 = new byte[iRecvPackageSize2];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult2, bRecvBuf2, iRecvDataLen2, MXCommand.CMD_TIMEOUT * 3);
        this.m_usbBase.closeDev();
        return iRet;
    }
    
    public int mxSetSleepMode() {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_SetSleepMode;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, null, 0);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT * 3);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        this.m_usbBase.closeDev();
        return 0;
    }
    
    public void mxCancelGetImage() {
        this.m_bCancelGetImage = true;
    }
    
    public int zzOpenDev(final int vid, final int pid) {
        return this.m_usbBase.openDev(vid, pid);
    }
    
    public int zzCloseDev() {
        return this.m_usbBase.closeDev();
    }
    
    private int zzLampOnOff(final int iLamp, final int iOnOff) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_LED_CONTROL;
        int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        bSendBuf[iSendLen++] = (byte)iLamp;
        bSendBuf[iSendLen++] = (byte)iOnOff;
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            return iRet;
        }
        return 0;
    }
    
    public int zzStartFPC() {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_START_FPC;
        final int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            return iRet;
        }
        return 0;
    }
    
    public int zzStopFPC() {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_STOP_FPC;
        final int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            return iRet;
        }
        return 0;
    }
    
    public int zzDetectFinger() {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_CHECK_FPC;
        final int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            return iRet;
        }
        if (bResult[0] == 0) {
            return 0;
        }
        return 1;
    }
    
    private int zzReadImage() {
        return 0;
    }
    
    private int zzUpImage(final byte[] bImgBuf, final int iImgLen) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_GET_IMAGE;
        final int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        MXLog.SendMsg("\u53d1\u9001\u6570\u636e\u5305");
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            MXLog.SendMsg("zzUpImage sendPacket iRet=" + iRet);
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        MXLog.SendMsg("\u63a5\u6536\u5e94\u7b54\u5305");
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            MXLog.SendMsg("zzUpImage recvPacket iRet=" + iRet);
            return iRet;
        }
        MXLog.SendMsg("\u63a5\u6536\u6570\u636e\u5305");
        int iLength = 0;
        int i = 0;
        while (i < iImgLen) {
            iLength = iImgLen - i;
            if (iLength > iRecvPackageSize) {
                iLength = iRecvPackageSize;
            }
            iRet = this.m_usbBase.recvData(bRecvBuf, bRecvBuf.length, MXCommand.CMD_TIMEOUT);
            if (iRet < 0) {
                MXLog.SendMsg("zzUpImage iImgLen=" + iImgLen);
                MXLog.SendMsg("zzUpImage iRecvPackageSize=" + iRecvPackageSize);
                MXLog.SendMsg("zzUpImage iLength=" + iLength);
                MXLog.SendMsg("zzUpImage i=" + i);
                MXLog.SendMsg("zzUpImage iRet=" + iRet);
                if (i > 30000) {
                    return 0;
                }
                return iRet;
            }
            else {
                for (int j = 0; j < iLength; ++j) {
                    bImgBuf[i + j] = bRecvBuf[j];
                }
                i += iRecvPackageSize;
            }
        }
        return 0;
    }
    
    private int zzUpImageAndroid(final byte[] bImgBuf, final int[] iImgWidth, final int[] iImgHeight, final boolean bHandshake, final byte[] imgdesc) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_GET_IMAGE_ANDROID;
        final int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        MXLog.SendMsg("\u53d1\u9001\u6570\u636e\u5305");
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            MXLog.SendMsg("zzUpImage sendPacket iRet=" + iRet);
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        MXLog.SendMsg("\u63a5\u6536\u5e94\u7b54\u5305");
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            MXLog.SendMsg("zzUpImage recvPacket iRet=" + iRet);
            return iRet;
        }
        int a = 0;
        int b = 0;
        a = JUnsigned(bRecvBuf[0]);
        b = JUnsigned(bRecvBuf[1]);
        int iPackCount = b * 256 + a;
        a = JUnsigned(bRecvBuf[2]);
        b = JUnsigned(bRecvBuf[3]);
        iImgWidth[0] = b * 256 + a;
        a = JUnsigned(bRecvBuf[4]);
        b = JUnsigned(bRecvBuf[5]);
        iImgHeight[0] = b * 256 + a;
        MXLog.SendMsg("\u5305\u5927\u5c0f\uff1a" + iRecvPackageSize);
        MXLog.SendMsg("\u5305\u6570:" + iPackCount);
        MXLog.SendMsg("\u5bbd\u5ea6:" + iImgWidth[0]);
        MXLog.SendMsg("\u9ad8\u5ea6:" + iImgHeight[0]);
        iPackCount = iImgWidth[0] * iImgHeight[0] / (iRecvPackageSize - 2);
        if (iImgWidth[0] * iImgHeight[0] % (iRecvPackageSize - 2) != 0) {
            ++iPackCount;
        }
        MXLog.SendMsg("\u5305\u6570:" + iPackCount);
        final byte[] bImgBufTmp = new byte[iImgWidth[0] * iImgHeight[0] + iSendPackageSize * 2];
        for (int i = 0; i < imgdesc.length; ++i) {
            imgdesc[i] = bRecvBuf[i + 6];
        }
        final int[] iPackNumArray = new int[iPackCount];
        final int[] iPackLenArray = new int[iPackCount];
        for (int j = 0; j < iPackNumArray.length; ++j) {
            iPackLenArray[j] = (iPackNumArray[j] = 0);
        }
        MXLog.SendMsg("\u63a5\u6536\u6570\u636e\u5305");
        int j = 0;
        int iLength = 0;
        for (j = 0; j < iPackCount; ++j) {
            iLength = iRecvPackageSize;
            if (j == iPackCount - 1) {
                iLength = iImgWidth[0] * iImgHeight[0] - j * (iRecvPackageSize - 2) + 2;
            }
            iRet = this.m_usbBase.recvData(bRecvBuf, bRecvBuf.length, MXCommand.CMD_TIMEOUT);
            if (iRet < 0) {
                break;
            }
            if (bHandshake && (j + 1) % 32 == 0) {
                MXLog.SendMsg("\u4e0e\u8bbe\u5907\u63e1\u624b\u6307\u4ee4");
                final byte[] bSendBufTmp = new byte[iSendPackageSize];
                bSendBufTmp[0] = -120;
                bSendBufTmp[1] = 1;
                bSendBufTmp[2] = 2;
                bSendBufTmp[3] = 1;
                bSendBufTmp[4] = 0;
                bSendBufTmp[5] = 1;
                bSendBufTmp[6] = 2;
                bSendBufTmp[7] = 0;
                final int ret = this.m_usbBase.sendData(bSendBufTmp, bSendBufTmp.length, 10);
                if (ret < 0) {
                    MXLog.SendMsg("\u4e0e\u8bbe\u5907\u63e1\u624b\u6307\u4ee4\u5931\u8d25\uff0ciRet=" + ret);
                    break;
                }
            }
            a = JUnsigned(bRecvBuf[0]);
            b = JUnsigned(bRecvBuf[1]);
            iPackNumArray[j] = b * 256 + a;
            iPackLenArray[j] = iLength - 2;
            final int iDstPos = iPackNumArray[j] * (iRecvPackageSize - 2);
            System.arraycopy(bRecvBuf, 2, bImgBufTmp, iDstPos, iLength - 2);
        }
        String strTmp = "\u6536\u5230\u5305\u5e8f\u53f7\uff1a";
        for (int k = 0; k < iPackCount; ++k) {
            strTmp = strTmp + "[" + iPackNumArray[k] + "] ";
        }
        MXLog.SendMsg(strTmp);
        if (iRet < 0) {
            MXLog.SendMsg("zzUpImage iRecvPackageSize=" + iRecvPackageSize);
            MXLog.SendMsg("zzUpImage i=" + j);
            MXLog.SendMsg("zzUpImage iLength=" + iLength);
            MXLog.SendMsg("zzUpImage iRet=" + iRet);
            int k = 0;
            int iLossPackNum = 0;
            final int[] iLossPackNumArray = new int[iPackCount - j];
            for (int l = 0; l < j; ++l) {
                if (iPackNumArray[k] != l) {
                    iLossPackNumArray[iLossPackNum] = l;
                    ++iLossPackNum;
                }
                else {
                    ++k;
                }
            }
            MXLog.SendMsg("\u4e22\u5305\u4e2a\u6570=" + iLossPackNum);
            strTmp = "\u4e22\u5305\u5e8f\u53f7\uff1a\r\n";
            for (k = 0; k < iLossPackNum; ++k) {
                strTmp = strTmp + "[" + iLossPackNumArray[k] + "] ";
            }
            if (iLossPackNum > 20) {
                return -20;
            }
            for (k = 0; k < iLossPackNum; ++k) {
                iRet = this.zzUpPackAndroid(bRecvBuf, iLossPackNumArray[k]);
                if (iRet != 0) {
                    break;
                }
                iLength = iRecvPackageSize;
                if (iLossPackNumArray[k] == iPackCount - 1) {
                    iLength = iImgWidth[0] * iImgHeight[0] - j * (iRecvPackageSize - 2);
                }
                final int iDstPos2 = iLossPackNumArray[k] * (iRecvPackageSize - 2);
                System.arraycopy(bRecvBuf, 0, bImgBufTmp, iDstPos2, iLength);
            }
        }
        System.arraycopy(bImgBufTmp, 0, bImgBuf, 0, iImgWidth[0] * iImgHeight[0]);
        return 0;
    }
    
    private int zzUpPackAndroid(final byte[] bDataBuf, final int iPackNum) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_GET_PACK_ANDROID;
        final int iSendLen = 2;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        bSendBuf[0] = (byte)iPackNum;
        bSendBuf[1] = (byte)(iPackNum >> 8);
        MXLog.SendMsg("\u83b7\u53d6\u7b2c[" + iPackNum + "]\u5305\u56fe\u50cf\u6570\u636e");
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            MXLog.SendMsg("zzUpPackAndroid sendPacket iRet=" + iRet);
            return iRet;
        }
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = this.m_usbBase.recvData(bRecvBuf, bRecvBuf.length, 100);
        if (iRet < 0) {
            return iRet;
        }
        final int a = JUnsigned(bRecvBuf[0]);
        final int b = JUnsigned(bRecvBuf[1]);
        MXLog.SendMsg("iRet=" + iRet);
        MXLog.SendMsg("\u6536\u5230\u5305\u5e8f\u53f7=" + (b * 256 + a));
        if (iPackNum != b * 256 + a) {
            MXLog.SendMsg(zzStringTrans.hex2str(bRecvBuf));
            return 88;
        }
        System.arraycopy(bRecvBuf, 2, bDataBuf, 0, iRet - 2);
        return 0;
    }
    
    public int zzUpAutoImage(final byte[] bImgBuf, final int[] iImgW, final int[] iImgH, final Boolean bOpenClose, final byte[] imgdesc) {
        int iRet = 0;
        byte bCmd = MXCommand.CMD_GET_AUTO_IMAGE;
        if (bOpenClose) {
            bCmd = MXCommand.CMD_GET_AUTO_IMAGE_OC;
        }
        final int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            MXLog.SendMsg("sendPacket=" + iRet);
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        Calendar time1 = Calendar.getInstance();
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        Calendar time2 = Calendar.getInstance();
        long bt_time = time2.getTimeInMillis() - time1.getTimeInMillis();
        MXLog.SendMsg("\u63a5\u6536\u5e94\u7b54\u5305\u8017\u65f6\uff1a" + bt_time);
        if (iRet != 0) {
            MXLog.SendMsg("recvPacket=" + iRet);
            return iRet;
        }
        int a = bRecvBuf[2];
        if (a < 0) {
            a += 256;
        }
        int b = bRecvBuf[3];
        if (b < 0) {
            b += 256;
        }
        final int wImgW = b * 256 + a;
        iImgW[0] = wImgW;
        a = bRecvBuf[4];
        if (a < 0) {
            a += 256;
        }
        b = bRecvBuf[5];
        if (b < 0) {
            b += 256;
        }
        final int wImgH = b * 256 + a;
        iImgH[0] = wImgH;
        for (int i = 0; i < imgdesc.length; ++i) {
            imgdesc[i] = bRecvBuf[i + 6];
        }
        final int reallen = wImgW * wImgH;
        int iLength = 0;
        time1 = Calendar.getInstance();
        for (int j = 0; j < reallen; j += iRecvPackageSize) {
            iLength = reallen - j;
            if (iLength > iRecvPackageSize) {
                iLength = iRecvPackageSize;
            }
            iRet = this.m_usbBase.recvData(bRecvBuf, bRecvBuf.length, MXCommand.CMD_TIMEOUT);
            if (iRet < 0) {
                return iRet;
            }
            for (int k = 0; k < iLength; ++k) {
                bImgBuf[j + k] = bRecvBuf[k];
            }
        }
        time2 = Calendar.getInstance();
        bt_time = time2.getTimeInMillis() - time1.getTimeInMillis();
        MXLog.SendMsg("\u63a5\u6536\u6570\u636e\u5305\u8017\u65f6\uff1a" + bt_time);
        return 0;
    }
    
    public int mxplayvoice(final int iVoiceCode1, final int iVoiceCode2) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_PLAY_VOICE;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        bSendBuf[0] = (byte)iVoiceCode1;
        bSendBuf[1] = (byte)iVoiceCode2;
        iSendLen = 2;
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        if (bResult[0] != 0) {
            this.m_usbBase.closeDev();
            return bResult[0];
        }
        this.m_usbBase.closeDev();
        return 0;
    }
    
    public void unRegUsbMonitor() {
        this.m_usbBase.unRegUsbMonitor();
    }
    
    public static int JUnsigned(final int x) {
        if (x >= 0) {
            return x;
        }
        return x + 256;
    }
    
    public int ComposePackNumPackageZX(final short SendBufLen, final byte PackNum, final byte[] bComDataPack, final int bComDataPackLen, final byte[] bSendDataPack) {
        final byte[] bytesPublicKeyLen = UsbPacket.SignedShortToByte(SendBufLen);
        bSendDataPack[0] = bytesPublicKeyLen[0];
        bSendDataPack[1] = bytesPublicKeyLen[1];
        bSendDataPack[2] = PackNum;
        System.arraycopy(bComDataPack, 0, bSendDataPack, 3, bComDataPackLen);
        return bComDataPackLen + 3;
    }
    
    public int SendMultiPackWithCommandZX(final UsbBase usbBase, final byte bCmd, final byte[] bSendBuf, final int iDataLen) {
        int lRV = 0;
        final byte[] TempBuff = new byte[1024];
        final byte[] bSendDataPack = new byte[1024];
        byte PackNum = 0;
        for (int wSendLen = this.m_usbBase.sendPacketSize() - 8 - 2 - 1, i = 0; i < iDataLen; i += wSendLen) {
            if (iDataLen - i < wSendLen) {
                wSendLen = iDataLen - i;
            }
            System.arraycopy(bSendBuf, i, TempBuff, 0, wSendLen);
            final int Len = this.ComposePackNumPackageZX((short)iDataLen, PackNum, TempBuff, wSendLen, bSendDataPack);
            lRV = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendDataPack, Len);
            if (0 != lRV) {
                return lRV;
            }
            ++PackNum;
        }
        return 0;
    }
    
    public int mxDownRSAPublicKeyZX(final byte[] iPublicKey, final short iPublicKeyLen) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_RSA_DOWN;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendLen = 3 + iPublicKeyLen;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[1024];
        final byte[] bytesPublicKeyLen = UsbPacket.SignedShortToByte(iPublicKeyLen);
        bSendBuf[0] = bytesPublicKeyLen[0];
        bSendBuf[1] = bytesPublicKeyLen[1];
        bSendBuf[2] = 0;
        for (int i = 0; i < iPublicKeyLen; ++i) {
            bSendBuf[i + 3] = iPublicKey[i];
        }
        if (iSendPackageSize > 100) {
            iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
            if (iRet != 0) {
                this.m_usbBase.closeDev();
                return iRet;
            }
        }
        else {
            iRet = this.SendMultiPackWithCommandZX(this.m_usbBase, bCmd, iPublicKey, iPublicKeyLen);
            if (iRet != 0) {
                this.m_usbBase.closeDev();
                return iRet;
            }
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        if (bResult[0] != 0) {
            this.m_usbBase.closeDev();
            return bResult[0];
        }
        this.m_usbBase.closeDev();
        return 0;
    }
    
    int detectFinger(final int iTimeOut) {
        int iRet;
        long duration;
        Calendar time1;
        Calendar time2;
        for (iRet = -1, duration = -1L, time1 = Calendar.getInstance(), iRet = this.zzDetectFinger(); iRet == 1 && duration <= iTimeOut && !this.m_bCancelGetImage; duration = time2.getTimeInMillis() - time1.getTimeInMillis()) {
            iRet = this.zzDetectFinger();
            SystemClock.sleep(10L);
            time2 = Calendar.getInstance();
            if (0 != iTimeOut) {}
        }
        if (this.m_bCancelGetImage) {
            iRet = -2;
            return iRet;
        }
        if (duration > iTimeOut) {
            iRet = -3;
            return iRet;
        }
        return 0;
    }
    
    public int mxGetDevTzZX(final int iTimeOut, final int iType, final byte[] bTzBuf) {
        int iRet = 0;
        MXLog.SendMsg("\u6253\u5f00\u8bbe\u5907");
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        iRet = this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_ON);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        iRet = this.zzStartFPC();
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        this.m_bCancelGetImage = false;
        MXLog.SendMsg("\u63a2\u6d4b\u624b\u6307");
        iRet = this.detectFinger(iTimeOut);
        if (iRet != 0) {
            this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            this.zzStopFPC();
            this.m_usbBase.closeDev();
            return iRet;
        }
        MXLog.SendMsg("\u751f\u6210\u7279\u5f81\u5e76\u4e0a\u4f20");
        iRet = this.zxGenAndUpTz(iType, bTzBuf);
        if (0 != iRet) {
            MXLog.SendMsg("\u7ea2\u706f\u706d");
            iRet = this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            MXLog.SendMsg("\u5173\u95edFPC\u4f20\u611f\u5668");
            iRet = this.zzStopFPC();
            MXLog.SendMsg("\u5173\u95ed\u8bbe\u5907");
            iRet = this.m_usbBase.closeDev();
            iRet = -5;
            return iRet;
        }
        this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
        this.zzStopFPC();
        MXLog.SendMsg("\u5173\u95ed\u8bbe\u5907");
        this.m_usbBase.closeDev();
        return 0;
    }
    
    private int zxGenAndUpTz(final int iType, final byte[] bTzBuf) {
        int iRet = 0;
        byte bCmd = MXCommand.CMD_ZX_GET_TZ;
        if (iType == 1) {
            bCmd = MXCommand.CMD_ZX_GET_ENCRY_TZ;
        }
        final int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        MXLog.SendMsg("\u53d1\u9001\u6570\u636e\u5305");
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            MXLog.SendMsg("zzUpImage sendPacket iRet=" + iRet);
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        MXLog.SendMsg("\u63a5\u6536\u5e94\u7b54\u5305");
        int TotleLen = 0;
        final byte[] TempBuff = new byte[1024];
        if (iRecvPackageSize <= 100) {
            for (int i = 0; i < 8; ++i) {
                iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT * 2);
                if (0 != iRet) {
                    return iRet;
                }
                if (bResult[0] != 0) {
                    return bResult[0];
                }
                System.arraycopy(bRecvBuf, 2, TempBuff, TotleLen, iRecvDataLen[0] - 2);
                TotleLen = TotleLen + iRecvDataLen[0] - 2;
            }
            System.arraycopy(TempBuff, 0, bTzBuf, 0, TotleLen);
            return 0;
        }
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT * 2);
        if (iRet != 0) {
            MXLog.SendMsg("zxUpTz recvPacket iRet=" + iRet);
            return iRet;
        }
        if (iRecvDataLen[0] != 258) {
            MXLog.SendMsg("zxUpTz iRecvDataLen=" + iRecvDataLen[0]);
            return -2000;
        }
        for (int i = 0; i < iRecvDataLen[0] - 2; ++i) {
            bTzBuf[i] = bRecvBuf[i + 2];
        }
        return 0;
    }
    
    public int zxGenTz(final short BufferID) {
        final short FingerNum = 11;
        final byte[] bytesFingerNum = UsbPacket.SignedShortToByte(FingerNum);
        final byte[] bytesPageID = UsbPacket.SignedShortToByte(BufferID);
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_ZX_GEN_TZ;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        final int iSendLen = 4;
        bSendBuf[0] = bytesFingerNum[0];
        bSendBuf[1] = bytesFingerNum[1];
        bSendBuf[2] = bytesPageID[0];
        bSendBuf[3] = bytesPageID[1];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT * 5);
        if (iRet != 0) {
            return iRet;
        }
        return 0;
    }
    
    private int zxMergeMbAndUp(final int iType, final byte[] bTzBuf) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_ZX_GET_MB;
        final int iSendLen = 1;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        bSendBuf[0] = (byte)iType;
        MXLog.SendMsg("\u53d1\u9001\u6570\u636e\u5305");
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            MXLog.SendMsg("zxMergeMbAndUp sendPacket iRet=" + iRet);
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        int TotleLen = 0;
        final byte[] TempBuff = new byte[1024];
        MXLog.SendMsg("\u63a5\u6536\u5e94\u7b54\u5305");
        if (iRecvPackageSize <= 100) {
            for (int i = 0; i < 8; ++i) {
                iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT * 2);
                if (0 != iRet) {
                    return iRet;
                }
                if (bResult[0] != 0) {
                    return bResult[0];
                }
                System.arraycopy(bRecvBuf, 2, TempBuff, TotleLen, iRecvDataLen[0] - 2);
                TotleLen = TotleLen + iRecvDataLen[0] - 2;
            }
            System.arraycopy(TempBuff, 0, bTzBuf, 0, TotleLen);
            return 0;
        }
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT * 2);
        if (iRet != 0) {
            MXLog.SendMsg("zxMergeMbAndUp recvPacket iRet=" + iRet);
            return iRet;
        }
        if (iRecvDataLen[0] != 258) {
            MXLog.SendMsg("zxMergeMbAndUp iRecvDataLen=" + iRecvDataLen[0]);
            return -2000;
        }
        for (int i = 0; i < iRecvDataLen[0] - 2; ++i) {
            bTzBuf[i] = bRecvBuf[i + 2];
        }
        return 0;
    }
    
    public int mxGetDevMbZX(final int iTimeOut, final int iType, final byte[] bMbBuf) {
        int iRet = 0;
        MXLog.SendMsg("\u6253\u5f00\u8bbe\u5907");
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        iRet = this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_ON);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        iRet = this.zzStartFPC();
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        this.m_bCancelGetImage = false;
        for (int i = 0; i < 3; ++i) {
            MXLog.SendMsg("\u7b2c" + (i + 1) + "\u6b21\u63a2\u6d4b\u624b\u6307");
            do {
                iRet = this.detectFinger(iTimeOut);
            } while (iRet != 0);
            MXLog.SendMsg("\u7b2c" + (i + 1) + "\u6b21\u751f\u6210\u7279\u5f81");
            iRet = this.zxGenTz((short)i);
            if (0 != iRet) {
                MXLog.SendMsg("\u7ea2\u706f\u706d");
                iRet = this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
                MXLog.SendMsg("\u5173\u95edFPC\u4f20\u611f\u5668");
                iRet = this.zzStopFPC();
                MXLog.SendMsg("\u5173\u95ed\u8bbe\u5907");
                iRet = this.m_usbBase.closeDev();
                iRet = -5;
                return iRet;
            }
        }
        MXLog.SendMsg("\u5408\u5e76\u6a21\u677f\u5e76\u4e0a\u4f20");
        iRet = this.zxMergeMbAndUp(iType, bMbBuf);
        if (0 != iRet) {
            MXLog.SendMsg("\u7ea2\u706f\u706d");
            iRet = this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
            MXLog.SendMsg("\u5173\u95edFPC\u4f20\u611f\u5668");
            iRet = this.zzStopFPC();
            MXLog.SendMsg("\u5173\u95ed\u8bbe\u5907");
            iRet = this.m_usbBase.closeDev();
            iRet = -5;
            return iRet;
        }
        this.zzLampOnOff(MXCommand.LED_RED_LIGHT, MXCommand.LED_OFF);
        this.zzStopFPC();
        MXLog.SendMsg("\u5173\u95ed\u8bbe\u5907");
        this.m_usbBase.closeDev();
        return 0;
    }
}
