package com.miaxis.judicialcorrection.dialog;

import android.content.Context;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.miaxis.camera.CameraConfig;
import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.CameraPreviewCallback;
import com.miaxis.camera.MXCamera;
import com.miaxis.camera.MXFrame;
import com.miaxis.judicialcorrection.adapter.PreviewAdapter;
import com.miaxis.judicialcorrection.base.R;
import com.miaxis.judicialcorrection.base.databinding.DialogHighShotMeterDialogBinding;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;
import com.miaxis.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


/**
 * 高拍仪
 */
public class HighShotMeterDialog extends BaseDialog<DialogHighShotMeterDialogBinding, HighShotMeterDialog.ClickListener> implements CameraPreviewCallback {

    private final Builder mBuilder;

    private File mFilePath;
    private final static Handler mHandler = new Handler();
    private List<String> base64List = new ArrayList<>();
    private List<String> pathList = new ArrayList<>();
    private PreviewAdapter mAdapter;
    @Inject
    AppHints mAppHints;

    public HighShotMeterDialog(@NonNull Context context, ClickListener clickListener, @NonNull Builder builder) {
        super(context, clickListener);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        this.mBuilder = builder;
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_high_shot_meter_dialog;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        mFilePath = FileUtils.createFileParent(getContext());
        binding.sv.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                ZZResponse<?> init = CameraHelper.getInstance().init();
                if (ZZResponse.isSuccess(init)) {
                    ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createMXCamera(CameraConfig.Camera_SM);
                    if (ZZResponse.isSuccess(mxCamera)) {
                        binding.sBar.setMax(mxCamera.getData().getMaxZoom());
//                      mxCamera.getData().setOrientation(CameraConfig.Camera_SM.previewOrientation);
                        mxCamera.getData().setPreviewCallback(HighShotMeterDialog.this);
                        mxCamera.getData().start(holder);
//                        mxCamera.getData().setNextFrameEnable();
                    }
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                CameraHelper.getInstance().free();
            }
        });
        binding.sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createMXCamera(CameraConfig.Camera_SM);
                if (ZZResponse.isSuccess(mxCamera)) {
                    mxCamera.getData().setZoom(progress);
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
            ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createMXCamera(CameraConfig.Camera_SM);
            if (ZZResponse.isSuccess(mxCamera)) {
                mxCamera.getData().setNextFrameEnable();
            }
        });

        binding.btnTryAgain.setOnClickListener(v -> {
            CameraHelper.getInstance().free();
            if (listener != null) {
                listener.onDetermine(base64List);
            }
            dismiss();
        });
        mAdapter = new PreviewAdapter();
        binding.rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvContent.setAdapter(mAdapter);

        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
//        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            String str = mAdapter.getData().get(position);
            showBigPicture(str);
        });
        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            mAppHints.showError("是否删除此图片", (dialog, which) -> {
                pathList.remove(position);
                mAdapter.notifyDataSetChanged();
            });
            return false;
        });
    }


    private void showBigPicture(String path) {
        new ToViewBigPictureDialog(getContext(), new ToViewBigPictureDialog.ClickListener() {
            @Override
            public void onTryAgain(AppCompatDialog appCompatDialog) {

            }

            @Override
            public void onTimeOut(AppCompatDialog appCompatDialog) {

            }
        }, new ToViewBigPictureDialog.Builder().setPathFile(path)).show();
    }


    @Override
    public void onPreview(MXFrame frame) {
        if (MXFrame.isNullCamera(frame)) {
            return;
        }
        String fileName = "sm" + System.currentTimeMillis() + ".jpg";
        File file = new File(mFilePath, fileName);
        boolean frameImage = frame.camera.saveFrameImage(file.getAbsolutePath());
        if (frameImage) {
            String base64Path = FileUtils.imageToBase64(file.getAbsolutePath());
            mHandler.post(() -> {
                Timber.e("获取图片 %s", file.getAbsolutePath());
                base64List.add(base64Path);
                pathList.add(file.getAbsolutePath());
                mAdapter.setNewInstance(pathList);
            });
        }
    }


    public interface ClickListener extends BaseDialogListener {

        void onDetermine(List<String> base64List);
    }

    public static class Builder {

        public String filePath;

        public Builder() {
        }

        public Builder setPathFile(String filePath) {
            this.filePath = filePath;
            return this;
        }
    }
}
