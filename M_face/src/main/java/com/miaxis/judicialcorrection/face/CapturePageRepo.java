package com.miaxis.judicialcorrection.face;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.SingUpJsonBean;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;
import com.miaxis.judicialcorrection.face.bean.UploadPicBean;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Headers;
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


    public LiveData<Resource<Object>> uploadFace(String id, File file,String fileName) {

        MultipartBody.Part partId =
                MultipartBody.Part.createFormData("id", id);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("image/jpg"), file);
        //参数1 数组名，参数2 文件名。
        MultipartBody.Part photo1part =
                MultipartBody.Part.createFormData("file",fileName, requestBody);
        LiveData<ApiResult<Object>> apiResultLiveData = apiService.uploadFace(partId, photo1part);

        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }

    public LiveData<Resource<Object>> uploadFace(String id, String base64Face) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("base64Face", base64Face);

//        UploadPicBean jsonBean =new UploadPicBean();
//        jsonBean.id=id;
//        jsonBean.base64Face=base64Face;
//        String toJson = new Gson().toJson(jsonBean);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);

        LiveData<ApiResult<Object>> apiResultLiveData = apiService.uploadFace(hashMap);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }


}
