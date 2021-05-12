package com.miaxis.judicialcorrection.id.readIdCard;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.repo.PersonRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.id.bean.IdCard;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

/**
 * @author Tank
 * @date 2021/4/25 2:55 PM
 * @des
 * @updateAuthor
 * @updateDes
 */

@HiltViewModel
public class ReadIdCardViewModel extends ViewModel {

    MutableLiveData<String> title = new MutableLiveData<>();

    MutableLiveData<Boolean> noIdCardEnable = new MutableLiveData<>(false);

    AppExecutors mAppExecutors;

    private final PersonRepo personRepo;

    @Inject
    public ReadIdCardViewModel(AppExecutors appExecutors, PersonRepo personRepo) {
        this.mAppExecutors = appExecutors;
        this.personRepo = personRepo;
    }

    private ReadIDCardBindingFragment.ReadIdCallback mReadIdCallback;

    public void readIdCard(ReadIDCardBindingFragment.ReadIdCallback callback) {
        this.mReadIdCallback = callback;
        this.mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                while (mReadIdCallback != null && !success) {
                    ApiResult<IdCard> read = ReadIdCardManager.getInstance().read();
                    Timber.e("读取身份证：" + read);
                    if (success = read.isSuccessful()) {
                        mAppExecutors.mainThread().execute(() -> {
                            if (mReadIdCallback != null) {
                                mReadIdCallback.onIdCardRead(read.getData());
                            }
                            stopRead();
                        });
                    }
                }
            }
        });
    }

    //private ReadIdCardCallback mReadIdCardCallback;

//    public void readIdCard(ReadIdCardCallback readIdCardCallback) {
//        this.mReadIdCardCallback = readIdCardCallback;
//        this.mAppExecutors.networkIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                boolean success = false;
//                while (mReadIdCardCallback != null && !success) {
//                    ApiResult<IdCard> read = ReadIdCardManager.getInstance().read();
//                    Timber.e("读取身份证：" + read);
//                    if (success = read.isSuccessful()) {
//                        mAppExecutors.mainThread().execute(() -> {
//                            if (mReadIdCardCallback != null) {
//                                mReadIdCardCallback.onIdCardRead(read.getData());
//                            }
//                            stopRead();
//                        });
//                    }
//                }
//            }
//        });
//    }

    public LiveData<Resource<PersonInfo>> login(String idCardNumber) {
        return personRepo.personInfo(idCardNumber);
    }

    public void stopRead() {
        this.mReadIdCallback = null;
    }

}
