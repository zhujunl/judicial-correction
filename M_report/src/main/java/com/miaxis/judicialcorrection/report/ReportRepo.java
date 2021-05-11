package com.miaxis.judicialcorrection.report;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
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
public class ReportRepo {

    private final ApiService apiService;

    @Inject
    public ReportRepo(ApiService apiService) {
        this.apiService = apiService;
    }

//    public LiveData<Resource<Report>> getReport(String pid, int page, int rows) {
//        //        HashMap<String, String> hashMap = new HashMap<>();
//        //        hashMap.put("pid", pid);
//        //        hashMap.put("page", "" + page);
//        //        hashMap.put("rows", "" + rows);
//        //        LiveData<ApiResult<Report>> login = apiService.getReport(hashMap);
//        LiveData<ApiResult<Report>> login = apiService.getReport(pid, page, rows);
//        return ResourceConvertUtils.convertToResource(login);
//    }


    public LiveData<Resource<Object>> reportAdd(String pid) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pid", pid);
        hashMap.put("type", "1");
        LiveData<ApiResult<Object>> login = apiService.reportAdd(hashMap);
        return ResourceConvertUtils.convertToResource(login);
    }


}
