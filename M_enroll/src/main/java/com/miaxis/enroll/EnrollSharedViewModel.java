package com.miaxis.enroll;

import android.annotation.SuppressLint;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.enroll.vo.Family;
import com.miaxis.enroll.vo.Job;
import com.miaxis.enroll.vo.OtherCardType;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIdCardManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public MutableLiveData<IdCard> idCardLiveData = new MutableLiveData<>();
    public MutableLiveData<String> errorMsgLiveData = new MutableLiveData<>();
    public MutableLiveData<OtherCardType> otherCardTypeLiveData = new MutableLiveData<>();
    public List<Job> jobs = new ArrayList<>();
    public List<Family> relationships = new ArrayList<>();
    public LiveData<String> todayLiveData = new MutableLiveData<String>() {
        @Override
        protected void onActive() {
            super.onActive();
            setValue(dateFormat.format(new Date()));
        }
    };


    public LiveData<JusticeBureau> justiceBureauLiveData;

    MutableLiveData<Resource<?>> loadingStatusLiveData = new MutableLiveData<>();

    private final EnrollRepo enrollRepo;
    private final AppExecutors appExecutors;

    @Inject
    public EnrollSharedViewModel(EnrollRepo enrollRepo, AppExecutors appExecutors, AppDatabase appDatabase) {
        this.enrollRepo = enrollRepo;
        this.appExecutors = appExecutors;
        justiceBureauLiveData = appDatabase.justiceBureauDao().load();
        otherCardTypeLiveData.setValue(new OtherCardType());
    }

    public LiveData<Resource<PersonInfo>> login(String idCardNumber) {
        return enrollRepo.login(idCardNumber);
    }

    public void readIdCard() {
        appExecutors.networkIO().execute(() -> {
            while (true) {
                ApiResult<IdCard> result = ReadIdCardManager.getInstance().read();
                Timber.i("ID result %s", result);
                if (result.isSuccessful()) {
                    idCardLiveData.postValue(result.getData());
                    break;
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.i("result %s",otherCardTypeLiveData.getValue());
    }
}
