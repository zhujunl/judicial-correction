package com.miaxis.enroll.guide.infos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.BaseMsgModel;
import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.GoHomeFragment;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentBaseMsgBinding;
import com.miaxis.enroll.guide.CaptureFuncFragment;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.utils.AppHints;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * BaseMsgFragment
 *
 * @author zhangyw
 * Created on 5/4/21.
 */
@AndroidEntryPoint
public class BaseMsgFragment extends BaseInfoFragment<FragmentBaseMsgBinding> {

    @Override
    protected int initLayout() {
        return R.layout.fragment_base_msg;
    }

    @Override
    protected void initView(@NonNull FragmentBaseMsgBinding binding, @Nullable Bundle savedInstanceState) {

    }

    @Inject
    Lazy<AppHints> appHintsLazy;
    EnrollSharedViewModel vm;
    @Override
    protected void initData(@NonNull FragmentBaseMsgBinding binding, @Nullable Bundle savedInstanceState) {
         vm = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);
//        BaseMsgModel model=new ViewModelProvider(this).get(BaseMsgModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(vm);
//        model.shiListLiveData.observe(this, listResource -> {
//            if (listResource.isSuccess()) {
//                model.setXian(model.xianChecked);
//            }
//        });
//        model.xianListLiveData.observe(this, listResource -> {
//            if (listResource.isSuccess()) {
//                model.setJiedao(model.jiedaoChecked);
//            }
//        });
//
//        model.jiedaoListLiveData.observe(this, listResource -> {
//            if (listResource.isSuccess()) {
//                setSpUnitView(listResource.data,binding.spinnerProvince);
//            }
//        });model.setSheng(null);
        binding.spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JusticeBureau justiceBureau = (JusticeBureau) parent.getSelectedItem();
                if (justiceBureau != null) {
                    vm.checkedJusticeBureau = justiceBureau;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vm.myPermissionJusticeBureau.observe(this, resource -> {
            Timber.i("MyPermissionJusticeBureau %s", resource);
            switch (resource.status) {
                case LOADING:
                    showLoading();
                    break;
                case ERROR:
                    dismissLoading();
                    appHintsLazy.get().showError(resource);
                    break;
                case SUCCESS:
                    dismissLoading();
                    SpAdapter spAdapter = new SpAdapter();

                    if (resource.data != null) {
                        boolean isHave = false;
                        for (JusticeBureau bean : resource.data) {
                            if ("请选择".equals(bean.getTeamName())){
                                isHave = true;
                                break;
                            }
                        }
                        if (!isHave) {
                            JusticeBureau justiceBureau = new JusticeBureau();
                            justiceBureau.setTeamName("请选择");
                            resource.data.add(0, justiceBureau);
                        }
                    }
                    spAdapter.submitList(resource.data);

                    int j = -1;
                    if (vm.checkedJusticeBureau != null && resource.data != null && vm.checkedJusticeBureau.getTeamId() != null) {//&vm.checkedJusticeBureau.getTeamId()
                        for (int i = 0; i < resource.data.size(); i++) {
                            if (vm.checkedJusticeBureau.getTeamId().equals(resource.data.get(i).getTeamId())) {
                                j = i;
                                break;
                            }
                        }
                    }
                    binding.spinnerProvince.setAdapter(spAdapter);
                    if (j != -1) {
                        binding.spinnerProvince.setSelection(j);
                    }
                    break;
            }
        });

    }
    @Inject
    AppHints appHints;

    @Override
    public boolean checkData() {
        JusticeBureau justiceBureau = vm.checkedJusticeBureau;
        if (justiceBureau == null || "请选择".equals(justiceBureau.getTeamName())) {
            appHints.showHint("请选择司法所");
        }

        return super.checkData();
    }

    //    private void setSpUnitView(List<JusticeBureau> beanList, Spinner spinner) {
//        SpAdapter adapter = new SpAdapter();
//        adapter.submitList(beanList);
//        spinner.setAdapter(adapter);
//    }
    public static class SpAdapter extends BaseAdapter {

        private List<JusticeBureau> data;

        public void submitList(List<JusticeBureau> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
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
            view.setText(data.get(position).getTeamName());
            return view;
        }
    }
}
