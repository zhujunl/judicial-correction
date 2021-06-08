package com.miaxis.judicialcorrection.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zlw.main.recorderlib.RecordManager;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

/**
 * BaseApplication
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
//@HiltAndroidApp
public class BaseApplication extends Application {

    public   static BaseApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        if (BuildConfig.DEBUG) {
            ARouter.openLog(); // 开启日志
            ARouter.openDebug(); // 使用InstantRun的时候，需要打开该开关，上线之后关闭，否则有安全风险
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(int priority, String tag, @NotNull String message, Throwable t) {
                    super.log(priority, "Mx" + tag, message, t);
                }
            });
        }
        Timber.e("DEBUG:" + BuildConfig.DEBUG);
        ARouter.init(this);
    }
}
