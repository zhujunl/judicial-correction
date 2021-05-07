package com.miaxis.enroll;

import android.annotation.SuppressLint;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.miaxis.enroll.vo.Family;
import com.miaxis.enroll.vo.Job;
import com.miaxis.enroll.vo.OtherCardType;
import com.miaxis.enroll.vo.OtherInfo;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.repo.PersonRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.bean.IdCardMsg;
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
public class EnrollSharedViewModel extends ViewModel {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * 错误信息，主界面会dialog显示
     */
    public MutableLiveData<String> errorMsgLiveData = new MutableLiveData<>();

    /**
     * 今天日期
     */
    public LiveData<String> todayLiveData = new MutableLiveData<String>() {
        @Override
        protected void onActive() {
            super.onActive();
            setValue(dateFormat.format(new Date()));
        }
    };

    /**
     * 当前选择的司法局
     */
    public LiveData<JusticeBureau> justiceBureauLiveData;

    /**
     * 身份证信息
     */
    public MutableLiveData<IdCard> idCardLiveData = new MutableLiveData<>();

    /**
     * 其他类型的身份卡
     */
    public MutableLiveData<OtherCardType> otherCardTypeLiveData = new MutableLiveData<>();

    /**
     * 其他信息
     */
    public MutableLiveData<OtherInfo> otherInfoLiveData = new MutableLiveData<>();

    /**
     * 简历,list
     */
    public List<Job> jobs = new ArrayList<>();
    /**
     * 社会关系,list
     */
    public List<Family> relationships = new ArrayList<>();


    private final PersonRepo personRepo;
    private final EnrollRepo enrollRepo;
    private final AppExecutors appExecutors;

    @Inject
    public EnrollSharedViewModel(PersonRepo personRepo, EnrollRepo enrollRepo, AppExecutors appExecutors, AppDatabase appDatabase) {
        this.personRepo = personRepo;
        this.enrollRepo = enrollRepo;
        this.appExecutors = appExecutors;
        justiceBureauLiveData = appDatabase.justiceBureauDao().load();
        otherCardTypeLiveData.setValue(new OtherCardType());
        otherInfoLiveData.setValue(new OtherInfo());
    }

    public LiveData<Resource<PersonInfo>> login(String idCardNumber) {
        return personRepo.personInfo(idCardNumber);
    }


    public void readIdCard() {
        appExecutors.networkIO().execute(() -> {
            while (true) {
                ApiResult<IdCard> result = ReadIdCardManager.getInstance().read();
                Timber.i("ID result %s", result);
                if (result.isSuccessful()) {
                    result.getData().idCardMsg.id_num += String.valueOf(new Random(10000).nextInt());
                    idCardLiveData.postValue(result.getData());
                    break;
                }
            }
        });
    }


    public LiveData<Resource<PersonInfo>> addPerson() {
        return enrollRepo.addPerson(Objects.requireNonNull(justiceBureauLiveData.getValue()), Objects.requireNonNull(idCardLiveData.getValue()).idCardMsg, otherCardTypeLiveData.getValue(), otherInfoLiveData.getValue());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.i("其他身份证 : %s", otherCardTypeLiveData.getValue());
        Timber.i("其他信息 : %s", otherInfoLiveData.getValue());
    }
}
