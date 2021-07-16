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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_common.adapter.PreviewPageAdapter;
import com.example.m_common.dialog.ToViewBigPictureDialog;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.db.po.Place;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.TimeUtils;
import com.miaxis.judicialcorrection.dialog.DatePickDialog;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.example.m_common.dialog.HighShotMeterDialog;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.leave.LeaveRepo;
import com.miaxis.judicialcorrection.leave.R;
import com.miaxis.judicialcorrection.leave.SpAdapter;
import com.miaxis.judicialcorrection.leave.databinding.FragmentLeaveApplyBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
public class LeaveApplyFragment extends BaseBindingFragment<FragmentLeaveApplyBinding> {

    private String title = "请假申请";

    private VerifyInfo verifyInfo;

    private ApplyViewModel mApplyViewModel;

    @Inject
    Lazy<AppHints> appHintsLazy;

    @Inject
    LeaveRepo mLeaveRepo;


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

        binding.btnApplicationScan.setOnClickListener(v -> {
            showPreInfo(1);
        });
        binding.btnApplicationMaterialsScan.setOnClickListener(v -> {
            showPreInfo(2);
        });
        binding.btnSubmit.setOnClickListener(v -> {
           submit();
        });

        mApplyViewModel.name.set(verifyInfo.name);
        mApplyViewModel.idCardNumber.set(verifyInfo.idCardNumber);
        binding.setData(mApplyViewModel);
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
            addPlace(places);
            adapter.submitList(places);
            binding.spProvince.setAdapter(adapter);
        });

        mApplyViewModel.allCity.observe(this, places -> {
            Timber.i("allCity %s", places);
            SpAdapter adapter = new SpAdapter();
            addPlace(places);
            adapter.submitList(places);
            binding.spCity.setAdapter(adapter);
        });
        mApplyViewModel.allDistrict.observe(this, places -> {
            Timber.i("allDistrict %s", places);
            SpAdapter adapter = new SpAdapter();
            addPlace(places);
            adapter.submitList(places);
            binding.spDistrict.setAdapter(adapter);
        });
        mApplyViewModel.allAgencies.observe(this, places -> {
            Timber.i("allAgencies%s", places);
            SpAdapter adapter = new SpAdapter();
            addPlace(places);
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
        initHeightCamera();
    }

    @Override
    protected void initData(@NonNull FragmentLeaveApplyBinding binding, @Nullable Bundle savedInstanceState) {

    }
    private void addPlace(List<Place> places) {
        for (Place p : places) {
            if (!TextUtils.isEmpty(p.VALUE)) {
                Place place = new Place();
                place.VALUE = "";
                place.ID = 0L;
                places.add(0, place);
                break;
            }
        }
    }

    private  void submit(){
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
            //base64高拍仪
            String[] changeApplication = new String[mAdapterData.getData().size()];
            for (int i = 0; i < mAdapterData.getData().size(); i++) {
                changeApplication[i] = mAdapterData.getData().get(i).getBase64();
            }
            String[] changeApplication2 = new String[mAdapterDataMaterial.getData().size()];
            for (int i = 0; i < mAdapterDataMaterial.getData().size(); i++) {
                changeApplication2[i] = mAdapterDataMaterial.getData().get(i).getBase64();
            }
            mLeaveRepo.leaveAdd(
                    verifyInfo.pid,
                    TimeUtils.dateToString(mApplyViewModel.applyTime.get()),
                    "",
                    changeApplication,
                    changeApplication2,
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
    }

    /*=====================================高拍仪========================================*/
    private PreviewPageAdapter mAdapterData;
    private PreviewPageAdapter mAdapterDataMaterial;

    private void initHeightCamera() {
        mAdapterData = new PreviewPageAdapter();
        binding.rvLeaveApplication.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        binding.rvLeaveApplication.setAdapter(mAdapterData);
        mAdapterData.setOnItemClickListener((adapter, view, position) -> {
            String path = mAdapterData.getData().get(position).getPath();
            showBigPicture(path);
        });
        mAdapterData.setOnItemLongClickListener((adapter, view, position) -> {
            appHintsLazy.get().showHint("是否要删除此图片", (dialog, which) -> {
                mAdapterData.getData().remove(position);
                mAdapterData.notifyDataSetChanged();
                if(mAdapterData.getData().isEmpty()) {
                    mApplyViewModel.setControlShowHide(1, true);
                }
            });
            return true;
        });

        mAdapterDataMaterial = new PreviewPageAdapter();
        binding.rvMaterial.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        binding.rvMaterial.setAdapter(mAdapterDataMaterial);

        mAdapterDataMaterial.setOnItemClickListener((adapter, view, position) -> {
            String path = mAdapterDataMaterial.getData().get(position).getPath();
            showBigPicture(path);
        });
        mAdapterDataMaterial.setOnItemLongClickListener((adapter, view, position) -> {
            appHintsLazy.get().showHint("是否要删除此图片", (dialog, which) -> {
                mAdapterDataMaterial.getData().remove(position);
                mAdapterDataMaterial.notifyDataSetChanged();
                if(mAdapterDataMaterial.getData().isEmpty()) {
                    mApplyViewModel.setControlShowHide(2, true);
                }
            });
            return true;
        });
    }

    //预览
    private void showBigPicture(String path) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        new ToViewBigPictureDialog(getActivity(), new ToViewBigPictureDialog.ClickListener() {
        }, new ToViewBigPictureDialog.Builder().setPathFile(path)).show();
    }

    //显示高拍仪预览内容
    private void showPreInfo(int type) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        new HighShotMeterDialog(getActivity(), list -> {
            boolean isNullOrEmpty = (list == null || list.size() == 0);
            mApplyViewModel.setControlShowHide(type, isNullOrEmpty);
            if (type == 1&&!isNullOrEmpty) {
                mAdapterData.setNewInstance(list);
            }
            if (type == 2&&!isNullOrEmpty) {
                mAdapterDataMaterial.setNewInstance(list);
            }
        }).show();
    }
    /*=====================================end高拍仪========================================*/
    private final static Handler mHandler = new Handler();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacksAndMessages(null);
    }
}
