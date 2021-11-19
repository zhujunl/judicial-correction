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
import com.miaxis.judicialcorrection.base.api.vo.bean.CentralizedBean;
import com.miaxis.judicialcorrection.databinding.FragmentCentralizedBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CentralizedFragment extends BaseInfoFragment<FragmentCentralizedBinding> {

    private MyAdpter myAdpter;
    private List<CentralizedBean.li> list=new ArrayList<>();
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
        return R.layout.fragment_centralized;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void initView(@NonNull FragmentCentralizedBinding binding, @Nullable Bundle savedInstanceState) {
        model=new ViewModelProvider(this).get(CloudModel.class);
        model.getEducation().observe(this, CentralizedBean->{
            if(CentralizedBean.isLoading()){showLoading();}
            else if(CentralizedBean.isSuccess()){
                myAdpter.setList(CentralizedBean.data.getList());
            }else if(CentralizedBean.isError()){
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
        private List<CentralizedBean.li> list;

        public MyAdpter(Context context, List<CentralizedBean.li> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(context).inflate(R.layout.item_centralized,parent,false);
            return new MyHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.num.setText(String.valueOf(position+1));
            holder.theme.setText(list.get(position).getEducationVo().getJiaoyuzhutiName());
            holder.tv_cotent.setText(list.get(position).getEducationVo().getJyxxzynr());
            holder.tv_address.setText(list.get(position).getEducationVo().getJiaoyudidian());
            holder.tv_time.setText(dateFormat.format(list.get(position).getEducationVo().getJzjysj()));
            holder.duration.setText(list.get(position).getEducationVo().getJyxxsc());
            holder.evaluate.setText(list.get(position).getPingjia());
        }
        public void bubbleSort(List<CentralizedBean.li> list){
            Collections.sort(list, new Comparator<CentralizedBean.li>() {
                @Override
                public int compare(CentralizedBean.li o1, CentralizedBean.li o2) {
                    if(o2.getEducationVo().getJzjysj().getTime()>o1.getEducationVo().getJzjysj().getTime()){
                        return 1;
                    }else if(o2.getEducationVo().getJzjysj().getTime()<o1.getEducationVo().getJzjysj().getTime()){
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

        public void setList(List<CentralizedBean.li> l){
            dismissLoading();
            list.clear();
            bubbleSort(l);
            list=l;
            notifyDataSetChanged();
        }

        class MyHolder extends RecyclerView.ViewHolder{
            TextView num,theme,tv_cotent,tv_address,tv_time,duration,evaluate;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                num=itemView.findViewById(R.id.num);
                theme=itemView.findViewById(R.id.theme);
                tv_cotent=itemView.findViewById(R.id.tv_cotent);
                tv_address=itemView.findViewById(R.id.tv_address);
                tv_time=itemView.findViewById(R.id.tv_time);
                duration=itemView.findViewById(R.id.duration);
                evaluate=itemView.findViewById(R.id.evaluate);
            }
        }
    }
}
