package com.miaxis.enroll.guide;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.finger.FingerManager;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class FingerprintCollectModel extends ViewModel {


    public MutableLiveData<Bitmap> fingerBitmap = new MutableLiveData<>();
    public MutableLiveData<ZZResponse<VerifyInfo>> verifyStatus = new MutableLiveData<>();


    private AppExecutors mAppExecutors;

    @Inject
    public FingerprintCollectModel(AppExecutors mAppExecutors, AppDatabase appDatabase) {
        this.mAppExecutors = mAppExecutors;
    }


    private OnFingerInitListener mOnFingerInitListener;

    public void initFingerDevice(OnFingerInitListener onFingerInitListener) {
        this.mOnFingerInitListener = onFingerInitListener;
        mAppExecutors.networkIO().execute(() -> {
            FingerManager.getInstance().initDevice(fingerStatusListener);
        });
    }

    public void releaseFingerDevice() {
        FingerManager.getInstance().setFingerListener(null);
    }

    private final FingerManager.OnFingerReadListener fingerReadListener = (feature, image) -> {
        Timber.e("FingerRead:" + (feature == null) + "   " + (image == null));
        if (feature != null) {
            //fingerHint.set("获取指纹成功 ");
            fingerBitmap.postValue(image);
            FingerManager.getInstance().releaseDevice();
            FingerManager.getInstance().setFingerListener(null);
//            verifyStatus.postValue(ZZResponse.CreateSuccess(new VerifyInfo(id.getValue(), name.getValue(), idCardNumber.getValue())));
        } else {
            SystemClock.sleep(100);
            try {
                readFinger();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private final FingerManager.OnFingerStatusListener fingerStatusListener = result -> {
        Timber.e("FingerStatus:" + result);
        if (this.mOnFingerInitListener != null) {
            this.mOnFingerInitListener.onInit(result);
        }
        if (result) {
            result = readFinger();
            if (!result) {
                result = readFinger();
            }
        }
        if (!result) {
            Timber.e("指纹初始化失败");
            FingerManager.getInstance().release();
        }
    };

    private boolean readFinger() {
        String device = FingerManager.getInstance().deviceInfo();
        Timber.e("device:" + device);
        if (TextUtils.isEmpty(device)) {
            //fingerHint.set("未找到指纹设备");
            Timber.e("未找到指纹设备");
            return false;
        } else {
            Timber.e("请在指纹采集仪上按压指纹");
            //fingerHint.set("请在指纹采集仪上按压指纹");
            FingerManager.getInstance().setFingerListener(fingerReadListener);
            mAppExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    FingerManager.getInstance().readFinger();
                }
            });
            return true;
        }
    }

    public interface OnFingerInitListener {
        void onInit(boolean result);
    }
}