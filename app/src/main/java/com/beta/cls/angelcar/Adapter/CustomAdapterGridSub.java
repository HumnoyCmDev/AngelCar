package com.beta.cls.angelcar.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dao.CarDataTypeDao;

import java.util.List;

public class CustomAdapterGridSub extends BaseAdapter {
    List<CarDataTypeDao> post;

    public CustomAdapterGridSub(List<CarDataTypeDao> post) {
        this.post = post;
    }

    @Override
    public int getCount() {
        if (post == null) return 0;
        return post.size();
    }

    @Override
    public CarDataTypeDao getItem(int position) {
        return post.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_row, parent, false);
            mViewHolder = new ViewHolder();
             mViewHolder.cartype_sub = (TextView) convertView.findViewById(R.id.name_cartype);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        CarDataTypeDao gao = getItem(position);

        mViewHolder.cartype_sub.setText(gao.getCarTypeSub());


        return convertView;
    }

    private static class ViewHolder {

        TextView cartype_sub;

    }
}