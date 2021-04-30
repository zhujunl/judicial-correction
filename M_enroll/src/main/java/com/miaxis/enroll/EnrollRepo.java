package com.miaxis.enroll;

import android.app.Person;

import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * EnrollRepo
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
@Singleton
public class EnrollRepo {

    private final ApiService apiService;

    @Inject
    public EnrollRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<PersonInfo>> login(String idCardNumber){
        LiveData<ApiResult<PersonInfo>> login = apiService.login(idCardNumber);
        return ResourceConvertUtils.convertToResource(login);
    }

}
