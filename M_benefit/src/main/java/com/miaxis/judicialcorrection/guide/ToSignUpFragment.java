package com.miaxis.judicialcorrection.guide;

import android.os.Bundle;

import com.miaxis.judicialcorrection.adapter.SignUpAdapter;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.HistorySignUpBean;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.api.vo.SignUpBean;
import com.miaxis.judicialcorrection.base.api.vo.SignUpContentBean;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.AppToast;
import com.miaxis.judicialcorrection.base.utils.numbers.HexStringUtils;
import com.miaxis.judicialcorrection.benefit.PublicWelfareActivity;
import com.miaxis.judicialcorrection.benefit.R;
import com.miaxis.judicialcorrection.benefit.WelfareViewModel;
import com.miaxis.judicialcorrection.benefit.databinding.FragmentToSignUpBinding;
import com.miaxis.judicialcorrection.dialog.DialogResult;
import com.miaxis.judicialcorrection.face.VerifyPageFragment;
import com.miaxis.judicialcorrection.id.bean.IdCard;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

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
    private String mPid;

    @Inject
    AppHints appHints;

    @Inject
    AppToast appToast;


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
        mAdapter.setChildItemClickListener((position, listBean) -> {
            if (listBean.isSignUpSucceed()) {
                appToast.show("您已报名");
                return;
            }
            IdCard idCardBean = viewModel.idCard;
            viewModel.mItem.set(listBean.getId());
            if (idCardBean != null && getActivity() != null) {
                PersonInfo info = new PersonInfo();
                info.setId(viewModel.mStrPid.get());
                info.setXm(idCardBean.idCardMsg.name);
                info.setIdCardNumber(idCardBean.idCardMsg.id_num);
                ((PublicWelfareActivity) getActivity()).replaceFragment(
                        new VerifyPageFragment("身份核验", info));
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
        mPid = viewModel.mStrPid.get();
        setData();
        if (getActivity() != null && getActivity() instanceof PublicWelfareActivity) {
            boolean b = ((PublicWelfareActivity) getActivity()).mVerificationSignUp;
            if (b) {
                ((PublicWelfareActivity) getActivity()).mVerificationSignUp = false;
                String s = viewModel.mItem.get();
                viewModel.getParticipate(s).observe(ToSignUpFragment.this, objectResource -> {
                    if (objectResource.isSuccess()) {
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
                                appCompatDialog.dismiss();
                            }
                        }, new DialogResult.Builder(
                                true,
                                true ? "报名" + "成功" : "验证失败",
                                true ? "系统将自动返回" + "公益活动" + "身份证刷取页面" : "请点击“重新验证”重新尝试验证，\n如还是失败，请联系现场工作人员。",
                                3, false
                        ).hideAllHideSucceedInfo(true)).show();
                    }
                    if (objectResource.isError()) {
                        appHints.showError("失败");
                    }
                });
            }
        }
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
                if (listResource.data != null && !listResource.data.getList().isEmpty()) {
                    List<SignUpContentBean> list = listResource.data.getList();
                    //过滤 当前时间之后的
                    List<SignUpContentBean> ls = new ArrayList<>();
                    for (SignUpContentBean bean : list) {
                        long l = HexStringUtils.convertGMTToLocalLong(bean.getSqfwjssj());
                        String s=String.valueOf(l);
                        if (s.length()<13){
                            l=l*1000;
                        }
                        if (l > System.currentTimeMillis()) {
                            ls.add(bean);
                        }
                    }
                    listResource.data.setList(ls);
                    getHistoryData(listResource);
                } else {
                    setListDataAdapter(listResource);
                }
            }
        });
    }

    private void setListDataAdapter(Resource<SignUpBean> listResource) {
        if (isRefresh) {
            if (listResource.data != null && listResource.data.getList() != null) {
                mAdapter.setNewInstance(listResource.data.getList());
                if (listResource.data.getList().size() < 10) {
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

    private Resource<HistorySignUpBean> historySignUpBeanResource;

    //得到历史数据
    private void getHistoryData(Resource<SignUpBean> list) {
        if (historySignUpBeanResource != null) {
            setHistoryData(list, historySignUpBeanResource);
        } else {
            viewModel.getHistoryWelfareInfo(page, 100, mPid).observe(this, listResource -> {
                if (listResource.isSuccess()) {
                    historySignUpBeanResource = listResource;
                    setHistoryData(list, listResource);
                }
                if (listResource.isError()) {
                    setListDataAdapter(list);
                }
            });
        }
    }

    //设置历史数据 与原始数据判断 是否有设置操作
    private void setHistoryData(Resource<SignUpBean> list, Resource<HistorySignUpBean> listResource) {
        if (listResource.data != null && listResource.data.getList() != null && listResource.data.getList().size() != 0) {
            for (SignUpContentBean bean : list.data.getList()) {
                for (HistorySignUpBean.ListBean b : listResource.data.getList()) {
                    if (b.getPublicActivityVo() != null && bean.getId().equals(b.getPublicActivityVo().getId())) {
                        bean.setSignUpSucceed(true);
                        break;
                    }
                }
            }
        }
        setListDataAdapter(list);
    }
}