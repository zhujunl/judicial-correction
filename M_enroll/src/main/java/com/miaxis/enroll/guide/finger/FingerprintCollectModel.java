package com.miaxis.enroll.guide.finger;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.finger.FingerManager;
import com.miaxis.judicialcorrection.base.api.vo.FingerEntity;
import com.miaxis.judicialcorrection.base.api.vo.FingerprintEntity;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.repo.FingerprintRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class FingerprintCollectModel extends ViewModel {

    //发送指纹数据
    public MutableLiveData<FingerprintEntity> fingerprintLiveData = new MutableLiveData<>();
    public File filePath;
    private AppExecutors mAppExecutors;
    private FingerprintRepo mFingerprintRepo;
    //如果指纹为空直接读取上传
    //指纹1
    public ObservableField<byte[]> fingerprint1 = new ObservableField<>();
    //指纹2
    public ObservableField<byte[]> fingerprint2 = new ObservableField<>();
    //提示
    public MutableLiveData<String> hint = new MutableLiveData<>();
    //指纹初始化结果
    public MutableLiveData<Boolean> resultState = new MutableLiveData<>();
//    //保存3个手指状态
//    public ObservableField<List<byte[]>> allFinger = new ObservableField<>();
//    //分数
//    public ObservableField<Integer> scoreFinger = new ObservableField<>();

    @Inject
    public FingerprintCollectModel(FingerprintRepo fingerprintRepo, AppExecutors mAppExecutors) {
        this.mAppExecutors = mAppExecutors;
        this.mFingerprintRepo = fingerprintRepo;
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
            Timber.e("FingerRead:" + (feature == null) + "   " + (image == null));
            if (image==null) {
                SystemClock.sleep(1000);
                try {
                    readFinger(getFingerToByte(), getFingerToByte2());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                hint.postValue("采集完成！");
                setFingerReadFile(feature, image);
            }

        }

        @Override
        public void onFingerReadComparison(byte[] feature, Bitmap image, int state) {
            Timber.e("FingerRead:" + (feature == null) + "   " + (image == null) + "===结果" + state);
            if (image == null) {
                SystemClock.sleep(1000);
                try {
                    readFinger(getFingerToByte(), getFingerToByte2());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (state == 0) {
                    hint.postValue("指纹比对成功！");
                    setFingerReadFile(feature, image);
                } else {
                    if (null != feature) {
                        hint.postValue("指纹比对失败！");
                    }
                    SystemClock.sleep(1000);
                    try {
                        readFinger(getFingerToByte(), getFingerToByte2());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void setFingerReadFile(byte[] feature, Bitmap image) {
            Timber.e("FingerRead:" + (feature == null) + "   " + (image == null));
            if (image != null) {
                String base64Str = Base64.encodeToString(feature, Base64.NO_WRAP);
                String[] strings = new String[]{base64Str};
                FingerprintEntity fingerprintEntity = new FingerprintEntity();
                fingerprintEntity.fingerprints = strings;
                fingerprintLiveData.postValue(fingerprintEntity);
                FingerManager.getInstance().releaseDevice();
                FingerManager.getInstance().setFingerListener(null);
            } else {
                SystemClock.sleep(1000);
                try {
                    readFinger(getFingerToByte(), getFingerToByte2());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     *判断byte[] 是否相等 做非空判断
     * @return 返回对应的byte[]
     */
    private byte[] getFingerToByte() {
        if (fingerprint1.get() == null) {
            return null;
        }
        boolean equals = Arrays.equals(fingerprint1.get(), new byte[512]);
        return equals ? null : fingerprint1.get();
    }

    private byte[] getFingerToByte2() {
        if (fingerprint2.get() == null) {
            return null;
        }
        boolean equals = Arrays.equals(fingerprint2.get(), new byte[512]);
        return equals ? null : fingerprint2.get();

    }

    private final FingerManager.OnFingerStatusListener fingerStatusListener = result -> {
        Timber.e("FingerStatus:" + result);
        resultState.postValue(result);

        if (result) {
            byte[] decode = getFingerToByte();
            byte[] decode2 = getFingerToByte2();
            result = readFinger(decode, decode2);
            if (!result) {
                result = readFinger(decode, decode2);
            }
        }
        if (!result) {
            hint.postValue("指纹初始化失败！");
            FingerManager.getInstance().release();
        }
    };

    private boolean readFinger(byte[] bytes, byte[] bytes2) {
        String device = FingerManager.getInstance().deviceInfo();
        Timber.e("device:" + device);
        if (TextUtils.isEmpty(device)) {
            hint.postValue("未找到指纹设备");
            return false;
        } else {
            hint.postValue("请按压手指");
            FingerManager.getInstance().setFingerListener(fingerReadListener);
            mAppExecutors.networkIO().execute(() -> {
                if (bytes == null && bytes2 == null) {
                    FingerManager.getInstance().readFinger();
                } else {
                    FingerManager.getInstance().redFingerComparison(bytes, bytes2);
                }
            });
            return true;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        releaseFingerDevice();
        FingerManager.getInstance().release();
    }

    /**
     * 上传指纹
     */
    public LiveData<Resource<Object>> uploadFingerprint(FingerprintEntity fingerprintEntity) {
        return mFingerprintRepo.uploadFingerprint(fingerprintEntity);
    }


    /**
     * 得到指纹 结果 进行比对操作
     */
    public LiveData<Resource<FingerEntity>> getFingerPrint(String id) {
        return mFingerprintRepo.getFingerPrint(id);
    }
}