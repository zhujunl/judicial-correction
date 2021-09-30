package com.miaxis.judicialcorrection.base.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;


import com.miaxis.judicialcorrection.base.api.ApiResult;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A Retrofit adapter that converts the Call into a LiveData of Response.
 *
 * @author yawei
 */
public class LiveDataCallAdapter<R> implements CallAdapter<ApiResult<R>, LiveData<ApiResult<R>>> {
    private final Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<ApiResult<R>> adapt(@NonNull final Call<ApiResult<R>> call) {
        return new LiveData<ApiResult<R>>() {
            AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<ApiResult<R>>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResult<R>> call, @NonNull Response<ApiResult<R>> response) {
                            if (response.isSuccessful()) {
                                Timber.d("HttpResponse, Request :[%s]\nResponse :[%s]", call.request().url(), response.body());
                                postValue(response.body());
                            } else {
                                Timber.w("HttpResponse, Request :[%s]\nResponse :[%d],[%s]", call.request().url(),response.code(), response.message());
                                ApiResult<R> apiResult = new ApiResult<>();
                                apiResult.code = response.code();
                                apiResult.msg = response.message();
                                postValue(apiResult);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiResult<R>> call, @NonNull Throwable t) {
                            Timber.w("Http ERROR , Request :[%s] Error :[%s]", call.request().url(), t);
                            ApiResult<R> apiResult = new ApiResult<>();
                            apiResult.code = 500;
                            if (TextUtils.isEmpty(t.getMessage())) {
                                apiResult.msg = t.toString();
                            } else {
                                apiResult.msg = t.getMessage();
                            }
                            postValue(apiResult);
                        }
                    });
                }
            }
        };
    }
}
