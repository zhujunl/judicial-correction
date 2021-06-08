package com.miaxis.enroll.guide.voice;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.api.ApiOtherResult;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.MD5Utils;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

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
        map.put("fingerprints",new String[]{base64Str});
        String toJson = gson.toJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiOtherResult<Object>> apiResultLiveData = apiService.uploadVoicePrint(body);
        return ResourceConvertUtils.convertToResourceFV(apiResultLiveData);
    }
}
