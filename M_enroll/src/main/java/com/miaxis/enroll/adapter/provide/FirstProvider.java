package com.miaxis.enroll.adapter.provide;

import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.miaxis.enroll.OnClickItemListener;
import com.miaxis.enroll.R;
import com.miaxis.enroll.adapter.NodeTreeAdapter;
import com.miaxis.enroll.vo.FirstNode;

import org.jetbrains.annotations.NotNull;

import retrofit2.http.POST;


public class FirstProvider extends BaseNodeProvider {

    public FirstProvider(com.miaxis.enroll.OnClickItemListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_first_list;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        FirstNode entity = (FirstNode) data;
        helper.setText(R.id.tv_content, entity.getTitle());

    }
    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        getAdapter().expandOrCollapse(position, true, true, NodeTreeAdapter.EXPAND_COLLAPSE_PAYLOAD);
        FirstNode entity = (FirstNode) data;
        if (listener != null) {
            boolean b = entity.getChildNode() != null && !entity.getChildNode().isEmpty();
            listener.onClickItemListener(position, b,entity.getTitle(),entity.getCoding());
        }
    }

    private final OnClickItemListener listener;

}