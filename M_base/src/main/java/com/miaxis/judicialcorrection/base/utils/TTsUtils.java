package com.miaxis.judicialcorrection.base.utils;

import android.speech.tts.TextToSpeech;

import com.miaxis.judicialcorrection.base.BaseApplication;

import java.util.Locale;

import timber.log.Timber;

/**
 * 语音提示 tts
 *  1是否支持语音功能 2.安装谷歌语音apk 在设置中设置语音为谷歌语音
 */
public class TTsUtils {

    private static TextToSpeech mTTs;

    private static boolean isSucceed = false;


    private static void init(TextToSpeech.OnInitListener listener) {
        //初始化tts监听对象
        mTTs = new TextToSpeech(BaseApplication.application, listener);
    }

    public static void textToSpeechStr(String str) {
        if (null == mTTs || !isSucceed) {
            init(status -> {
                Timber.i("初始化状态 %s", status);
                isSucceed = status == TextToSpeech.SUCCESS;
                if (isSucceed) {
                    if (mTTs != null) {
                        Timber.i("初始化状态 %s", "设置中文");
                        mTTs.setLanguage(Locale.SIMPLIFIED_CHINESE);
                    }
                    setSpeak(str);
                }
            });
        } else {
            setSpeak(str);
        }
    }

    private static void setSpeak(String str) {
        if (mTTs != null) {
            //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
            mTTs.speak(str, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    public  static void close(){
        if (mTTs!=null) {
            mTTs.stop(); // 不管是否正在朗读TTS都被打断
            mTTs.shutdown(); // 关闭，释放资源
        }
    }
}
