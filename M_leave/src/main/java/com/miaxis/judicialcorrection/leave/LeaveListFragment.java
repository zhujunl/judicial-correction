package com.miaxis.judicialcorrection.leave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.leave.bean.ItemData;
import com.miaxis.judicialcorrection.leave.databinding.FragmentLeaveListBinding;
import com.miaxis.judicialcorrection.leave.databinding.LayoutItemBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Tank
 * @date 2021/5/7 10:08
 * @des
 * @updateAuthor
 * @updateDes
 */
public class LeaveListFragment extends BaseBindingFragment<FragmentLeaveListBinding> {

    private String title = "请销假";

    public LeaveListFragment() {
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_leave_list;
    }

    @Override
    protected void initView(@NonNull FragmentLeaveListBinding binding, @Nullable Bundle savedInstanceState) {
        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Adapter adapter = new Adapter(this);
        binding.rvList.setAdapter(adapter);
    }

    @Override
    protected void initData(@NonNull FragmentLeaveListBinding binding, @Nullable Bundle savedInstanceState) {

    }

    public static class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<ItemData> list = new ArrayList<>();
        private final LifecycleOwner lifecycleOwner;

        public Adapter(@Nullable LifecycleOwner lifecycleOwner) {
            this.lifecycleOwner = lifecycleOwner;
        }

        public void setList(List<ItemData> list) {
            this.list.clear();
            if (list != null) {
                this.list.addAll(list);
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
                return new Item(binding.getRoot(), binding);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (position != 0 && holder instanceof Item) {
                Item item = (Item) holder;
                item.bind(list.get(position - 1));
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

            public Item(@NonNull View itemView, LayoutItemBinding bind) {
                super(itemView);
                this.bind = bind;
            }

            public void bind(ItemData item) {
                if (this.bind != null) {
                    this.bind.setItem(item);
                }
            }
        }

    }



}
