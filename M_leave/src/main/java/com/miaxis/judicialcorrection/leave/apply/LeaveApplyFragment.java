package com.miaxis.judicialcorrection.leave.apply;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.camera.CameraConfig;
import com.miaxis.camera.CameraHelper;
import com.miaxis.camera.CameraPreviewCallback;
import com.miaxis.camera.MXCamera;
import com.miaxis.camera.MXFrame;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.db.po.Place;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.TimeUtils;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DatePickDialog;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.leave.LeaveRepo;
import com.miaxis.judicialcorrection.leave.R;
import com.miaxis.judicialcorrection.leave.SpAdapter;
import com.miaxis.judicialcorrection.leave.databinding.FragmentLeaveApplyBinding;
import com.miaxis.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * @author Tank
 * @date 2021/5/7 10:08
 * @des
 * @updateAuthor
 * @updateDes
 */
@AndroidEntryPoint
public class LeaveApplyFragment extends BaseBindingFragment<FragmentLeaveApplyBinding> implements CameraPreviewCallback {

    private String title = "请假申请";

    private VerifyInfo verifyInfo;

    private ApplyViewModel mApplyViewModel;

    @Inject
    Lazy<AppHints> appHintsLazy;

    @Inject
    LeaveRepo mLeaveRepo;
    //申请书
    private String base64LiveChangeApplication;
    //申请资料
    private String base64LiveChangeData;
    private File mFilePath;
    //扫描类型
    private int scanType = 0;

    public LeaveApplyFragment(@NotNull VerifyInfo verifyInfo) {
        this.verifyInfo = verifyInfo;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_leave_apply;
    }

    @Override
    protected void initView(@NonNull FragmentLeaveApplyBinding binding, @Nullable Bundle savedInstanceState) {
        mApplyViewModel = new ViewModelProvider(this).get(ApplyViewModel.class);
        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> finish());
        mApplyViewModel.applyTime.set(TimeUtils.getTime());
        mFilePath = FileUtils.createFileParent(getContext());
        binding.btnApplicationScan.setOnClickListener(v -> {
            scanType = 0;
            openScanning();
        });
        binding.btnApplicationMaterialsScan.setOnClickListener(v -> {
            scanType = 1;
            openScanning();
        });
        binding.btnSubmit.setOnClickListener(v -> {
            String checkContent = mApplyViewModel.checkContent();
            if (!TextUtils.isEmpty(checkContent)) {
                appHintsLazy.get().showError(checkContent);
            } else {
                String typeCode="1";
                try {
                    int typePosition = binding.spReason.getSelectedItemPosition();
                    String[] stringArray = getResources().getStringArray(R.array.leavetype_code);
                    typeCode = stringArray[typePosition];
                }catch (Exception e){
                    e.getStackTrace();
                }

                mLeaveRepo.leaveAdd(
                        verifyInfo.pid,
                        TimeUtils.dateToString(mApplyViewModel.applyTime.get()),
                        "",
                        base64LiveChangeApplication,
                        base64LiveChangeData,
                        typeCode + "",
                        mApplyViewModel.temporaryGuardian.get() + "",
                        mApplyViewModel.relationShip.get() + "",
                        mApplyViewModel.contactNumber.get() + "",
                        mApplyViewModel.specificReasons.get(),
                        TimeUtils.dateToString(mApplyViewModel.endTime.get()),
                        TimeUtils.dateToString(mApplyViewModel.startTime.get()),
                        mApplyViewModel.days.get(),
                        "0",
                        mApplyViewModel.mAgencies.getValue().ID + "",
                        mApplyViewModel.details.get(),
                        mApplyViewModel.mProvince.getValue().ID + "",
                        mApplyViewModel.mDistrict.getValue().ID + "",
                        mApplyViewModel.mCity.getValue().ID + "",
                        mApplyViewModel.mAgencies.getValue().VALUE + "",
                        mApplyViewModel.mProvince.getValue().VALUE + "",
                        mApplyViewModel.mDistrict.getValue().VALUE + "",
                        mApplyViewModel.mCity.getValue().VALUE + ""

                ).observe(this, objectResource -> {
                    switch (objectResource.status) {
                        case LOADING:
                            showLoading(title, "正在提交" + title + "信息，请稍后");
                            break;
                        case ERROR:
                            dismissLoading();
                            appHintsLazy.get().showError("Error:" + objectResource.errorMessage);
                            break;
                        case SUCCESS:
                            dismissLoading();
                            new DialogResult(getContext(), new DialogResult.ClickListener() {
                                @Override
                                public void onBackHome(AppCompatDialog appCompatDialog) {
                                    appCompatDialog.dismiss();
                                    finish();
                                }

                                @Override
                                public void onTryAgain(AppCompatDialog appCompatDialog) {
                                    appCompatDialog.dismiss();
                                }

                                @Override
                                public void onTimeOut(AppCompatDialog appCompatDialog) {
                                    appCompatDialog.dismiss();
                                    finish();
                                }
                            }, new DialogResult.Builder(
                                    true,
                                    "提交成功",
                                    "",
                                    3, false
                            ).hideAllHideSucceedInfo(true)).show();
                            break;
                    }
                });
            }
        });

        mApplyViewModel.name.set(verifyInfo.name);
        mApplyViewModel.idCardNumber.set(verifyInfo.idCardNumber);
        binding.setData(mApplyViewModel);

        //        binding.tvApplyTime.setOnClickListener(v ->
        //                new DatePickDialog(getContext(), date -> mApplyViewModel.applyTime.set(date)).show()
        //        );

        binding.tvApplyStartTime.setOnClickListener(v -> {
                    new DatePickDialog(getContext(), date -> {
                        mApplyViewModel.startTime.set(date);
                        mApplyViewModel.days.set((int) TimeUtils.getDays(mApplyViewModel.startTime.get(), mApplyViewModel.endTime.get()) + "");
                    }).show();
                }
        );

        binding.tvApplyEndTime.setOnClickListener(v -> {
                    new DatePickDialog(getContext(), date -> {
                        mApplyViewModel.endTime.set(date);
                        mApplyViewModel.days.set((int) TimeUtils.getDays(mApplyViewModel.startTime.get(), mApplyViewModel.endTime.get()) + "");
                    }).show();
                }
        );

        // 户籍地
        mApplyViewModel.allProvince.observe(this, places -> {
            Timber.i("allProvince %s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spProvince.setAdapter(adapter);
        });

        mApplyViewModel.allCity.observe(this, places -> {
            Timber.i("allCity %s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spCity.setAdapter(adapter);
        });
        mApplyViewModel.allDistrict.observe(this, places -> {
            Timber.i("allDistrict %s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spDistrict.setAdapter(adapter);
        });
        mApplyViewModel.allAgencies.observe(this, places -> {
            Timber.i("allAgencies%s", places);
            SpAdapter adapter = new SpAdapter();
            adapter.submitList(places);
            binding.spStreet.setAdapter(adapter);
        });

        binding.spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    mApplyViewModel.mProvince.postValue((Place) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mApplyViewModel.mProvince.postValue(null);
            }
        });
        binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    mApplyViewModel.mCity.postValue((Place) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mApplyViewModel.mCity.postValue(null);
            }
        });
        binding.spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    mApplyViewModel.mDistrict.postValue((Place) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mApplyViewModel.mDistrict.postValue(null);
            }
        });
        binding.spStreet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (item instanceof Place) {
                    mApplyViewModel.mAgencies.postValue((Place) item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mApplyViewModel.mAgencies.postValue(null);
            }
        });
    }

    @Override
    protected void initData(@NonNull FragmentLeaveApplyBinding binding, @Nullable Bundle savedInstanceState) {


    }

    private void openScanning() {
        ZZResponse<?> init = CameraHelper.getInstance().init();
        if (ZZResponse.isSuccess(init)) {
            ZZResponse<MXCamera> mxCamera = CameraHelper.getInstance().createMXCamera(CameraConfig.Camera_SM);
            if (ZZResponse.isSuccess(mxCamera)) {
//                mxCamera.getData().setOrientation(CameraConfig.Camera_SM.previewOrientation);
                mxCamera.getData().setPreviewCallback(this);
                mxCamera.getData().start(null);
                mxCamera.getData().setNextFrameEnable();
            } else {
                appHintsLazy.get().showError("扫描失败！");
            }
        } else {
            appHintsLazy.get().showError("扫描设备初始化失败请点击重试！");
        }
    }

    @Override
    public void onPreview(MXFrame frame) {
        if (MXFrame.isNullCamera(frame)) {
            return;
        }
        String fileName = "sm" + scanType + ".jpg";
        File file = new File(mFilePath, fileName);
        boolean frameImage = frame.camera.saveFrameImage(file.getAbsolutePath());
        if (frameImage) {
            String base64Path = FileUtils.imageToBase64(file.getAbsolutePath());
            if (scanType == 0) {
                base64LiveChangeApplication = base64Path;
            } else {
                base64LiveChangeData = base64Path;
            }
            mHandler.post(() -> {
                if (scanType == 0) {
                    binding.tvApplicationInfoShow.setText(fileName);
                } else {
                    binding.tvApplicationInfoShow2.setText(fileName);
                }
            });
        } else {
            mHandler.post(() -> appHintsLazy.get().showError("扫描文件保存失败，请点击重试！"));
        }
        try {
            CameraHelper.getInstance().stop();
            frame.camera.stop();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private final static Handler mHandler = new Handler();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        base64LiveChangeApplication = null;
        base64LiveChangeData = null;
        mHandler.removeCallbacksAndMessages(null);
    }


}
