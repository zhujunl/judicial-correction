package com.miaxis.judicialcorrection.live;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressChangeDetailsBean;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressListBean;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;
import com.miaxis.judicialcorrection.bean.LiveAddressChangeBean;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

@Singleton
public class LiveAddressChangeRepo {

    private final ApiService apiService;

    @Inject
    public LiveAddressChangeRepo(ApiService apiService) {
        this.apiService = apiService;
    }

    @SuppressWarnings("all")
    public LiveData<Resource<Object>> setLiveAddressChange(LiveAddressChangeBean liveAddressChangeBean) {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
        String toJson = new Gson().toJson(liveAddressChangeBean);
        Timber.e("setLiveAddressChange %s",toJson);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson);
        LiveData<ApiResult<Object>> apiResultLiveData=  apiService.changeLiveAddress(body);

        // 上传文件
        // file1Location文件的路径 ,我是在手机存储根目录下创建了一个文件夹,里面放着了一张图片;
//        File file = new File("");
//        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        //添加文件(uploadfile就是你服务器中需要的文件参数)
//        builder.addFormDataPart("uploadfile", file.getName(), imageBody);
//        File file2 = new File(""); //file1Location文件的路径 ,我是在手机存储根目录下创建了一个文件夹,里面放着了一张图片;
//        RequestBody imageBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
//        //添加文件(uploadfile就是你服务器中需要的文件参数)
//        builder.addFormDataPart("uploadfile2", file.getName(), imageBody2);
//        List<MultipartBody.Part> parts = builder.build().parts();
//        LiveData<ApiResult<String>> apiResultLiveData = apiService.changeLiveAddress(parts);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }

    @SuppressWarnings("all")
    public LiveData<Resource<LiveAddressListBean>> searchLiveAddressChangeList(int page, int rows, String pid) {
        LiveData<ApiResult<LiveAddressListBean>> apiResultLiveData = apiService.searchLiveAddressChangeList(page, rows, pid);
        return ResourceConvertUtils.convertToResource(apiResultLiveData);
    }

    @SuppressWarnings("all")
    public LiveData<Resource<LiveAddressChangeDetailsBean>> getLiveAddressChangeDetails(String id) {
        LiveData<ApiResult<LiveAddressChangeDetailsBean>> liveAddressChangeDetails = apiService.getLiveAddressChangeDetails(id);
        return ResourceConvertUtils.convertToResource(liveAddressChangeDetails);
    }

}
