package com.miaxis.finger;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.fingerprint.zzFingerAlgID;
import com.mx.finger.alg.MxFingerAlg;
import com.mx.finger.api.msc.MxMscBigFingerApi;
import com.mx.finger.api.msc.MxMscBigFingerApiFactory;
import com.mx.finger.common.MxImage;
import com.mx.finger.common.Result;
import com.mx.finger.utils.RawBitmapUtils;

import org.zz.api.MXFingerAPI;

import timber.log.Timber;

public class FingerStrategy implements FingerManager.FingerStrategy {

//    private MxMscBigFingerApi mxMscBigFingerApi;
//    private MxFingerAlg mxFingerAlg;
    private MXFingerAPI fingerStrategy;
    private FingerManager.OnFingerStatusListener statusListener;
    int iTimeout = 15 * 1000;
    private FingerManager.OnFingerReadListener readListener;

    private Context mContext;

    public FingerStrategy(Context context) {
        this.mContext = context;
    }

    @Override
    public void init(FingerManager.OnFingerStatusListener statusListener) {
        try {
            this.statusListener = statusListener;
           // MxMscBigFingerApiFactory fingerFactory = new MxMscBigFingerApiFactory(mContext.getApplicationContext());
            int iVID = 0x821B;
            int iPID = 0x0202;
            fingerStrategy = new MXFingerAPI(this.mContext, iPID, iVID);
            String s = deviceInfo();
            if (!TextUtils.isEmpty(s)){
                statusListener.onFingerStatus(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            statusListener.onFingerStatus(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setFingerListener(FingerManager.OnFingerReadListener readListener) {
        this.readListener = readListener;
    }

    @Override
    public String deviceInfo() {
        if (fingerStrategy != null) {
            byte[] version = new byte[100];
            int i = fingerStrategy.mxGetDevVersion(version);
            if (i==0){
                return new String(version).trim();
            }
        }
        return "";
    }

    @Override
    public void readFinger() {
        try {
            if (fingerStrategy != null) {
                int iImageX = 256;
                int iImageY = 360;
                int iImageSize = iImageX * iImageY;
                //int iImageSize = iImageX * iImageY;
                byte[] bFingerImage = new byte[iImageSize];
                int ID_TZ_SIZE = 512;
                byte[] m_bFingerMbId = new byte[ID_TZ_SIZE];
                int ret = fingerStrategy.mxExtractFeatureID(bFingerImage, iTimeout, 0, m_bFingerMbId);
                if (ret == 0) {
                  Bitmap m_bitmap = fingerStrategy.Raw2Bimap(bFingerImage, iImageX, iImageY);
                    if (readListener != null) {
                        readListener.onFingerRead(m_bFingerMbId, m_bitmap);
                        return;
                    }
                }
//                Result<MxImage> result = mxMscBigFingerApi.getFingerImageBig(5000);
//                if (result.isSuccess()) {
//                    MxImage image = result.data;
//                    if (image != null) {
//                        byte[] feature = mxFingerAlg.extractFeature(image.data, image.width, image.height);
//                        if (feature != null) {
//                            Bitmap bitmap = RawBitmapUtils.raw2Bimap(image.data, image.width, image.height);
//                            if (readListener != null) {
//                                readListener.onFingerRead(feature, bitmap);
//                                return;
//                            }
//                        }
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (readListener != null) {
            readListener.onFingerRead(null, null);
        }
    }

    @Override
    public void release() {
        statusListener = null;
        readListener = null;
        if (fingerStrategy!=null){
            try {
                fingerStrategy.mxCancelCaptue();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                fingerStrategy.unRegUsbMonitor();
            }catch (Exception e){
                e.printStackTrace();
            }
            fingerStrategy=null;
        }
    }

    @Override
    public void releaseDevice() {
        this.statusListener = null;
    }

    @Override
    public void comparison(byte[] b, byte[] b2) {
        try {
            if (fingerStrategy != null) {
                int iImageX = 256;
                int iImageY = 360;
                int iImageSize = iImageX * iImageY;
                //int iImageSize = iImageX * iImageY;
                byte[] bFingerImage = new byte[iImageSize];
                int ID_TZ_SIZE = 512;
                byte[] m_bFingerMbId = new byte[ID_TZ_SIZE];
                int ret = fingerStrategy.mxExtractFeatureID(bFingerImage, iTimeout, 0, m_bFingerMbId);
                if (ret == 0) {
                    int match = fingerStrategy.mxMatchFeatureID(m_bFingerMbId, b,3);
                    int m;
                    if (match==MxFingerAlg.SUCCESS){
                        m = MxFingerAlg.SUCCESS;
                    }else {
                        int match2 = fingerStrategy.mxMatchFeatureID(m_bFingerMbId, b2, 3);
                        if (match2==MxFingerAlg.SUCCESS) {
                            m = MxFingerAlg.SUCCESS;
                        } else {
                            m = MxFingerAlg.ERROR;
                        }
                    }
                    Bitmap bitmap = RawBitmapUtils.raw2Bimap(bFingerImage, iImageX, iImageY);
                    if (readListener != null) {
                        readListener.onFingerReadComparison(m_bFingerMbId, bitmap, m);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (readListener != null) {
            readListener.onFingerReadComparison(null, null, MxFingerAlg.ERROR);
        }
    }

    @Override
    public void comparison(byte[] b) {
        try {
            if (fingerStrategy != null) {
                int iImageX = 256;
                int iImageY = 360;
                int iImageSize = iImageX * iImageY;
                byte[] bFingerImage = new byte[iImageSize];
                int ID_TZ_SIZE = 512;
                byte[] m_bFingerMbId = new byte[ID_TZ_SIZE];
                int ret = fingerStrategy.mxExtractFeatureID(bFingerImage, iTimeout, 0, m_bFingerMbId);
                if (ret == 0) {
                    int match = fingerStrategy.mxMatchFeatureID(m_bFingerMbId,b,3);
                    int m;
                    if (match == MxFingerAlg.SUCCESS){
                        m = MxFingerAlg.SUCCESS;
                    }else {
                        m = MxFingerAlg.ERROR;
                    }
                    Bitmap bitmap = RawBitmapUtils.raw2Bimap(bFingerImage, iImageX, iImageY);
                    if (readListener != null) {
                        readListener.onFingerReadComparison(m_bFingerMbId, bitmap, m);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (readListener != null) {
            readListener.onFingerReadComparison(null, null, MxFingerAlg.ERROR);
        }
    }

}
