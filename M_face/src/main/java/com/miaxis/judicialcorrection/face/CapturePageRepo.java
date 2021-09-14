package com.miaxis.judicialcorrection.face;

import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.RequestBody;

/**
 * EnrollRepo
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
@Singleton
public class CapturePageRepo {

    private final ApiService apiService;

    @Inject
    public CapturePageRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<Object>> uploadFace(String id, String base64Face) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("id",RequestBody.create(null, String.valueOf(id)));
        map.put("base64Face",RequestBody.create(null, String.valueOf(base64Face)));

        ApiResult<Object> objectApiResult = new ApiResult<>();
        objectApiResult.code = 0;
        LiveData<ApiResult<Object>> apiResultLiveData = new LiveData<ApiResult<Object>>(objectApiResult){};
//        LiveData<ApiResult<Object>> apiResultLiveData = apiService.uploadFace(map);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }


}
