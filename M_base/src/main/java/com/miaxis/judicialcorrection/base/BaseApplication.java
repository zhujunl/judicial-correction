package com.miaxis.judicialcorrection.base;

import android.app.Application;

import com.miaxis.judicialcorrection.BuildConfig;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

/**
 * BaseApplication
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
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
