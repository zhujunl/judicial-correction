package com.miaxis.judicialcorrection.base.repo;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

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
public class PersonRepo {

    private final ApiService apiService;

    @Inject
    public PersonRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<PersonInfo>> personInfo(String idCardNumber) {
        LiveData<ApiResult<PersonInfo>> login = apiService.login(idCardNumber);
        return ResourceConvertUtils.convertToResource(login);
    }

    public LiveData<Resource<Object>> getFace(String id) {
        LiveData<ApiResult<Object>> login = apiService.getFace(id);
        return ResourceConvertUtils.convertToResource(login);
    }

}
