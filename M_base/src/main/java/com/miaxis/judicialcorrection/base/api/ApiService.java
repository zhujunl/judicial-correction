package com.miaxis.judicialcorrection.base.api;

import com.miaxis.judicialcorrection.base.api.vo.Education;
import com.miaxis.judicialcorrection.base.api.vo.JusticeBureauList;
import com.miaxis.judicialcorrection.base.api.vo.Leave;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.api.vo.User;

import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    LiveData<ApiResult<List<User>>> personList(
            @Query("lastModifiedTime") String lastModifiedTime
    );

    @GET("/personInfo/getByCard")
    LiveData<ApiResult<PersonInfo>> login(
            @Query("zjhm") String idCardNumber
    );

    @POST("/personInfo/add")
    LiveData<ApiResult<PersonInfo>> addPerson(
            @Body() RequestBody body
    );


    @POST("/personInfo/add")
    LiveData<ApiResult<Object>> addJob(
            @Body() String json
    );


    @GET("/wegov/team/list")
    LiveData<ApiResult<JusticeBureauList>> justiceBureauList(
            @Query("teamId") String teamId
    );

    @GET("/personInfo/face")
    LiveData<ApiResult<Object>> getFace(
            @Query("id") String id
    );

    @POST("/report/add")
    LiveData<ApiResult<Object>> reportAdd(
            @Body() Map<String, String> body
    );

    @GET("/focusEducation/list")
    LiveData<ApiResult<Education>> getEducation(
            @Query("page") int page,
            @Query("rows") int rows
    );

    @POST("/focusEducation/person/add")
    LiveData<ApiResult<Object>> educationAdd(
            @Body() Map<String, String> body
    );

    @POST("/personEducation/add")
    LiveData<ApiResult<Object>> personEducationAdd(
            @Body() Map<String, String> body
    );

    @GET("/leave/list")
    LiveData<ApiResult<Leave>> getLeaveList(
            @Query("pid") String pid,
            @Query("page") int page,
            @Query("rows") int rows
    );

    @POST("/leave/add")
    LiveData<ApiResult<Object>> leaveAdd(
            @Body() Map<String, String> body
    );

}
