package com.miaxis.judicialcorrection.common.ui.adapter;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

/**
 * @email zyawei@live.com
 */
public abstract class BaseDataBoundDiffAdapter<T, VB extends ViewDataBinding> extends ListAdapter<T, DataBoundViewHolder<VB>> {

    protected BaseDataBoundDiffAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    protected BaseDataBoundDiffAdapter(@NonNull AsyncDifferConfig<T> config) {
        super(config);
    }

    @NonNull
    @Override
    public DataBoundViewHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataBoundViewHolder<>(createBinding(parent, viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<VB> holder, int position) {
        bind(holder.binding, getItem(position));
    }


    /**
     * 创建一个 V
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new DataBoundViewHolder<> that holds a View of the given view type.
     */
    protected abstract VB createBinding(ViewGroup parent, int viewType);

    /**
     * 数据绑定
     *
     * @param binding 视图
     * @param item    数据
     */
    protected abstract void bind(VB binding, T item);

}
