package com.miaxis.enroll.guide.voice;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.VocalPrintEntity;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.MD5Utils;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;


@Singleton
public class VoicePrintRepo {

    private final ApiService apiService;

    @Inject
    public VoicePrintRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    private final Gson gson = new Gson();

    public LiveData<Resource<Object>> uploadVoicePrint(String id,String base64Str) {
        Map<String,Object> map=new HashMap<>();
        String sign=ApiService.appkey+ApiService.appsecret;
        String s = MD5Utils.md5(sign);
        map.put("id",id);
        map.put("sign",s);
        map.put("appkey",ApiService.appkey);
        map.put("vocalprint",new String[]{base64Str});
        String toJson = gson.toJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        //配置地址
        String url = MMKV.defaultMMKV().getString("baseUrl2", BuildConfig.SERVER_URL2) + ApiService.uploadVoice;
        LiveData<ApiResult<Object>> apiResultLiveData = apiService.uploadVoicePrint(url,body);
        return ResourceConvertUtils.convertToResourceFV(apiResultLiveData);
    }

    /**
     * 得到声纹
     * @param id
     * @return
     */
    public LiveData<Resource<VocalPrintEntity>> getVocalPrint(String id) {
        Map<String,Object> map=new HashMap<>();
        String sign=ApiService.appkey+ApiService.appsecret;
        String s = MD5Utils.md5(sign);
        map.put("id",id);
        map.put("sign",s);
        map.put("appkey",ApiService.appkey);
        String toJson = gson.toJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        //配置地址
        String url = MMKV.defaultMMKV().getString("baseUrl2", BuildConfig.SERVER_URL2) + ApiService.getVoice;
        LiveData<ApiResult<VocalPrintEntity>> apiResultLiveData = apiService.getVocalPrint(url,body);
        return ResourceConvertUtils.convertToResourceFV(apiResultLiveData);
    }



}
