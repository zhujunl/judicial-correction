package com.miaxis.judicialcorrection.live;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressChangeDetailsBean;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressListBean;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;
import com.miaxis.judicialcorrection.bean.LiveAddressChangeBean;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

@Singleton
public class LiveAddressChangeRepo {

    private final ApiService apiService;

    @Inject
    public LiveAddressChangeRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    private final   Gson gson=new Gson();
    @SuppressWarnings("all")
    public LiveData<Resource<Object>> setLiveAddressChange(LiveAddressChangeBean liveAddressChangeBean,String jzdbgsqs,String jzdbgsqcl) {
        String cardTypeJson = gson.toJson(liveAddressChangeBean);
        Map<String, Object> hashMap= new Gson().fromJson(cardTypeJson,Map.class);
        //文件数组方式base64

        if (!TextUtils.isEmpty(jzdbgsqs)){
            hashMap.put("jzdbgsqs", new String[]{jzdbgsqs});
        }
        if (!TextUtils.isEmpty(jzdbgsqcl)){
            hashMap.put("jzdbgsqcl", new String[]{jzdbgsqcl});
        }
        String toJson = gson.toJson(hashMap);
        Timber.e("setLiveAddressChange %s",toJson);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiResult<Object>> apiResultLiveData=  apiService.changeLiveAddress(body);

        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }

    @SuppressWarnings("all")
    public LiveData<Resource<LiveAddressListBean>> searchLiveAddressChangeList(int page, int rows, String pid) {
        LiveData<ApiResult<LiveAddressListBean>> apiResultLiveData = apiService.searchLiveAddressChangeList(page, rows, pid);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }

    @SuppressWarnings("all")
    public LiveData<Resource<LiveAddressChangeDetailsBean>> getLiveAddressChangeDetails(String id) {
        LiveData<ApiResult<LiveAddressChangeDetailsBean>> liveAddressChangeDetails = apiService.getLiveAddressChangeDetails(id);
        return ResourceConvertUtils.convertToResource(liveAddressChangeDetails);
    }

}
