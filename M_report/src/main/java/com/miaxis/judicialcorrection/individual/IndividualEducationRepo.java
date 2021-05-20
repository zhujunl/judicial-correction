package com.miaxis.judicialcorrection.individual;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.IndividualEducationBean;
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
public class IndividualEducationRepo {

    private final ApiService apiService;

    @Inject
    public IndividualEducationRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<Object>> individualAdd( String pid,String id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pid", pid);
//        hashMap.put("id",id);
        LiveData<ApiResult<Object>> login = apiService.personEducationAdd(hashMap);
        return ResourceConvertUtils.convertToResource(login);
    }

    public LiveData<Resource<IndividualEducationBean>> getPersonEducation(String pid) {
        LiveData<ApiResult<IndividualEducationBean>> login = apiService.getPersonEducation(pid,1,100);
        return ResourceConvertUtils.convertToResource(login);
    }

}
