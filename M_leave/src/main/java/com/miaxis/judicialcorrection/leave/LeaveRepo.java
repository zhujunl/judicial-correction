package com.miaxis.judicialcorrection.leave;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.Leave;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import timber.log.Timber;

/**
 * EnrollRepo
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
@Singleton
public class LeaveRepo {

    private final ApiService apiService;

    @Inject
    public LeaveRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<Leave>> getLeaveList(String pid, int page, int rows) {
        LiveData<ApiResult<Leave>> login = apiService.getLeaveList(pid, page, rows);
        return ResourceConvertUtils.convertToResource(login);
    }

    public LiveData<Resource<Object>> leaveAdd(String pid,
                                               String sqsj,
                                               String xjms,
                                                String[] qjsqs,
                                                String[] qjsqcl,
                                               String wcly,
                                               String jsrq,
                                               String ksqr,
                                               String wcts,
                                               String sfyxj,

                                               String wcmddxz,
                                               String wcmddmx,
                                               String wcmddszs,
                                               String wcmddszx,
                                               String wcmddszd
    ) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sqsj", sqsj);
        hashMap.put("xjms", xjms);
        hashMap.put("pid", pid);
        if (qjsqs!=null&&qjsqs.length!=0){
            for (String str:qjsqs) {
                if (!TextUtils.isEmpty(str)) {
                    hashMap.put("qjsqs", qjsqs);
                    break;
                }
            }
        }
        if (qjsqcl!=null&&qjsqcl.length!=0){
            for (String str:qjsqcl) {
                if (!TextUtils.isEmpty(str)) {
                    hashMap.put("qjsqcl", qjsqcl);
                    break;
                }
            }
        }
        List<Bean> list = new ArrayList<>();
        Bean bean = new Bean();
        bean.pid = pid;
        bean.wcmddxz = wcmddxz;
        bean.wcmddmx = wcmddmx;
        bean.wcmddszs = wcmddszs;
        bean.wcmddszx = wcmddszx;
        bean.wcmddszd = wcmddszd;
        list.add(bean);
        hashMap.put("list", list);

        hashMap.put("wcly", wcly);
        hashMap.put("jsrq", jsrq);
        hashMap.put("ksqr", ksqr);
        hashMap.put("wcts", wcts);
        hashMap.put("sfyxj", sfyxj);

        Timber.i("leaveAdd:%s", new Gson().toJson(hashMap));
        LiveData<ApiResult<Object>> login = apiService.leaveAdd(hashMap);
        return ResourceConvertUtils.convertToResource(login);
    }

    public LiveData<Resource<Object>> leaveEnd(String id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        LiveData<ApiResult<Object>> login = apiService.leaveEnd(hashMap);
        return ResourceConvertUtils.convertToResource(login);
    }

    public LiveData<Resource<Leave.ListBean>> getLeave(String id) {
        LiveData<ApiResult<Leave.ListBean>> login = apiService.getLeave(id);
        return ResourceConvertUtils.convertToResource(login);
    }


    public static class Bean {
        public String pid;
        public String wcmddxz;
        public String wcmddmx;
        public String wcmddszs;
        public String wcmddszx;
        public String wcmddszd;
    }

}
