package com.miaxis.judicialcorrection.benefit;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.HistorySignUpBean;
import com.miaxis.judicialcorrection.base.api.vo.SignUpBean;
import com.miaxis.judicialcorrection.base.api.vo.SingUpJsonBean;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.Query;
import timber.log.Timber;

@Singleton
public class PublicWelfareRepo {

    private final ApiService apiService;

    @Inject
    public PublicWelfareRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    @SuppressWarnings("all")
    public LiveData<Resource<SignUpBean>> addSignUpParameter(int page, int rows) {
        LiveData<ApiResult<SignUpBean>> activityListInfo = apiService. getActivityListInfo(page, rows);
        return ResourceConvertUtils.convertToResource(activityListInfo);
    }

    @SuppressWarnings("all")
    public LiveData<Resource<HistorySignUpBean>> addHistorySignUpParameter(int page, int rows, String pid) {
        LiveData<ApiResult<HistorySignUpBean>> activityListInfo = apiService.getHistoryActivityInfo(page, rows,pid);
        return ResourceConvertUtils.convertToResource(activityListInfo);
    }

    @SuppressWarnings("all")
    public LiveData<Resource<Object>> participate(String pid,String gyldId) {
        SingUpJsonBean jsonBean =new SingUpJsonBean();
        jsonBean.gyldId=gyldId;
        jsonBean.pid=pid;
        String toJson = new Gson().toJson(jsonBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiResult<Object>> apiResultLiveData=  apiService.participate(body);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }
}
