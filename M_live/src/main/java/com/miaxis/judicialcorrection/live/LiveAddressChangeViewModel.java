package com.miaxis.judicialcorrection.live;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.arch.core.util.Function;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressChangeDetailsBean;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressListBean;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.db.po.Place;
import com.miaxis.judicialcorrection.base.repo.JusticeBureauRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.base.utils.numbers.HexStringUtils;
import com.miaxis.judicialcorrection.bean.LiveAddressChangeBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * EnroolViewModel
 *
 * @author zhangyw
 * Created on 4/28/21.
 */
@HiltViewModel
public class LiveAddressChangeViewModel extends ViewModel {

    @Inject
    JusticeBureauRepo justiceBureauRepo;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormatNoT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public MutableLiveData<LiveAddressChangeBean> liveBean = new MutableLiveData<>();

    public String mId;

    public MutableLiveData<LiveAddressChangeDetailsBean> LiveDetailsBean = new MutableLiveData<>();

    public MutableLiveData<PersonInfo> personInfoMutableLiveData = new MutableLiveData<>();
    //省
    public MutableLiveData<List<Place>> provinceList = new MutableLiveData<>();

    public MutableLiveData<List<Place>> cityList = new MutableLiveData<>();

    public MutableLiveData<List<Place>> smallTown = new MutableLiveData<>();

    public MutableLiveData<List<Place>> street = new MutableLiveData<>();


    MutableLiveData<JusticeBureau> mShengLiveData = new MutableLiveData<>();
    MutableLiveData<JusticeBureau> mShiLiveData = new MutableLiveData<>();
    MutableLiveData<JusticeBureau> mXianLiveData = new MutableLiveData<>();
    MutableLiveData<JusticeBureau> mJiedaoLiveData = new MutableLiveData<>();

    public ObservableField<Integer> isHideScanning=new ObservableField<>();

    //是否影藏申请书
    public ObservableField<Integer> isHideApplicationButton=new ObservableField<>();
    public ObservableField<Integer> isHideApplication=new ObservableField<>();

    public final LiveData<Resource<List<JusticeBureau>>> shiListLiveData = Transformations.switchMap(mShengLiveData, new Function<JusticeBureau, LiveData<Resource<List<JusticeBureau>>>>() {
        @Override
        public LiveData<Resource<List<JusticeBureau>>> apply(JusticeBureau input) {
            return justiceBureauRepo.getAllJusticeBureau(input == null ? null : input.getTeamId());
        }
    });
    public final LiveData<Resource<List<JusticeBureau>>> xianListLiveData = Transformations.switchMap(mShiLiveData, new Function<JusticeBureau, LiveData<Resource<List<JusticeBureau>>>>() {
        @Override
        public LiveData<Resource<List<JusticeBureau>>> apply(JusticeBureau input) {
            return justiceBureauRepo.getAllJusticeBureau(input == null ? null : input.getTeamId());
        }
    });
    public final LiveData<Resource<List<JusticeBureau>>> jiedaoListLiveData = Transformations.switchMap(mXianLiveData, new Function<JusticeBureau, LiveData<Resource<List<JusticeBureau>>>>() {
        @Override
        public LiveData<Resource<List<JusticeBureau>>> apply(JusticeBureau input) {
            return justiceBureauRepo.getAllJusticeBureau(input == null ? null : input.getTeamId());
        }
    });


    /**
     * 今天日期
     */
    public MutableLiveData<String> todayLiveData = new MutableLiveData<String>();

    public String currentTime(boolean isHaveT) {
        if (isHaveT) {
            return HexStringUtils.convertCurrentGMT();
        } else {
            return dateFormatNoT.format(new Date());
        }
    }

    LiveAddressChangeRepo mLiveAddressChangeRepo;

    AppDatabase appDatabase;

    AppExecutors appExecutors;

    @Inject
    public LiveAddressChangeViewModel(LiveAddressChangeRepo liveAddressChangeRepo, AppDatabase appDatabase, AppExecutors appExecutors) {
        this.mLiveAddressChangeRepo = liveAddressChangeRepo;
        this.appDatabase = appDatabase;
        this.appExecutors = appExecutors;
        LiveAddressChangeBean bean = new LiveAddressChangeBean();
        bean.sqsj = currentTime(false);
        liveBean.postValue(bean);
        int vis=(BuildConfig.EQUIPMENT_TYPE==1||BuildConfig.EQUIPMENT_TYPE==3)?View.VISIBLE:View.INVISIBLE;
        isHideScanning.set(vis);
        //默认
        isHideApplicationButton.set(View.VISIBLE);
        isHideApplication.set(View.GONE);
    }

    //信息变更
    public LiveData<Resource<Object>> setLiveAddressChange(String jzdbgsqs,String jzdbgsqcl) {
        return mLiveAddressChangeRepo.setLiveAddressChange(liveBean.getValue(),jzdbgsqs,jzdbgsqcl);
    }

    //查询
    public LiveData<Resource<LiveAddressListBean>> searchLiveAddressChangeList(String pid) {
        return mLiveAddressChangeRepo.searchLiveAddressChangeList(1, 1000, pid);
    }

    //详情
    public LiveData<Resource<LiveAddressChangeDetailsBean>> getLiveAddressChangeDetails(String id) {
        return mLiveAddressChangeRepo.getLiveAddressChangeDetails(id);
    }

    //得到省
    public void getProvince() {
        appExecutors.diskIO().execute(() -> {
            List<Place> allProvince = appDatabase.placeDao().findAllProvinceSync();
            provinceList.postValue(allProvince);
        });
    }

    //市
    public void findAllCity(long id) {
        appExecutors.diskIO().execute(() -> {
            List<Place> allProvince = appDatabase.placeDao().findAllCitySync(id);
            cityList.postValue(allProvince);
        });
    }

    //县
    public void findAllDistrict(long id) {
        appExecutors.diskIO().execute(() -> {
            List<Place> allProvince = appDatabase.placeDao().findAllDistrictSync(id);
            smallTown.postValue(allProvince);
        });
    }

    //街道
    public void getProvince(long id) {
        appExecutors.diskIO().execute(() -> {
            List<Place> allProvince = appDatabase.placeDao().findAllAgenciesSync(id);
            street.postValue(allProvince);
        });
    }

    public void setSheng(JusticeBureau justiceBureau) {
        mShengLiveData.postValue(justiceBureau);
    }

    public void setShi(JusticeBureau justiceBureau) {
        mShiLiveData.setValue(justiceBureau);
    }

    public void setXian(JusticeBureau justiceBureau) {
        mXianLiveData.setValue(justiceBureau);
    }

    public void setJiedao(JusticeBureau justiceBureau) {
        mJiedaoLiveData.setValue(justiceBureau);
    }

    public void addBureau(JusticeBureau shi, JusticeBureau xian, JusticeBureau jiedao) {
        justiceBureauRepo.addBureau(shi, xian, jiedao);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
