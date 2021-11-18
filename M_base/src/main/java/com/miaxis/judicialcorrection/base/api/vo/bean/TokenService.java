package com.miaxis.judicialcorrection.base.api.vo.bean;

import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.api.TokenResult;

import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TokenService {
    @POST("/oauth/token?grant_type=client_credentials")
    LiveData<TokenResult<TokenBean>> getToken(@Header("Authorization") String token);
}
