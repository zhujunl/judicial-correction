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
import com.miaxis.judicialcorrection.base.api.vo.bean.WarningBean;
import com.miaxis.judicialcorrection.databinding.FragmentWarningBinding;
import com.miaxis.judicialcorrection.report.ReportRepo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WarningFragment extends BaseInfoFragment<FragmentWarningBinding> {
    private MyAdpter myAdpter;
    private List<WarningBean.Data> list=new ArrayList<>();
    @Inject
    ReportRepo reportRepo;
    CloudModel model;
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
//
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
        return R.layout.fragment_warning;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void initView(@NonNull FragmentWarningBinding binding, @Nullable Bundle savedInstanceState) {
        model=new ViewModelProvider(this).get(CloudModel.class);
        model.getWarning().observe(this, warningBeanResource -> {
            if (warningBeanResource.isSuccess()){
                myAdpter.setList(warningBeanResource.data.getList());
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
        private List<WarningBean.Data> list;

        public MyAdpter(Context context, List<WarningBean.Data> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(context).inflate(R.layout.item_warning,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.num.setText(String.valueOf(position+1));
            holder.fact.setText(list.get(position).getJgly());
            holder.time.setText(dateFormat.format(list.get(position).getSfssqsj()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<WarningBean.Data> l){
            list.clear();
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
