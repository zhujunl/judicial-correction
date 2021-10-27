package com.miaxis.enroll.guide.download;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.ApiResult;
import com.miaxis.judicialcorrection.base.api.ApiService;
import com.miaxis.judicialcorrection.base.api.ApkVersionResult;
import com.miaxis.judicialcorrection.base.repo.DownLoadRepo;
import com.miaxis.judicialcorrection.base.utils.AppExecutors;
import com.tencent.mmkv.MMKV;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DownLoadModel extends ViewModel {


    private final DownLoadRepo downLoadRepo;

    @Inject
    AppExecutors appExecutors;

    @Inject
    public DownLoadModel(DownLoadRepo downLoadRepo) {
        this.downLoadRepo = downLoadRepo;
    }

    public  LiveData<ApiResult<ApkVersionResult>> compareVersion(String type, String apkName, String apkVersion) {
        return downLoadRepo.compareVersions(type, apkName, apkVersion);
    }

    public void downloadApk(String path, String apkId, DownLoadListener downLoadListener) {
        appExecutors.diskIO().execute(()->{
            new DownloadClient()
                    .bindDownloadInfo(MMKV.defaultMMKV().getString("baseUrl2", BuildConfig.SERVER_URL2) + ApiService.downAPK + "?apkId=" +apkId, path)
                    .bindDownloadTimeOut(5 * 1000, 5 * 1000)
                    .download(downLoadListener);
        });
    }

    public interface DownLoadListener{
        void onProgress(int process);
        void onSuccess(String filePath);
        void onFail(String strError);
    }
}
