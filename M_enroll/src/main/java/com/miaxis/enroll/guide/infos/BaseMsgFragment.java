package com.miaxis.enroll.guide.infos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.BaseMsgModel;
import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentBaseMsgBinding;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;
import com.miaxis.judicialcorrection.base.db.po.Place;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

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

    @Override
    protected void initData(@NonNull FragmentBaseMsgBinding binding, @Nullable Bundle savedInstanceState) {
        EnrollSharedViewModel vm = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);
        BaseMsgModel model=new ViewModelProvider(this).get(BaseMsgModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(vm);
        model.shiListLiveData.observe(this, listResource -> {
            if (listResource.isSuccess()) {
                model.setXian(model.xianChecked);
            }
        });
        model.xianListLiveData.observe(this, listResource -> {
            if (listResource.isSuccess()) {
                model.setJiedao(model.jiedaoChecked);
            }
        });

        model.jiedaoListLiveData.observe(this, listResource -> {
            if (listResource.isSuccess()) {
                setSpView(listResource.data,binding.spinnerProvince);
            }
        });model.setSheng(null);

    }
    private void setSpView(List<JusticeBureau> observer, Spinner spinner) {
        List<String> list = new ArrayList<>();
        for (JusticeBureau p : observer) {
            list.add(p.getTeamName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
    }
}
