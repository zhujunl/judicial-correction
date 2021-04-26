package com.miaxis.judicialcorrection.base;

import android.app.Application;


import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;

/**
 * BaseApplication
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
//@HiltAndroidApp
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree(){
                @Override
                protected void log(int priority, String tag, @NotNull String message, Throwable t) {
                    super.log(priority, "Mx"+tag, message, t);
                }
            });
        }
    }
}
