package com.miaxis.judicialcorrection.base.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.RoomDatabase;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.NoAuthApiService;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.api.vo.JusticeBureauList;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * JusticeBureau
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@Singleton
public class JusticeBureauRepo {
    private final NoAuthApiService apiService;
    private final AppDatabase database;

    @Inject
    public JusticeBureauRepo(NoAuthApiService apiService, AppDatabase database) {
        this.apiService = apiService;
        this.database = database;
    }

    public LiveData<Resource<List<JusticeBureau>>> getAllJusticeBureau() {
        LiveData<ApiResult<JusticeBureauList>> apiResultLiveData = apiService.justiceBureauList(null);
        LiveData<Resource<ApiResult<JusticeBureauList>>> convert = ResourceConvertUtils.convert(apiResultLiveData);

        return Transformations.map(convert, input -> {
            if (input.isError()) {
                return Resource.copyError(input);
            }else if (input.isLoading()){
                return Resource.loading(null);
            }
            assert input.data != null;
            List<JusticeBureau> list = input.data.getData().list;
            return Resource.success(list);
        });
    }

    public long setMyJusticeBureau(JusticeBureau justiceBureau) {
        JusticeBureau justiceBureau1 = database.justiceBureauDao().loadSync();
        if (justiceBureau1 != null) {
            database.justiceBureauDao().delete(justiceBureau1);
        }
        return database.justiceBureauDao().insert(justiceBureau);
    }

    public LiveData<JusticeBureau> getMyJusticeBureau() {
        return database.justiceBureauDao().load();
    }
    public JusticeBureau getMyJusticeBureauSync() {
        return database.justiceBureauDao().loadSync();
    }
}
