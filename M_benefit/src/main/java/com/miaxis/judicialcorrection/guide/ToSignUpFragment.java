package com.miaxis.judicialcorrection.guide;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.miaxis.judicialcorrection.ChildItemClickListener;
import com.miaxis.judicialcorrection.adapter.SignUpAdapter;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.api.vo.SignUpContentBean;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.benefit.PublicWelfareActivity;
import com.miaxis.judicialcorrection.benefit.R;
import com.miaxis.judicialcorrection.benefit.WelfareViewModel;
import com.miaxis.judicialcorrection.benefit.databinding.FragmentToSignUpBinding;
import com.miaxis.judicialcorrection.common.response.ZZResponse;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.VerifyPageFragment;
import com.miaxis.judicialcorrection.id.bean.IdCard;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * 去报名列表
 */
@AndroidEntryPoint
public class ToSignUpFragment extends BaseBindingFragment<FragmentToSignUpBinding> {

    private WelfareViewModel viewModel;
    private int page = 1;
    private SignUpAdapter mAdapter;
    private boolean isRefresh = true;

    @Inject
    AppHints appHints;

    @Override
    protected int initLayout() {
        return R.layout.fragment_to_sign_up;
    }

    @Override
    protected void initView(@NonNull @NotNull FragmentToSignUpBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding.btnBackToHome.setOnClickListener(v -> {
            finish();
        });
        binding.tvHistoryRecord.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((PublicWelfareActivity) getActivity()).replaceFragment(new AlreadySignUpFragment());
            }
        });
        mAdapter = new SignUpAdapter();
        mAdapter.setChildItemClickListener(new ChildItemClickListener() {
            @Override
            public void onItemClick(SignUpContentBean listBean) {
                IdCard idCardBean = viewModel.idCard;
                viewModel.mItemId=listBean.getId();
                viewModel.getParticipate(listBean.getId()).observe(ToSignUpFragment.this, observer -> {
                    if (observer.isSuccess()) {
                        new DialogResult(getActivity(), new DialogResult.ClickListener() {
                            @Override
                            public void onBackHome(AppCompatDialog appCompatDialog) {
                                appCompatDialog.dismiss();
                                finish();
                            }

                            @Override
                            public void onTryAgain(AppCompatDialog appCompatDialog) {
                                appCompatDialog.dismiss();
                            }

                            @Override
                            public void onTimeOut(AppCompatDialog appCompatDialog) {
                                finish();
                            }
                        }, new DialogResult.Builder(
                               true,
                                true ? "报名" + "成功" : "验证失败",
                                true ? "系统将自动返回" + "公益活动" + "身份证刷取页面" : "请点击“重新验证”重新尝试验证，\n如还是失败，请联系现场工作人员。",
                                10, false
                        ).hideAllHideSucceedInfo(true)).show();
                    }
                    if (observer.isError()){
                        appHints.showError("失败");
                    }
                });
//                if (idCardBean != null && getActivity() != null) {
//                    PersonInfo info = new PersonInfo();
//                    info.setXm(idCardBean.idCardMsg.name);
//                    info.setIdCardNumber(idCardBean.idCardMsg.id_num);
//                    ((PublicWelfareActivity) getActivity()).replaceFragment(
//                            new VerifyPageFragment("身份核验", info));
//                }
            }
        });
    }

    @Override
    protected void initData(@NonNull @NotNull FragmentToSignUpBinding binding, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(WelfareViewModel.class);
        initLoadMore();
        binding.rvActivityList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvActivityList.setAdapter(mAdapter);
        if (viewModel.idCard != null) {
            binding.tvName.setText("姓名：" + viewModel.idCard.idCardMsg.name);
            binding.tvIdCard.setText("身份证号：" + viewModel.idCard.idCardMsg.id_num);
        }
        setData();
    }

    /**
     * 初始化加载更多
     */
    private void initLoadMore() {
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            isRefresh = false;
            page++;
            setData();
        });
        mAdapter.getLoadMoreModule().setAutoLoadMore(true);
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void setData() {
        viewModel.getWelfareInfo(page).observe(this, listResource -> {
            if (listResource.isSuccess()) {
                if (isRefresh) {
                    if (listResource.data != null && listResource.data.getList() != null) {
                        mAdapter.setNewInstance(listResource.data.getList());
                        if(listResource.data.getList().size()<10){
                            mAdapter.getLoadMoreModule().loadMoreEnd();
                        }
                    } else {
                        mAdapter.setNewInstance(new ArrayList<>());
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