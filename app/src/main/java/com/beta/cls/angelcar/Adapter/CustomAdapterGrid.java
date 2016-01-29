package com.beta.cls.angelcar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.beta.cls.angelcar.R;

public class CustomAdapterGrid extends BaseAdapter {
    private Context mContext;
    private int[] resId;
//    private LayoutInflater mInflater;

    public CustomAdapterGrid(Context context, int[] resId) {
        this.mContext= context;
        this.resId = resId;
//        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return resId.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.grid_row, parent, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.imageGrid);
        imageView.setBackgroundResource(resId[position]);

        return view;
    }

    public class ViewHolder{

    }

}