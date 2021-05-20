package com.miaxis.finger;

import android.graphics.Bitmap;

public class FingerManager {

    private FingerManager() {
    }

    public static FingerManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final FingerManager instance = new FingerManager();
    }

    /**
     * ================================ 静态内部类单例 ================================
     **/

    private FingerStrategy fingerStrategy;

    public void init(FingerStrategy fingerStrategy) {
        this.fingerStrategy = fingerStrategy;
    }

    public interface FingerStrategy {
        void init(OnFingerStatusListener statusListener);

        void setFingerListener(OnFingerReadListener readListener);

        String deviceInfo();

        void readFinger();

        void release();

        void releaseDevice();

        void comparison(byte[] b);
    }

    public interface OnFingerStatusListener {
        void onFingerStatus(boolean result);
    }

    public interface OnFingerReadListener {
        void onFingerRead(byte[] feature, Bitmap image);

        void onFingerReadComparison(byte[] feature, Bitmap image,int  state);
    }

    public void initDevice(OnFingerStatusListener statusListener) {
        if (fingerStrategy != null) {
            fingerStrategy.init(statusListener);
        }
    }

    public void releaseDevice() {
        if (fingerStrategy != null) {
            fingerStrategy.releaseDevice();
        }
    }

    public void setFingerListener(OnFingerReadListener readListener) {
        if (fingerStrategy != null) {
            fingerStrategy.setFingerListener(readListener);
        }
    }

    public String deviceInfo() {
        if (fingerStrategy != null) {
            return fingerStrategy.deviceInfo();
        }
        return "";
    }

    public void readFinger() {
        if (fingerStrategy != null) {
            fingerStrategy.readFinger();
        }
    }
    public void redFingerComparison(byte[] bytes){
        if (fingerStrategy != null) {
            fingerStrategy.comparison(bytes);
        }
    }

    public void release() {
        if (fingerStrategy != null) {
            fingerStrategy.release();
        }
    }

}
