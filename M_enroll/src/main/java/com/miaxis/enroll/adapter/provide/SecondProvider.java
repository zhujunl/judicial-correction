package com.miaxis.enroll.adapter.provide;

import android.view.View;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.miaxis.enroll.OnClickItemListener;
import com.miaxis.enroll.R;
import com.miaxis.enroll.vo.SecondNode;

import org.jetbrains.annotations.NotNull;

public class SecondProvider extends BaseNodeProvider {

    public SecondProvider(OnClickItemListener listener) {
        this.listener=listener;
    }

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_second_list;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        SecondNode entity = (SecondNode) data;
        helper.setText(R.id.tv_content, entity.getTitle());

    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        if (listener!=null){
            SecondNode entity = (SecondNode) data;
            listener.onClickItemChildListener(position,entity.getTitle(),entity.getCoding());
        }
    }


    private OnClickItemListener listener;


}

