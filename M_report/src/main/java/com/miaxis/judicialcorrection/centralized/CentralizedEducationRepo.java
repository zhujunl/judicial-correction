package com.miaxis.judicialcorrection.centralized;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.Education;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;

/**
 * EnrollRepo
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
@Singleton
public class CentralizedEducationRepo {

    private final ApiService apiService;

    @Inject
    public CentralizedEducationRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<Education>> getEducation(int page, int rows) {
        LiveData<ApiResult<Education>> login = apiService.getEducation(page, rows);
        return ResourceConvertUtils.convertToResource(login);
    }


    public LiveData<Resource<Object>> educationAdd(String id, String pid) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("jzjyId", id);
        hashMap.put("pid", pid);
        LiveData<ApiResult<Object>> login = apiService.educationAdd(hashMap);
        return ResourceConvertUtils.convertToResource(login);
    }


}
