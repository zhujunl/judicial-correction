package com.miaxis.judicialcorrection.face;

import com.miaxis.judicialcorrection.base.utils.AppExecutors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @author Tank
 * @date 2021/4/25 2:55 PM
 * @des
 * @updateAuthor
 * @updateDes
 */

@HiltViewModel
public class VerifyPageViewModel extends BaseFaceViewModel {


    @Inject
    public VerifyPageViewModel(AppExecutors appExecutors) {
        super(appExecutors);
    }

}
