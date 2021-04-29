package com.miaxis.judicialcorrection.base.di;


import android.text.TextUtils;

import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.base.utils.LiveDataCallAdapterFactory;
import com.miaxis.judicialcorrection.base.utils.gson.converter.GsonConverterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * 基础依赖提供
 *
 * @author yawei
 * @data on 2018/7/10 下午1:24
 * @email zyawei@live.com
 */
@InstallIn(SingletonComponent.class)
@Module()
public abstract class AppModule {

    @Singleton
    @Provides
    static AppExecutors appExecutors() {
        return new AppExecutors();
    }


    @Singleton
    @Provides
    static OkHttpClient provideOkHttp(AutoTokenInterceptor autoTokenInterceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(8000, TimeUnit.MILLISECONDS)
                .readTimeout(8000, TimeUnit.MILLISECONDS)
                .writeTimeout(8000, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(autoTokenInterceptor)
                .build();
    }

    @Singleton
    @Provides
    static ApiService provideApiService(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .baseUrl(BuildConfig.SERVER_URL)
                .client(okHttpClient)
                .build()
                .create(ApiService.class);
    }
}
