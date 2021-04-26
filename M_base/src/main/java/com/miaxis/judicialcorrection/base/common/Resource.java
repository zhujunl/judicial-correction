package com.miaxis.judicialcorrection.base.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.miaxis.judicialcorrection.base.common.Status.ERROR;
import static com.miaxis.judicialcorrection.base.common.Status.LOADING;
import static com.miaxis.judicialcorrection.base.common.Status.SUCCESS;


/**
 * 用于{@link androidx.lifecycle.LiveData}对外暴露请求状态,比如web请求
 * <p>
 * <p>
 * 一旦创建,数据不允许更改
 * 每次使用都会创建新的实例
 *
 * @author yawei
 * @data on 17-10-30  上午9:43
 * @org www.komlin.com
 * @email zyawei@live.com
 */

public final class Resource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }
}
