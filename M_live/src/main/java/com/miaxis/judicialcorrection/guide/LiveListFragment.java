package com.miaxis.judicialcorrection.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.miaxis.judicialcorrection.LiveChildItemClickListener;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.LiveAddressListBean;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundAdapter;
import com.miaxis.judicialcorrection.face.VerifyPageFragment;
import com.miaxis.judicialcorrection.live.LiveAddressChangeActivity;
import com.miaxis.judicialcorrection.live.LiveAddressChangeViewModel;
import com.miaxis.judicialcorrection.live.R;
import com.miaxis.judicialcorrection.live.databinding.FragmentLiveListBinding;
import com.miaxis.judicialcorrection.live.databinding.LayoutLiveItemBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LiveListFragment extends BaseBindingFragment<FragmentLiveListBinding> {

    private String title = "居住地变更申请";

    private LiveAddressChangeViewModel model;

    private Adapter adapter;

    public LiveListFragment() {
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_live_list;
    }

    @Override
    protected void initView(@NonNull FragmentLiveListBinding binding, @Nullable Bundle savedInstanceState) {
        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new Adapter();
        binding.rvList.setAdapter(adapter);
        binding.tvApply.setOnClickListener(v -> {
            if (getActivity() != null&&getActivity() instanceof LiveAddressChangeActivity) {
               ((LiveAddressChangeActivity) getActivity()).replaceFragment(new
                       VerifyPageFragment("居住地变更",model.personInfoMutableLiveData.getValue()));
            }
        });
    }

    @Override
    protected void initData(@NonNull FragmentLiveListBinding binding, @Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(getActivity()).get(LiveAddressChangeViewModel.class);
        if (model.personInfoMutableLiveData.getValue() != null) {
            model.searchLiveAddressChangeList(model.personInfoMutableLiveData.getValue().getId()).observe(this, observer -> {
                if (observer.data != null && observer.isSuccess()) {
                    adapter.submitList(observer.data.getList());
                }
            });
        } else {
            model.personInfoMutableLiveData.observe(this, ob -> {
                model.searchLiveAddressChangeList(model.personInfoMutableLiveData.getValue().getId()).observe(this, observer -> {
                    if (observer.isSuccess() && observer.data != null) {
                        adapter.submitList(observer.data.getList());
                    }
                });
            });
        }
        adapter.setChildItemClickListener(bean -> {
            if (getActivity() != null) {
                model.mId = bean.getId();
                ((LiveAddressChangeActivity) getActivity()).replaceFragment(new ToLiveProgressFragment());
            }
        });
    }

    static class Adapter extends BaseDataBoundAdapter<LiveAddressListBean.ListDTO, LayoutLiveItemBinding> {

        private LiveChildItemClickListener listener;

        public void setChildItemClickListener(LiveChildItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        protected LayoutLiveItemBinding createBinding(ViewGroup parent, int viewType) {
            return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_live_item, parent, false);
        }

        @Override
        protected void bind(LayoutLiveItemBinding binding, LiveAddressListBean.ListDTO item) {
            binding.setItem(item);
            binding.llOperation.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }

        @Override
        public void getPosition(LayoutLiveItemBinding binding, LiveAddressListBean.ListDTO item, int position) {
            super.getPosition(binding, item, position);
            item.setPos(position + 1);
        }
    }
}
