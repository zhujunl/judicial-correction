package com.miaxis.enroll.guide.infos;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.miaxis.enroll.vo.Addr;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.Place;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

/**
 * AddressViewModel
 *
 * @author zhangyw
 * Created on 5/7/21.
 */
@HiltViewModel
public class AddressViewModel extends ViewModel {

    private AppDatabase appDatabase;
    public final Place[] mSelect2 = new Place[4];
    MutableLiveData<Place> mProvince2 = new MutableLiveData<>();
    MutableLiveData<Place> mCity2 = new MutableLiveData<>();
    MutableLiveData<Place> mDistrict2 = new MutableLiveData<>();
    MutableLiveData<Place> mAgencies2 = new MutableLiveData<>();

    LiveData<List<Place>> allProvince2;
    LiveData<List<Place>> allCity2 = Transformations.switchMap(mProvince2, new Function<Place, LiveData<List<Place>>>() {
        @Override
        public LiveData<List<Place>> apply(Place input) {
            Timber.i("mProvince2 %s", input);
            if (input.ZXS == 1) {
                MutableLiveData<List<Place>> placeMutableLiveData = new MutableLiveData<>();
                ArrayList<Place> objects = new ArrayList<>();
                objects.add(input);
                placeMutableLiveData.setValue(objects);
                return placeMutableLiveData;
            }
            return appDatabase.placeDao().findAllCity(input.ID);
        }
    });
    LiveData<List<Place>> allDistrict2 = Transformations.switchMap(mCity2, new Function<Place, LiveData<List<Place>>>() {
        @Override
        public LiveData<List<Place>> apply(Place input) {
            Timber.i("mCity2 %s", input);
            return appDatabase.placeDao().findAllDistrict(input.ID);
        }
    });
    LiveData<List<Place>> allAgencies2 = Transformations.switchMap(mDistrict2, new Function<Place, LiveData<List<Place>>>() {
        @Override
        public LiveData<List<Place>> apply(Place input) {
            Timber.i("mDistrict2 %s", input);
            return appDatabase.placeDao().findAllAgencies(input.ID);
        }
    });


    public final Place[] mSelect = new Place[4];
    MutableLiveData<Place> mProvince = new MutableLiveData<>();
    MutableLiveData<Place> mCity = new MutableLiveData<>();
    MutableLiveData<Place> mDistrict = new MutableLiveData<>();
    MutableLiveData<Place> mAgencies = new MutableLiveData<>();

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

    public MutableLiveData<Boolean> addrSame = new MutableLiveData<>();
    public MutableLiveData<Addr> addrLiveData = new MutableLiveData<>();


    @Inject
    public AddressViewModel(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        addrSame.setValue(false);
        addrLiveData.setValue(new Addr());
        allProvince2 = appDatabase.placeDao().findAllProvince();
        allProvince = appDatabase.placeDao().findAllProvince();
    }


}
