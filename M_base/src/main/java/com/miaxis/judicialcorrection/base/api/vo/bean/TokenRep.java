package com.miaxis.judicialcorrection.base.api.vo.bean;

import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.api.TokenResult;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TokenRep {
    private TokenService apiService;

    @Inject
    public TokenRep(TokenService tokenService) {
        this.apiService = tokenService;
    }

    public LiveData<TokenResult<TokenBean>> getToken(){
        LiveData<TokenResult<TokenBean>> login = apiService.getToken("Basic eGlueGlodWEucGxhdGZvcm06WGlueGlodWEyMDIx");
        return login;
    }

}
