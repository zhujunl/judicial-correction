package com.miaxis.enroll.guide.infos;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        adapter.setItemPresenter(new RecyclerBindPresenter());
        String[] re = getResources().getStringArray(R.array.job);
        if (vm.jobs.size() < 2) {
            Job job = new Job();
            job.jb.set( re[0]);
            vm.jobs.add(job);
            Job job1 = new Job();
            job1.jb.set(re[0]);
            vm.jobs.add(job1);
        }
        adapter.submitList(vm.jobs);
        binding.recyclerview.setAdapter(adapter);
        binding.addLine.setOnClickListener(v -> {
            Job job = new Job();
            job.jb.set( re[0]);
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
        for (Job job: vm.jobs){
            job.startTime=job.st.get();
            job.endTime=job.et.get();
            job.company=job.cy.get();
            job.job=job.jb.get();
        }
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



        @Override
        protected void bind(ItemFragmentResumeBinding binding, Job item) {
            binding.setJob(item);
            binding.setItemPresenter(ItemPresenter);


        }

        private RecyclerBindPresenter ItemPresenter;

        /**
         * 用于设置Item的事件Presenter
         */
        public void setItemPresenter(RecyclerBindPresenter itemPresenter) {
            ItemPresenter = itemPresenter;
        }
    }

    public class RecyclerBindPresenter implements IBaseBindingPresenter {

        public void onStartTime(Job item, TextView textView) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DatePickerDialog datePickerDialog = new DatePickerDialog(binding.getRoot().getContext());
            DatePicker datePicker = datePickerDialog.getDatePicker();
            Date d = new Date();
            datePicker.setMaxDate(d.getTime());
            datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                item.st.set(simpleDateFormat.format(new Date(year - 1900, month, dayOfMonth)));
//                textView.setText(item.st.get());
//                binding.invalidateAll();
            });
            datePickerDialog.show();
        }

        /**
         *
         */
        public void onEndTime(Job item,TextView textView) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DatePickerDialog datePickerDialog = new DatePickerDialog(binding.getRoot().getContext());
            DatePicker datePicker = datePickerDialog.getDatePicker();
            Date d = new Date();
            datePicker.setMaxDate(d.getTime());
            datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                item.et.set(simpleDateFormat.format(new Date(year - 1900, month, dayOfMonth)));
//                textView.setText(item.et.get());
//                binding.invalidateAll();
            });
            datePickerDialog.show();
        }
    }
}
