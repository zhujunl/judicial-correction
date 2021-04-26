package com.miaxis.judicialcorrection.id.readIdCard;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

/**
 * @author Tank
 * @date 2021/4/25 2:55 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ReadIDCardModel extends ViewModel {

    MutableLiveData<String> title = new MutableLiveData<>();

    MutableLiveData<Boolean> noIdCardEnable = new MutableLiveData<>(false);

    MutableLiveData<Boolean> autoCheckEnable = new MutableLiveData<>(false);


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void init() {



    }


}
