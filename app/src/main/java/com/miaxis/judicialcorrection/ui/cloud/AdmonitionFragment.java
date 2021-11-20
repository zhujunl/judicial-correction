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
import com.miaxis.judicialcorrection.base.api.vo.bean.WarningBean;
import com.miaxis.judicialcorrection.databinding.FragmentAdmonitionBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdmonitionFragment extends BaseInfoFragment<FragmentAdmonitionBinding> {

    private MyAdpter myAdpter;
    private List<AdmonitionBean.Data> list=new ArrayList<>();
    CloudModel model;
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");

    //    {
//        list.add("1");
//        list.add("2");
//        list.add("3");
//        list.add("4");
//        list.add("5");
//        list.add("7");
//        list.add("8");
//        list.add("9");
//        list.add("10");
//        list.add("11");
//        list.add("12");
//        list.add("13");
//
//    }
    @Override
    protected int initLayout() {
        return R.layout.fragment_admonition;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void initView(@NonNull FragmentAdmonitionBinding binding, @Nullable Bundle savedInstanceState) {
        model=new ViewModelProvider(this).get(CloudModel.class);
        model.getAdmonition().observe(this,admonitionBeanResource -> {
            if (admonitionBeanResource.isLoading()){
                showLoading();
            } else  if(admonitionBeanResource.isSuccess()){
                myAdpter.setList(admonitionBeanResource.data.getList());
            }else if(admonitionBeanResource.isError()){
                dismissLoading();
            }
        });
        myAdpter=new MyAdpter(getContext(),list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        int width=getActivity().getDisplay().getWidth();
        int height=getActivity().getDisplay().getHeight();
        if(width>height)
            binding.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(myAdpter);
    }

    private class MyAdpter extends RecyclerView.Adapter<MyAdpter.MyHolder>{
        private Context context;
        private List<AdmonitionBean.Data> list;

        public MyAdpter(Context context, List<AdmonitionBean.Data> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(context).inflate(R.layout.item_adminition,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.num.setText(String.valueOf(position+1));
            holder.time.setText(dateFormat.format(list.get(position).getSfssqsj()));
            holder.fact.setText("\t"+list.get(position).getJgss());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        public void bubbleSort(List<AdmonitionBean.Data> list){
            Collections.sort(list, new Comparator<AdmonitionBean.Data>() {
                @Override
                public int compare(AdmonitionBean.Data o1, AdmonitionBean.Data o2) {
                    if(o2.getSfssqsj().getTime()>o1.getSfssqsj().getTime()){
                        return 1;
                    }else if(o2.getSfssqsj().getTime()<o1.getSfssqsj().getTime()){
                        return -1;
                    }else {
                        return 0;
                    }
                }
            });
        }
        public void setList(List<AdmonitionBean.Data> l){
            dismissLoading();
            list.clear();
            bubbleSort(l);
            list=l;
            notifyDataSetChanged();
        }

        class MyHolder extends RecyclerView.ViewHolder{
            TextView num,fact,time;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                num=itemView.findViewById(R.id.num);
                fact=itemView.findViewById(R.id.fact);
                time=itemView.findViewById(R.id.time);
            }
        }
    }
}
