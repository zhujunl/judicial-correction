package com.miaxis.judicialcorrection.ui.cloud;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.judicialcorrection.base.api.vo.bean.AdmonitionBean;
import com.miaxis.judicialcorrection.base.api.vo.bean.CentralizedBean;
import com.miaxis.judicialcorrection.base.api.vo.bean.CloudRep;
import com.miaxis.judicialcorrection.base.api.vo.bean.DailyBean;
import com.miaxis.judicialcorrection.base.api.vo.bean.IndividualBean;
import com.miaxis.judicialcorrection.base.api.vo.bean.WarningBean;
import com.miaxis.judicialcorrection.base.api.vo.bean.WelfareBean;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.tencent.mmkv.MMKV;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class CloudModel extends ViewModel {

    private final CloudRep cloudRep;
    @Inject
    public CloudModel(CloudRep reportRepo) {
        this.cloudRep = reportRepo;
    }

    public LiveData<Resource<DailyBean>> getreport(){
        String pid= MMKV.defaultMMKV().decodeString("pid");
        String token=MMKV.defaultMMKV().decodeString("cloudtoken");
        Timber.e("CloudModel_token=="+token);
        Timber.e("CloudModel:getreport");
        return cloudRep.getreport(token,1,1,pid);
    }

    public LiveData<Resource<CentralizedBean>> getEducation(){
        String pid= MMKV.defaultMMKV().decodeString("pid");
        String token=MMKV.defaultMMKV().decodeString("cloudtoken");
        Timber.e("CloudModel:getEducation");
        return cloudRep.getEducation(token,1,1,pid);
    }

    public LiveData<Resource<IndividualBean>> getPersonEducation(){
        String pid= MMKV.defaultMMKV().decodeString("pid");
        String token=MMKV.defaultMMKV().decodeString("cloudtoken");
        Timber.e("CloudModel:getPersonEducation");
        return cloudRep.getPersonEducation(token,1,1,pid);
    }

    public LiveData<Resource<WelfareBean>> getHistoryActivityInfo(){
        String pid= MMKV.defaultMMKV().decodeString("pid");
        String token=MMKV.defaultMMKV().decodeString("cloudtoken");
        Timber.e("CloudModel:getHistoryActivityInfo");
        return cloudRep.getHistoryActivityInfo(token,1,1,pid);
    }

    public LiveData<Resource<AdmonitionBean>> getAdmonition(){
        Timber.e("CloudModel:getAdmonition");
        return cloudRep.getAdmonition(MMKV.defaultMMKV().decodeString("cloudtoken"),"6447f4064e5a45f896f8dd60b12a8dd6");
    }

    public LiveData<Resource<WarningBean>> getWarning(){
        Timber.e("CloudModel:getWarning");
        String pid= MMKV.defaultMMKV().decodeString("pid");
        String token=MMKV.defaultMMKV().decodeString("cloudtoken");
        return cloudRep.getWarning(token,"db0dea7f6b09425e84f891d41cfc89c3");
    }



}
