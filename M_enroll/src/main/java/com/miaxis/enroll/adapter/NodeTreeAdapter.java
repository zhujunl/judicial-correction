package com.miaxis.enroll.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.miaxis.enroll.OnClickItemListener;
import com.miaxis.enroll.adapter.provide.FirstProvider;
import com.miaxis.enroll.adapter.provide.SecondProvider;
import com.miaxis.enroll.vo.FirstNode;
import com.miaxis.enroll.vo.SecondNode;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NodeTreeAdapter extends BaseNodeAdapter  {




    public NodeTreeAdapter(BaseNodeProvider first,BaseNodeProvider second) {
        super();
        addNodeProvider(first);
        addNodeProvider(second);

    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof FirstNode) {
            return 1;
        } else if (node instanceof SecondNode) {
            return 2;
        }
        return -1;
    }

    public static final int EXPAND_COLLAPSE_PAYLOAD = 110;
}
