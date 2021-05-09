package com.miaxis.judicialcorrection.base.api;

import com.miaxis.judicialcorrection.base.api.vo.Education;
import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.api.vo.HistorySignUpBean;
import com.miaxis.judicialcorrection.base.api.vo.JusticeBureauList;
import com.miaxis.judicialcorrection.base.api.vo.Leave;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressChangeDetailsBean;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressListBean;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.api.vo.SignUpBean;
import com.miaxis.judicialcorrection.base.api.vo.User;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import androidx.lifecycle.LiveData;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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


    @POST("/personInfo/resume/add")
    LiveData<ApiResult<Object>> addJob(
            @Body() RequestBody body
    );
  @POST("/personInfo/relationships/add")
    LiveData<ApiResult<Object>> addRelationship(
            @Body() RequestBody body
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



    //得到活动
    @GET("/publicActivity/list")
    LiveData<ApiResult<SignUpBean>> getActivityListInfo(@Query("page") int page,
                                                        @Query("rows") int rows);

    //历史活动
    @GET("/publicActivity/list")
    LiveData<ApiResult<HistorySignUpBean>> getHistoryActivityInfo(@Query("page") int page,
                                                                  @Query("rows") int rows, @Query("pid") String pid);

    //参与社区服务
    @GET("/publicActivity/person/add")
    LiveData<ApiResult<Object>> participate(@Query("pid") String pid, @Query("sqfwbx") String servicePerformance,
                                            @Query("gyldId") String serviceRepresentation);

    //社区矫正对象居住地变更查询
    @GET("/placeChange/list")
    LiveData<ApiResult<LiveAddressListBean>> searchLiveAddressChangeList(@Query("page") int page,
                                                                         @Query("rows") int rows, @Query("pid") String pid);

    //社区矫正对象居住地详情
    @GET("placeChange/get")
    LiveData<ApiResult<LiveAddressChangeDetailsBean>> getLiveAddressChangeDetails(@Query("id") String id);


    //社区对象变更采集 文件和参数一起上传
    @POST("/placeChange/add")
    LiveData<ApiResult<Object>> changeLiveAddress(@Body() RequestBody body);
//            @Part() List<MultipartBody.Part > files);
}
