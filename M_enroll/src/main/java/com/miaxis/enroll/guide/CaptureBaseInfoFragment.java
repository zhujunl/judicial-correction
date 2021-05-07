package com.miaxis.enroll.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.EnrollActivity;
import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentCaptureBaseInfoBinding;
import com.miaxis.enroll.databinding.ItemFragmentBaseInfoBinding;
import com.miaxis.enroll.guide.infos.AddressFragment;
import com.miaxis.enroll.guide.infos.BaseInfoFragment;
import com.miaxis.enroll.guide.infos.BaseMsgFragment;
import com.miaxis.enroll.guide.infos.OtherIdTypeFragment;
import com.miaxis.enroll.guide.infos.OtherInfoFragment;
import com.miaxis.enroll.guide.infos.RelationshipFragment;
import com.miaxis.enroll.guide.infos.ResumeFragment;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * InfoFuncFragment
 *
 * @author zhangyw
 * Created on 4/29/21.
 */
@AndroidEntryPoint
public class CaptureBaseInfoFragment extends BaseBindingFragment<FragmentCaptureBaseInfoBinding> {

    private final List<NvListItem> listItems = new ArrayList<>();
    private final int pageCount = 6;

    {
        listItems.add(new NvListItem("基础信息\n登记", BaseMsgFragment.class, 0, pageCount));
        listItems.add(new NvListItem("身份证件\n信息", OtherIdTypeFragment.class, 1, pageCount));
        listItems.add(new NvListItem("居住地\n信息", AddressFragment.class, 2, pageCount));
        listItems.add(new NvListItem("个人简历\n信息", ResumeFragment.class, 3, pageCount));
        listItems.add(new NvListItem("家庭成员及\n社会关系", RelationshipFragment.class, 4, pageCount));
        listItems.add(new NvListItem("其他基本\n信息", OtherInfoFragment.class, 5, pageCount));
    }

    private int currentPageIndex = -1;
    private MyProgressAdapter adapter;
    private NvController controller;
    private EnrollSharedViewModel viewModel;


    @Inject
    AppHints appHints;

    @Override
    protected int initLayout() {
        return R.layout.fragment_capture_base_info;
    }

    @Override
    protected void initView(@NonNull FragmentCaptureBaseInfoBinding binding, @Nullable Bundle savedInstanceState) {
        binding.btnBackToHome.setOnClickListener(v -> finish());
    }


    @Override
    protected void initData(@NonNull FragmentCaptureBaseInfoBinding binding, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);

        controller = new NvController(getChildFragmentManager(), R.id.infoContainer);
        adapter = new MyProgressAdapter();
        adapter.submitList(listItems);
        binding.recyclerview.setAdapter(adapter);
        binding.nextBtn.setOnClickListener(v -> nextPage());
        binding.preBtn.setOnClickListener(v -> prePage());
        nextPage();
    }

    void nextPage() {
        Fragment top = controller.top();
        if (top instanceof BaseInfoFragment<?>) {
            if (!((BaseInfoFragment<?>) top).checkData()) {
                return;
            }
        }
        if (currentPageIndex == pageCount - 1) {
            submitAll();
            return;
        }

        currentPageIndex++;

        // 控制导航
        for (int i = 0; i < listItems.size(); i++) {
            listItems.get(i).current = currentPageIndex;
        }
        adapter.submitList(listItems);
        // 控制按钮
        refreshControlBtn();
        // 控制页面
        try {
            Fragment fragment = listItems.get(currentPageIndex).targetCls.newInstance();
            controller.nvTo(fragment, true);
        } catch (Exception e) {
            appHints.toast("未找到页面!");
        }
    }

    void prePage() {
        currentPageIndex--;
        // 控制导航
        for (int i = 0; i < listItems.size(); i++) {
            listItems.get(i).current = currentPageIndex;
        }
        adapter.submitList(listItems);
        // 控制按钮
        refreshControlBtn();
        // 控制页面
        controller.back();
    }

    void refreshControlBtn() {
        if (currentPageIndex == 0 || currentPageIndex == pageCount - 1) {
            binding.preBtn.setVisibility(View.GONE);
        } else {
            binding.preBtn.setVisibility(View.VISIBLE);
        }
        if (currentPageIndex == pageCount - 1) {
            binding.nextBtn.setText("提交");
        }
    }


    private void submitAll() {
        LiveData<Resource<PersonInfo>> resourceLiveData = viewModel.addPerson();
        resourceLiveData.observe(this, personInfoResource -> {
            Timber.i("Login %s", personInfoResource);
            switch (personInfoResource.status) {
                case LOADING:
                    showLoading();
                    break;
                case ERROR:
                    dismissLoading();
                    appHints.showError("Error:" + personInfoResource.errorMessage);
                    break;
                case SUCCESS:
                    dismissLoading();
                    appHints.toast("提交成功");
                    FragmentActivity activity = getActivity();
                    if (activity instanceof EnrollActivity) {
                        ((EnrollActivity) activity).getNvController().back();
                    }
                    break;
            }
        });
    }

    private void submitJobs(PersonInfo personInfo) {

    }

    private void submitRelationship(PersonInfo personInfo) {

    }
    static class MyProgressAdapter extends BaseDataBoundAdapter<NvListItem, ItemFragmentBaseInfoBinding> {

        @Override
        protected ItemFragmentBaseInfoBinding createBinding(ViewGroup parent, int viewType) {
            return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_fragment_base_info, parent, false);
        }

        @Override
        protected void bind(ItemFragmentBaseInfoBinding binding, NvListItem item) {
            binding.setData(item);
        }

    }

}
