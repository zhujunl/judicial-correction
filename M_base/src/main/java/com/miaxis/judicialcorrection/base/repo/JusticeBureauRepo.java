package com.miaxis.judicialcorrection.base.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.NoAuthApiService;
import com.miaxis.judicialcorrection.base.api.vo.JusticeBureauList;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * JusticeBureau
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@Singleton
public class JusticeBureauRepo {
    private final NoAuthApiService apiService;
    private ApiService apiServiceAuth;
    private final AppDatabase database;

    @Inject
    public JusticeBureauRepo(NoAuthApiService apiService,ApiService apiServiceAuth, AppDatabase database) {
        this.apiService = apiService;
        this.apiServiceAuth = apiServiceAuth;
        this.database = database;
    }
    public LiveData<Resource<List<JusticeBureau>>> getMyPermissionJusticeBureau() {
        LiveData<ApiResult<JusticeBureauList>> apiResultLiveData = apiServiceAuth.justiceBureauList(null);
        LiveData<Resource<ApiResult<JusticeBureauList>>> convert = ResourceConvertUtils.convert(apiResultLiveData);

        return Transformations.map(convert, input -> {
            if (input.isError()) {
                return Resource.copyError(input);
            } else if (input.isLoading()) {
                return Resource.loading(null);
            }
            assert input.data != null;
            List<JusticeBureau> list = input.data.getData().list;
            return Resource.success(list);
        });
    }

    public LiveData<Resource<List<JusticeBureau>>> getAllJusticeBureau(String id) {
        LiveData<ApiResult<JusticeBureauList>> apiResultLiveData = apiService.justiceBureauList(id);
        LiveData<Resource<ApiResult<JusticeBureauList>>> convert = ResourceConvertUtils.convert(apiResultLiveData);

        return Transformations.map(convert, input -> {
            if (input.isError()) {
                return Resource.copyError(input);
            } else if (input.isLoading()) {
                return Resource.loading(null);
            }
            assert input.data != null;
            List<JusticeBureau> list = input.data.getData().list;
            return Resource.success(list);
        });
    }

    @Inject
    AppExecutors appExecutors;

    public void setShi(JusticeBureau justiceBureau) {
        appExecutors.diskIO().execute(() -> {
            database.justiceBureauDao().deleteWithTeamLevel(justiceBureau.getTeamLevel());
            long id = database.justiceBureauDao().insert(justiceBureau);
            Timber.i("setShi = %s", id);
        });

    }

    public void setXian(JusticeBureau justiceBureau) {
        appExecutors.diskIO().execute(() -> {
            database.justiceBureauDao().deleteWithTeamLevel(justiceBureau.getTeamLevel());
            long id = database.justiceBureauDao().insert(justiceBureau);
            Timber.i("setXian = %s", id);
        });
    }

    public void setJiedao(JusticeBureau justiceBureau) {
        appExecutors.diskIO().execute(() -> {
            if (justiceBureau == null) {
                database.justiceBureauDao().deleteWithTeamLevel("TEAM_LEVEL_3");
                Timber.i("Clear 街道");
            } else {
                database.justiceBureauDao().deleteWithTeamLevel(justiceBureau.getTeamLevel());
                database.justiceBureauDao().insert(justiceBureau);
                Timber.i("setjiedao = %s",justiceBureau);
            }
        });
    }

    public LiveData<List<JusticeBureau>> getMyJusticeBureaus() {
        return database.justiceBureauDao().loadAll();
    }

    public LiveData<JusticeBureau> getMyJusticeBureau() {
        return database.justiceBureauDao().load();
    }

    public void addBureau(JusticeBureau shi, JusticeBureau xian, JusticeBureau jiedao) {
        Timber.d( "addBureau() called with: shi = [" + shi + "], xian = [" + xian + "], jiedao = [" + jiedao + "]");

        appExecutors.diskIO().execute(() -> {
            setShi(shi);
            setXian(xian);
            setJiedao(jiedao);
        });
    }
}
