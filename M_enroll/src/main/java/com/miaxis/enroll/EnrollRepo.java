package com.miaxis.enroll;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
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
 * <p>
 * 注意 需根据编码
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
        String cardTypeJson = gson.toJson(cardType);
        Map<String, String> mapCardType = new Gson().fromJson(cardTypeJson,Map.class);
        Timber.v("addPerson 地址信息 %s", cardTypeJson);
        String addrJson = gson.toJson(addr);
        Timber.v("addPerson 地址信息 %s", addrJson);
        Map<String, String> mapAddr = new Gson().fromJson(addrJson, Map.class);
        String otherInfoJson = gson.toJson(otherInfo);
        Timber.v("addPerson 其他个人信息 %s", otherInfoJson);
        Map<String, String> mapOtherInfo = new Gson().fromJson(otherInfoJson, Map.class);

        Map<String, String> map = new HashMap<>();
        map.putAll(mapCardType);
        map.putAll(mapAddr);
        map.putAll(mapOtherInfo);
        //矫正机构
        {
            // 身份证号
            String sex;
            if (idCard.sex.equals("男")) {
                sex = "1";
            } else if (idCard.sex.equals("女")) {
                sex = "2";
            } else {
                sex = "0";
            }
            map.put("jzjg", justiceBureau.getTeamId());
            map.put("xm", idCard.name.trim());
            map.put("xb", sex);
            map.put("mz", idCard.nation_str);
            map.put("sfzh", idCard.id_num);
            Date date = new Date(Integer.parseInt(idCard.birth_year) - 1900, Integer.parseInt(idCard.birth_month) - 1, Integer.parseInt(idCard.birth_day));
            map.put("csrq", simpleDateFormat.format(date));
        }
        String toJson = gson.toJson(map);
        Timber.v("addPerson All %s", toJson);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiResult<PersonInfo>> apiResultLiveData = apiService.addPerson(body);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }

    public LiveData<Resource<Object>> addJob(String pid, Job job) {
        Timber.v("addJob [%s]，pid=[%s]", job, pid);
        job.pid = pid;
        String toJson = gson.toJson(job);
        Timber.v("addJob %s", toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiResult<Object>> apiResultLiveData = apiService.addJob(body);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }


    public LiveData<Resource<Object>> addRelationship(String pid, Family family) {
        //[Family{name='tttt', relationship='直系亲属', job='tttt', phone='2222'}]，pid=[null]
        Map<String, String> map = new HashMap<>();
        map.put("gx", family.relationship);
        map.put("szdw", family.job);
        map.put("gx", family.relationship);
        //因需求 这边提的 传无
        if (TextUtils.isEmpty(family.phone)) {
            map.put("lxdh", "无");
        } else {
            map.put("lxdh", family.phone);
        }
        map.put("pid", pid);
        //如需要分开
        String toJson = gson.toJson(map);
        Timber.v("addRelationship [%s]，pid=[%s]", toJson, pid);
        //    居住地址	jtzz	string
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiResult<Object>> apiResultLiveData = apiService.addRelationship(body);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }
}
