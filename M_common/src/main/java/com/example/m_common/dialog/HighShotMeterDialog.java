package com.example.m_common.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_common.R;
import com.example.m_common.adapter.PreviewPageAdapter;
import com.example.m_common.bean.PreviewPictureEntity;
import com.example.m_common.databinding.DialogHighShotMeterDialogBinding;
import com.miaxis.camera.CameraConfig;
import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.CameraPreviewCallback;
import com.miaxis.camera.MXCamera;
import com.miaxis.camera.MXFrame;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogNoListener;
import com.miaxis.judicialcorrection.dialog.base.BaseNoListenerDialog;
import com.miaxis.utils.FileUtils;

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
public class HighShotMeterDialog extends BaseNoListenerDialog<DialogHighShotMeterDialogBinding, HighShotMeterDialog.ClickListener> implements CameraPreviewCallback {

    private File mFilePath;
    private final static Handler mHandler = new Handler();

    private List<PreviewPictureEntity> pathList = new ArrayList<>();
    private PreviewPageAdapter mAdapter;
    private AppExecutors appExecutors;
    private Disposable subscribe;

    public HighShotMeterDialog(@NonNull Context context, ClickListener clickListener) {
        super(context, clickListener);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
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
        appExecutors = new AppExecutors();
        mFilePath = FileUtils.createFileParent(getContext(), "height_camera");
        binding.sv.getHolder().addCallback(callback);
        binding.sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().find(CameraConfig.Camera_SM);
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
            ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().find(CameraConfig.Camera_SM);
            if (ZZResponse.isSuccess(mxCamera)) {
                mxCamera.getData().setNextFrameEnable();
            }
        });

        binding.btnTryAgain.setOnClickListener(v -> {
            CameraHelper.getInstance().free();
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
        binding.sv.getHolder().removeCallback(callback);
        super.dismiss();
    }

    private void showBigPicture(String path) {
        new ToViewBigPictureDialog(getContext(), new ToViewBigPictureDialog.ClickListener() {
        }, new ToViewBigPictureDialog.Builder().setPathFile(path)).show();
    }


    @Override
    public void onPreview(MXFrame frame) {
        if (MXFrame.isNullCamera(frame)) {
            return;
        }
        appExecutors.diskIO().execute(() -> {
            String fileName = "sm" + System.currentTimeMillis() + ".jpg";
            File file = new File(mFilePath, fileName);
            //保存图片
            boolean frameImage = frame.camera.saveFrameImage(file.getAbsolutePath());
            if (frameImage && isShowing()) {
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
        });
    }

    private final SurfaceHolder.Callback callback= new SurfaceHolder.Callback(){

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            ZZResponse<?> init = CameraHelper.getInstance().init();
            if (ZZResponse.isSuccess(init)) {
                ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createMXCamera(CameraConfig.Camera_SM);
                if (ZZResponse.isSuccess(mxCamera)) {
                    binding.sBar.setMax(mxCamera.getData().getMaxZoom());
                    mxCamera.getData().setPreviewCallback(HighShotMeterDialog.this);
                    mxCamera.getData().start(holder);
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
    };

    public interface ClickListener extends BaseDialogNoListener {

        void onDetermine(List<PreviewPictureEntity> list);
    }
}
