package com.miaxis.enroll.dialog;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.miaxis.enroll.OnClickItemListener;
import com.miaxis.enroll.R;
import com.miaxis.enroll.adapter.NodeTreeAdapter;
import com.miaxis.enroll.adapter.provide.FirstProvider;
import com.miaxis.enroll.adapter.provide.SecondProvider;
import com.miaxis.enroll.databinding.DialogFlotCheckedBinding;
import com.miaxis.enroll.vo.FirstNode;
import com.miaxis.enroll.vo.SecondNode;
import com.miaxis.judicialcorrection.dialog.base.BaseDialog;
import com.miaxis.judicialcorrection.dialog.base.BaseDialogListener;

import java.util.ArrayList;
import java.util.List;

public class FloatCheckedDialog extends BaseDialog<DialogFlotCheckedBinding, FloatCheckedDialog.ClickListener> implements OnClickItemListener {

    public FloatCheckedDialog(Context context, ClickListener listener,int type) {
        super(context, listener);
        this.type=type;
    }

    public int mFirstPosition = -1;
    public int mFirstChildPosition = -1;
    private String content;
    private  int type=0;

    @Override
    public int initLayout() {
        return R.layout.dialog_flot_checked;
    }

    @Override
    public void initView() {
        binding.rvInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        NodeTreeAdapter mAdapter = new NodeTreeAdapter(new FirstProvider(this), new SecondProvider(this));
        binding.rvInfo.setAdapter(mAdapter);
        if (type==0){
            mAdapter.setList(getOfficeEntity());
        }else{
            mAdapter.setList(getRelationshipEntity());
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClickItemListener(int position, boolean isNext, String title,String coding) {
        mFirstPosition = position;
        content = title;
        listener.onCheckedItem(content,coding);
        if (!isNext) {
            dismiss();
        }
    }

    @Override
    public void onClickItemChildListener(int position, String title,String coding) {
        mFirstChildPosition = position;
        content = content +"/"+ title;
        listener.onCheckedItem(content,coding);
        dismiss();
    }

    public interface ClickListener extends BaseDialogListener {

        void onCheckedItem(String title,String coding);

    }


    //得到职位数据
    private List<BaseNode> getOfficeEntity() {
        List<BaseNode> list = new ArrayList<>();
        String[] job = getContext().getResources().getStringArray(R.array.job);
        String[] jobNationalStaff = getContext().getResources().getStringArray(R.array.job_nationalStaff);
        String[] jobPost = getContext().getResources().getStringArray(R.array.job_post);
        String[] jobFarmer = getContext().getResources().getStringArray(R.array.job_farmer);

        String[] job_coding = getContext().getResources().getStringArray(R.array.job_coding);
        String[] jobNationalStaff_coding = getContext().getResources().getStringArray(R.array.job_nationalStaff_coding);
        String[] jobPost_coding = getContext().getResources().getStringArray(R.array.job_post_coding);
        String[] jobFarmer_coding = getContext().getResources().getStringArray(R.array.job_farmer_coding);

        for (int i = 0; i < job.length; i++) {
            List<BaseNode> secondNodeList = new ArrayList<>();
            if (i == 0) {
                for (int n=0;n< jobNationalStaff.length;n++) {
                    SecondNode seNode = new SecondNode(null,jobNationalStaff[n] ,jobNationalStaff_coding[n]);
                    secondNodeList.add(seNode);
                }
            } else if (i == 2) {
                for (int n=0;n< jobPost.length;n++) {
                    SecondNode seNode = new SecondNode(null,jobPost[n] ,jobPost_coding[n]);
                    secondNodeList.add(seNode);
                }
            } else if (i == 3) {
                for (int n=0;n< jobFarmer.length;n++) {
                    SecondNode seNode = new SecondNode(null,jobFarmer[n] ,jobFarmer_coding[n]);
                    secondNodeList.add(seNode);
                }
            }
            FirstNode entity = new FirstNode(secondNodeList, job[i],job_coding[i]);
            // 默认所有的都展开
            entity.setExpanded(false);
            list.add(entity);
        }
        return list;
    }


    //得到职位数据
    private List<BaseNode> getRelationshipEntity() {
        List<BaseNode> list = new ArrayList<>();
        String[] relationship = getContext().getResources().getStringArray(R.array.relationship);
        String[] relationship_relatives = getContext().getResources().getStringArray(R.array.relationship_relatives);


        String[] relationship_coding = getContext().getResources().getStringArray(R.array.relationship_coding);
        String[] relationship_relatives_coding = getContext().getResources().getStringArray(R.array.relationship_relatives_coding);

        for (int i = 0; i < relationship.length; i++) {
            List<BaseNode> secondNodeList = new ArrayList<>();
            if (i == 0) {
                for (int n=0;n< relationship_relatives.length;n++) {
                    SecondNode seNode = new SecondNode(null,relationship_relatives[n] ,relationship_relatives_coding[n]);
                    secondNodeList.add(seNode);
                }
            }
            FirstNode entity = new FirstNode(secondNodeList, relationship[i],relationship_coding[i]);
            // 默认所有的都展开
            entity.setExpanded(false);
            list.add(entity);
        }
        return list;
    }


}
