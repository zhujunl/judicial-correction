package com.miaxis.judicialcorrection.base.utils;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.common.Resource;


/**
 * @author yawei
 * @data on 2018/7/16 上午10:49
 * @email zyawei@live.com
 */
public class ResourceConvertUtils {

    @MainThread
    public static <T> LiveData<Resource<ApiResult<T>>> convert(LiveData<ApiResult<T>> source) {
        MutableLiveData<Resource<ApiResult<T>>> map = (MutableLiveData<Resource<ApiResult<T>>>) Transformations.map(source, input -> {
            if (input != null && input.isSuccessful()) {
                return Resource.success(input);
            } else {
                return Resource.error(input == null ? null : input.msg, input);
            }
        });
        map.setValue(Resource.loading(null));
        return map;
    }

}
