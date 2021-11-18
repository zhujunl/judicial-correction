package com.miaxis.judicialcorrection.base.api.vo.bean;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.api.TokenResult;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LiveTokenCallAdapter <R> implements CallAdapter<TokenResult<R>, LiveData<TokenResult<R>>> {

    private final Type responseType;

    public LiveTokenCallAdapter(Type responseType) {
        this.responseType = responseType;
    }
    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<TokenResult<R>> adapt(Call<TokenResult<R>> call) {
        return new LiveData<TokenResult<R>>() {
            final AtomicBoolean started = new AtomicBoolean(false);
            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<TokenResult<R>>() {
                        @Override
                        public void onResponse(@NonNull Call<TokenResult<R>> call, @NonNull Response<TokenResult<R>> response) {
                            if (response.isSuccessful()) {
                                Timber.d("HttpResponse, Request :[%s]\nResponse :[%s]", call.request().url(), response.body());
                                postValue(response.body());
                            } else {
                                Timber.w("HttpResponse, Request :[%s]\nResponse :[%d],[%s]", call.request().url(),response.code(), response.message());
                                TokenResult<R> apiResult = new TokenResult<>();
//                                apiResult.code = response.code();
//                                apiResult.msg = response.message();
                                postValue(apiResult);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<TokenResult<R>> call, @NonNull Throwable t) {
                            Timber.w("Http ERROR , Request :[%s] Error :[%s]", call.request().url(), t);
                            TokenResult<R> apiResult = new TokenResult<>();
//                            apiResult.code = 500;
//                            if (TextUtils.isEmpty(t.getMessage())) {
//                                apiResult.msg = t.toString();
//                            } else {
//                                apiResult.msg = t.getMessage();
//                            }
                            postValue(apiResult);
                        }
                    });
                }
            }
        };
    }
}
