package com.miaxis.enroll.vo;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FirstNode extends BaseExpandNode {

    private List<BaseNode> childNode;
    private String title;
    private String coding;

    public FirstNode(List<BaseNode> childNode, String title,String coding) {
        this.childNode = childNode;
        this.title = title;
        this.coding=coding;

        setExpanded(false);
    }

    public String getTitle() {
        return title;
    }

    public String getCoding() {
        return coding;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return childNode;
    }
}
