package com.miaxis.judicialcorrection.leave.apply;

import android.text.TextUtils;
import android.view.View;

import androidx.arch.core.util.Function;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.Place;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

/**
 * @author Tank
 * @date 2021/5/7 18:44
 * @des
 * @updateAuthor
 * @updateDes
 */
@HiltViewModel
public class ApplyViewModel extends ViewModel {

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> idCardNumber = new ObservableField<>();

    public ObservableField<String> applyTime = new ObservableField<>();

    public ObservableField<String> startTime = new ObservableField<>();

    public ObservableField<String> endTime = new ObservableField<>();

    public ObservableField<String> days = new ObservableField<>();

    public ObservableField<String> details = new ObservableField<>();

    public ObservableField<String> outType = new ObservableField<>();

    public ObservableField<String> specificReasons = new ObservableField<>();

    public ObservableField<String> relationShip = new ObservableField<>();

    public ObservableField<String> temporaryGuardian = new ObservableField<>();

    public ObservableField<String> contactNumber = new ObservableField<>();

    private final AppDatabase appDatabase;

    MutableLiveData<Place> mProvince = new MutableLiveData<>();
    MutableLiveData<Place> mCity = new MutableLiveData<>();
    MutableLiveData<Place> mDistrict = new MutableLiveData<>();
    MutableLiveData<Place> mAgencies = new MutableLiveData<>();

    //是否影藏申请书
    public ObservableField<Integer> isHideApplicationButton=new ObservableField<>();
    public ObservableField<Integer> isHideApplication=new ObservableField<>();

    //申请资料资料
    public ObservableField<Integer> isHideMaterialButton=new ObservableField<>();
    public ObservableField<Integer> isHideMaterial=new ObservableField<>();

    LiveData<List<Place>> allProvince;
    LiveData<List<Place>> allCity = Transformations.switchMap(mProvince, new Function<Place, LiveData<List<Place>>>() {
        @Override
        public LiveData<List<Place>> apply(Place input) {
            Timber.i("mProvince %s", input);
            if (input.ZXS == 1) {
                // mCity.postValue(input);
                MutableLiveData<List<Place>> placeMutableLiveData = new MutableLiveData<>();
                ArrayList<Place> objects = new ArrayList<>();
                objects.add(input);
                placeMutableLiveData.setValue(objects);
                return placeMutableLiveData;
            }
            return appDatabase.placeDao().findAllCity(input.ID);
        }
    });
    LiveData<List<Place>> allDistrict = Transformations.switchMap(mCity, new Function<Place, LiveData<List<Place>>>() {
        @Override
        public LiveData<List<Place>> apply(Place input) {
            Timber.i("mCity %s", input);
            return appDatabase.placeDao().findAllDistrict(input.ID);
        }
    });
    LiveData<List<Place>> allAgencies = Transformations.switchMap(mDistrict, new Function<Place, LiveData<List<Place>>>() {
        @Override
        public LiveData<List<Place>> apply(Place input) {
            Timber.i("mDistrict %s", input);
            return appDatabase.placeDao().findAllAgencies(input.ID);
        }
    });

    @Inject
    public ApplyViewModel(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.allProvince = appDatabase.placeDao().findAllProvince();
        //默认
        isHideApplicationButton.set(View.VISIBLE);
        isHideApplication.set(View.GONE);

        isHideMaterialButton.set(View.VISIBLE);
        isHideMaterial.set(View.GONE);
    }


    public  void setControlShowHide(int type,boolean isNullOrEmpty){
        if (type == 1) {
            if (isNullOrEmpty) {
                isHideApplicationButton.set(View.VISIBLE);
                isHideApplication.set(View.GONE);
            } else {
                isHideApplicationButton.set(View.GONE);
                isHideApplication.set(View.VISIBLE);
            }
        } else if (type == 2) {
            if (isNullOrEmpty) {
                isHideMaterialButton.set(View.VISIBLE);
                isHideMaterial.set(View.GONE);
            } else {
                isHideMaterialButton.set(View.GONE);
                isHideMaterial.set(View.VISIBLE);
            }
        }
    }
    public String checkContent() {
        if (TextUtils.isEmpty(applyTime.get())) {
            return "请选择申请时间";
        }
        if (TextUtils.isEmpty(startTime.get())) {
            return "请选择请假开始时间";
        }
        if (TextUtils.isEmpty(endTime.get())) {
            return "请选择请假结束时间";
        }
        try{
            if (startTime.get().compareTo(endTime.get()) > 0) {
                return "结束日期不能小于开始日期";
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        Timber.e("mProvince:" + mProvince.getValue());
        if (mProvince.getValue() == null || TextUtils.isEmpty(mProvince.getValue().VALUE)) {
            return "请选择省份";
        }
        Timber.e("mCity:" + mCity.getValue());
        if (mCity.getValue() == null || TextUtils.isEmpty(mCity.getValue().VALUE)) {
            return "请选择市";
        }
        Timber.e("mDistrict:" + mDistrict.getValue());
        if (mDistrict.getValue() == null || TextUtils.isEmpty(mDistrict.getValue().VALUE)) {
            return "请选择县区";
        }
        Timber.e("mAgencies:" + mAgencies.getValue());
        if (mAgencies.getValue() == null || TextUtils.isEmpty(mAgencies.getValue().VALUE)) {
            return "请选择乡镇街道";
        }
        if (TextUtils.isEmpty(outType.get()) || "请选择".equals(outType.get())) {
            return "请输选择外出类型";
        }
        if (TextUtils.isEmpty(specificReasons.get())) {
            return "请输入具体事由";
        }
        if (TextUtils.isEmpty(temporaryGuardian.get())) {
            return "请输入临时监护人姓名";
        }
        if (TextUtils.isEmpty(relationShip.get())) {
            return "请输入与临时监护人的关系";
        }
        if (TextUtils.isEmpty(contactNumber.get())) {
            return "请输入临时监护人的联系电话";
        }
        return null;
    }
}
