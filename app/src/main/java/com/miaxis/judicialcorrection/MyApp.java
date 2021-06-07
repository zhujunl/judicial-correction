package com.miaxis.judicialcorrection;

import com.miaxis.judicialcorrection.base.BaseApplication;
import com.miaxis.judicialcorrection.base.utils.carch.CrashHandler;
import com.tencent.mmkv.MMKV;

import dagger.hilt.android.HiltAndroidApp;

/**
 * MyApp
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
@HiltAndroidApp
public class MyApp extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //        if (!BuildConfig.DEBUG) {
                    CrashHandler crashHandler = CrashHandler.getInstance();
                    crashHandler.init(this);
        //        }
        MMKV.initialize(this);
    }
}
