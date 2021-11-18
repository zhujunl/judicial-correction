package com.miaxis.judicialcorrection.ui.cloud;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.judicialcorrection.base.api.vo.bean.AdmonitionBean;
import com.miaxis.judicialcorrection.base.api.vo.bean.CentralizedBean;
import com.miaxis.judicialcorrection.base.api.vo.bean.CloudRep;
import com.miaxis.judicialcorrection.base.api.vo.bean.CloudRep2;
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

    private final CloudRep2 cloudRep;
    @Inject
    public CloudModel(CloudRep2 reportRepo) {
        this.cloudRep = reportRepo;
    }

    public LiveData<Resource<DailyBean>> getreport(){
        String pid= MMKV.defaultMMKV().decodeString("pid");
        String token=MMKV.defaultMMKV().decodeString("cloudtoken");
        Timber.e("CloudModel_token=="+token);
        Timber.e("CloudModel:getreport");
        return cloudRep.getreport(token,1,9999,pid);
    }

    public LiveData<Resource<CentralizedBean>> getEducation(){
        String pid= MMKV.defaultMMKV().decodeString("pid");
        String token=MMKV.defaultMMKV().decodeString("cloudtoken");
        Timber.e("CloudModel:getEducation");
        return cloudRep.getEducation(token,1,9999,pid);
    }

    public LiveData<Resource<IndividualBean>> getPersonEducation(){
        String pid= MMKV.defaultMMKV().decodeString("pid");
        String token=MMKV.defaultMMKV().decodeString("cloudtoken");
        Timber.e("CloudModel:getPersonEducation");
        return cloudRep.getPersonEducation(token,1,9999,pid);
    }

    public LiveData<Resource<WelfareBean>> getHistoryActivityInfo(){
        String pid= MMKV.defaultMMKV().decodeString("pid");
        String token=MMKV.defaultMMKV().decodeString("cloudtoken");
        Timber.e("CloudModel:getHistoryActivityInfo");
        return cloudRep.getHistoryActivityInfo(token,1,9999,pid);
    }

    public LiveData<Resource<AdmonitionBean>> getAdmonition(){
        String pid= MMKV.defaultMMKV().decodeString("pid");
        Timber.e("CloudModel:getAdmonition");
        return cloudRep.getAdmonition("Bearer 3a2b4564-499f-4fbc-a865-f8593a92bf6a",pid);
    }

    public LiveData<Resource<WarningBean>> getWarning(){
        Timber.e("CloudModel:getWarning");
        String pid= MMKV.defaultMMKV().decodeString("pid");
        String token=MMKV.defaultMMKV().decodeString("cloudtoken");
        return cloudRep.getWarning("Bearer 3a2b4564-499f-4fbc-a865-f8593a92bf6a",pid);
    }



}
