package com.miaxis.judicialcorrection.leave.apply;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.po.Place;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.TimeUtils;
import com.miaxis.judicialcorrection.dialog.DatePickDialog;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.leave.LeaveRepo;
import com.miaxis.judicialcorrection.leave.R;
import com.miaxis.judicialcorrection.leave.databinding.FragmentLeaveApplyBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

        binding.btnSubmit.setOnClickListener(v -> {
            String checkContent = mApplyViewModel.checkContent();
            if (!TextUtils.isEmpty(checkContent)) {
                appHintsLazy.get().showError(checkContent);
            } else {
                mLeaveRepo.leaveAdd(
                        verifyInfo.pid,
                        mApplyViewModel.applyTime.get(),
                        "",
                        mApplyViewModel.specificReasons.get(),
                        mApplyViewModel.endTime.get(),
                        mApplyViewModel.startTime.get(),
                        mApplyViewModel.days.get(),
                        "否",
                        mApplyViewModel.mAgencies.getValue().ID + "",
                        mApplyViewModel.details.get(),
                        mApplyViewModel.mProvince.getValue().ID + "",
                        mApplyViewModel.mDistrict.getValue().ID + "",
                        mApplyViewModel.mCity.getValue().ID + ""
                ).observe(this, new Observer<Resource<Object>>() {
                    @Override
                    public void onChanged(Resource<Object> objectResource) {
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
                                adapter.setData(leaveResource.data);
                                break;
                        }
                    }
                });
            }
        });

        mApplyViewModel.name.set(verifyInfo.name);
        mApplyViewModel.idCardNumber.set(verifyInfo.idCardNumber);
        binding.setData(mApplyViewModel);

        binding.tvApplyTime.setOnClickListener(v ->
                new DatePickDialog(getContext(), binding.tvApplyTime::setText).show()
        );

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
            LeaveApplyFragment.SpAdapter adapter = new LeaveApplyFragment.SpAdapter();
            adapter.submitList(places);
            binding.spProvince.setAdapter(adapter);
        });

        mApplyViewModel.allCity.observe(this, places -> {
            Timber.i("allCity %s", places);
            LeaveApplyFragment.SpAdapter adapter = new LeaveApplyFragment.SpAdapter();
            adapter.submitList(places);
            binding.spCity.setAdapter(adapter);
        });
        mApplyViewModel.allDistrict.observe(this, places -> {
            Timber.i("allDistrict %s", places);
            LeaveApplyFragment.SpAdapter adapter = new LeaveApplyFragment.SpAdapter();
            adapter.submitList(places);
            binding.spDistrict.setAdapter(adapter);
        });
        mApplyViewModel.allAgencies.observe(this, places -> {
            Timber.i("allAgencies%s", places);
            LeaveApplyFragment.SpAdapter adapter = new LeaveApplyFragment.SpAdapter();
            adapter.submitList(places);
            binding.spStreet.setAdapter(adapter);
        });

        //居住地
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

    public static class SpAdapter extends BaseAdapter {

        private List<Place> data;

        public void submitList(List<Place> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Place getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
            view.setText(getItem(position).VALUE);
            return view;
        }
    }

}
