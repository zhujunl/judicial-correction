package com.miaxis.enroll.guide.finger;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.enroll.vo.FingerprintEntity;
import com.miaxis.finger.FingerManager;
import com.miaxis.judicialcorrection.base.api.vo.FingerEntity;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.utils.BitmapUtils;
import com.miaxis.utils.FileUtils;

import java.io.File;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class FingerprintCollectModel extends ViewModel {


    public MutableLiveData<FingerprintEntity> fingerprintLiveData = new MutableLiveData<>();
    public File filePath;
    private AppExecutors mAppExecutors;
    private FingerprintRepo mFingerprintRepo;
    public ObservableField<byte[]> fingerprint1 = new ObservableField<>();

    @Inject
    public FingerprintCollectModel(FingerprintRepo fingerprintRepo, AppExecutors mAppExecutors, AppDatabase appDatabase) {
        this.mAppExecutors = mAppExecutors;
        this.mFingerprintRepo = fingerprintRepo;
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

    private final FingerManager.OnFingerReadListener fingerReadListener = new FingerManager.OnFingerReadListener() {
        @Override
        public void onFingerRead(byte[] feature, Bitmap image) {
            setFingerReadFile(feature, image);
        }

        @Override
        public void onFingerReadComparison(byte[] feature, Bitmap image, int state) {
            Timber.e("FingerRead:" + (feature == null) + "   " + (image == null) + "===结果" + state);
            setFingerReadFile(feature, image);
        }
        private void setFingerReadFile(byte[] feature, Bitmap image) {
            Timber.e("FingerRead:" + (feature == null) + "   " + (image == null));
            if (image != null) {
                String fileName = "fingerprint.jpg";
                File file = new File(filePath, fileName);
                boolean frameImage = getFrameImage(image, file.getAbsolutePath());
                if (frameImage) {
                    String base64Path = FileUtils.imageToBase64(file.getAbsolutePath());
                    String[] strings = new String[1];
                    strings[0] = base64Path;
                    FingerprintEntity fingerprintEntity = new FingerprintEntity();
                    fingerprintEntity.fingerprints = strings;
                    fingerprintLiveData.postValue(fingerprintEntity);
                    FingerManager.getInstance().releaseDevice();
                    FingerManager.getInstance().setFingerListener(null);
                }
            } else {
                SystemClock.sleep(100);
                try {
                    readFinger(getFingerToByte());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private byte[] getFingerToByte() {
        byte[] decode = null;
        for (byte b : Objects.requireNonNull(fingerprint1.get())) {
            if (b != 0x0) {
                Timber.v("不为 %s", b);
                decode = fingerprint1.get();
                break;
            }
        }
        return decode;
    }

    private final FingerManager.OnFingerStatusListener fingerStatusListener = result -> {
        Timber.e("FingerStatus:" + result);
        if (this.mOnFingerInitListener != null) {
            this.mOnFingerInitListener.onInit(result);
        }
        if (result) {
            byte[] decode = getFingerToByte();
            result = readFinger(decode);
            if (!result) {
                result = readFinger(decode);
            }
        }
        if (!result) {
            Timber.e("指纹初始化失败");
            FingerManager.getInstance().release();
        }
    };

    private boolean readFinger(byte[] bytes) {
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
                    if (bytes == null) {
                        FingerManager.getInstance().readFinger();
                    } else {
                        FingerManager.getInstance().redFingerComparison(bytes);
                    }
                }
            });
            return true;
        }
    }

    public interface OnFingerInitListener {
        void onInit(boolean result);
    }

    /**
     * 上传指纹
     *
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


    public boolean getFrameImage(Bitmap bitmap, String savePath) {
        try {

            File file = new File(savePath);
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (parentFile != null && !parentFile.exists()) {
                    boolean mkdirs = parentFile.mkdirs();
                }
            } else {
                boolean newFile = file.createNewFile();
            }
            return BitmapUtils.saveBitmap(bitmap, savePath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}