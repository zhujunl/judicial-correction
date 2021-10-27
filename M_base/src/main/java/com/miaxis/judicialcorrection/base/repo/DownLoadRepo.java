package com.miaxis.judicialcorrection.base.repo;


import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.ApkVersionResult;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
@Singleton
public class DownLoadRepo {

    private final ApiService apiService;

    @Inject
    public DownLoadRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    private final Gson gson = new Gson();

    public LiveData<ApiResult<ApkVersionResult>> compareVersions(String type, String apkName, String apkVersion) {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("type", type);
        paramsMap.put("apkName", apkName);
        paramsMap.put("apkVersion", apkVersion);
        String toJson = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        String url = MMKV.defaultMMKV().getString("baseUrl2", BuildConfig.SERVER_URL2) + ApiService.compareVersions;
        return apiService.compareVersions(url,body);
    }

    public Call<ResponseBody> downApk(String apkId) {
        String url = MMKV.defaultMMKV().getString("baseUrl2", BuildConfig.SERVER_URL2) + ApiService.downAPK + "?apkId=" +apkId;
        return apiService.downApk(url);
    }
}
