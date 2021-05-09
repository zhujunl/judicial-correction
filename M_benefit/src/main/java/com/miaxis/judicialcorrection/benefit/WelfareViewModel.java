package com.miaxis.judicialcorrection.benefit;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.enroll.EnrollRepo;
import com.miaxis.enroll.vo.Family;
import com.miaxis.enroll.vo.Job;
import com.miaxis.enroll.vo.OtherCardType;
import com.miaxis.enroll.vo.OtherInfo;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.vo.HistorySignUpBean;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.api.vo.SignUpBean;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.repo.PersonRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIdCardManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

/**
 * EnroolViewModel
 *
 * @author zhangyw
 * Created on 4/28/21.
 */
@HiltViewModel
public class WelfareViewModel extends ViewModel {
    public  IdCard idCard;
    public  MutableLiveData<String>  mStrPid=new MutableLiveData<>();

    private final PublicWelfareRepo mPublicWelfareRepo;

    public   String mItemId;

    @Inject
    public WelfareViewModel(PublicWelfareRepo publicWelfareRepo) {
        this.mPublicWelfareRepo = publicWelfareRepo;

    }


    public LiveData<Resource<SignUpBean>> getWelfareInfo(int page) {
        return mPublicWelfareRepo.addSignUpParameter(page, 10);
    }

    /**
     * 得到历史
     */
    public LiveData<Resource<HistorySignUpBean>> getHistoryWelfareInfo(int page,String pid) {
        return mPublicWelfareRepo.addHistorySignUpParameter(page, 10,pid);
    }

    //参加活动
    public LiveData<Resource<Object>> getParticipate(String gyldId) {
        return mPublicWelfareRepo.participate(mStrPid.getValue(),gyldId);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
