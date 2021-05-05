package com.miaxis.judicialcorrection.id.readIdCard;

import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.miaxis.judicialcorrection.id.bean.IdCard;
import com.miaxis.judicialcorrection.id.callback.ReadIdCardCallback;
import javax.inject.Inject;
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

    @Inject
    public ReadIdCardViewModel(AppExecutors appExecutors) {
        this.mAppExecutors = appExecutors;
    }

    private ReadIdCardCallback mReadIdCardCallback;

    public void readIdCard(ReadIdCardCallback readIdCardCallback) {
        this.mReadIdCardCallback = readIdCardCallback;
        this.mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                while (mReadIdCardCallback != null && !success) {
                    ApiResult<IdCard> read = ReadIdCardManager.getInstance().read();
                    Timber.e("读取身份证：" + read);
                    if (success = read.isSuccessful()) {
                        mAppExecutors.mainThread().execute(() -> {
                            if (mReadIdCardCallback != null) {
                                mReadIdCardCallback.onIdCardRead(read.getData());
                            }
                            stopRead();
                        });
                    }
                }
            }
        });
    }

    public void stopRead() {
        this.mReadIdCardCallback = null;
    }

}
