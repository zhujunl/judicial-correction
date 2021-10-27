package com.miaxis.judicialcorrection.base.api;

import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.vo.Education;
import com.miaxis.judicialcorrection.base.api.vo.FingerEntity;
import com.miaxis.judicialcorrection.base.api.vo.HistorySignUpBean;
import com.miaxis.judicialcorrection.base.api.vo.IndividualEducationBean;
import com.miaxis.judicialcorrection.base.api.vo.JusticeBureauList;
import com.miaxis.judicialcorrection.base.api.vo.Leave;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressChangeDetailsBean;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressListBean;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.api.vo.SignUpBean;
import com.miaxis.judicialcorrection.base.api.vo.User;
import com.miaxis.judicialcorrection.base.api.vo.VocalPrintEntity;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * ApiService
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
public interface ApiService {
    //    @GET("/api/aidy-base/atm/enter/login/student/code")
    //    LiveData<ApiResult<User>> login(@Header("tenementCode") String tenementCode, @Query("rfid") String rfid, @Query("sn") String sn);

    String appkey = "1c7d017e-eebe-40fa-9b17-285e62bcbeb1";

    String appsecret = "B6C943010F95B2205FCA2FC6B7B23715";
    //得到指纹
    String getFingerUrl = "/sqjzsjzx/correctFingerprints/queryById";
    //指纹上传
    String uploadFingerUrl = "/sqjzsjzx/correctFingerprints/add";
    //得到声纹
    String getVoice = "/sqjzsjzx/correctVocalprint/queryById";
    //上传声纹
    String uploadVoice = "/sqjzsjzx/correctVocalprint/add";
    //检查版本更新
    String compareVersions = "/sqjzsjzx/version/apkVersion/compareVersions";
    //下载应用
    String downAPK = "/sqjzsjzx/version/apkVersion/downloadTwo";
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

    @Multipart
    @POST("/personInfo/uploadFaceImg")
    LiveData<ApiResult<Object>> uploadFaceImg(
            @Body() Map<String, String> body,
            @Part("face") RequestBody face
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
    Call<ResponseBody> getFace(
            @Query("id") String id
    );


    @Multipart
    @POST("/personInfo/uploadFaceImg")
    LiveData<ApiResult<Object>> uploadFace(@PartMap Map<String, RequestBody> map);


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

    @GET("/personEducation/list")
    LiveData<ApiResult<IndividualEducationBean>> getPersonEducation(@Query("pid") String pid,
                                                                    @Query("page") int page,
                                                                    @Query("rows") int rows);

    @GET("/leave/list")
    LiveData<ApiResult<Leave>> getLeaveList(
            @Query("pid") String pid,
            @Query("page") int page,
            @Query("rows") int rows
    );

    //
    @POST("/leave/add")
    LiveData<ApiResult<Object>> leaveAdd(@Body() RequestBody body);

    @POST("/leave/end")
    LiveData<ApiResult<Object>> leaveEnd(
            @Body() Map<String, String> body
    );

    @GET("/leave/get")
    LiveData<ApiResult<Leave.ListBean>> getLeave(
            @Query("id") String id
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
    @POST("/publicActivity/person/add")
    LiveData<ApiResult<Object>> participate(@Body() RequestBody body);

    //社区矫正对象居住地变更查询
    @GET("/placeChange/list")
    LiveData<ApiResult<LiveAddressListBean>> searchLiveAddressChangeList(@Query("page") int page,
                                                                         @Query("rows") int rows, @Query("pid") String pid);

    //社区矫正对象居住地详情
    @GET("/placeChange/get")
    LiveData<ApiResult<LiveAddressChangeDetailsBean>> getLiveAddressChangeDetails(@Query("id") String id);


    //社区对象变更采集 文件和参数一起上传 //
    @POST("/placeChange/add")
    LiveData<ApiResult<Object>> changeLiveAddress(@Body() RequestBody body);

    //指纹上传
    @POST()
    LiveData<ApiResult<Object>> uploadFingerprint(@Url String url,@Body() RequestBody body);

    //获取指纹
    @POST()
    LiveData<ApiResult<FingerEntity>> getFinger(@Url String url, @Body() RequestBody body);

    //声纹上传
    @POST()
    LiveData<ApiResult<Object>> uploadVoicePrint(@Url String url, @Body() RequestBody body);
    //得到声纹
    @POST()
    LiveData<ApiResult<VocalPrintEntity>> getVocalPrint(@Url String url,@Body() RequestBody body);
    //检测是否有最新版本
    @POST()
    LiveData<ApiResult<ApkVersionResult>> compareVersions(@Url String url, @Body() RequestBody body);

    @GET()
    Call<ResponseBody> downApk(@Url String url);
}
