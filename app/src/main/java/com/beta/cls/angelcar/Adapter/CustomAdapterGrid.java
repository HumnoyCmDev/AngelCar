package com.beta.cls.angelcar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beta.cls.angelcar.R;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        ViewHolder holder;
        if(view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_row,parent,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.imgBrand.setBackgroundResource(resId[position]);
        holder.tvName.setText(getBrand(position));

        return view;
    }

    public class ViewHolder{
        @Bind(R.id.imageGrid) ImageView imgBrand;
        @Bind(R.id.name_cartype) TextView tvName;

        public ViewHolder(View v) {
            ButterKnife.bind(this,v);
        }
    }

    private String getBrand(int position){
        switch (position){
            case 0 : return "toyota";
            case 1 : return "honda";
            case 2 : return "nissan";
            case 3 : return "isuzu";
            case 4 : return "mitsubishi";
            case 5 : return "chevrolet";
            case 6 : return "ford";
            case 7 : return "mazda";
            case 8 : return "benz";
            case 9 : return "audi";
            case 10 : return "bmw";
            case 11 : return "hyundai";
            case 12 : return "kia";
            case 13 : return "land rover";
            case 14 : return "mini";
            case 15 : return "suzuki";
            case 16 : return "volkswagen";
            case 17 : return "volvo";
            case 18 : return "tata";
        }
        return "";
    }
}