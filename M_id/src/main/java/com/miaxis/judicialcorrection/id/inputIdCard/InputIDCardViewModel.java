package com.miaxis.judicialcorrection.id.inputIdCard;

import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.repo.PersonRepo;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @author Tank
 * @date 2021/4/25 2:55 PM
 * @des
 * @updateAuthor
 * @updateDes
 */
@HiltViewModel
public class InputIDCardViewModel extends ViewModel {

    MutableLiveData<String> title = new MutableLiveData<>();

    private final PersonRepo personRepo;

    @Inject
    public InputIDCardViewModel(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    public LiveData<Resource<PersonInfo>> login(String idCardNumber) {
        return personRepo.personInfo(idCardNumber);
    }
}
