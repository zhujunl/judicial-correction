package com.miaxis.judicialcorrection.leave.cancel;

import javax.inject.Inject;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @author Tank
 * @date 2021/5/7 18:44
 * @des
 * @updateAuthor
 * @updateDes
 */
@HiltViewModel
public class CancelViewModel extends ViewModel {

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> idCardNumber = new ObservableField<>();

    public ObservableField<String> cancelTime = new ObservableField<>();


    @Inject
    public CancelViewModel() {
    }

}
