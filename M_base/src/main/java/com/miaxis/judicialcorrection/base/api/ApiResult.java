package com.miaxis.judicialcorrection.base.api;

import com.google.gson.annotations.SerializedName;

/**
 * @author yawei
 * @data on 2018/7/16 上午10:18
 * @email zyawei@live.com
 */
public class ApiResult<T>{
    @SerializedName(value = "code")
    public int code;
    @SerializedName(value = "desc")
    public String msg;

    @SerializedName(value = "data")
    private T data;

    public T getData() {
        return data;
    }


    public void setData(T data) {
        this.data = data;
    }

    public final boolean isSuccessful() {
        return code == 0;
    }

    public final String errorMsg() {
        return null == msg ? "UnKnow Error!" : msg;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
