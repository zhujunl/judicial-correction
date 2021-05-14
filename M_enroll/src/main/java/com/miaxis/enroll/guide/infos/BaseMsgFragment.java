package com.miaxis.enroll.guide.infos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.BaseMsgModel;
import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentBaseMsgBinding;
import com.miaxis.judicialcorrection.base.db.po.JusticeBureau;

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
                setSpUnitView(listResource.data,binding.spinnerProvince);
            }
        });model.setSheng(null);
        binding.spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JusticeBureau  justiceBureau= (JusticeBureau) binding.spinnerProvince.getSelectedItem();
                if (justiceBureau!=null) {
                    vm.checkedJusticeBureau = justiceBureau;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void setSpUnitView(List<JusticeBureau> beanList, Spinner spinner) {
        SpAdapter adapter = new SpAdapter();
        adapter.submitList(beanList);
        spinner.setAdapter(adapter);
    }
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
