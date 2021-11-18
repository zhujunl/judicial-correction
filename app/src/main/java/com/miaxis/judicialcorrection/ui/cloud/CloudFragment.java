package com.miaxis.judicialcorrection.ui.cloud;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miaxis.enroll.guide.NvController;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.BaseBindingFragment;
import com.miaxis.judicialcorrection.base.api.vo.PersonInfo;
import com.miaxis.judicialcorrection.base.utils.AppHints;
import com.miaxis.judicialcorrection.databinding.FragmentCloudBaseInfoBinding;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CloudFragment extends BaseBindingFragment<FragmentCloudBaseInfoBinding> {

    private PersonInfo personInfo;
    String title = "云端查询";

    private final List<cloudItem> list =new ArrayList<>();
    private NvController controller;
    private CloudModel model;

    public CloudFragment(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    @Inject
    AppHints appHints;

    {
        list.add(new cloudItem("日常报告", DailyFragment.class,true));
        list.add(new cloudItem("集中教育", CentralizedFragment.class,false));
        list.add(new cloudItem("个别教育", IndividualFragment.class,false));
        list.add(new cloudItem("公益活动", WelfareFragment.class,false));
        list.add(new cloudItem("训诫", AdmonitionFragment.class,false));
        list.add(new cloudItem("警告", WarningFragment.class,false));
    }

//    Daily report, centralized education, individual education, public welfare activities, admonition and warning


    @Override
    protected int initLayout() {
        return R.layout.fragment_cloud_base_info;
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void initView(@NonNull FragmentCloudBaseInfoBinding binding, @Nullable Bundle savedInstanceState) {
        model=new ViewModelProvider(this).get(CloudModel.class);
        StringBuilder name=new StringBuilder();
        StringBuilder place=new StringBuilder();
        name.append("姓名：").append(personInfo.getXm());
        place.append("矫正单位：").append(MMKV.defaultMMKV().decodeString("JAuthInfolevel1")).append(MMKV.defaultMMKV().decodeString("JAuthInfolevel2"));
        binding.tvTitle.setText(String.valueOf(this.title));
        binding.btnBackToHome.setOnClickListener(v -> finish());
        binding.tvName.setText(name.toString());
        binding.tvAddress.setText(place.toString());
        MyRecycleAdpter myadpter=new MyRecycleAdpter(getActivity(),list);
        int width=getActivity().getDisplay().getWidth();
        int height=getActivity().getDisplay().getHeight();
        if(width>height) binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerview.setAdapter(myadpter);

    }

    @Override
    protected void initData(@NonNull FragmentCloudBaseInfoBinding binding, @Nullable Bundle savedInstanceState) {
        controller=new NvController(getChildFragmentManager(), R.id.infoContainer);
        try {
            controller.nvTo(list.get(0).getFargment().newInstance(), true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }

    private class MyRecycleAdpter extends RecyclerView.Adapter<MyRecycleAdpter.MyHolder>{

        private Context context;
        private List<cloudItem> list;

        public MyRecycleAdpter(Context context, List<cloudItem> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(context).inflate(R.layout.item_cloud_type,parent,false);
            MyHolder myHolder=new MyHolder(v);
            return myHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.name.setText(list.get(position).getName());
            if(list.get(position).getIscheck()) {
                holder.name.setTextColor(Color.parseColor("#FFFFFF"));
            }else {
                holder.name.setTextColor(Color.parseColor("#000000"));
            }
            holder.name.setEnabled(list.get(position).getIscheck());
            holder.itemView.setOnClickListener(v -> {
                try {
                    Fragment fragment = list.get(position).getFargment().newInstance();
                    controller.nvTo(fragment, true);
                    setData(position);
                } catch (Exception e) {
                    appHints.toast("未找到页面!");
                }
            });
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setData( int position){
            for (int i=0;i<list.size();i++){
                list.get(i).setIscheck(i == position);
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{
            TextView name;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.tv_item_name);
            }
        }
    }
}
