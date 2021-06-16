package com.miaxis.judicialcorrection.adapter;


import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.miaxis.judicialcorrection.live.R;
import com.miaxis.judicialcorrection.live.databinding.ItemPreviewBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;


public class PreviewAdapter extends BaseQuickAdapter< String, BaseDataBindingHolder<ItemPreviewBinding>> {

    public PreviewAdapter() {
        super(R.layout.item_preview);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemPreviewBinding> bindingHolder, String item) {
        ItemPreviewBinding binding = bindingHolder.getDataBinding();
         if (getContext() instanceof FragmentActivity){
             boolean finishing = ((FragmentActivity) getContext()).isFinishing();
             if (!finishing){
                 Glide.with(getContext()).load(new File(item)).into(binding.img);
             }
         }
    }
}
