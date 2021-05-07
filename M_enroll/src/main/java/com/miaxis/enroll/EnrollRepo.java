package com.miaxis.enroll;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.enroll.vo.Job;
import com.miaxis.enroll.vo.OtherCardType;
import com.miaxis.enroll.vo.OtherInfo;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.bean.IdCardMsg;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

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

    private final Gson gson = new Gson();

    @SuppressWarnings("all")
    public LiveData<Resource<PersonInfo>> addPerson(JusticeBureau justiceBureau, IdCardMsg idCard, OtherCardType cardType, OtherInfo otherInfo) {
        Map<String, String> mapCardType = new Gson().fromJson(gson.toJson(cardType), Map.class);
        Map<String, String> mapOtherInfo = new Gson().fromJson(gson.toJson(otherInfo), Map.class);
        Map<String, String> map = new HashMap<>();
        map.putAll(mapCardType);
        map.putAll(mapOtherInfo);
        map.put("jzjg", justiceBureau.getTeamId());
        map.put("xm", idCard.name);
        map.put("xb", idCard.sex);
        map.put("mz", idCard.nation_str);
        map.put("sfzh", idCard.id_num);
        map.put("csrq", String.format("%s-%s-%s", idCard.birth_year, idCard.birth_month, idCard.birth_day));
        LiveData<ApiResult<PersonInfo>> apiResultLiveData = apiService.addPerson(map);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }

    public LiveData<Resource<Object>> addJob(String pid,Job job) {
        job.pid = pid;
        String toJson = gson.toJson(job);
        LiveData<ApiResult<Object>> apiResultLiveData = apiService.addJob(toJson);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }

}
