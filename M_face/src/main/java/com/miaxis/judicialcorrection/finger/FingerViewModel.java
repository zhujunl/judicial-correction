package com.miaxis.judicialcorrection.finger;

import androidx.lifecycle.ViewModel;

public class FingerViewModel extends ViewModel {

//    public ObservableField<String> fingerHint = new ObservableField<>();
//    public ObservableField<String> deviceInfo = new ObservableField<>();
//
//    public Bitmap imageCache;
//
//    public FingerViewModel() {
//        super();
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
//    public void initFingerDevice() {
//        fingerHint.set("正在初始化指纹");
//        App.getInstance().getThreadExecutor().execute(() -> {
//            FingerManager.getInstance().initDevice(fingerStatusListener);
//        });
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//    public void releaseFingerDevice() {
//        App.getInstance().getThreadExecutor().execute(() -> {
//            FingerManager.getInstance().setFingerListener(null);
//            FingerManager.getInstance().release();
//            GpioManager.getInstance().fingerPowerControl(false);
//        });
//    }
//
//    private final FingerManager.OnFingerReadListener fingerReadListener = (feature, image) -> {
//        if (feature != null) {
//            fingerHint.set("获取指纹成功 ");
//            imageCache = image;
//            fingerImageFlag.postValue(Boolean.TRUE);
//        }
//        FingerManager.getInstance().readFinger();
//    };
//
//    private final FingerManager.OnFingerStatusListener fingerStatusListener = result -> {
//        if (result) {
//            String device = FingerManager.getInstance().deviceInfo();
//            if (TextUtils.isEmpty(device)) {
//                fingerHint.set("未找到指纹设备");
//                deviceInfo.set("无法获取");
//            } else {
//                deviceInfo.set(device);
//                if (automatic) {
//                    automaticTestOver(true, 1000);
//                } else {
//                    fingerHint.set("请在指纹采集仪上按压指纹");
//                    FingerManager.getInstance().setFingerListener(fingerReadListener);
//                    FingerManager.getInstance().readFinger();
//                }
//                return;
//            }
//        } else {
//            fingerHint.set("指纹初始化失败");
//            FingerManager.getInstance().release();
//        }
//        if (automatic) {
//            automaticTestOver(false, 1000);
//        }
//    };

}
