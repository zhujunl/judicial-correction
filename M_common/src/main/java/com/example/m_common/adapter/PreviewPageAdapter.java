package com.example.m_common.adapter;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.example.m_common.R;
import com.example.m_common.bean.PreviewPictureEntity;
import com.example.m_common.databinding.ItemPreviewPageBinding;


import org.jetbrains.annotations.NotNull;

import java.io.File;


public class PreviewPageAdapter extends BaseQuickAdapter<PreviewPictureEntity, BaseDataBindingHolder<ItemPreviewPageBinding>> {

    public PreviewPageAdapter() {
        super(R.layout.item_preview_page);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemPreviewPageBinding> bindingHolder, PreviewPictureEntity item) {
        ItemPreviewPageBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            Glide.with(getContext()).load(new File(item.getPath())).into(binding.img);
        }
    }
}
