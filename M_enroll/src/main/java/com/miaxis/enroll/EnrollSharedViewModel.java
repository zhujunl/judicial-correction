package com.miaxis.enroll;

import android.annotation.SuppressLint;

import com.miaxis.enroll.vo.Addr;
import com.miaxis.enroll.vo.Family;
import com.miaxis.enroll.vo.Job;
import com.miaxis.enroll.vo.OtherCardType;
import com.miaxis.enroll.vo.OtherInfo;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.repo.JusticeBureauRepo;
import com.miaxis.judicialcorrection.base.repo.PersonRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.readIdCard.ReadIdCardManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

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
//    public LiveData<JusticeBureau> justiceBureauLiveData;

    public JusticeBureau checkedJusticeBureau = new JusticeBureau();

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

    public boolean haveFaceImage;
    public boolean haveIdInfo;

    //test
    public String mPid;
    private final PersonRepo personRepo;
    private final EnrollRepo enrollRepo;
    private final AppExecutors appExecutors;
    private Addr address;
    public LiveData<Resource<List<JusticeBureau>>> myPermissionJusticeBureau;

    @Inject
    public EnrollSharedViewModel(JusticeBureauRepo justiceBureauRepo, PersonRepo personRepo, EnrollRepo enrollRepo, AppExecutors appExecutors, AppDatabase appDatabase) {
        this.personRepo = personRepo;
        this.enrollRepo = enrollRepo;
        this.appExecutors = appExecutors;
        /**
         * 获取当前token有权限的司法所
         */
        myPermissionJusticeBureau = justiceBureauRepo.getMyPermissionJusticeBureau();

//        justiceBureauLiveData = Transformations.map(appDatabase.justiceBureauDao().loadAll(), input -> {
//            for (int i = 0; i < input.size(); i++) {
//                JusticeBureau justiceBureau = input.get(i);
//                if (input.size() == 3 && Objects.equals("TEAM_LEVEL_3", justiceBureau.getTeamLevel())) {
//                    return justiceBureau;
//                }
//                if (input.size() == 2 && Objects.equals("TEAM_LEVEL_2", justiceBureau.getTeamLevel())) {
//                    return justiceBureau;
//                }
//                if (input.size() == 1) {
//                    return justiceBureau;
//                }
//            }
//            return null;
//        });
        otherCardTypeLiveData.setValue(new OtherCardType());
        otherInfoLiveData.setValue(new OtherInfo());
    }

    public LiveData<Resource<PersonInfo>> login(String idCardNumber) {
        return personRepo.personInfo(idCardNumber);
    }

    boolean isActive = true;

    public void readIdCard() {
        appExecutors.networkIO().execute(() -> {
            while (isActive) {
                ApiResult<IdCard> result = ReadIdCardManager.getInstance().read();
                Timber.i("ID result %s", result);
                if (result.isSuccessful()) {
                    //result.getData().idCardMsg.id_num += ("Y" + new Random().nextInt(100));
                    idCardLiveData.postValue(result.getData());
                    break;
                }
            }
        });
    }

    public void setAddress(Addr address) {
        this.address = address;
    }


    public MediatorLiveData<PersonInfo> personInfoLiveData = new MediatorLiveData<>();

    public LiveData<Resource<PersonInfo>> addPerson() {
        OtherInfo value = otherInfoLiveData.getValue();
        Timber.i("addPerson %s", value);
        LiveData<Resource<PersonInfo>> resourceLiveData = enrollRepo.addPerson(checkedJusticeBureau, Objects.requireNonNull(idCardLiveData.getValue()).idCardMsg, otherCardTypeLiveData.getValue(), address, otherInfoLiveData.getValue());
        return Transformations.map(resourceLiveData, input -> {
            if (input.isSuccess() && input.data != null) {
                personInfoLiveData.postValue(input.data);
            }
            return input;
        });
    }

    public LiveData<Resource<Object>> addJob(String pid, Job job) {
        return enrollRepo.addJob(pid, job);
    }

    public LiveData<Resource<Object>> addRelationship(String pid, Family family) {
        return enrollRepo.addRelationship(pid, family);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.i("其他身份证 : %s", otherCardTypeLiveData.getValue());
        Timber.i("其他信息 : %s", otherInfoLiveData.getValue());
        isActive = false;
    }
}
