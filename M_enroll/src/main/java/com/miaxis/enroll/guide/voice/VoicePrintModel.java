package com.miaxis.enroll.guide.voice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.judicialcorrection.base.BaseApplication;
import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
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

    public MutableLiveData<File> observableFile = new MutableLiveData<>();

    @Inject
    public VoicePrintModel(AppExecutors mAppExecutors, VoicePrintRepo voicePrintRepo) {
        this.voicePrintRepo = voicePrintRepo;
        init();
    }


    public LiveData<Resource<Object>> uploadVoicePrint(String id, String base64Str) {
        return voicePrintRepo.uploadVoicePrint(id, base64Str);
    }


    final RecordManager mRecordManager = RecordManager.getInstance();


    public void init() {
        mRecordManager.init(BaseApplication.application, BuildConfig.DEBUG);
        mRecordManager.changeFormat(RecordConfig.RecordFormat.PCM);
//        String recordDir = String.format(Locale.getDefault(), "%s/Record/",
//                Environment.getExternalStorageDirectory().getAbsolutePath());
//        mRecordManager.changeRecordDir(recordDir);
        initRecordEvent();
    }

    private void initRecordEvent() {
        mRecordManager.setRecordResultListener(result -> {
            observableFile.postValue(result);
            Timber.i("路径: %s", result.getAbsolutePath());
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
        if (isRunning()) {
            stop();
        }
        if (mRecordManager != null) {
            mRecordManager.setRecordResultListener(null);
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
     * 是否空闲
     * @return
     */
    public  boolean isIdle() {
        RecordHelper.RecordState state = RecordManager.getInstance().getState();
        return state == RecordHelper.RecordState.IDLE;
    }

}