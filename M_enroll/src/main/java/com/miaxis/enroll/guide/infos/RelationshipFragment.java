package com.miaxis.enroll.guide.infos;

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
import com.miaxis.enroll.databinding.FragmentRelationshipBinding;
import com.miaxis.enroll.databinding.ItemFragmentRelationshipBinding;
import com.miaxis.enroll.vo.Family;
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
public class RelationshipFragment extends BaseInfoFragment<FragmentRelationshipBinding> {

    @Override
    protected int initLayout() {
        return R.layout.fragment_relationship;
    }

    @Override
    protected void initView(@NonNull FragmentRelationshipBinding binding, @Nullable Bundle savedInstanceState) {

    }

    EnrollSharedViewModel vm;

    @Override
    protected void initData(@NonNull FragmentRelationshipBinding binding, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(vm);

        MyAdapter adapter = new MyAdapter();
        if (vm.relationships.size()<2){
            vm.relationships.add(new Family());
            vm.relationships.add(new Family());
        }
        adapter.submitList(vm.relationships);
        binding.recyclerview.setAdapter(adapter);
        binding.addLine.setOnClickListener(v -> {
            vm.relationships.add(new Family());
            adapter.submitList(vm.relationships);
        });
    }

    @Inject
    AppHints appHints;

    @Override
    public boolean checkData() {
        Timber.v("relationships %s", vm.relationships);
        if (TextUtils.isEmpty(vm.relationships.get(0).name)){
            appHints.showHint("您没有填写社会关系");
            return true;
        }
        for (int i = 0; i < vm.relationships.size(); i++) {
            if (TextUtils.isEmpty(vm.relationships.get(i).name)) {
                appHints.showHint("请填写姓名");
                return false;
            }
            if (TextUtils.isEmpty(vm.relationships.get(i).relationship)) {
                appHints.showHint("请选择关系");
                return false;
            }
            if (TextUtils.isEmpty(vm.relationships.get(i).job)) {
                appHints.showHint("请填写工作单位");
                return false;
            }
            if (TextUtils.isEmpty(vm.relationships.get(i).phone)) {
                appHints.showHint("请填写联系方式");
                return false;
            }
        }
        return super.checkData();
    }

    static class MyAdapter extends BaseDataBoundAdapter<Family, ItemFragmentRelationshipBinding> {

        @Override
        protected ItemFragmentRelationshipBinding createBinding(ViewGroup parent, int viewType) {
            ItemFragmentRelationshipBinding inflate = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_fragment_relationship, parent, false);
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
        protected void bind(ItemFragmentRelationshipBinding binding, Family item) {
            binding.setData(item);
            String rs = binding.getData().relationship;
            if (TextUtils.isEmpty(rs)) {
                binding.spinner.setSelection(0);
            } else {
                Resources res = binding.getRoot().getContext().getResources();
                String[] city = res.getStringArray(R.array.relationship);
                for (int i = 0; i < city.length; i++) {
                    if (Objects.equals(city[i], rs)) {
                        binding.spinner.setSelection(i);
                        break;
                    }
                }
            }
            binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        binding.getData().relationship = parent.getSelectedItem().toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
