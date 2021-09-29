package org.zz.mxhidfingerdriver;

import android.os.*;
import android.util.*;
import org.zz.protocol.*;
import android.hardware.usb.*;
import android.app.*;
import android.content.*;
import java.util.*;

public class UsbBase
{
    private int m_iSendPackageSize;
    private int m_iRecvPackageSize;
    private UsbDevice m_usbDevice;
    private UsbInterface m_usbInterface;
    private UsbEndpoint m_inEndpoint;
    private UsbEndpoint m_outEndpoint;
    private UsbDeviceConnection m_connection;
    private Context m_ctx;
    private Handler m_fHandler;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver;
    
    public UsbBase(final Context context) {
        this.m_iSendPackageSize = 0;
        this.m_iRecvPackageSize = 0;
        this.m_usbDevice = null;
        this.m_usbInterface = null;
        this.m_inEndpoint = null;
        this.m_outEndpoint = null;
        this.m_connection = null;
        this.m_ctx = null;
        this.m_fHandler = null;
        this.mUsbReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if ("com.android.example.USB_PERMISSION".equals(action)) {
                    synchronized (this) {
                        final UsbDevice device = (UsbDevice)intent.getParcelableExtra("device");
                        if (intent.getBooleanExtra("permission", false)) {
                            if (device != null) {}
                        }
                        else {
                            Log.d("MIAXIS", "permission denied for device " + device);
                        }
                    }
                }
                if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                    final UsbDevice device2 = (UsbDevice)intent.getParcelableExtra("device");
                    if (device2 != null) {
                        UsbBase.this.m_connection.releaseInterface(UsbBase.this.m_usbInterface);
                        UsbBase.this.m_connection.close();
                    }
                }
            }
        };
        this.m_ctx = context;
        this.m_fHandler = null;
        this.regUsbMonitor();
    }
    
    public UsbBase(final Context context, final Handler bioHandler) {
        this.m_iSendPackageSize = 0;
        this.m_iRecvPackageSize = 0;
        this.m_usbDevice = null;
        this.m_usbInterface = null;
        this.m_inEndpoint = null;
        this.m_outEndpoint = null;
        this.m_connection = null;
        this.m_ctx = null;
        this.m_fHandler = null;
        this.mUsbReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if ("com.android.example.USB_PERMISSION".equals(action)) {
                    synchronized (this) {
                        final UsbDevice device = (UsbDevice)intent.getParcelableExtra("device");
                        if (intent.getBooleanExtra("permission", false)) {
                            if (device != null) {}
                        }
                        else {
                            Log.d("MIAXIS", "permission denied for device " + device);
                        }
                    }
                }
                if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                    final UsbDevice device2 = (UsbDevice)intent.getParcelableExtra("device");
                    if (device2 != null) {
                        UsbBase.this.m_connection.releaseInterface(UsbBase.this.m_usbInterface);
                        UsbBase.this.m_connection.close();
                    }
                }
            }
        };
        this.m_ctx = context;
        MXLog.SetHandler(this.m_fHandler = bioHandler);
        this.regUsbMonitor();
    }
    
    public String getDevAttribute() {
        if (this.m_ctx == null) {
            return null;
        }
        int iDevNum = 0;
        String strDevInfo = "";
        String strTmp = null;
        final UsbManager usbManager = (UsbManager)this.m_ctx.getSystemService("usb");
        final HashMap<String, UsbDevice> map = (HashMap<String, UsbDevice>)usbManager.getDeviceList();
        for (final UsbDevice device : map.values()) {
            if (!usbManager.hasPermission(device)) {
                final PendingIntent pi = PendingIntent.getBroadcast(this.m_ctx, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
                usbManager.requestPermission(device, pi);
                return null;
            }
            ++iDevNum;
            strTmp = "describeContents:" + device.describeContents();
            strDevInfo = strDevInfo + strTmp + "\r\n";
            strTmp = "DeviceProtocol:" + device.getDeviceProtocol();
            strDevInfo = strDevInfo + strTmp + "\r\n";
            strTmp = "DeviceClass:" + device.getDeviceClass();
            strDevInfo = strDevInfo + strTmp + "\r\n";
            strTmp = "DeviceSubclass:" + device.getDeviceSubclass();
            strDevInfo = strDevInfo + strTmp + "\r\n";
            strTmp = "InterfaceCount:" + device.getInterfaceCount();
            strDevInfo = strDevInfo + strTmp + "\r\n";
            strTmp = "DeviceId:" + device.getDeviceId();
            strDevInfo = strDevInfo + strTmp + "\r\n";
            strTmp = "DeviceName:" + device.getDeviceName();
            strDevInfo = strDevInfo + strTmp + "\r\n";
            strTmp = "VendorId:" + device.getVendorId();
            strDevInfo = strDevInfo + strTmp + "\r\n";
            strTmp = "ProductId:" + device.getProductId();
            strDevInfo = strDevInfo + strTmp + "\r\n";
        }
        return strDevInfo;
    }
    
    public int getDevNum(final int vid, final int pid) {
        if (this.m_ctx == null) {
            return -102;
        }
        int iDevNum = 0;
        final UsbManager usbManager = (UsbManager)this.m_ctx.getSystemService("usb");
        final HashMap<String, UsbDevice> map = (HashMap<String, UsbDevice>)usbManager.getDeviceList();
        for (final UsbDevice device : map.values()) {
            if (!usbManager.hasPermission(device)) {
                final PendingIntent pi = PendingIntent.getBroadcast(this.m_ctx, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
                usbManager.requestPermission(device, pi);
                return -101;
            }
            if (vid != device.getVendorId() || pid != device.getProductId()) {
                continue;
            }
            ++iDevNum;
        }
        return iDevNum;
    }
    
    public int openDev(final int vid, final int pid) {
        int iCount = 0;
        int nRet = this.openDev_In(vid, pid);
        while (nRet == -101) {
            ++iCount;
            MySleep(200);
            nRet = this.openDev_In(vid, pid);
            if (iCount > 5) {
                break;
            }
        }
        return nRet;
    }
    
    public int openDev_In(final int vid, final int pid) {
        if (this.m_ctx == null) {
            MXLog.SendMsg("ERRCODE_NO_CONTEXT");
            return -102;
        }
        MXLog.SendMsg(String.format("vid: 0x%x \t pid: 0x%x", vid, pid));
        final UsbManager usbManager = (UsbManager)this.m_ctx.getSystemService("usb");
        final HashMap<String, UsbDevice> map = (HashMap<String, UsbDevice>)usbManager.getDeviceList();
        for (final UsbDevice device : map.values()) {
            if (!usbManager.hasPermission(device)) {
                final PendingIntent pi = PendingIntent.getBroadcast(this.m_ctx, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
                usbManager.requestPermission(device, pi);
                return -101;
            }
            if (vid == device.getVendorId() && pid == device.getProductId()) {
                this.m_usbDevice = device;
                this.m_usbInterface = this.m_usbDevice.getInterface(0);
                this.m_inEndpoint = this.m_usbInterface.getEndpoint(0);
                this.m_outEndpoint = this.m_usbInterface.getEndpoint(1);
                (this.m_connection = usbManager.openDevice(this.m_usbDevice)).claimInterface(this.m_usbInterface, true);
                this.m_iSendPackageSize = this.m_outEndpoint.getMaxPacketSize();
                this.m_iRecvPackageSize = this.m_inEndpoint.getMaxPacketSize();
                return 0;
            }
        }
        return -100;
    }
    
    public int sendPacketSize() {
        return this.m_iSendPackageSize;
    }
    
    public int recvPacketSize() {
        return this.m_iRecvPackageSize;
    }
    
    public int clearBuffer() {
        int iRV = 0;
        final int iEndpointSize = this.recvPacketSize();
        final byte[] bRecvBufTmp = new byte[iEndpointSize];
        do {
            iRV = this.m_connection.bulkTransfer(this.m_inEndpoint, bRecvBufTmp, iEndpointSize, 5);
        } while (iRV >= 0);
        return 0;
    }
    
    public int sendData(final byte[] bSendBuf, final int iSendLen, final int iTimeOut) {
        int iRV = -1;
        if (iSendLen > bSendBuf.length) {
            return -18;
        }
        final int iPackageSize = this.sendPacketSize();
        if (iSendLen > iPackageSize) {
            return -18;
        }
        final byte[] bSendBufTmp = new byte[iPackageSize];
        System.arraycopy(bSendBuf, 0, bSendBufTmp, 0, iSendLen);
        iRV = this.m_connection.bulkTransfer(this.m_outEndpoint, bSendBufTmp, iPackageSize, iTimeOut);
        return iRV;
    }
    
    public int recvData(final byte[] bRecvBuf, final int iRecvLen, final int iTimeOut) {
        int iRV = -1;
        if (iRecvLen > bRecvBuf.length) {
            return -18;
        }
        final int iPackageSize = this.recvPacketSize();
        final byte[] bRecvBufTmp = new byte[iPackageSize];
        for (int i = 0; i < iRecvLen; i += iPackageSize) {
            int nDataLen = iRecvLen - i;
            if (nDataLen > iPackageSize) {
                nDataLen = iPackageSize;
            }
            iRV = this.m_connection.bulkTransfer(this.m_inEndpoint, bRecvBufTmp, nDataLen, iTimeOut);
            if (iRV < 0) {
                return iRV;
            }
            System.arraycopy(bRecvBufTmp, 0, bRecvBuf, i, iRV);
        }
        return iRV;
    }
    
    public int closeDev() {
        if (this.m_connection != null) {
            MXLog.SendMsg("m_connection.releaseInterface");
            this.m_connection.releaseInterface(this.m_usbInterface);
            MXLog.SendMsg("m_connection.close");
            this.m_connection.close();
            this.m_connection = null;
        }
        return 0;
    }
    
    private void regUsbMonitor() {
        final IntentFilter filter = new IntentFilter("com.android.example.USB_PERMISSION");
        this.m_ctx.registerReceiver(this.mUsbReceiver, filter);
    }
    
    public void unRegUsbMonitor() {
        this.m_ctx.unregisterReceiver(this.mUsbReceiver);
    }
    
    public static void MySleep(final int iTimeout) {
        long duration = -1L;
        Calendar time2;
        for (Calendar time1 = Calendar.getInstance(); duration <= iTimeout; duration = time2.getTimeInMillis() - time1.getTimeInMillis()) {
            time2 = Calendar.getInstance();
        }
    }
}
