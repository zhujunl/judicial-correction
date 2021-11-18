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
import com.miaxis.judicialcorrection.base.api.vo.bean.IndividualBean;
import com.miaxis.judicialcorrection.databinding.FragmentIndividualBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class IndividualFragment extends BaseInfoFragment<FragmentIndividualBinding> {

    private MyAdpter myAdpter;
    private List<IndividualBean.Data> list=new ArrayList<>();

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
        return R.layout.fragment_individual;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void initView(@NonNull FragmentIndividualBinding binding, @Nullable Bundle savedInstanceState) {
        model=new ViewModelProvider(this).get(CloudModel.class);
        model.getPersonEducation().observe(this,reportRepo->{
            if(reportRepo.isSuccess()){
                myAdpter.setList(reportRepo.data.getList());
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
        private List<IndividualBean.Data> list;

        public MyAdpter(Context context, List<IndividualBean.Data> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(context).inflate(R.layout.item_individual,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.num.setText(String.valueOf(position+1));
            holder.theme.setText(list.get(position).getJyxxfsName());
            holder.talk_date.setText(dateFormat.format(list.get(position).getJyxxkssj()));
            holder.working_personnel.setText(list.get(position).getJlr());
            holder.talk_place.setText(list.get(position).getJyxxthdd());
            holder.duration.setText(list.get(position).getJyxxsc());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<IndividualBean.Data> l){
            list.clear();
            list=l;
            notifyDataSetChanged();
        }
        class MyHolder extends RecyclerView.ViewHolder{
            TextView num,theme,talk_date,working_personnel,talk_place,duration;
            public MyHolder(@NonNull View itemView) {
                super(itemView);
                num=itemView.findViewById(R.id.num);
                theme=itemView.findViewById(R.id.theme);
                talk_date=itemView.findViewById(R.id.talk_date);
                working_personnel=itemView.findViewById(R.id.working_personnel);
                talk_place=itemView.findViewById(R.id.talk_place);
                duration=itemView.findViewById(R.id.duration);
            }
        }
    }

}
