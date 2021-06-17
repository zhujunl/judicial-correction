package com.example.m_common.adapter;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.example.m_common.R;
import com.example.m_common.bean.PreviewPictureEntity;
import com.example.m_common.databinding.ItemPreviewBinding;


import org.jetbrains.annotations.NotNull;

import java.io.File;


public class PreviewAdapter extends BaseQuickAdapter<PreviewPictureEntity, BaseDataBindingHolder<ItemPreviewBinding>> {

    public PreviewAdapter() {
        super(R.layout.item_preview);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemPreviewBinding> bindingHolder, PreviewPictureEntity item) {
        ItemPreviewBinding binding = bindingHolder.getDataBinding();
        if (binding!=null) {
            Glide.with(getContext()).load(new File(item.getPath())).into(binding.img);
        }
    }
}
