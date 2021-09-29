package org.zz.mxhidfingerdriver;

import org.zz.protocol.*;
import java.util.*;

public class UsbPacket
{
    public static int sendPacket(final UsbBase usbBase, final byte bCmd, final byte[] bSendBuf, final int iDataLen) {
        int iRet = -1;
        int offsize = 0;
        short iCheckSum = 0;
        final int iSendPackageSize = usbBase.sendPacketSize();
        final byte[] DataBuffer = new byte[iSendPackageSize];
        DataBuffer[offsize++] = MXCommand.CMD_REQ_FLAG;
        DataBuffer[offsize++] = 0;
        DataBuffer[offsize++] = 0;
        DataBuffer[offsize++] = (byte)(iDataLen + 1 & 0xFF);
        DataBuffer[offsize++] = (byte)(iDataLen + 1 >> 8);
        DataBuffer[offsize++] = bCmd;
        if (iDataLen > 0) {
            for (int i = 0; i < iDataLen; ++i) {
                DataBuffer[offsize++] = bSendBuf[i];
            }
        }
        for (int i = 3; i < offsize; ++i) {
            final int iTmp = DataBuffer[i];
            iCheckSum += (short)JUnsigned(iTmp);
        }
        DataBuffer[offsize++] = (byte)(iCheckSum & 0xFF);
        DataBuffer[offsize++] = (byte)(iCheckSum >> 8);
        usbBase.clearBuffer();
        iRet = usbBase.sendData(DataBuffer, DataBuffer.length, MXCommand.CMD_TIMEOUT);
        if (iRet < 0) {
            MXLog.SendMsg("sendData=" + iRet);
            return -1;
        }
        return 0;
    }
    
    public static int recvPacket(final UsbBase usbBase, final byte[] bResult, final byte[] bRecvBuf, final int[] iRecvDataLen, final int iTimeOut) {
        int iRet = -1;
        int offsize = 0;
        int iDataLen = 0;
        int a = 0;
        int b = 0;
        final int iRecvPackageSize = usbBase.recvPacketSize();
        final byte[] DataBuffer = new byte[iRecvPackageSize];
        final byte[] SRN = new byte[2];
        short recvCheckSum = 0;
        short currentCheckSum = 0;
        iRet = usbBase.recvData(DataBuffer, DataBuffer.length, iTimeOut);
        if (iRet < 0) {
            MXLog.SendMsg("recvData=" + iRet);
            return iRet;
        }
        if (DataBuffer[offsize++] != MXCommand.CMD_RET_FLAG) {
            return -12;
        }
        SRN[0] = DataBuffer[offsize++];
        SRN[1] = DataBuffer[offsize++];
        a = DataBuffer[offsize++];
        if (a < 0) {
            a += 256;
        }
        b = DataBuffer[offsize++];
        if (b < 0) {
            b += 256;
        }
        iDataLen = b * 256 + a;
        if (iDataLen > iRecvPackageSize - 5) {
            return -15;
        }
        bResult[0] = DataBuffer[offsize];
        iRecvDataLen[0] = iDataLen - 1;
        if (iDataLen - 1 > 0) {
            for (int i = 1; i < iDataLen; ++i) {
                bRecvBuf[i - 1] = DataBuffer[offsize + i];
            }
        }
        offsize += iDataLen;
        for (int i = 3; i < offsize; ++i) {
            a = DataBuffer[i];
            if (a < 0) {
                a += 256;
            }
            currentCheckSum += (short)a;
        }
        a = DataBuffer[offsize++];
        if (a < 0) {
            a += 256;
        }
        b = DataBuffer[offsize++];
        if (b < 0) {
            b += 256;
        }
        recvCheckSum = (short)(b * 256 + a);
        if (currentCheckSum != recvCheckSum) {}
        return 0;
    }
    
    public static int sendMultiPacket(final UsbBase usbBase, final byte bCmd, final byte[] lpData, final int iLength) {
        int iRet = -1;
        final int iSendPackageSize = usbBase.sendPacketSize();
        final byte[] DataBuffer = new byte[iSendPackageSize];
        final int wSendLength = iSendPackageSize - 8 - 2;
        int packnum = iLength / wSendLength;
        int lastpacksize = iLength % wSendLength;
        if (lastpacksize != 0) {
            ++packnum;
        }
        else {
            lastpacksize = wSendLength;
        }
        for (int i = 0; i < packnum; ++i) {
            MXLog.SendMsg("\u53d1\u9001\u7b2c\u3010" + i + "\u3011\u6570\u636e\u5305");
            if (i == packnum - 1) {
                for (int j = 0; j < lastpacksize; ++j) {
                    DataBuffer[j] = lpData[i * wSendLength + j];
                }
                iRet = sendOnePacketWithPacknum(usbBase, bCmd, 65535, DataBuffer, lastpacksize);
            }
            else {
                for (int j = 0; j < wSendLength; ++j) {
                    DataBuffer[j] = lpData[i * wSendLength + j];
                }
                iRet = sendOnePacketWithPacknum(usbBase, bCmd, i, DataBuffer, wSendLength);
            }
            if (iRet != 0) {
                break;
            }
            MySleep(10);
        }
        return iRet;
    }
    
    public static int sendOnePacketWithPacknum(final UsbBase usbBase, final byte bCmd, final int packnum, final byte[] bSendBuf, final int iDataLen) {
        int iRet = -1;
        int offsize = 0;
        short iCheckSum = 0;
        final int iSendPackageSize = usbBase.sendPacketSize();
        final byte[] DataBuffer = new byte[iSendPackageSize];
        DataBuffer[offsize++] = MXCommand.CMD_REQ_FLAG;
        DataBuffer[offsize++] = 0;
        DataBuffer[offsize++] = 0;
        DataBuffer[offsize++] = (byte)(iDataLen + 1 + 2 & 0xFF);
        DataBuffer[offsize++] = (byte)(iDataLen + 1 + 2 >> 8);
        DataBuffer[offsize++] = bCmd;
        final byte[] bpack = SignedShortToByte((short)packnum);
        DataBuffer[offsize++] = bpack[0];
        DataBuffer[offsize++] = bpack[1];
        if (iDataLen > 1) {
            for (int i = 0; i < iDataLen; ++i) {
                DataBuffer[offsize++] = bSendBuf[i];
            }
        }
        for (int i = 3; i < offsize; ++i) {
            final int iTmp = DataBuffer[i];
            iCheckSum += (short)JUnsigned(iTmp);
        }
        DataBuffer[offsize++] = (byte)(iCheckSum & 0xFF);
        DataBuffer[offsize++] = (byte)(iCheckSum >> 8);
        usbBase.clearBuffer();
        MXLog.SendMsg("======\u53d1\u9001\u6570\u636e\u5305======");
        MXLog.SendMsg(hex2str(DataBuffer));
        iRet = usbBase.sendData(DataBuffer, DataBuffer.length, MXCommand.CMD_TIMEOUT);
        if (iRet < 0) {
            return -1;
        }
        return 0;
    }
    
    public static int recvMultiPacket(final UsbBase usbBase, final byte[] lpResult, final byte[] lpData, final int[] lpLength, final int iTimeOut) {
        int iRet = -1;
        int iTotalDataLen = 0;
        final byte[] bResult = { 0 };
        final int[] sPackNum = { 0 };
        final int[] iRecvDataLen = { 0 };
        final int iRecvPackageSize = usbBase.recvPacketSize();
        final byte[] DataBuffer = new byte[iRecvPackageSize];
        while (true) {
            iRet = recvOnePacketWithPacknum(usbBase, bResult, sPackNum, DataBuffer, iRecvDataLen, iTimeOut);
            MXLog.SendMsg("iRet=" + iRet);
            MXLog.SendMsg("bResult=" + bResult[0]);
            MXLog.SendMsg("sPackNum=" + sPackNum[0]);
            MXLog.SendMsg("iRecvDataLen=" + iRecvDataLen[0]);
            if (iRet != 0) {
                break;
            }
            if (bResult[0] != 0) {
                iRet = bResult[0];
                break;
            }
            if (iRecvDataLen[0] > 0) {
                for (int i = 0; i < iRecvDataLen[0]; ++i) {
                    lpData[i + iTotalDataLen] = DataBuffer[i];
                }
                iTotalDataLen += iRecvDataLen[0];
            }
            if (sPackNum[0] == 65535) {
                break;
            }
            MXLog.SendMsg(hex2str(DataBuffer));
        }
        if (iRet == 0) {
            lpLength[0] = iTotalDataLen;
        }
        return iRet;
    }
    
    public static int recvOnePacketWithPacknum(final UsbBase usbBase, final byte[] bResult, final int[] sPackNum, final byte[] bRecvBuf, final int[] iRecvDataLen, final int iTimeOut) {
        int iRet = -1;
        int offsize = 0;
        int iDataLen = 0;
        int a = 0;
        int b = 0;
        final int iRecvPackageSize = usbBase.recvPacketSize();
        final byte[] DataBuffer = new byte[iRecvPackageSize];
        final byte[] SRN = new byte[2];
        short recvCheckSum = 0;
        short currentCheckSum = 0;
        iRet = usbBase.recvData(DataBuffer, DataBuffer.length, iTimeOut);
        if (iRet < 0) {
            MXLog.SendMsg("recvData=" + iRet);
            return iRet;
        }
        MXLog.SendMsg("======\u63a5\u6536\u6570\u636e\u5305======");
        MXLog.SendMsg(hex2str(DataBuffer));
        if (DataBuffer[offsize++] != MXCommand.CMD_RET_FLAG) {
            return -12;
        }
        SRN[0] = DataBuffer[offsize++];
        SRN[1] = DataBuffer[offsize++];
        a = DataBuffer[offsize++];
        if (a < 0) {
            a += 256;
        }
        b = DataBuffer[offsize++];
        if (b < 0) {
            b += 256;
        }
        iDataLen = b * 256 + a;
        if (iDataLen > iRecvPackageSize - 5) {
            return -15;
        }
        bResult[0] = DataBuffer[offsize];
        final byte[] bPack = { DataBuffer[offsize + 1], DataBuffer[offsize + 2] };
        sPackNum[0] = JUnsigned(bPack[1]) * 256 + JUnsigned(bPack[0]);
        iRecvDataLen[0] = iDataLen - 1 - 2;
        if (iDataLen - 1 - 2 > 0) {
            for (int i = 3; i < iDataLen; ++i) {
                bRecvBuf[i - 3] = DataBuffer[offsize + i];
            }
        }
        offsize += iDataLen;
        for (int i = 3; i < offsize; ++i) {
            a = DataBuffer[i];
            if (a < 0) {
                a += 256;
            }
            currentCheckSum += (short)a;
        }
        a = DataBuffer[offsize++];
        if (a < 0) {
            a += 256;
        }
        b = DataBuffer[offsize++];
        if (b < 0) {
            b += 256;
        }
        recvCheckSum = (short)(b * 256 + a);
        if (currentCheckSum != recvCheckSum) {
            return -14;
        }
        return 0;
    }
    
    public static String hex2str(final byte[] hex) {
        final StringBuilder sb = new StringBuilder();
        for (final byte b : hex) {
            sb.append(String.format("%02x ", b));
        }
        return sb.toString();
    }
    
    private static int JUnsigned(final int x) {
        if (x >= 0) {
            return x;
        }
        return x + 256;
    }
    
    public static void MySleep(final int iTimeout) {
        long duration = -1L;
        Calendar time2;
        for (Calendar time1 = Calendar.getInstance(); duration <= iTimeout; duration = time2.getTimeInMillis() - time1.getTimeInMillis()) {
            time2 = Calendar.getInstance();
        }
    }
    
    public static byte[] SignedIntToByte(final int test) {
        final byte[] bytes = { (byte)test, (byte)(test >> 8), (byte)(test >> 16), (byte)(test >> 24) };
        return bytes;
    }
    
    public static byte[] SignedShortToByte(final short test) {
        final byte[] bytes = { (byte)test, (byte)(test >> 8) };
        return bytes;
    }
}
