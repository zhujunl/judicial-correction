package com.miaxis.judicialcorrection.ui.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.BaseApplication;
import com.miaxis.judicialcorrection.base.BuildConfig;
import com.miaxis.judicialcorrection.base.api.vo.EquipmentConfigCameraEntity;
import com.miaxis.judicialcorrection.base.db.AppDatabase;
import com.miaxis.judicialcorrection.base.utils.MacUtils;
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
public class ConfigModel extends ViewModel {
    //摄像头id
    public ObservableField<String> cameraRGBId = new ObservableField<>();
    public ObservableField<String> cameraNIRId = new ObservableField<>();
    public ObservableField<String> cameraGPId = new ObservableField<>();
    //版本
    public ObservableField<String> version = new ObservableField<>();
    //地址
    public ObservableField<String> baseUrl = new ObservableField<>();
    //tokenUrl
    public ObservableField<String> baseToken = new ObservableField<>();
    //声纹指纹baseUrl
    public ObservableField<String> baseUrlFingerAndFace = new ObservableField<>();
    //人脸质量阈值
    public ObservableField<String> faceQuality = new ObservableField<>();
    //人脸比对阈值
    public ObservableField<String> faceComparison = new ObservableField<>();
    //产品mac
    public ObservableField<String> productMac = new ObservableField<>();
    //是否影藏camera id选项从
    public ObservableField<Integer> isHide = new ObservableField<>();

    @Inject
    public ConfigModel(AppDatabase appDatabase) {
        String version = getVersionInfo();
        this.version.set(version);
        String mac = getMacInfo();
        this.productMac.set(mac);
        MMKV mmkv = MMKV.defaultMMKV();
        String baseUrl = mmkv.getString("baseUrl", BuildConfig.SERVER_URL);
        this.baseUrl.set(baseUrl);
        String baseToken = mmkv.getString("baseToken", BuildConfig.TOKEN_URL);
        this.baseToken.set(baseToken);
        String baseUrl2 = mmkv.getString("baseUrl2", BuildConfig.SERVER_URL2);
        this.baseUrlFingerAndFace.set(baseUrl2);
        String faceQuality = mmkv.getString("faceQuality", String.valueOf(30));
        this.faceQuality.set(faceQuality);
        String faceComparison = mmkv.getString("faceComparison", String.valueOf(75));
        this.faceComparison.set(faceComparison);
        if (BuildConfig.EQUIPMENT_TYPE == 3) {
            isHide.set(View.VISIBLE);
        } else {
            isHide.set(View.GONE);
        }
    }



    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersionInfo() {
        try {
            PackageManager manager = BaseApplication.application.getPackageManager();
            PackageInfo info = manager.getPackageInfo(BaseApplication.application.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * 得到mac地址
     *
     * @return mac地址
     */
    private String getMacInfo() {
        return MacUtils.getMac(BaseApplication.application);
    }

    public void init() {
        MMKV mmkv = MMKV.defaultMMKV();
        int cameraRGBId = mmkv.getInt("cameraRGBId", 2);
        this.cameraRGBId.set(cameraRGBId + "");

        int cameraNIRId = mmkv.getInt("cameraNIRId", 0);
        this.cameraNIRId.set(cameraNIRId + "");

        int cameraGPId = mmkv.getInt("cameraGPId", 1);
        this.cameraGPId.set(cameraGPId + "");
    }

    public void save() {
        MMKV mmkv = MMKV.defaultMMKV();
        String s = cameraRGBId.get();
        if (TextUtils.isEmpty(s)) {
            s = "2";
        }
        mmkv.putInt("cameraRGBId", Integer.parseInt(s));
        String s1 = cameraNIRId.get();
        if (TextUtils.isEmpty(s1)) {
            s1 = "0";
        }
        mmkv.putInt("cameraNIRId", Integer.parseInt(s1));
        String s2 = cameraGPId.get();
        if (TextUtils.isEmpty(s2)) {
            s2 = "1";
        }
        mmkv.putInt("cameraGPId", Integer.parseInt(s2));
        mmkv.putString("baseUrl", baseUrl.get());
        mmkv.putString("baseToken", baseToken.get());
        mmkv.putString("baseUrl2", baseUrlFingerAndFace.get());
        mmkv.putString("faceQuality", faceQuality.get());
        mmkv.putString("faceComparison", faceComparison.get());

        //        if (TextUtils.isEmpty(quality)) {
//            FaceConfig.threshold = 0.75f;
//            FaceConfig.thresholdIdCard = 0.75f;
//        } else {
//            FaceConfig.threshold = Float.parseFloat(quality) * 0.01;
//            FaceConfig.thresholdIdCard = Float.parseFloat(quality) *0.01;
//        }
//        if (TextUtils.isEmpty(faceCom)) {
//            FaceConfig.faceComparison = 30;
//        } else {
//            FaceConfig.faceComparison = Integer.parseInt(faceCom);
//        }
    }

    /**
     * dev 配置摄像头设备类型
     * @param equipmentType 设备类型  1柜式 3小台式
     */
    public EquipmentConfigCameraEntity setCameraInfo(int equipmentType) {
        MMKV mmkv = MMKV.defaultMMKV();
        Gson gson = new Gson();
        EquipmentConfigCameraEntity entity = new EquipmentConfigCameraEntity();
        if (equipmentType == 3) {
            entity.cameraRGBId = 1;
            entity.cameraNIRId = 0;
            entity.cameraSMId = 2;
            entity.bufferOrientation = 90;
            entity.previewOrientation = 90;
            entity.bufferOrientationSM = 90;
            entity.previewOrientationSM = 90;
            entity.savePictureRotationSize = 270;
        } else {
            String s = cameraRGBId.get();
            if (TextUtils.isEmpty(s)) {
                s = "2";
            }
            String s1 = cameraNIRId.get();
            if (TextUtils.isEmpty(s1)) {
                s1 = "0";
            }
            String s2 = cameraGPId.get();
            if (TextUtils.isEmpty(s2)) {
                s2 = "1";
            }
            entity.cameraRGBId = Integer.parseInt(s);
            entity.cameraNIRId = Integer.parseInt(s1);
            entity.cameraSMId = Integer.parseInt(s2);
            entity.bufferOrientation = 0;
            entity.previewOrientation = 0;
            entity.bufferOrientationSM = 0;
            entity.previewOrientationSM = 0;
            entity.savePictureRotationSize = 0;
        }
        int fq=TextUtils.isEmpty(faceQuality.get())?30:Integer.parseInt(faceQuality.get());
        int fc=TextUtils.isEmpty(faceComparison.get())?75:Integer.parseInt(faceComparison.get());
        entity.faceQuality=fq;
        entity.faceComparison=fc;
        String toJson = gson.toJson(entity);
        mmkv.putString("camera_info", toJson);
        return entity;
    }
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
