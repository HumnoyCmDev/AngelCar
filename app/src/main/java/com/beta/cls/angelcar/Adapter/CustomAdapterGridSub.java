package com.beta.cls.angelcar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.gao.CarDataTypeGao;

import java.util.List;

public class CustomAdapterGridSub extends BaseAdapter {
    private LayoutInflater mInflater;
    List<CarDataTypeGao> post;
    private ViewHolder mViewHolder;

    private Bitmap mBitmap;
    private Activity mActivity;

    public CustomAdapterGridSub(Activity activity, List<CarDataTypeGao> post) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.post = post;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return post.size();
    }

    @Override
    public CarDataTypeGao getItem(int position) {
        return post.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_row, parent, false);
            mViewHolder = new ViewHolder();
             mViewHolder.cartype_sub = (TextView) convertView.findViewById(R.id.name_cartype);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        CarDataTypeGao gao = getItem(position);

        mViewHolder.cartype_sub.setText(gao.getCarTypeSub());


        return convertView;
    }

    private static class ViewHolder {

        TextView cartype_sub;

    }
}