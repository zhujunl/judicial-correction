package com.miaxis.judicialcorrection.base.utils;

import androidx.annotation.MainThread;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.miaxis.judicialcorrection.base.api.ApiOtherResult;
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
                return Resource.error(input == null ? -999 : input.code, input == null ? null : input.msg, input);
            }
        });
        map.setValue(Resource.loading(null));
        return map;
    }

    @MainThread
    public static <T> LiveData<Resource<T>> convertToResource(LiveData<ApiResult<T>> source) {
        MutableLiveData<Resource<T>> map = LiveDataTransformations.map(source, new Function<ApiResult<T>, Resource<T>>() {
            @Override
            public Resource<T> apply(ApiResult<T> input) {
                if (null == input) {
                    return Resource.error(0x9999, "UnKnow Error !!!", null);
                } else if (input.isSuccessful()) {
                    return Resource.success(input.getData());
                } else {
                    return Resource.error(input.code, input.errorMsg(), input.getData());
                }
            }
        });
        map.setValue(Resource.loading(null));
        return map;
    }


    @MainThread
    public static <T> LiveData<Resource<T>> convertToResourceFV(LiveData<ApiOtherResult<T>> source) {
        MutableLiveData<Resource<T>> map = LiveDataTransformations.map(source, new Function<ApiOtherResult<T>, Resource<T>>() {
            @Override
            public Resource<T> apply(ApiOtherResult<T> input) {
                if (null == input) {
                    return Resource.error(0x9999, "UnKnow Error !!!", null);
                } else if (input.isSuccessful()) {
                    return Resource.success(input.getData());
                } else {
                    return Resource.error(input.code, input.errorMsg(), input.getData());
                }
            }
        });
        map.setValue(Resource.loading(null));
        return map;
    }



}
