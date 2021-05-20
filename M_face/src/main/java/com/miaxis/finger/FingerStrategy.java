package com.miaxis.finger;

import android.content.Context;
import android.graphics.Bitmap;

import com.mx.finger.alg.MxFingerAlg;
import com.mx.finger.api.msc.MxMscBigFingerApi;
import com.mx.finger.api.msc.MxMscBigFingerApiFactory;
import com.mx.finger.common.MxImage;
import com.mx.finger.common.Result;
import com.mx.finger.utils.RawBitmapUtils;

public class FingerStrategy implements FingerManager.FingerStrategy {

    private MxMscBigFingerApi mxMscBigFingerApi;
    private MxFingerAlg mxFingerAlg;

    private FingerManager.OnFingerStatusListener statusListener;

    private FingerManager.OnFingerReadListener readListener;

    private Context mContext;

    public FingerStrategy(Context context) {
        this.mContext = context;
    }

    @Override
    public void init(FingerManager.OnFingerStatusListener statusListener) {
        try {
            this.statusListener = statusListener;
            MxMscBigFingerApiFactory fingerFactory = new MxMscBigFingerApiFactory(mContext.getApplicationContext());
            mxMscBigFingerApi = fingerFactory.getApi();
            mxFingerAlg = fingerFactory.getAlg();
            statusListener.onFingerStatus(true);
            return;
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
        if (mxMscBigFingerApi != null) {
            Result<String> deviceInfo = mxMscBigFingerApi.getDeviceInfo();
            if (deviceInfo.isSuccess()) {
                return deviceInfo.data;
            }
        }
        return "";
    }

    @Override
    public void readFinger() {
        try {
            if (mxMscBigFingerApi != null || mxFingerAlg != null) {
                Result<MxImage> result = mxMscBigFingerApi.getFingerImageBig(5000);
                if (result.isSuccess()) {
                    MxImage image = result.data;
                    if (image != null) {
                        //int quality = mxFingerAlg.imageQuality(image.data, image.width, image.height);
                        byte[] feature = mxFingerAlg.extractFeature(image.data, image.width, image.height);
                        if (feature != null) {
                            Bitmap bitmap = RawBitmapUtils.raw2Bimap(image.data, image.width, image.height);
                            if (readListener != null) {
                                readListener.onFingerRead(feature, bitmap);
                                return;
                            }
                        }
                    }
                }
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
        mxMscBigFingerApi = null;
        mxFingerAlg = null;
    }

    @Override
    public void releaseDevice() {
        this.statusListener = null;
    }

    @Override
    public void comparison(byte[] b) {
        try {
            if (mxMscBigFingerApi != null || mxFingerAlg != null) {
                Result<MxImage> result = mxMscBigFingerApi.getFingerImageBig(5000);
                if (result.isSuccess()) {
                    MxImage image = result.data;
                    if (image != null) {
                        //int quality = mxFingerAlg.imageQuality(image.data, image.width, image.height);
                        byte[] feature = mxFingerAlg.extractFeature(image.data, image.width, image.height);
                        if (feature != null) {
                            int match = mxFingerAlg.match(b, feature, 3);
                            Bitmap bitmap = RawBitmapUtils.raw2Bimap(image.data, image.width, image.height);
                            if (readListener != null) {
                                readListener.onFingerReadComparison(feature, bitmap,match);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (readListener != null) {
            readListener.onFingerReadComparison(null, null,MxFingerAlg.ERROR);
        }
    }

}
