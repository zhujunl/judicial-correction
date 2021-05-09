package com.miaxis.judicialcorrection.leave;

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

    public LiveData<Resource<Leave>> getReport(String pid, int page, int rows) {
        LiveData<ApiResult<Leave>> login = apiService.getLeaveList(pid, page, rows);
        return ResourceConvertUtils.convertToResource(login);
    }

    //{\r\n
    // \"sqsj\": \"2021-04-08\", \r\n
    // \"xjms\": \"\", \r\n
    // \"pid\": \"c31Kp5rcrlmAG5DoQPMpwaTpguFvhgyh\", \r\n
    // \"list\":
    //
    // [\r\n

    // {\r\n
    // \"wcmddxz\": \"86bccb890788455592caec817358811d\", \r\n
    // \"wcmddmx\": \"上沙12巷10号码\", \r\n
    // \"pid\": \"1a5aecea3ca1425fae2fa5a7ff936fbf\", \r\n
    // \"wcmddszs\": \"440000\", \r\n
    // \"wcmddszx\": \"441523\", \r\n
    // \"wcmddszd\": \"441500\"\r\n
    // }\r\n

    // ], \r\n

    // \"wcly\": \"外出办事\", \r\n
    // \"jsrq\": \"2021-04-08\", \r\n
    // \"id\": \"\", \r\n
    // \"ksqr\": \"2021-04-08\", \r\n
    // \"wcts\": \"1\", \r\n
    // \"sfyxj\": \"0\"\r\n}",

    public LiveData<Resource<Object>> leaveAdd(String pid,
                                               String sqsj,
                                               String xjms,

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
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sqsj", sqsj);
        hashMap.put("xjms", xjms);
        hashMap.put("pid", pid);

        List<Bean> list = new ArrayList<>();
        Bean bean = new Bean();
        bean.pid = pid;
        bean.wcmddxz = wcmddxz;
        bean.wcmddmx = wcmddmx;
        bean.wcmddszs = wcmddszs;
        bean.wcmddszx = wcmddszx;
        bean.wcmddszd = wcmddszd;
        list.add(bean);
        hashMap.put("list", new Gson().toJson(list));


        hashMap.put("wcly", wcly);
        hashMap.put("jsrq", jsrq);
        hashMap.put("ksqr", ksqr);
        hashMap.put("wcts", wcts);
        hashMap.put("sfyxj", sfyxj);

        LiveData<ApiResult<Object>> login = apiService.leaveAdd(hashMap);
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
