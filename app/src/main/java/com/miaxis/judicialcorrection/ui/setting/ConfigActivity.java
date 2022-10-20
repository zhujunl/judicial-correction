package com.miaxis.judicialcorrection.ui.setting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.miaxis.enroll.guide.download.DownLoadModel;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingActivity;
import com.miaxis.judicialcorrection.base.api.ApkVersionResult;
import com.miaxis.judicialcorrection.base.utils.AppToast;
import com.miaxis.judicialcorrection.base.utils.DeviceModelUtils;
import com.miaxis.judicialcorrection.base.utils.FileUtils;
import com.miaxis.judicialcorrection.databinding.ActivityCameraConfigBinding;
import com.miaxis.judicialcorrection.ui.main.MainActivity;
import com.miaxis.m_facelicense.Dialog.LicenseDialog;
import com.miaxis.m_facelicense.License.LicenseManager;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ConfigActivity extends BaseBindingActivity<ActivityCameraConfigBinding> {

    private ConfigModel model;
    private static final Handler mHandler = new Handler();
    private DownLoadModel downLoadModel;

    @Inject
    AppToast appToast;

    private ProgressDialog progressDialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_camera_config;
    }

    @Override
    protected void initView(@NonNull @NotNull ActivityCameraConfigBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        downLoadModel = new ViewModelProvider(this).get(DownLoadModel.class);
        initDialog();
        binding.btnClose.setOnClickListener(v -> finish());
        //清空时间戳
        binding.btnClearTime.setOnClickListener(v -> {

        });
        //更新
        binding.btnCheckForUpdates.setOnClickListener(v -> {
            String path = FileUtils.createSerialNumberFile() + ".apk";
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(getApplicationContext(), "无法保存apk", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                downLoadModel.compareVersion(DeviceModelUtils.getDeviceUpdateName(), "司法矫正",
                        getPackageManager().getPackageInfo(getPackageName(), 0).versionName).observe(ConfigActivity.this, apkVersionResource -> {
                    if (apkVersionResource.code == 200) {
                        if (null != apkVersionResource.getData()) {
                            String apkId = apkVersionResource.getData().getId();
                            String fileLength = apkVersionResource.getData().getFileLength();
                            int max = Integer.parseInt(fileLength.substring(0, fileLength.indexOf(".")));
                            double per = max * 1.00 / 100;
                            if (TextUtils.isEmpty(apkId)) {
                                return;
                            }
                            ApkVersionResult apkVersionResult = apkVersionResource.getData();
                            new MaterialDialog.Builder(this)
                                    .title("APP安装")
                                    .content("确认安装【" + apkVersionResult.getPackageName() + "】，版本：" + apkVersionResult.getVersionName() + " ？")
                                    .positiveText("安装")
                                    .onPositive((dialog, which) -> {
                                        dialog.dismiss();
                                        downLoadModel.downloadApk(path, apkId, new DownLoadModel.DownLoadListener() {
                                            @Override
                                            public void onProgress(int process) {
                                                runOnUiThread(() -> {
                                                    progressDialog.show();
                                                    progressDialog.setProgress((int) (process / per));
                                                });

                                            }

                                            @Override
                                            public void onSuccess(String filePath) {
                                                runOnUiThread(() -> {
                                                    progressDialog.dismiss();
                                                    installApkFile(filePath);
                                                });
                                            }

                                            @Override
                                            public void onFail(String strError) {
                                                runOnUiThread(() -> progressDialog.cancel());
                                            }
                                        });
                                    })
                                    .negativeText("取消")
                                    .onNegative((dialog, which) -> {
                                        dialog.dismiss();
                                    })
                                    .autoDismiss(false)
                                    .cancelable(false)
                                    .show();

                        } else {
                            appToast.show("当前已是最新版本");
                        }
                    } else {
                        appToast.show("网络请求失败，请检查联网情况");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.btnCheckForLicense.setOnClickListener(v -> {
            String license = LicenseManager.getInstance().VerifyLicense(this);
            Toast.makeText(this, license, Toast.LENGTH_SHORT).show();
            if (!TextUtils.equals(license, "验证通过")) {
                LicenseDialog licenseDialog = new LicenseDialog(this, false, result -> runOnUiThread(() -> Toast.makeText(ConfigActivity.this, result, Toast.LENGTH_SHORT).show()));
                licenseDialog.show();
            }
        });
    }

    public void installApkFile(String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = androidx.core.content.FileProvider.getUriForFile(this, this.getPackageName() + ".fileProvider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }


    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在下载中");
        progressDialog.setMessage(null);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }


    @Override
    protected void initData(@NonNull @NotNull ActivityCameraConfigBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(this).get(ConfigModel.class);
        binding.setData(model);

        model.init();
        //        if ("2".equals(model.cameraRGBId.get())) {
        //            binding.group1.check(R.id.cameraId_2);
        //        } else {
        //            binding.group1.check(R.id.cameraId_0);
        //        }
        //        if ("2".equals(model.cameraNIRId.get())) {
        //            binding.group2.check(R.id.cameraNir_2);
        //        } else {
        //            binding.group2.check(R.id.cameraNir_0);
        //        }

        binding.btnSave.setOnClickListener(v -> {
                    //                    if (binding.group1.getCheckedRadioButtonId() == R.id.cameraId_2) {
                    //                        model.cameraRGBId.set("2");
                    //                    } else {
                    //                        model.cameraRGBId.set("0");
                    //                    }
                    //                    if (binding.group2.getCheckedRadioButtonId() == R.id.cameraNir_2) {
                    //                        model.cameraNIRId.set("2");
                    //                    } else {
                    //                        model.cameraNIRId.set("0");
                    //                    }
                    RadioButton radioButton1 = findViewById(binding.group1.getCheckedRadioButtonId());
                    model.cameraRGBId.set(radioButton1.getText().toString());
                    RadioButton radioButton2 = findViewById(binding.group2.getCheckedRadioButtonId());
                    RadioButton radioButton3 = findViewById(binding.group3.getCheckedRadioButtonId());
                    model.cameraNIRId.set(radioButton2.getText().toString());
                    model.cameraGPId.set(radioButton3.getText().toString());
                    model.save();
                    //            EquipmentConfigCameraEntity equipmentConfigCameraEntity = model.setCameraInfo(3);
                    //            BaseApplication.application.setCameraConfig(equipmentConfigCameraEntity);
                    showLoading();
                    mHandler.postDelayed(() -> {
                        dismissLoading();
                        finish();
                    }, 2000);
                }
        );
    }
}