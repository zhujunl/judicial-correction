package com.miaxis.judicialcorrection.base.api;

import androidx.lifecycle.LiveData;

import com.miaxis.judicialcorrection.base.api.vo.JusticeBureauList;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * ApiService
 *
 * @author zhangyw
 * Created on 4/25/21.
 */
public interface NoAuthApiService {
    @GET("/wegov/team/list")
    LiveData<ApiResult<JusticeBureauList>> justiceBureauList(
            @Query("teamId") String teamId
    );

}
