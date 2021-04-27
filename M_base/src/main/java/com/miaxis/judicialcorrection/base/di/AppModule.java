package com.miaxis.judicialcorrection.base.di;


import android.text.TextUtils;

import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.base.utils.LiveDataCallAdapterFactory;
import com.miaxis.judicialcorrection.base.utils.gson.converter.GsonConverterFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    static OkHttpClient provideOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(8000, TimeUnit.MILLISECONDS)
                .readTimeout(8000, TimeUnit.MILLISECONDS)
                .writeTimeout(8000, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder newBuilder = original.newBuilder();
                    String value = "Bearer d1a381f9-8485-4bbf-ad10-c5be681979e3";
                    if (!TextUtils.isEmpty(value)) {
                        newBuilder.addHeader("Authorization", value);
                    }
                    if (Objects.equals("POST", original.method())) {
                        RequestBody bodyUnSign = original.body();
                        assert bodyUnSign != null;
                        newBuilder.post(bodyUnSign);
                    } else if (Objects.equals("PUT", original.method())) {
                        RequestBody bodyUnSign = original.body();
                        assert bodyUnSign != null;
                        newBuilder.put(bodyUnSign);
                    } else {
                        newBuilder.get();
                    }
                    Request request = newBuilder.build();
                    Timber.v("OKHttp Request URL= [%s]", original.url());
                    //Timber.v("OKHttp Request Header=[%s]\nURL= [%s]", request.headers(), request.url());
                    return chain.proceed(request);
                })
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
