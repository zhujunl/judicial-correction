package com.miaxis.judicialcorrection.ui.setting;

import android.text.TextUtils;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.tencent.mmkv.MMKV;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * SettingViewModel
 *
 * @author zhangyw
 * Created on 5/7/21.
 */
@HiltViewModel
public class CameraConfigModel extends ViewModel {

    public ObservableField<String> cameraRGBId = new ObservableField<>();
    public ObservableField<String> cameraNIRId = new ObservableField<>();
    public ObservableField<String> cameraGPId = new ObservableField<>();

    @Inject
    public CameraConfigModel(AppDatabase appDatabase) {

    }

    public  void init(){
        MMKV mmkv = MMKV.defaultMMKV();
        int cameraRGBId = mmkv.getInt("cameraRGBId", 2);
        this.cameraRGBId.set(cameraRGBId+"");

        int cameraNIRId = mmkv.getInt("cameraNIRId", 0);
        this.cameraNIRId.set(cameraNIRId+"");

        int cameraGPId = mmkv.getInt("cameraGPId", 1);
        this.cameraGPId.set(cameraGPId+"");
    }

    public void save() {
        MMKV mmkv = MMKV.defaultMMKV();
        String s = cameraRGBId.get();
        if (TextUtils.isEmpty(s)){
            s="2";
        }
        mmkv.putInt("cameraRGBId",Integer.parseInt(s));
        String s1 = cameraNIRId.get();
        if (TextUtils.isEmpty(s1)){
            s1="0";
        }
        mmkv.putInt("cameraNIRId",Integer.parseInt(s1));
        String s2 = cameraGPId.get();
        if (TextUtils.isEmpty(s2)){
            s2="1";
        }
        mmkv.putInt("cameraGPId",Integer.parseInt(s2));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
