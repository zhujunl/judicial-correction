package com.miaxis.judicialcorrection.base.api;

import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.api.vo.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * ApiService
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
public interface ApiService {
//    @GET("/api/aidy-base/atm/enter/login/student/code")
//    LiveData<ApiResult<User>> login(@Header("tenementCode") String tenementCode, @Query("rfid") String rfid, @Query("sn") String sn);

    @GET("/personInfo/list")
    LiveData<ApiResult<List<Object>>> personList(
            @Query("lastModifiedTime") String lastModifiedTime
    );



}
