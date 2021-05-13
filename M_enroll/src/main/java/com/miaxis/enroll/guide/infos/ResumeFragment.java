package com.miaxis.enroll.guide.infos;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentResumeBinding;
import com.miaxis.enroll.databinding.ItemFragmentResumeBinding;
import com.miaxis.enroll.vo.Job;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * BaseMsgFragment
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@AndroidEntryPoint
public class ResumeFragment extends BaseInfoFragment<FragmentResumeBinding> {

    @Override
    protected int initLayout() {
        return R.layout.fragment_resume;
    }

    @Override
    protected void initView(@NonNull FragmentResumeBinding binding, @Nullable Bundle savedInstanceState) {

    }

    EnrollSharedViewModel vm;
    MyAdapter adapter;

    @Override
    protected void initData(@NonNull FragmentResumeBinding binding, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(vm);

        adapter = new MyAdapter();
        String[] re = getResources().getStringArray(R.array.job);
        if (vm.jobs.size() < 2) {
            Job job = new Job();
            job.job = re[0];
            vm.jobs.add(job);
            vm.jobs.add(job);
        }
        adapter.submitList(vm.jobs);
        binding.recyclerview.setAdapter(adapter);
        binding.addLine.setOnClickListener(v -> {
            Job job = new Job();
            job.job = re[0];
            vm.jobs.add(job);
            adapter.submitLists(vm.jobs);
            adapter.notifyItemRangeInserted(vm.jobs.size() - 1, 1);
            if (vm.jobs.size() > 2) {
                binding.deleteLine.setVisibility(View.VISIBLE);
            }
            setRvHeight();
        });

        binding.deleteLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = vm.jobs.size() - 1;
                vm.jobs.remove(size);
                adapter.notifyItemRemoved(size);
                if (vm.jobs.size() <= 2) {
                    binding.deleteLine.setVisibility(View.GONE);
                }
            }
        });
        setRvHeight();
    }

    private void setRvHeight() {
        if (adapter.getItemCount() >= 9) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.recyclerview.getLayoutParams();
            params.height = 700;
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.recyclerview.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        }
    }

    @Inject
    AppHints appHints;

    @Override
    public boolean checkData() {
        Timber.v("jobs %s", vm.jobs);
        if (TextUtils.isEmpty(vm.jobs.get(0).company)) {
            appHints.toast("您没有填写简历");
            return true;
        }
        if (TextUtils.isEmpty(vm.jobs.get(1).company)) {
            appHints.showHint("请至少填写两项简历");
            return false;
        }

        for (int i = 0; i < vm.jobs.size(); i++) {
            if (TextUtils.isEmpty(vm.jobs.get(i).job)) {
                appHints.showHint("请填写职位");
                return false;
            }
            if (TextUtils.isEmpty(vm.jobs.get(i).startTime)
                    || TextUtils.isEmpty(vm.jobs.get(i).endTime)) {
                appHints.showHint("请填写日期");
                return false;
            }
            if (vm.jobs.get(i).startTime.compareTo(vm.jobs.get(i).endTime) > 0) {
                appHints.showHint("结束日期不能小于开始日期");
                return false;
            }
            if (TextUtils.isEmpty(vm.jobs.get(i).company)) {
                appHints.showHint("请填写工作单位");
                return false;
            }

        }
        return super.checkData();

    }

    static class MyAdapter extends BaseDataBoundAdapter<Job, ItemFragmentResumeBinding> {

        @Override
        protected ItemFragmentResumeBinding createBinding(ViewGroup parent, int viewType) {
            ItemFragmentResumeBinding inflate = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_fragment_resume, parent, false);
            if (viewType == 1) {
                inflate.getRoot().setBackgroundColor(0xffF2F2F2);
            } else {
                inflate.getRoot().setBackgroundColor(0xffffffff);
            }
            return inflate;
        }


        @Override
        public int getItemViewType(int position) {
            return position % 2;
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        protected void bind(ItemFragmentResumeBinding binding, Job item) {
            binding.setJob(item);
            binding.startTime.setOnClickListener(v -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(binding.getRoot().getContext());
                DatePicker datePicker = datePickerDialog.getDatePicker();
                Date d = new Date();
                datePicker.setMaxDate(d.getTime());
                datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                    item.startTime = simpleDateFormat.format(new Date(year - 1900, month, dayOfMonth));
                    binding.invalidateAll();
                });
                datePickerDialog.show();
            });
            binding.endTime.setOnClickListener(v -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(binding.getRoot().getContext());
                DatePicker datePicker = datePickerDialog.getDatePicker();
                Date d = new Date();
                datePicker.setMaxDate(d.getTime());
                datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                    item.endTime = simpleDateFormat.format(new Date(year - 1900, month, dayOfMonth));
                    binding.invalidateAll();
                });
                datePickerDialog.show();
            });
        }
    }
}
