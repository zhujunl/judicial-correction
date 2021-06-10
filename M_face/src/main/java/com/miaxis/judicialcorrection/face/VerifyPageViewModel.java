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
    //身份证指纹1
    public ObservableField<byte[]> fingerprint1 = new ObservableField<>();
    //身份证指纹2
    public ObservableField<byte[]> fingerprint2 = new ObservableField<>();
    //下载的指纹
    public ObservableField<byte[]> fingerprint3 = new ObservableField<>();
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
            stateLiveData.postValue(state);
            if (state == 0) {
                setFingerReadFile(feature, image);
            } else {
                hint.postValue("指纹比对失败！");
                SystemClock.sleep(100);
                try {
                    readFinger(getFingerToByte(),getFingerToByte2(),fingerprint3.get());
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
                SystemClock.sleep(1000);
                try {
                    readFinger(getFingerToByte(),getFingerToByte2(),fingerprint3.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private byte[] getFingerToByte() {
        return fingerprint1.get() == null ? new byte[512] : fingerprint1.get();
    }

    private byte[] getFingerToByte2() {
        return fingerprint2.get() == null ? new byte[512] : fingerprint2.get();
    }

    private final FingerManager.OnFingerStatusListener fingerStatusListener = result -> {
        resultState.setValue(result);
        if (result) {
            byte[] byte1 = getFingerToByte();
            byte[] byte2 = getFingerToByte2();
            byte[] byte3 = fingerprint3.get();
            result = readFinger(byte1, byte2, byte3);
            if (!result) {
                result = readFinger(byte1, byte2, byte3);
            }
        }
        if (!result) {
            Timber.e("指纹初始化失败");
            FingerManager.getInstance().release();
        }
    };

    private boolean readFinger(byte[] bytes, byte[] bytes2, byte[] bytes3) {
        String device = FingerManager.getInstance().deviceInfo();
        if (TextUtils.isEmpty(device)) {
            hint.postValue("未找到指纹设备");
            return false;
        } else {
            hint.postValue("请在指纹采集仪上按压指纹");
            FingerManager.getInstance().setFingerListener(fingerReadListener);
            mAppExecutors.networkIO().execute(() -> {
                FingerManager.getInstance().redFingerComparison(bytes, bytes2, bytes3);
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

    // 使用  fingerPositionCovert(bFingerData1[5]);
    public String fingerPositionCovert(byte finger) {
        switch ((int) finger) {
            case 11:
                return "右手拇指";
            case 12:
                return "右手食指";
            case 13:
                return "右手中指";
            case 14:
                return "右手环指";
            case 15:
                return "右手小指";
            case 16:
                return "左手拇指";
            case 17:
                return "左手食指";
            case 18:
                return "左手中指";
            case 19:
                return "左手环指";
            case 20:
                return "左手小指";
            case 97:
                return "右手不确定指位";
            case 98:
                return "左手不确定指位";
            case 99:
                return "其他不确定指位";
            default:
                return "其他不确定指位";
        }
    }

}
