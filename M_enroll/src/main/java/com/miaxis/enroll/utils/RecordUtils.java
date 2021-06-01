package com.miaxis.enroll.utils;

import android.media.AudioFormat;

import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.utils.Logger;

import java.io.File;

import timber.log.Timber;

/**
 *  //https://github.com/zhaolewei/ZlwAudioRecorder
 *  录音
 */
public class RecordUtils {


    private  static  File  pathFile;

    public static void init() {
        //修改录音格式PCM
        RecordManager.getInstance().changeFormat(RecordConfig.RecordFormat.WAV);
        //修改录音配置
        RecordManager.getInstance().changeRecordConfig(RecordManager.getInstance().getRecordConfig().setSampleRate(16000));
        RecordManager.getInstance().changeRecordConfig(RecordManager.getInstance().getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_8BIT));
        //路径
//        RecordManager.getInstance().changeRecordDir(recordDir);
        //监听录音状态
//        RecordManager.getInstance().setRecordStateListener(new RecordStateListener() {
//            @Override
//            public void onStateChange(RecordHelper.RecordState state) {
//                Logger.i("TAG", "状态:" + state);
//            }
//
//            @Override
//            public void onError(String error) {
//                Logger.i("TAG", "状态错误:" + error);
//            }
//        });
        //数据监听
//        RecordManager.getInstance().setRecordDataListener(new RecordDataListener() {
//            @Override
//            public void onData(byte[] data) {
//                Logger.i("TAG", "结果:" + data.length);
//            }
//        });
        //结果
        RecordManager.getInstance().setRecordResultListener(result -> {
            pathFile=result;
            Timber.i("TAG 文件地址:%s" ,result.getAbsolutePath());
        });
    }

    public static File getPathFile() {
        return pathFile;
    }


    /**
     * 开始
     */
    public static void start() {
        RecordManager.getInstance().start();
    }

    /**
     * 开始
     */
    public static void stop() {
        RecordManager.getInstance().stop();
    }

    /**
     * 运行状态
     * @return 是否在录音中
     */
    public static boolean isRunning() {
        RecordHelper.RecordState state = RecordManager.getInstance().getState();
        return state == RecordHelper.RecordState.RECORDING;
    }
}
