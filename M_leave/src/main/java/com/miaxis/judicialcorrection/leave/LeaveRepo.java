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

import okhttp3.MediaType;
import okhttp3.RequestBody;
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
                                               String qjsqs,
                                               String qjsqcl,
                                               String wclyType,
                                               String lsjhr,
                                               String gx,
                                               String lxdh,
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
                                               ,
                                                String wcmddxzName,
                                               String wcmddszsName,
                                               String wcmddszxName,
                                               String wcmddszdName
    ) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sqsj", sqsj);
        hashMap.put("xjms", xjms);
        hashMap.put("pid", pid);
        hashMap.put("wclyName",wclyType);

        if (!TextUtils.isEmpty(qjsqs)){
            hashMap.put("qjsqs", new String[]{qjsqs});
        }
        if (!TextUtils.isEmpty(qjsqcl)){
            hashMap.put("qjsqcl", new String[]{qjsqcl});
        }

        List<Bean> list = new ArrayList<>();
        Bean bean = new Bean();
        bean.pid = pid;
        bean.wcmddmx = TextUtils.isEmpty(wcmddmx)?"":wcmddmx;;
        bean.wcmddxz = wcmddxz;
        bean.wcmddszs = wcmddszs;
        bean.wcmddszx = wcmddszx;
        bean.wcmddszd = wcmddszd;
        bean.wcmddxzName = TextUtils.isEmpty(wcmddxzName)?"":wcmddxzName;
        bean.wcmddszsName = TextUtils.isEmpty(wcmddszsName)?"":wcmddszsName;;
        bean.wcmddszxName = TextUtils.isEmpty(wcmddszxName)?"":wcmddszxName;;
        bean.wcmddszdName = TextUtils.isEmpty(wcmddszdName)?"":wcmddszdName;;
        list.add(bean);
        hashMap.put("list", list);

        hashMap.put("wcly", wclyType);
        hashMap.put("jtsy",wcly);
        hashMap.put("jsrq", jsrq);
        hashMap.put("ksqr", ksqr);
        hashMap.put("wcts", wcts);
        hashMap.put("sfyxj", sfyxj);

        hashMap.put("lsjhr", lsjhr);
        hashMap.put("gx", gx);
        hashMap.put("lxdh", lxdh);

        String toJson = new Gson().toJson(hashMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        Timber.i("leaveAdd:%s", toJson);
        LiveData<ApiResult<Object>> login = apiService.leaveAdd(body);
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

        public String wcmddszsName;
        public String wcmddszdName;
        public String wcmddszxName;
        public String wcmddxzName;


    }

}
