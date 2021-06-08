package com.miaxis.enroll.guide.finger;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.enroll.vo.Addr;
import com.miaxis.enroll.vo.Family;
import com.miaxis.enroll.vo.FingerprintEntity;
import com.miaxis.enroll.vo.Job;
import com.miaxis.enroll.vo.OtherCardType;
import com.miaxis.enroll.vo.OtherInfo;
import com.miaxis.judicialcorrection.base.api.ApiOtherResult;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.FingerEntity;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.utils.MD5Utils;
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
public class FingerprintRepo {

    private final ApiService apiService;

    @Inject
    public FingerprintRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    private final Gson gson = new Gson();



    public LiveData<Resource<Object>> uploadFingerprint(FingerprintEntity entity) {
        entity.appkey=ApiService.appkey;
        String sign=ApiService.appkey+ApiService.appsecret;
        entity.sign= MD5Utils.md5(sign);
        String toJson = gson.toJson(entity);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiOtherResult<Object>> apiResultLiveData = apiService.uploadFingerprint(body);
        return ResourceConvertUtils.convertToResourceFV(apiResultLiveData);
    }

    /**
     * 得到指纹
     */
    public LiveData<Resource<FingerEntity>> getFingerPrint(String id) {
        String sign=ApiService.appkey+ApiService.appsecret;
        String signMd5= MD5Utils.md5(sign);
        Map<String,String> map=new HashMap<>();
        map.put("id",id);
        map.put("sign",signMd5);
        map.put("appkey",ApiService.appkey);
        String toJson = gson.toJson(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiOtherResult<FingerEntity>> apiResultLiveData = apiService.getFinger(body);
        return ResourceConvertUtils.convertToResourceFV(apiResultLiveData);
    }



}
