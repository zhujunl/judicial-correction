package org.zz.tool;

import java.util.*;

public class ImgFormatTrans
{
    static int FPICryptMAC(final byte[] psKey, final byte[] psRandom, final byte[] psInput, final int nInLen, final byte[] psOutput, final int[] pnOutLen) {
        final byte[] keyData = new byte[16];
        final byte[] random = new byte[8];
        final byte[] input = new byte[nInLen];
        final byte[] output = new byte[8];
        final byte[] buf = new byte[8];
        Arrays.fill(keyData, (byte)0);
        Arrays.fill(random, (byte)0);
        Arrays.fill(input, (byte)0);
        Arrays.fill(output, (byte)0);
        System.arraycopy(psKey, 0, keyData, 0, 16);
        System.arraycopy(psRandom, 0, random, 0, 8);
        System.arraycopy(psInput, 0, input, 0, nInLen);
        final int nLength = nInLen;
        int blockCount = nLength / 8;
        int lastBlockSize = nLength % 8;
        if (lastBlockSize > 0) {
            ++blockCount;
        }
        else {
            lastBlockSize = 8;
        }
        System.arraycopy(random, 0, output, 0, 8);
        for (int i = 0; i < blockCount; ++i) {
            if (i < blockCount - 1) {
                System.arraycopy(input, 8 * i, buf, 0, 8);
            }
            else {
                Arrays.fill(buf, (byte)0);
                System.arraycopy(input, 8 * i, buf, 0, lastBlockSize);
            }
            for (int j = 0; j < 8; ++j) {
                final byte[] array = output;
                final int n = j;
                array[n] ^= buf[j];
            }
        }
        for (int j = 0; j < 8; ++j) {
            final byte[] array2 = output;
            final int n2 = j;
            array2[n2] ^= keyData[j];
        }
        for (int j = 0; j < 8; ++j) {
            final byte[] array3 = output;
            final int n3 = j;
            array3[n3] ^= keyData[j + 8];
        }
        System.arraycopy(output, 0, psOutput, 0, 8);
        pnOutLen[0] = 8;
        return -1;
    }
    
    static void FillImgHead(final byte[] imgData, final int iDpi, final int iWidth, final int iHeight) {
        Arrays.fill(imgData, (byte)0);
        int nLength = 0;
        System.arraycopy("FIR".getBytes(), 0, imgData, 0, 3);
        nLength += 4;
        System.arraycopy("010".getBytes(), 0, imgData, nLength, 3);
        nLength += 4;
        imgData[nLength] = (byte)((iWidth * iHeight + 46) % 256);
        imgData[nLength + 1] = (byte)((iWidth * iHeight + 46) / 256);
        imgData[nLength + 5] = (imgData[nLength + 4] = 0);
        nLength += 6;
        imgData[nLength] = 1;
        imgData[nLength + 1] = 2;
        nLength += 2;
        imgData[nLength] = 1;
        imgData[nLength + 1] = 0;
        nLength += 2;
        imgData[nLength] = 1;
        ++nLength;
        imgData[nLength] = 1;
        ++nLength;
        imgData[nLength] = (byte)(iDpi / 256);
        imgData[nLength + 1] = (byte)(iDpi % 256);
        nLength += 2;
        imgData[nLength] = (byte)(iDpi / 256);
        imgData[nLength + 1] = (byte)(iDpi % 256);
        nLength += 2;
        imgData[nLength] = (byte)(iDpi / 256);
        imgData[nLength + 1] = (byte)(iDpi % 256);
        nLength += 2;
        imgData[nLength] = (byte)(iDpi / 256);
        imgData[nLength + 1] = (byte)(iDpi % 256);
        nLength += 2;
        imgData[nLength] = 8;
        ++nLength;
        imgData[nLength] = 0;
        ++nLength;
        imgData[nLength + 1] = (imgData[nLength] = 0);
        nLength += 2;
        imgData[nLength] = (byte)(iWidth * iHeight % 256);
        imgData[nLength + 1] = (byte)(iWidth * iHeight / 256);
        nLength += 4;
        imgData[nLength] = 0;
        ++nLength;
        imgData[nLength] = 1;
        ++nLength;
        imgData[nLength] = 1;
        ++nLength;
        imgData[nLength] = 87;
        ++nLength;
        imgData[nLength] = 0;
        ++nLength;
        imgData[nLength] = (byte)(iWidth / 256);
        imgData[nLength + 1] = (byte)(iWidth % 256);
        nLength += 2;
        imgData[nLength] = (byte)(iHeight / 256);
        imgData[nLength + 1] = (byte)(iHeight % 256);
        nLength += 2;
        ++nLength;
    }
    
    static void FillImgHeadEx(final byte[] imgData, final int iDpi, final int iWidth, final int iHeight) {
        Arrays.fill(imgData, (byte)0);
        int nLength = 0;
        System.arraycopy("FIR".getBytes(), 0, imgData, 0, "FIR".length());
        nLength += 4;
        System.arraycopy("010".getBytes(), 0, imgData, nLength, "010".length());
        nLength += 4;
        imgData[nLength] = (byte)((iWidth * iHeight + 46) % 256);
        imgData[nLength + 1] = (byte)((iWidth * iHeight + 46) / 256);
        for (int i = nLength + 2; i < 2; ++i) {
            imgData[nLength + 2 + i] = 0;
        }
        imgData[nLength + 5] = (imgData[nLength + 4] = 0);
        nLength += 6;
        imgData[nLength] = 1;
        imgData[nLength + 1] = 2;
        nLength += 2;
        imgData[nLength] = 1;
        imgData[nLength + 1] = 0;
        nLength += 2;
        imgData[nLength] = 1;
        ++nLength;
        imgData[nLength] = 1;
        ++nLength;
        imgData[nLength] = (byte)(iDpi / 256);
        imgData[nLength + 1] = (byte)(iDpi % 256);
        nLength += 2;
        imgData[nLength] = (byte)(iDpi / 256);
        imgData[nLength + 1] = (byte)(iDpi % 256);
        nLength += 2;
        imgData[nLength] = (byte)(iDpi / 256);
        imgData[nLength + 1] = (byte)(iDpi % 256);
        nLength += 2;
        imgData[nLength] = (byte)(iDpi / 256);
        imgData[nLength + 1] = (byte)(iDpi % 256);
        nLength += 2;
        imgData[nLength] = 8;
        ++nLength;
        imgData[nLength] = 0;
        ++nLength;
        imgData[nLength + 1] = (imgData[nLength] = 0);
        nLength += 2;
        for (int i = nLength; i < 2; ++i) {
            imgData[nLength + i] = 0;
        }
        imgData[nLength] = (byte)(iWidth * iHeight % 256);
        imgData[nLength + 1] = (byte)(iWidth * iHeight / 256);
        for (int i = nLength + 2; i < 2; ++i) {
            imgData[nLength + 2 + i] = 0;
        }
        nLength += 4;
        imgData[nLength] = 0;
        ++nLength;
        imgData[nLength] = 1;
        ++nLength;
        imgData[nLength] = 1;
        ++nLength;
        imgData[nLength] = 87;
        ++nLength;
        imgData[nLength] = 0;
        ++nLength;
        imgData[nLength] = (byte)(iWidth / 256);
        imgData[nLength + 1] = (byte)(iWidth % 256);
        nLength += 2;
        imgData[nLength] = (byte)(iHeight / 256);
        imgData[nLength + 1] = (byte)(iHeight % 256);
        nLength += 2;
        ++nLength;
    }
    
    public static int ImgToIso(final byte[] imageBuf, final int iDpi, final int iWidth, final int iHeight, final byte[] isoImageBuf) {
        final String strKey = "MIAXIS1234567890";
        final byte[] psKey = strKey.getBytes();
        final String strRandom = "12345678";
        final byte[] psRandom = strRandom.getBytes();
        final byte[] tmpHead = new byte[47];
        final byte[] tmpMac = new byte[9];
        final int[] iMacLen = { 0 };
        final int iImageSize = iWidth * iHeight;
        FillImgHeadEx(tmpHead, iDpi, iWidth, iHeight);
        System.arraycopy(tmpHead, 0, isoImageBuf, 0, 46);
        System.arraycopy(imageBuf, 0, isoImageBuf, 46, iImageSize);
        FPICryptMAC(psKey, psRandom, isoImageBuf, iImageSize + 46, tmpMac, iMacLen);
        System.arraycopy(tmpMac, 0, isoImageBuf, iImageSize + 46, 8);
        return 0;
    }
}
