package com.miaxis.judicialcorrection.guide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.miaxis.judicialcorrection.adapter.HistorySignUpAdapter;
import com.miaxis.judicialcorrection.adapter.SignUpAdapter;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.benefit.R;
import com.miaxis.judicialcorrection.benefit.WelfareViewModel;
import com.miaxis.judicialcorrection.benefit.databinding.FragmentAlreadySignUpBinding;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * 已报名列表
 */
@AndroidEntryPoint
public class AlreadySignUpFragment extends BaseBindingFragment<FragmentAlreadySignUpBinding> {

    private WelfareViewModel viewModel;

    private int page = 1;
    private HistorySignUpAdapter mAdapter;
    private boolean isRefresh = true;
    private String mPid;


    @Override
    protected int initLayout() {
        return R.layout.fragment_already_sign_up;
    }

    @Override
    protected void initView(@NonNull @NotNull FragmentAlreadySignUpBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding.btnBackToHome.setOnClickListener(v -> {
            finish();
        });
    }
    @Override
    protected void initData(@NonNull @NotNull FragmentAlreadySignUpBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(WelfareViewModel.class);
        SignUpAdapter adapter = new SignUpAdapter();
        binding.rvActivityList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvActivityList.setAdapter(adapter);
        mAdapter = new HistorySignUpAdapter();
        initLoadMore();
        mPid = viewModel.mStrPid.getValue();
        binding.rvActivityList.setAdapter(mAdapter);
        setData();
    }


    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isRefresh = false;
                page++;
                setData();
            }
        });
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void setData() {
        viewModel.getHistoryWelfareInfo(page,mPid).observe(this, listResource -> {
            if (listResource.isSuccess()) {
                if (isRefresh) {
                    if (listResource.data != null && listResource.data.getList() != null) {
                        mAdapter.setNewInstance(listResource.data.getList());
                        if(listResource.data.getList().size()<10){
                            mAdapter.getLoadMoreModule().loadMoreEnd();
                        }
                    }else{
                        mAdapter.getLoadMoreModule().loadMoreEnd();
                    }
                } else {
                    if (listResource.data != null && listResource.data.getList() != null) {
                        mAdapter.addData(listResource.data.getList());
                        if (listResource.data.getList().size() < 10) {
                            //如果不够一页,显示没有更多数据布局
                            mAdapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            mAdapter.getLoadMoreModule().loadMoreComplete();
                        }
                    } else {
                        mAdapter.getLoadMoreModule().loadMoreEnd();
                    }
                }
            }
        });
    }
}