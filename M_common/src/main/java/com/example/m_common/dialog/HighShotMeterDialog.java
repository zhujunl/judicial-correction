package com.example.m_common.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_common.R;
import com.example.m_common.adapter.PreviewPageAdapter;
import com.example.m_common.bean.DeviceInfo;
import com.example.m_common.bean.PreviewPictureEntity;
import com.example.m_common.databinding.DialogHighShotMeterDialogBinding;
import com.example.m_common.utils.BitmapUtils;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.miaxis.camera.MXFrame;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogNoListener;
import com.miaxis.judicialcorrection.dialog.base.BaseNoListenerDialog;
import com.miaxis.utils.FileUtils;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import top.zibin.luban.Luban;


/**
 * 高拍仪
 */
public class HighShotMeterDialog extends BaseNoListenerDialog<DialogHighShotMeterDialogBinding, HighShotMeterDialog.ClickListener> implements CameraDialog.CameraDialogParent, CameraViewInterface.Callback {
    private final static String TAG="HighShotMeterDialog";
    private File mFilePath;
    private final static Handler mHandler = new Handler();

    private List<PreviewPictureEntity> pathList = new ArrayList<>();
    private PreviewPageAdapter mAdapter;
    private AppExecutors appExecutors;
    private Disposable subscribe;
    private Context mContext;
    private UVCCameraHelper mCameraHelper;
    private CameraViewInterface mUVCCameraView;
    private boolean isPreview;
    private boolean isRequest;
    public static final int HEIGHT_DEVICE_VID = 12936;
    public static final int HEIGHT_DEVICE_PID = 7119;


    public HighShotMeterDialog(@NonNull Context context, ClickListener clickListener) {
        super(context, clickListener);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mContext = context;
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_high_shot_meter_dialog;
    }

    @Override
    public void initView() {
        // step.1 initialize UVCCameraHelper
        mUVCCameraView = (CameraViewInterface) binding.sv;
        mUVCCameraView.setCallback(this);
        mCameraHelper = UVCCameraHelper.getInstance(2048, 1536);
        mCameraHelper.setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_MJPEG);
        mCameraHelper.initUSBMonitor((Activity) mContext, mUVCCameraView, cameraListener);
    }

    @Override
    public void initData() {
        // step.2 register USB event broadcast
        if (mCameraHelper != null) {
            Log.d(TAG, "registerUSB");
            mCameraHelper.registerUSB();
        }
        appExecutors = new AppExecutors();
        mFilePath = FileUtils.createFileParent(getContext(), "height_camera");
        binding.sBar.setMax(100);
        binding.sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mCameraHelper != null && mCameraHelper.isCameraOpened()) {
                    mCameraHelper.setModelValue(UVCCameraHelper.MODE_BRIGHTNESS,progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.btnScreen.setOnClickListener(v -> {
            if (null != pathList && pathList.size() >= 3) {
                Toast.makeText(mContext, "最多只能拍摄3张照片",Toast.LENGTH_SHORT).show();
                return;
            }
            String fileName = "sm" + System.currentTimeMillis() + ".jpg";
            File file = new File(mFilePath, fileName);
            String picPath = file.getPath();

            mCameraHelper.capturePicture(picPath, new AbstractUVCCameraHandler.OnCaptureListener() {
                @Override
                public void onCaptureResult(String path) {
                    Log.d(TAG, "Capture---》" + Thread.currentThread().getName());
                    if (TextUtils.isEmpty(path)) {
                        return;
                    }
                    final Bitmap bit = BitmapUtils.getBitmap(path);
                    if (bit != null && isShowing()) {
                        //再次进行压缩图片
                        subscribe = Flowable.just(file)
                                .observeOn(Schedulers.io())
                                .map(f -> {
                                    // 同步方法直接返回压缩后的文件
                                    List<File> files = Luban.with(getContext()).load(f).setTargetDir(mFilePath.getAbsolutePath()).get();
                                    if (files != null && files.size() != 0) {
                                        String absolutePath = files.get(0).getAbsolutePath();
                                        String base64Path = FileUtils.imageToBase64(absolutePath);
                                        mHandler.post(() -> {
                                            if (pathList.size() >= 3) {
                                                Toast.makeText(mContext, "最多只能拍摄3张照片",Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            Timber.e("获取图片 %s", absolutePath);
                                            PreviewPictureEntity entity = new PreviewPictureEntity();
                                            entity.setBase64(base64Path);
                                            entity.setPath(absolutePath);
                                            pathList.add(entity);
                                            mAdapter.notifyDataSetChanged();
                                        });
                                    }
                                    return files;
                                })
                                .subscribe();
                    }

                }

            });

        });

        binding.btnTryAgain.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetermine(mAdapter.getData());
            }
            dismiss();
        });

        mAdapter = new PreviewPageAdapter();
        binding.rvContent.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvContent.setAdapter(mAdapter);
        mAdapter.setNewInstance(pathList);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            PreviewPictureEntity str = mAdapter.getData().get(position);
            showBigPicture(str.getPath());
        });
        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("注意")
                    .setMessage("是否删除此图片")
                    .setPositiveButton("删除", (dialog, which) -> {
                        pathList.remove(position);
                        mAdapter.notifyDataSetChanged();
                    })
                    .show();
            return true;
        });
    }

    @Override
    public void dismiss() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
        Timber.v("执行dismiss");
        if (mCameraHelper != null) {
            Log.d(TAG, "closeCamera");
            mCameraHelper.closeCamera();
            Log.d(TAG, "unregisterUSB");
            mCameraHelper.unregisterUSB();
            mCameraHelper.release();
            Log.d(TAG, "releaseCamera");
        }
        super.dismiss();
    }



    private void showBigPicture(String path) {
        new ToViewBigPictureDialog(getContext(), new ToViewBigPictureDialog.ClickListener() {
        }, new ToViewBigPictureDialog.Builder().setPathFile(path)).show();
    }


    @Override
    public void onSurfaceCreated(CameraViewInterface view, Surface surface) {
        Log.d(TAG, "onSurfaceCreated");
        if (!isPreview && mCameraHelper.isCameraOpened()) {
            mCameraHelper.startPreview(mUVCCameraView);
            isPreview = true;
        }
    }

    @Override
    public void onSurfaceChanged(CameraViewInterface view, Surface surface, int width, int height) {

    }

    @Override
    public void onSurfaceDestroy(CameraViewInterface view, Surface surface) {
        Log.d(TAG, "onSurfaceDestroy");
        if (isPreview && mCameraHelper.isCameraOpened()) {
            mCameraHelper.stopPreview();
            isPreview = false;
        }
    }

    private UVCCameraHelper.OnMyDevConnectListener cameraListener = new UVCCameraHelper.OnMyDevConnectListener() {

        @Override
        public void onAttachDev(UsbDevice device) {
            // request open permission
            Log.d(TAG, "onAttachDev");
            if (!isRequest) {
                isRequest = true;
                requestCurrHightDevicePermission();
            }
        }

        @Override
        public void onDettachDev(UsbDevice device) {
            // close camera
            Log.d(TAG, "onDettachDev");
            if (isRequest) {
                isRequest = false;
                mCameraHelper.closeCamera();
                //showShortMsg(device.getDeviceName() + " is out");
            }
        }

        @Override
        public void onConnectDev(UsbDevice device, boolean isConnected) {
            Log.d(TAG, "onConnectDev");
            if (!isConnected) {
                //showShortMsg("fail to connect,please check resolution params");
                isPreview = false;
            } else {
                isPreview = true;
                //showShortMsg("connecting");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Looper.prepare();
                        if(mCameraHelper != null && mCameraHelper.isCameraOpened()) {
                            binding.sBar.setProgress(mCameraHelper.getModelValue(UVCCameraHelper.MODE_BRIGHTNESS));
                            //contrastBar.setProgress(mCameraHelper.getModelValue(UVCCameraHelper.MODE_CONTRAST));
                        }
                        Looper.loop();
                    }
                }).start();
            }
        }

        @Override
        public void onDisConnectDev(UsbDevice device) {
            Log.d(TAG, "onDisConnectDev");
            //showShortMsg("disconnecting");
        }
    };


    private void requestCurrHightDevicePermission() {
        List<DeviceInfo> infoList = getUSBDevInfo();
        if (infoList == null || infoList.isEmpty()) {
            Toast.makeText(mContext, "Find devices failed.", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < infoList.size(); i++) {
            DeviceInfo deviceInfo = infoList.get(i);
            Log.d(TAG, "Device：PID_" + deviceInfo.getPID() + " & " + "VID_" + deviceInfo.getVID());
            if (deviceInfo.getPID() == HEIGHT_DEVICE_PID && deviceInfo.getVID() == HEIGHT_DEVICE_VID) {
                mCameraHelper.requestPermission(i);
                return;
            }
        }
    }

    private List<DeviceInfo> getUSBDevInfo() {
        if (mCameraHelper == null)
            return null;
        List<DeviceInfo> devInfos = new ArrayList<>();
        List<UsbDevice> list = mCameraHelper.getUsbDeviceList();
        //target:  vid==7119       pid==12936
        for (UsbDevice dev : list) {
            DeviceInfo info = new DeviceInfo();
            info.setPID(dev.getVendorId());
            info.setVID(dev.getProductId());
            devInfos.add(info);
        }
        return devInfos;
    }

    @Override
    public USBMonitor getUSBMonitor() {
        return mCameraHelper.getUSBMonitor();
    }

    @Override
    public void onDialogResult(boolean canceled) {

    }

    public interface ClickListener extends BaseDialogNoListener {

        void onDetermine(List<PreviewPictureEntity> list);
    }
}
