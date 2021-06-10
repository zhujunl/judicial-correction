package com.miaxis.enroll.guide.voice;

import android.os.Environment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.camera.CameraConfig;
import com.miaxis.camera.MXFrame;
import com.miaxis.enroll.vo.VoiceEntity;
import com.miaxis.judicialcorrection.base.BaseApplication;
import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.face.utils.FileUtil;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class VoicePrintModel extends ViewModel {


    private final VoicePrintRepo voicePrintRepo;

    public MutableLiveData<VoiceEntity> observableFile = new MutableLiveData<>();

    private  AppExecutors  mAppExecutors;

    @Inject
    public VoicePrintModel(AppExecutors mAppExecutors, VoicePrintRepo voicePrintRepo) {
        this.voicePrintRepo = voicePrintRepo;
        this.mAppExecutors=mAppExecutors;
        init();
    }


    public LiveData<Resource<Object>> uploadVoicePrint(String id, String base64Str) {
        return voicePrintRepo.uploadVoicePrint(id, base64Str);
    }

    final RecordManager mRecordManager = RecordManager.getInstance();

    public void init() {
        mRecordManager.init(BaseApplication.application, BuildConfig.DEBUG);
        mRecordManager.changeFormat(RecordConfig.RecordFormat.PCM);

        String path = BaseApplication.application.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        mRecordManager.changeRecordDir(path);
        initRecordEvent();
    }

    private void initRecordEvent() {
        mRecordManager.setRecordResultListener(result -> {
            mAppExecutors.networkIO().execute(() -> {
                String s = FileUtil.fileToBase64(result);
                VoiceEntity entity=new VoiceEntity();
                entity.path=result.getAbsolutePath();
                entity.base64Path=s;
                observableFile.postValue(entity);
                Timber.i("路径: %s", result.getAbsolutePath());
            });
        });
    }

    /**
     * 开始
     */
    public void start() {
        if (mRecordManager != null) {
            mRecordManager.start();
        }
    }

    /**
     * 开始
     */
    public void stop() {
        if (mRecordManager != null) {
            mRecordManager.stop();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mRecordManager != null) {
            mRecordManager.setRecordResultListener(null);
        }
        if (isRunning()) {
            stop();
        }
    }

    /**
     * 运行状态
     *
     * @return 是否在录音中
     */
    public  boolean isRunning() {
        RecordHelper.RecordState state = RecordManager.getInstance().getState();
        return state == RecordHelper.RecordState.RECORDING || state == RecordHelper.RecordState.PAUSE;
    }

    /**
     *
     */
    public  boolean isIdle() {
        RecordHelper.RecordState state = RecordManager.getInstance().getState();
        return state == RecordHelper.RecordState.IDLE;
    }

}