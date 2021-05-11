package com.miaxis.judicialcorrection.leave.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.Leave;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.base.utils.TimeUtils;
import com.miaxis.judicialcorrection.face.bean.VerifyInfo;
import com.miaxis.judicialcorrection.leave.LeaveListener;
import com.miaxis.judicialcorrection.leave.LeaveRepo;
import com.miaxis.judicialcorrection.leave.R;
import com.miaxis.judicialcorrection.leave.databinding.FragmentLeaveListBinding;
import com.miaxis.judicialcorrection.leave.databinding.LayoutItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Lazy;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * @author Tank
 * @date 2021/5/7 10:08
 * @des
 * @updateAuthor
 * @updateDes
 */

@AndroidEntryPoint
public class LeaveListFragment extends BaseBindingFragment<FragmentLeaveListBinding> {

    private String title = "请销假";

    @Inject
    LeaveRepo mLeaveRepo;

    VerifyInfo verifyInfo;

    @Inject
    Lazy<AppHints> appHintsLazy;

    AtomicInteger page = new AtomicInteger(1);

    public LeaveListFragment(@NotNull VerifyInfo verifyInfo) {
        this.verifyInfo = verifyInfo;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_leave_list;
    }

    @Override
    protected void initView(@NonNull FragmentLeaveListBinding binding, @Nullable Bundle savedInstanceState) {
        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> finish());

        binding.tvApply.setOnClickListener(v -> {
            FragmentActivity activity = getActivity();
            if (activity instanceof LeaveListener) {
                LeaveListener listener = (LeaveListener) activity;
                listener.onApply(verifyInfo);
            }
        });

        binding.rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Adapter adapter = new Adapter(this);
        FragmentActivity activity = getActivity();
        if (activity instanceof LeaveListener) {
            adapter.bind((LeaveListener) activity);
        }
        adapter.bind(verifyInfo);
        binding.rvList.setAdapter(adapter);
        mLeaveRepo.getLeaveList(this.verifyInfo.pid, page.get(), 1000).observe(this, new Observer<Resource<Leave>>() {
            @Override
            public void onChanged(Resource<Leave> leaveResource) {
                switch (leaveResource.status) {
                    case LOADING:
                        showLoading(title, "正在获取" + title + "信息，请稍后");
                        break;
                    case ERROR:
                        dismissLoading();
                        appHintsLazy.get().showError("Error:" + leaveResource.errorMessage);
                        break;
                    case SUCCESS:
                        dismissLoading();
                        adapter.setData(leaveResource.data);
                        break;
                }
            }
        });
    }

    @Override
    protected void initData(@NonNull FragmentLeaveListBinding binding, @Nullable Bundle savedInstanceState) {

    }

    public static class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<Leave.ListBean> list = new ArrayList<>();
        private final LifecycleOwner lifecycleOwner;
        private LeaveListener mClickListener;

        public Adapter(@Nullable LifecycleOwner lifecycleOwner) {
            this.lifecycleOwner = lifecycleOwner;
        }

        public void bind(LeaveListener clickListener) {
            this.mClickListener = clickListener;
        }

        VerifyInfo verifyInfo;

        public void bind(VerifyInfo verifyInfo) {
            this.verifyInfo = verifyInfo;
        }

        public void setData(Leave data) {
            this.list.clear();
            if (data != null && data.list != null && !data.list.isEmpty()) {
                this.list.addAll(data.list);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            if (viewType == 0) {
                return new Title(layoutInflater.inflate(R.layout.layout_title, parent, false));
            } else {
                LayoutItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_item, parent, false);
                binding.setLifecycleOwner(this.lifecycleOwner);
                Item item = new Item(binding.getRoot(), binding, this.mClickListener);
                item.bind(verifyInfo);
                return item;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (position != 0 && holder instanceof Item) {
                Item item = (Item) holder;
                Leave.ListBean listBean = list.get(position - 1);
                listBean.index = position;
                Timber.i(position + ":" + new Gson().toJson(listBean));
                item.bind(listBean);
            }
        }

        @Override
        public int getItemCount() {
            return list.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        public static class Title extends RecyclerView.ViewHolder {
            public Title(@NonNull View itemView) {
                super(itemView);
            }
        }

        public static class Item extends RecyclerView.ViewHolder {

            private final LayoutItemBinding bind;
            private LeaveListener mClickListener;
            private Leave.ListBean mListBean;

            public Item(@NonNull View itemView, LayoutItemBinding bind, LeaveListener clickListener) {
                super(itemView);
                this.bind = bind;
                this.mClickListener = clickListener;
                if (this.bind != null) {
                    this.bind.tvProgress.setOnClickListener(v -> {
                        if (mClickListener != null) {
                            mClickListener.onQueryProgress(verifyInfo, mListBean);
                        }
                    });
                    this.bind.tvCancel.setOnClickListener(v -> {
                        if (mClickListener != null) {
                            mClickListener.onCancel(verifyInfo, mListBean);
                        }
                    });
                }
            }

            VerifyInfo verifyInfo;

            public void bind(VerifyInfo verifyInfo) {
                this.verifyInfo = verifyInfo;
            }

            public void bind(Leave.ListBean item) {
                this.mListBean = item;
                if (this.bind != null && item != null) {
                    Timber.e("显示");
                    this.bind.tvNumber.setText(String.valueOf(item.index));
                    this.bind.tvName.setText(item.pname);
                    if (item.list != null && !item.list.isEmpty()) {
                        this.bind.tvLocation.setText(
                                item.list.get(0).wcmddszsName +
                                        item.list.get(0).wcmddszdName +
                                        item.list.get(0).wcmddszxName +
                                        item.list.get(0).wcmddxzName +
                                        item.list.get(0).wcmddmx
                        );
                    }
                    this.bind.tvStartTime.setText(TimeUtils.simpleDateFormat.format(item.ksqr));
                    this.bind.tvEndTime.setText(TimeUtils.simpleDateFormat.format(item.jsrq));
                    this.bind.tvStatus.setText(item.sfyxjName);
                    this.bind.tvCancel.setVisibility("1".equals(item.sfyxj)?View.GONE:View.VISIBLE);
                    this.bind.tvProgress.setText("进度查询");
                } else {
                    Timber.e("不显示");
                }
            }
        }
    }


}
