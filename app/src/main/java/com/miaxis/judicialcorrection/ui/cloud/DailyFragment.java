package com.miaxis.judicialcorrection.ui.cloud;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miaxis.enroll.guide.infos.BaseInfoFragment;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.api.vo.bean.AdmonitionBean;
import com.miaxis.judicialcorrection.base.api.vo.bean.DailyBean;
import com.miaxis.judicialcorrection.databinding.FragmentDailyBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DailyFragment extends BaseInfoFragment<FragmentDailyBinding> {

    private MyAdpter myAdpter;
    private List<DailyBean.Data> list=new ArrayList<>();
    private CloudModel viewmodel;
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected int initLayout() {
        return R.layout.fragment_daily;
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void initView(@NonNull FragmentDailyBinding binding, @Nullable Bundle savedInstanceState) {
        viewmodel=new ViewModelProvider(this).get(CloudModel.class);
        viewmodel.getreport().observe(this,dailyBeanResource -> {
            if(dailyBeanResource.isLoading()){showLoading();}
            else if(dailyBeanResource.isSuccess()){
                myAdpter.setList(dailyBeanResource.data.getList());
            }else if(dailyBeanResource.isError()){
                dismissLoading();
            }
        });

        myAdpter=new MyAdpter(getActivity(),list);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        int width=getActivity().getDisplay().getWidth();
        int height=getActivity().getDisplay().getHeight();
        if(width>height)
            binding.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(myAdpter);

    }

    @Override
    protected void initData(@NonNull FragmentDailyBinding binding, @Nullable Bundle savedInstanceState) {

    }


    private class MyAdpter extends RecyclerView.Adapter<MyAdpter.MyHolder>{

        private Context context;
        private List<DailyBean.Data> list;

        public MyAdpter(Context context, List<DailyBean.Data> list) {
            this.context = context;
            this.list = list;
        }


        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(context).inflate(R.layout.item_daily,parent,false);
            MyHolder myHolder=new MyHolder(v);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.id.setText(String.valueOf(position+1));
            holder.mode.setText(list.get(position).getBgfsName());
            holder.style.setText("按时报告");
            holder.time.setText(dateFormat.format(list.get(position).getBgsj()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        public void bubbleSort(List<DailyBean.Data> list){
            Collections.sort(list, new Comparator<DailyBean.Data>() {
                @Override
                public int compare(DailyBean.Data o1, DailyBean.Data o2) {
                    if(o2.getBgsj().getTime()>o1.getBgsj().getTime()){
                        return 1;
                    }else if(o2.getBgsj().getTime()<o1.getBgsj().getTime()){
                        return -1;
                    }else {
                        return 0;
                    }
                }
            });
        }
        public void setList(List<DailyBean.Data> l){
            dismissLoading();
            list.clear();
            bubbleSort(l);
            list=l;
            notifyDataSetChanged();
        }

        class MyHolder extends RecyclerView.ViewHolder{

            TextView id,mode,style,time;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                id=itemView.findViewById(R.id.num);
                mode=itemView.findViewById(R.id.mode);
                style=itemView.findViewById(R.id.style);
                time=itemView.findViewById(R.id.time);
            }
        }
    }
}