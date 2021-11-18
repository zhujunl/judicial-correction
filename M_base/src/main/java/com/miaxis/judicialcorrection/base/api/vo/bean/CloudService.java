package com.miaxis.judicialcorrection.base.api.vo.bean;

import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.api.ApiResult;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CloudService {
    @GET("/report/list")
    LiveData<ApiResult<DailyBean>> getreport(
            @Header("Authorization") String token,
            @Query("page") int page,
            @Query("rows") int rows,
            @Query("pid") String pid
    );

    @GET("/focusEducation/list")
    LiveData<ApiResult<CentralizedBean>> getEducation(
            @Header("Authorization") String token,
            @Query("page") int page,
            @Query("rows") int rows,
            @Query("pid") String pid
    );

    @GET("/personEducation/list")
    LiveData<ApiResult<IndividualBean>> getPersonEducation(
            @Header("Authorization") String token,
            @Query("pid") String pid,
            @Query("page") int page,
            @Query("rows") int rows);
    //历史活动
    @GET("/publicActivity/list")
    LiveData<ApiResult<WelfareBean>> getHistoryActivityInfo(
            @Header("Authorization") String token,
            @Query("page") int page,
           @Query("rows") int rows, @Query("pid") String pid);
    //训诫
    @GET("/xj/list")
    LiveData<ApiResult<AdmonitionBean>> getAdmonition(
            @Header("Authorization") String token,@Query("pid") String  pid);

    //警告
    @GET("/warning/list")
    LiveData<ApiResult<WarningBean>> getWarning(
            @Header("Authorization") String token,@Query("pid") String  pid);


}
