package com.miaxis.judicialcorrection.ui.setting;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.miaxis.judicialcorrection.base.api.NoAuthApiService;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.repo.JusticeBureauRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

/**
 * SettingViewModel
 *
 * @author zhangyw
 * Created on 5/7/21.
 */
@HiltViewModel
public class SettingViewModel extends ViewModel {

    @Inject
    JusticeBureauRepo justiceBureauRepo;

    MutableLiveData<JusticeBureau> mShengLiveData = new MutableLiveData<>();
    MutableLiveData<JusticeBureau> mShiLiveData = new MutableLiveData<>();
    MutableLiveData<JusticeBureau> mXianLiveData = new MutableLiveData<>();
//    MutableLiveData<JusticeBureau> mJiedaoLiveData = new MutableLiveData<>();


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
    private final LiveData<Resource<List<JusticeBureau>>> jiedaoListLiveData = Transformations.switchMap(mXianLiveData, new Function<JusticeBureau, LiveData<Resource<List<JusticeBureau>>>>() {
        @Override
        public LiveData<Resource<List<JusticeBureau>>> apply(JusticeBureau input) {
            return justiceBureauRepo.getAllJusticeBureau(input == null ? null : input.getTeamId());
        }
    });
    public final LiveData<Resource<List<JusticeBureau>>> jiedaoListLiveDataWithUncheck = Transformations.map(jiedaoListLiveData, new Function<Resource<List<JusticeBureau>>, Resource<List<JusticeBureau>>>() {
        @Override
        public Resource<List<JusticeBureau>> apply(Resource<List<JusticeBureau>> input) {
            if (input.isSuccess()) {
                if (input.data != null && input.data.size() > 0) {
                    input.data.add(0, nullJusticeBureau);
                }
            }
            return input;
        }
    });
    JusticeBureau shiChecked;
    JusticeBureau xianChecked;
    JusticeBureau jiedaoChecked;

    JusticeBureau nullJusticeBureau = new JusticeBureau();


    @Inject
    public SettingViewModel(AppDatabase appDatabase) {
        justiceLiveData = appDatabase.justiceBureauDao().loadAll();
        justiceLiveData.observeForever(observer);
        nullJusticeBureau.setTeamName("请选择");
    }


    LiveData<List<JusticeBureau>> justiceLiveData;
    Observer<List<JusticeBureau>> observer = (List<JusticeBureau> justiceBureaus) -> {
        Timber.i("loadAll  :[%d], %s", justiceBureaus.size(), justiceBureaus);
        for (int i = 0; i < justiceBureaus.size(); i++) {
            JusticeBureau jb = justiceBureaus.get(i);
            switch (jb.getTeamLevel()) {
                case "TEAM_LEVEL_1":
                    shiChecked = jb;
                    break;
                case "TEAM_LEVEL_2":
                    xianChecked = jb;
                    break;
                case "TEAM_LEVEL_3":
                    jiedaoChecked = jb;
                    break;
            }
        }
    };

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
        //mJiedaoLiveData.setValue(justiceBureau);
    }

    public void addBureau(JusticeBureau shi, JusticeBureau xian, JusticeBureau jiedao) {
        justiceBureauRepo.addBureau(shi, xian, jiedao);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        justiceLiveData.removeObserver(observer);
    }
}
