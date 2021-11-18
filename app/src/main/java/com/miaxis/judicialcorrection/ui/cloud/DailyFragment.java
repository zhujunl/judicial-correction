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
import com.miaxis.judicialcorrection.databinding.FragmentDailyBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DailyFragment extends BaseInfoFragment<FragmentDailyBinding> {

    private MyAdpter myAdpter;
//    private List<DailyBean.Data> list=new ArrayList<>();
    List<String> list=new ArrayList<>();
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
//        viewmodel.getreport().observe(this,dailyBeanResource -> {
//            if(dailyBeanResource.isSuccess()){
//                myAdpter.setList(dailyBeanResource.data.getList());
//            }
//        });

        myAdpter=new MyAdpter(getActivity(),list);
        {
            list.add("1");
            list.add("2");
            list.add("3");
            list.add("4");
            list.add("5");
            list.add("7");
            list.add("8");
            list.add("9");
            list.add("10");
            list.add("11");
            list.add("12");
            list.add("13");
            list.add("14");
            list.add("15");
            list.add("16");
            list.add("17");
            list.add("18");
            list.add("19");
        }

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
//        private List<DailyBean.Data> list;
//
//        public MyAdpter(Context context, List<DailyBean.Data> list) {
//            this.context = context;
//            this.list = list;
//        }

        private List<String> list;

        public MyAdpter(Context context, List<String> list) {
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
//            holder.mode.setText(list.get(position).getBgfsName());
//            holder.style.setText("按时报告");
//            holder.time.setText(dateFormat.format(list.get(position).getBgsj()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

//        public void setList(List<DailyBean.Data> l){
//            list.clear();
//            list=l;
//            notifyDataSetChanged();
//        }

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