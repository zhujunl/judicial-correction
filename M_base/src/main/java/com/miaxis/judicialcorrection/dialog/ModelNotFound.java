package com.miaxis.judicialcorrection.dialog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author Tank
 * @date 2021/4/25 2:55 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ModelNotFound extends ViewModel {

    MutableLiveData<String> title=new MutableLiveData<>();

    MutableLiveData<Boolean> autoCheckEnable=new MutableLiveData<>(false);


}
