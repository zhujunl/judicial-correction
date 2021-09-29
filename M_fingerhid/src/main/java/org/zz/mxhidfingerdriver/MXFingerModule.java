package org.zz.mxhidfingerdriver;

import android.content.*;
import android.os.*;
import org.zz.protocol.*;
import org.zz.tool.*;
import android.util.*;
import java.util.*;
import java.nio.charset.*;

public class MXFingerModule
{
    private UsbBase m_usbBase;
    private static final String TAG = "MXFingerModule";
    
    public MXFingerModule(final Context context) {
        this.m_usbBase = new UsbBase(context);
        this.mxSetPVID(514, 33307);
    }
    
    public MXFingerModule(final Context context, final Handler bioHandler) {
        MXLog.SetHandler(bioHandler);
        this.m_usbBase = new UsbBase(context, bioHandler);
        this.mxSetPVID(514, 33307);
    }
    
    public void unRegUsbMonitor() {
        this.m_usbBase.unRegUsbMonitor();
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
    
    public int mxGetImageArea(final int[] iArea) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_82_IMAGE;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        final int iSendLen = 0;
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
        iArea[0] = bRecvBuf[0];
        return 0;
    }
    
    public int mxGenTz(final short iFlag, final short PageID) {
        final byte[] bytesFlag = UsbPacket.SignedShortToByte(iFlag);
        final byte[] bytesPageID = UsbPacket.SignedShortToByte(PageID);
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_82_GENTZ;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        final int iSendLen = 4;
        bSendBuf[0] = bytesFlag[0];
        bSendBuf[1] = bytesFlag[1];
        bSendBuf[2] = bytesPageID[0];
        bSendBuf[3] = bytesPageID[1];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT * 5);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        this.m_usbBase.closeDev();
        return 0;
    }
    
    public int mxUpTz(final short iFlag, final short PageID, final byte[] TzBuffer, final int[] ioTzLen) {
        final byte[] bytesFlag = UsbPacket.SignedShortToByte(iFlag);
        final byte[] bytesPageID = UsbPacket.SignedShortToByte(PageID);
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_82_UPTZ;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        final int iSendLen = 4;
        bSendBuf[0] = bytesFlag[0];
        bSendBuf[1] = bytesFlag[1];
        bSendBuf[2] = bytesPageID[0];
        bSendBuf[3] = bytesPageID[1];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT * 5);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        iRet = UsbPacket.recvMultiPacket(this.m_usbBase, bResult, TzBuffer, ioTzLen, MXCommand.CMD_TIMEOUT);
        this.m_usbBase.closeDev();
        return 0;
    }
    
    public int mxDownTz(final short iFlag, final short PageID, final byte[] TzBuffer, final int iTzLen) {
        final byte[] bytesFlag = UsbPacket.SignedShortToByte(iFlag);
        final byte[] bytesPageID = UsbPacket.SignedShortToByte(PageID);
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_82_DOWNTZ;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        final int iSendLen = 4;
        bSendBuf[0] = bytesFlag[0];
        bSendBuf[1] = bytesFlag[1];
        bSendBuf[2] = bytesPageID[0];
        bSendBuf[3] = bytesPageID[1];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, iSendLen);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] bRecvBuf = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT * 5);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        iRet = UsbPacket.sendMultiPacket(this.m_usbBase, MXCommand.CMD_82_DOWNDATA, TzBuffer, iTzLen);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final int[] sPackNum = { 0 };
        iRet = UsbPacket.recvOnePacketWithPacknum(this.m_usbBase, bResult, sPackNum, bRecvBuf, iRecvDataLen, MXCommand.CMD_TIMEOUT);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        this.m_usbBase.closeDev();
        return 0;
    }
    
    public int mxMatchTz(final short iFlagA, final short PageIDA, final short iFlagB, final short PageIDB, final int[] oMatchResult) {
        final byte[] bytesFlagA = UsbPacket.SignedShortToByte(iFlagA);
        final byte[] bytesPageIDA = UsbPacket.SignedShortToByte(PageIDA);
        final byte[] bytesFlagB = UsbPacket.SignedShortToByte(iFlagB);
        final byte[] bytesPageIDB = UsbPacket.SignedShortToByte(PageIDB);
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_82_MATCH;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        final int iSendLen = 8;
        bSendBuf[0] = bytesFlagA[0];
        bSendBuf[1] = bytesFlagA[1];
        bSendBuf[2] = bytesPageIDA[0];
        bSendBuf[3] = bytesPageIDA[1];
        bSendBuf[4] = bytesFlagB[0];
        bSendBuf[5] = bytesFlagB[1];
        bSendBuf[6] = bytesPageIDB[0];
        bSendBuf[7] = bytesPageIDB[1];
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
        oMatchResult[0] = bRecvBuf[0];
        return 0;
    }
    
    public int mxGetDevTzCapacity(final int[] oNum) {
        int iRet = 0;
        final byte bCmd = MXCommand.CMD_82_COUNT;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        final int iSendLen = 0;
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
        oNum[0] = bRecvBuf[0];
        return 0;
    }
    
    public int mxReadCard(final int[] resultType, final byte[] idCardBytes) {
        this.mxSetPVID(7, 4292);
        final byte bCmd = 21;
        int iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, 0);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        final byte[] bResult = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = this.m_usbBase.recvPacketSize();
        final byte[] tempBuffer = new byte[iRecvPackageSize];
        iRet = UsbPacket.recvPacket(this.m_usbBase, bResult, tempBuffer, iRecvDataLen, MXCommand.CMD_ID_DOOR_CONTROL_TIMEOUT);
        if (iRet != 0) {
            this.m_usbBase.closeDev();
            return iRet;
        }
        Log.v("MXFingerModule", "mxReadCard: iRecvDataLen =[" + iRecvDataLen[0] + "],Buffer[" + zzStringTrans.hex2str(tempBuffer) + "]");
        if (iRecvDataLen[0] < 2) {
            this.m_usbBase.closeDev();
            return -21;
        }
        final int packageCount = tempBuffer[0];
        final int type = tempBuffer[1];
        resultType[0] = type;
        if (type == 1 || type == 2) {
            System.arraycopy(tempBuffer, 2, idCardBytes, 0, iRecvDataLen[0] - 2);
            this.m_usbBase.closeDev();
            return 0;
        }
        Log.i("MXFingerModule", "mxReadCard: packageCount =[" + packageCount + "],type=[" + type + "],id=[" + new String(tempBuffer) + "]");
        Log.i("MXFingerModule", "mxReadCard: " + zzStringTrans.hex2str(tempBuffer));
        int idCardIndex = 24;
        System.arraycopy(tempBuffer, 2, idCardBytes, 0, 24);
        for (int i = 1; i < 21; ++i) {
            iRet = this.m_usbBase.recvData(tempBuffer, tempBuffer.length, MXCommand.CMD_ID_DOOR_CONTROL_TIMEOUT);
            Log.i("MXFingerModule", String.format("recvData: index[%s],result[%s],data[%s]", i, iRet, zzStringTrans.hex2str(tempBuffer)));
            if (iRet != 64 || i != tempBuffer[0]) {
                break;
            }
            System.arraycopy(tempBuffer, 1, idCardBytes, idCardIndex, 63);
            idCardIndex += 63;
        }
        Log.v("MXFingerModule", String.format("mxReadCard: length[%s],data[%s]\n", idCardIndex + 1, zzStringTrans.hex2str(idCardBytes)));
        Log.i("MXFingerModule", "mxReadCard: " + new String(hexH2L(Arrays.copyOf(idCardBytes, 256)), Charset.forName("UTF-16")));
        Log.i("MXFingerModule", "mxReadCard: " + new String(Arrays.copyOf(idCardBytes, 256), Charset.forName("UTF-16")));
        return 0;
    }
    
    public int mxCheckControlDoor(final int type, final int result, final int openTime, final byte[] IDInfo) {
        this.mxSetPVID(7, 4292);
        int iRet = 0;
        final byte bCmd = 22;
        iRet = this.m_usbBase.openDev(MXCommand.VENDORID, MXCommand.PRODUCTID);
        if (iRet != 0) {
            return iRet;
        }
        final int iSendLen = 0;
        final int iSendPackageSize = this.m_usbBase.sendPacketSize();
        final byte[] bSendBuf = new byte[iSendPackageSize];
        bSendBuf[0] = (byte)type;
        bSendBuf[1] = (byte)result;
        bSendBuf[2] = (byte)openTime;
        if (IDInfo.length > 20) {
            return -15;
        }
        System.arraycopy(IDInfo, 0, bSendBuf, 3, IDInfo.length);
        iRet = UsbPacket.sendPacket(this.m_usbBase, bCmd, bSendBuf, 3 + IDInfo.length);
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
    
    public static byte[] hexH2L(final byte[] bytes) {
        byte temp = 0;
        for (int i = 0; i < bytes.length; ++i) {
            if (i % 2 == 0) {
                temp = bytes[i];
                bytes[i] = bytes[i + 1];
            }
            else {
                bytes[i] = temp;
            }
        }
        return bytes;
    }
}
