package com.miaxis.judicialcorrection.base.di;


import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.NoAuthApiService;
import com.miaxis.judicialcorrection.base.api.vo.bean.CloudService;
import com.miaxis.judicialcorrection.base.api.vo.bean.LiveTokenCallAdapterFactory;
import com.miaxis.judicialcorrection.base.api.vo.bean.TokenService;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.base.utils.LiveDataCallAdapterFactory;
import com.miaxis.judicialcorrection.base.utils.gson.converter.GsonConverterFactory;
import com.tencent.mmkv.MMKV;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
    @AuthInterceptorOkHttpClient
    @Provides
    static OkHttpClient provideAutoAuthOkHttp(AutoTokenInterceptor autoTokenInterceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(8000, TimeUnit.MILLISECONDS)
                .readTimeout(8000, TimeUnit.MILLISECONDS)
                .writeTimeout(8000, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(autoTokenInterceptor)
                .build();
    }


    @Singleton
    @Provides
    static ApiService provideApiService(@AuthInterceptorOkHttpClient OkHttpClient okHttpClient) {
        String baseUrl = MMKV.defaultMMKV().getString("baseUrl", BuildConfig.SERVER_URL);
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
                .create(ApiService.class);
    }


    @Singleton
    @Provides
    @OtherInterceptorOkHttpClient
    static OkHttpClient provideOtherOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(8000, TimeUnit.MILLISECONDS)
                .readTimeout(8000, TimeUnit.MILLISECONDS)
                .writeTimeout(8000, TimeUnit.MILLISECONDS)
                .build();
    }

    @Singleton
    @Provides
    static NoAuthApiService provideNoAuthApiService(@OtherInterceptorOkHttpClient OkHttpClient okHttpClient) {
        String baseUrl = MMKV.defaultMMKV().getString("baseUrl", BuildConfig.SERVER_URL);
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
                .create(NoAuthApiService.class);
    }


    @Singleton
    @Provides
    static CloudService provideNoAuthApiService2(@OtherInterceptorOkHttpClient OkHttpClient okHttpClient) {
        String baseUrl = MMKV.defaultMMKV().getString("baseUrl", BuildConfig.SERVER_URL3);
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
                .create(CloudService.class);
    }

    @Singleton
    @Provides
    static TokenService provideNoAuthApiService3(@OtherInterceptorOkHttpClient OkHttpClient okHttpClient) {
        String baseUrl = MMKV.defaultMMKV().getString("baseUrl", BuildConfig.TOKEN_URL3);
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveTokenCallAdapterFactory())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
                .create(TokenService.class);
    }
}
