package com.miaxis.judicialcorrection.face;

import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * @author Tank
 * @date 2021/4/25 2:55 PM
 * @des
 * @updateAuthor
 * @updateDes
 */

@HiltViewModel
public class GetFaceViewModel extends BaseFaceViewModel {

    CapturePageRepo mCapturePageRepo;

    @Inject
    public GetFaceViewModel(CapturePageRepo capturePageRepo, AppExecutors appExecutors) {
        super(appExecutors);
        this.mCapturePageRepo = capturePageRepo;
    }

    public LiveData<Resource<Object>> uploadPic(String pid, String base64Str) {
        return mCapturePageRepo.uploadFace(pid, base64Str);
    }

}
