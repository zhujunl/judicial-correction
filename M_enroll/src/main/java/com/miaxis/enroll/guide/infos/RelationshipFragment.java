package com.miaxis.enroll.guide.infos;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.miaxis.enroll.EnrollSharedViewModel;
import com.miaxis.enroll.R;
import com.miaxis.enroll.databinding.FragmentRelationshipBinding;
import com.miaxis.enroll.databinding.ItemFragmentRelationshipBinding;
import com.miaxis.enroll.dialog.FloatCheckedDialog;
import com.miaxis.enroll.vo.Family;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.common.ui.adapter.BaseDataBoundAdapter;

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
    MyAdapter adapter;

    @Override
    protected void initData(@NonNull FragmentRelationshipBinding binding, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(getActivity()).get(EnrollSharedViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(vm);

        adapter = new MyAdapter();
        adapter.setItemPresenter(new RecyclerRelationshipBindPresenter());
        if (vm.relationships.size() < 2) {
            Family family = new Family();
            Family family2 = new Family();
            vm.relationships.add(family);
            vm.relationships.add(family2);
        }
        adapter.submitList(vm.relationships);
        binding.recyclerview.setAdapter(adapter);
        binding.addLine.setOnClickListener(v -> {
            Family family = new Family();
            vm.relationships.add(family);
            adapter.submitLists(vm.relationships);
            adapter.notifyItemRangeInserted(vm.relationships.size() - 1, 1);
            if (vm.relationships.size() > 2) {
                binding.deleteLine.setVisibility(View.VISIBLE);
            }
            setRvHeight();
        });
        binding.deleteLine.setOnClickListener(v -> {
            int size = vm.relationships.size() - 1;
            vm.relationships.remove(size);
            adapter.notifyItemRemoved(size);
            if (vm.relationships.size()<=2){
                binding.deleteLine.setVisibility(View.GONE);
            }
        });
        setRvHeight();
    }

    private void setRvHeight() {
        Configuration mConfiguration = this.getResources().getConfiguration();
        int ori = mConfiguration.orientation;
        if (Configuration.ORIENTATION_LANDSCAPE==ori) {//横屏
            if (adapter.getItemCount() >= 4) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.recyclerview.getLayoutParams();
                params.height = 400;
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.recyclerview.getLayoutParams();
                params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            }
        }else {
            if (adapter.getItemCount() >= 9) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.recyclerview.getLayoutParams();
                params.height = 700;
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.recyclerview.getLayoutParams();
                params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            }
        }
    }


    @Inject
    AppHints appHints;

    @Override
    public boolean checkData() {
        Timber.v("relationships %s", vm.relationships);
        if (TextUtils.isEmpty(vm.relationships.get(0).name)) {
            appHints.toast("您没有填写社会关系");
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
            binding.setItemPresenter(ItemPresenter);
        }

        private RecyclerRelationshipBindPresenter ItemPresenter;

        /**
         * 用于设置Item的事件Presenter
         */
        public void setItemPresenter(RecyclerRelationshipBindPresenter itemPresenter) {
            ItemPresenter = itemPresenter;
        }
    }
    public class RecyclerRelationshipBindPresenter implements IBaseBindingPresenter {


        public void onClickRelationship(Family item, TextView textView) {
            new FloatCheckedDialog(getActivity(), new FloatCheckedDialog.ClickListener() {
                @Override
                public void onCheckedItem(String title,String coding) {
                    item.relationshipName=title;
                    item.relationship=coding;
                    //一般不更新
                    textView.setText(title);
                }
                @Override
                public void onTryAgain(AppCompatDialog appCompatDialog) {

                }

                @Override
                public void onTimeOut(AppCompatDialog appCompatDialog) {

                }
            },1).show();
        }

    }
}
