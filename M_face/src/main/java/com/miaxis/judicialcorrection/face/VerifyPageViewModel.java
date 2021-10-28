package com.miaxis.judicialcorrection.face;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.miaxis.finger.FingerManager;
import com.miaxis.judicialcorrection.base.api.vo.FingerEntity;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.repo.FingerprintRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;


@HiltViewModel
public class VerifyPageViewModel extends BaseFaceViewModel {


    private final FingerprintRepo mFingerprintRepo;

    public MutableLiveData<String> hint = new MutableLiveData<>();
    //下载指纹
    public ObservableField<byte[]> fingerprint1 = new ObservableField<>();
    //指纹初始化结果
    public MutableLiveData<Boolean> resultState = new MutableLiveData<>();
    //指纹bitmap图片
    public MutableLiveData<Bitmap> bitmapFinger = new MutableLiveData<>();
    //比对结果
    public MutableLiveData<Integer> stateLiveData = new MutableLiveData<>();

    @Inject
    public VerifyPageViewModel(AppExecutors appExecutors, FingerprintRepo mFingerprintRepo) {
        super(appExecutors);
        this.mFingerprintRepo = mFingerprintRepo;
    }

    public void initFingerDevice() {
        mAppExecutors.networkIO().execute(() -> {
            FingerManager.getInstance().initDevice(fingerStatusListener);
        });
    }

    public void releaseFingerDevice() {
        FingerManager.getInstance().setFingerListener(null);
    }

    private final FingerManager.OnFingerReadListener fingerReadListener = new FingerManager.OnFingerReadListener() {
        @Override
        public void onFingerRead(byte[] feature, Bitmap image) {

        }

        @Override
        public void onFingerReadComparison(byte[] feature, Bitmap image, int state) {
            Timber.e("FingerRead:" + (feature == null) + "   " + (image == null) + "===结果" + state);
            if (state == 0) {
                stateLiveData.postValue(state);
                setFingerReadFile(feature, image);
            } else {
                if (null != feature && null != image) {
                    hint.postValue("指纹比对失败！");
                }
                SystemClock.sleep(1500);
                try {
                    readFinger(getFingerToByte());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void setFingerReadFile(byte[] feature, Bitmap image) {
            Timber.e("FingerRead:" + (feature == null) + "   " + (image == null));
            if (image != null) {
                hint.postValue("指纹比对成功！");
                bitmapFinger.postValue(image);
                FingerManager.getInstance().releaseDevice();
                FingerManager.getInstance().setFingerListener(null);
            } else {
                SystemClock.sleep(1500);
                try {
                    readFinger(getFingerToByte());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private byte[] getFingerToByte() {
        return fingerprint1.get() == null ? new byte[512] : fingerprint1.get();
    }


    private final FingerManager.OnFingerStatusListener fingerStatusListener = result -> {
        resultState.postValue(result);
        if (result) {
            byte[] byte1 = getFingerToByte();
            result = readFinger(byte1);
            if (!result) {
                result = readFinger(byte1);
            }
        }
        if (!result) {
            Timber.e("指纹初始化失败");
            FingerManager.getInstance().release();
        }
    };

    private boolean readFinger(byte[] bytes) {
        String device = FingerManager.getInstance().deviceInfo();
        if (TextUtils.isEmpty(device)) {
            hint.postValue("未找到指纹设备");
            return false;
        } else {
            hint.postValue("请按压手指");
            FingerManager.getInstance().setFingerListener(fingerReadListener);
            mAppExecutors.networkIO().execute(() -> {
                FingerManager.getInstance().redFingerComparison(bytes);
            });
            return true;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        releaseFingerDevice();
    }

    /**
     * 得到指纹 结果 进行比对操作
     */
    public LiveData<Resource<FingerEntity>> getFingerPrint(String id) {
        return mFingerprintRepo.getFingerPrint(id);
    }
}
