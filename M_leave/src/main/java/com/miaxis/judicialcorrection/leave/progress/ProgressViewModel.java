package com.miaxis.judicialcorrection.leave.progress;

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
public class ProgressViewModel extends ViewModel {

    public ObservableField<String> progress = new ObservableField<>();

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> idCardNumber = new ObservableField<>();
    public ObservableField<String> startTime = new ObservableField<>();
    public ObservableField<String> endTime = new ObservableField<>();
    public ObservableField<String> days = new ObservableField<>();
    public ObservableField<String> reasonType = new ObservableField<>();
    public ObservableField<String> reason = new ObservableField<>();
    public ObservableField<String> temporaryGuardian = new ObservableField<>();
    public ObservableField<String> relationship = new ObservableField<>();
    public ObservableField<String> phone = new ObservableField<>();

    public ObservableField<String> cancelTime = new ObservableField<>();


    @Inject
    public ProgressViewModel() {
    }

}
