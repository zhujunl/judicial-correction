package com.miaxis.judicialcorrection.face;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

/**
 * EnrollRepo
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
@Singleton
public class CapturePageRepo {

    private final ApiService apiService;

    @Inject
    public CapturePageRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    private final Gson gson = new Gson();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");



    public LiveData<Resource<Object>> uploadFace(String id, File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", id);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);
        List<MultipartBody.Part> parts = builder.build().parts();
        LiveData<ApiResult<Object>> apiResultLiveData = apiService.uploadFace(parts);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }
}
