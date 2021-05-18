package com.miaxis.judicialcorrection.base.databinding.kvsp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * KvSpinnerAdapter
 *
 * @author zhangyw
 * Created on 5/17/21.
 */
public class KvSpinnerAdapter extends BaseAdapter {

    private List<KvSpinnerVo> data;

    public void submitList(List<KvSpinnerVo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public KvSpinnerVo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        view.setText(data.get(position).value);
        return view;
    }
}