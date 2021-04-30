package com.miaxis.enroll;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.ResourceConvertUtils;

import javax.inject.Inject;

import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import timber.log.Timber;

/**
 * EnroolViewModel
 *
 * @author zhangyw
 * Created on 4/28/21.
 */
@HiltViewModel
public class EnrollViewModel extends ViewModel {


    private EnrollRepo enrollRepo;

    @Inject
    public EnrollViewModel(EnrollRepo enrollRepo) {
        this.enrollRepo = enrollRepo;
    }

    public LiveData<Resource<PersonInfo>> login(String idCardNumber){
        return enrollRepo.login(idCardNumber);
    }


}
