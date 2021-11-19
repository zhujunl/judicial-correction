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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miaxis.enroll.guide.infos.BaseInfoFragment;
import com.miaxis.judicialcorrection.R;
import com.miaxis.judicialcorrection.base.api.vo.bean.WelfareBean;
import com.miaxis.judicialcorrection.base.common.Resource;
import com.miaxis.judicialcorrection.databinding.FragmentWelfareBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WelfareFragment extends BaseInfoFragment<FragmentWelfareBinding> {

    private MyAdpter myAdpter;
    private List<WelfareBean.Dat> list=new ArrayList<>();
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");

    CloudModel cloudModel;

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
        return R.layout.fragment_welfare;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void initView(@NonNull FragmentWelfareBinding binding, @Nullable Bundle savedInstanceState) {
        cloudModel=new ViewModelProvider(this).get(CloudModel.class);
        cloudModel.getHistoryActivityInfo().observe(this, new Observer<Resource<WelfareBean>>() {
            @Override
            public void onChanged(Resource<WelfareBean> welfareBeanResource) {
                if(welfareBeanResource.isLoading()){showLoading();}
                else if (welfareBeanResource.isSuccess()){
                    myAdpter.setList(welfareBeanResource.data.getList());
                }else if (welfareBeanResource.isError()){
                    dismissLoading();
                }
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
        private List<WelfareBean.Dat> list;

        public MyAdpter(Context context, List<WelfareBean.Dat> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(context).inflate(R.layout.item_welfare,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.num.setText(String.valueOf(position+1));
            holder.time.setText(dateFormat.format(list.get(position).getPublicActivityVo().getSqfwkssj()));
            holder.mode.setText(list.get(position).getPublicActivityVo().getSqffxs());
            holder.description.setText(list.get(position).getPublicActivityVo().getSqfwnr());
            holder.place.setText(list.get(position).getPublicActivityVo().getSqfwdd());
            holder.duration.setText(list.get(position).getPublicActivityVo().getSqfwsc());
        }
        public void bubbleSort(List<WelfareBean.Dat> list){
            Collections.sort(list, new Comparator<WelfareBean.Dat>() {
                @Override
                public int compare(WelfareBean.Dat o1, WelfareBean.Dat o2) {
                    if(o2.getPublicActivityVo().getSqfwkssj().getTime()>o1.getPublicActivityVo().getSqfwkssj().getTime()){
                        return 1;
                    }else if(o2.getPublicActivityVo().getSqfwkssj().getTime()<o1.getPublicActivityVo().getSqfwkssj().getTime()){
                        return -1;
                    }else {
                        return 0;
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<WelfareBean.Dat> l){
            dismissLoading();
            list.clear();
            bubbleSort(l);
            list=l;
            notifyDataSetChanged();
        }

        class MyHolder extends RecyclerView.ViewHolder{

            TextView num,time,mode,description,place,duration;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                num=itemView.findViewById(R.id.num);
                time=itemView.findViewById(R.id.time);
                mode=itemView.findViewById(R.id.mode);
                description=itemView.findViewById(R.id.description);
                place=itemView.findViewById(R.id.place);
                duration=itemView.findViewById(R.id.duration);
            }
        }
    }
}
