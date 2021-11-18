package com.miaxis.judicialcorrection.base.api.vo.bean;

import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class CloudRep {
    private final CloudService apiService;

    @Inject
    public CloudRep(CloudService apiService) {
        this.apiService = apiService;
    }

//    public LiveData<Resource<TokenBean>> getToken(){
//        LiveData<ApiResult<TokenBean>> login = apiService.getToken("Basic eGlueGlodWEucGxhdGZvcm06WGlueGlodWEyMDIx");
//        return ResourceConvertUtils.convertToResource(login);
//    }

    public LiveData<Resource<DailyBean>> getreport(String token,int page, int rows, String pid){
        LiveData<ApiResult<DailyBean>> login = apiService.getreport(token,page, rows,pid);
        Timber.tag("getreory:").e("{token=" + token + ",page=" + page + ",rows=" + rows + ",pid=" + pid+"}");
        return ResourceConvertUtils.convertToResource(login);
    }

    public LiveData<Resource<CentralizedBean>> getEducation(String token,int page, int rows, String pid){
        LiveData<ApiResult<CentralizedBean>> login = apiService.getEducation(token,page, rows,pid);
        Timber.tag("getEducation:").e("{token=" + token + ",page=" + page + ",rows=" + rows + ",pid=" + pid+"}");
        return ResourceConvertUtils.convertToResource(login);
    }

    public LiveData<Resource<IndividualBean>> getPersonEducation(String token, int page, int rows, String pid){
        LiveData<ApiResult<IndividualBean>> login = apiService.getPersonEducation(token,pid, rows,page);
        Timber.tag("getPersonEducation:").e("{token=" + token + ",page=" + page + ",rows=" + rows + ",pid=" + pid+"}");
        return ResourceConvertUtils.convertToResource(login);
    }

    public LiveData<Resource<WelfareBean>> getHistoryActivityInfo(String token, int page, int rows, String pid){
        LiveData<ApiResult<WelfareBean>> login = apiService.getHistoryActivityInfo(token,page, rows,pid+"}");
        Timber.tag("getHistoryActivityInfo:").e("{token=" + token + ",page=" + page + ",rows=" + rows + ",pid=" + pid);
        return ResourceConvertUtils.convertToResource(login);
    }



    public LiveData<Resource<AdmonitionBean>> getAdmonition(String token,String pid){
        LiveData<ApiResult<AdmonitionBean>> login = apiService.getAdmonition(token,pid);
        Timber.tag("getAdmonition:").e("{token=" + token  + ",pid=" + pid+"}");
        return ResourceConvertUtils.convertToResource(login);
    }

    public LiveData<Resource<WarningBean>> getWarning(String token,String pid){
        LiveData<ApiResult<WarningBean>> login = apiService.getWarning(token,pid);
        Timber.tag("getWarning:").e("{token=" + token  + ",pid=" + pid+"}");
        return ResourceConvertUtils.convertToResource(login);
    }
}
