package com.miaxis.enroll;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.enroll.guide.infos.RelationshipFragment;
import com.miaxis.enroll.vo.Addr;
import com.miaxis.enroll.vo.Family;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

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
public class EnrollRepo {

    private final ApiService apiService;

    @Inject
    public EnrollRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    private final Gson gson = new Gson();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressWarnings("all")
    public LiveData<Resource<PersonInfo>> addPerson(JusticeBureau justiceBureau, IdCardMsg idCard, OtherCardType cardType, Addr addr, OtherInfo otherInfo) {
        Map<String, String> mapCardType = new Gson().fromJson(gson.toJson(cardType), Map.class);
        Map<String, String> mapAddr = new Gson().fromJson(gson.toJson(addr), Map.class);
        Map<String, String> mapOtherInfo = new Gson().fromJson(gson.toJson(otherInfo), Map.class);

        Map<String, String> map = new HashMap<>();
        map.putAll(mapCardType);
        map.putAll(mapAddr);
        map.putAll(mapOtherInfo);
        map.put("jzjg", justiceBureau.getTeamId());
        map.put("xm", idCard.name.trim());
        map.put("xb", idCard.sex);
        map.put("mz", idCard.nation_str);
        map.put("sfzh", idCard.id_num);
        Date date = new Date(Integer.parseInt(idCard.birth_year)-1900, Integer.parseInt(idCard.birth_month) - 1, Integer.parseInt(idCard.birth_day));
        map.put("csrq", simpleDateFormat.format(date));

        String toJson = gson.toJson(map);
        Timber.v("addPerson %s", toJson);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiResult<PersonInfo>> apiResultLiveData = apiService.addPerson(body);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }

    public LiveData<Resource<Object>> addJob(String pid, Job job) {
        Timber.v("addJob [%s]，pid=[%s]", job, pid);
        job.pid = pid;
        String toJson = gson.toJson(job);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiResult<Object>> apiResultLiveData = apiService.addJob(body);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }

    public LiveData<Resource<Object>> addRelationship(String pid, Family family) {
        Timber.v("addRelationship [%s]，pid=[%s]", family, pid);
        family.pid = pid;
        String toJson = gson.toJson(family);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiResult<Object>> apiResultLiveData = apiService.addRelationship(body);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }
}
