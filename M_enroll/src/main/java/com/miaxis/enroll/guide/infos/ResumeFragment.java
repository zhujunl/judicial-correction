package com.miaxis.enroll.guide.infos;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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

    @Override
    protected void initData(@NonNull FragmentResumeBinding binding, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(vm);

        MyAdapter adapter = new MyAdapter();
        if (vm.jobs.size() < 2) {
            vm.jobs.add(new Job());
            vm.jobs.add(new Job());
        }
        adapter.submitList(vm.jobs);
        binding.recyclerview.setAdapter(adapter);
        binding.addLine.setOnClickListener(v -> {
            vm.jobs.add(new Job());
            adapter.submitList(vm.jobs);
        });
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

        @Override
        protected void bind(ItemFragmentResumeBinding binding, Job item) {
            binding.setJob(item);
            binding.startTime.setOnClickListener(v -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(binding.getRoot().getContext());
                datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                    item.startTime = String.format("%s-%s-%s", year, month + 1, dayOfMonth);
                    binding.invalidateAll();
                });
                datePickerDialog.show();
            });
            binding.endTime.setOnClickListener(v -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(binding.getRoot().getContext());
                datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                    item.endTime = String.format("%s-%s-%s", year, month + 1, dayOfMonth);
                    binding.invalidateAll();
                });
                datePickerDialog.show();
            });

            String job = binding.getJob().job;
            if (TextUtils.isEmpty(job)) {
                binding.spinner.setSelection(0);
            } else {
                Resources res = binding.getRoot().getContext().getResources();
                String[] city = res.getStringArray(R.array.job);
                for (int i = 0; i < city.length; i++) {
                    if (Objects.equals(city[i], job)) {
                        binding.spinner.setSelection(i);
                        break;
                    }
                }
            }
            binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        binding.getJob().job = parent.getSelectedItem().toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
