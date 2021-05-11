package com.miaxis.judicialcorrection.leave;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.miaxis.judicialcorrection.base.db.po.Place;

import java.util.List;

/**
 * @author Tank
 * @date 2021/5/9 17:45
 * @des
 * @updateAuthor
 * @updateDes
 */
public class SpAdapter extends BaseAdapter {

    private List<Place> data;

    public void submitList(List<Place> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Place getItem(int position) {
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
        view.setText(getItem(position).VALUE);
        return view;
    }
}
