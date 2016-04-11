package com.beta.cls.angelcar.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beta.cls.angelcar.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 24/2/59.
 */
public class FilterBrandDialog extends DialogFragment{
    @Bind(R.id.list_view) ListView listView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_layout,container,false);
        ButterKnife.bind(this,view);
        initInstance(savedInstanceState);
        return view;
    }

    private void initInstance(Bundle savedInstanceState) {

        ListViewAdapter viewAdapter = new ListViewAdapter();
        viewAdapter.setData(getBrand());
        listView.setAdapter(viewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getActivity().getIntent();
                intent.putExtra("BRAND",getBrand().get(position));
                intent.putExtra("LOGO",getImageBrand()[position]);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
                dismiss();
            }
        });
    }

    private List<String> getBrand(){
        List<String> brand = new ArrayList<>();
        brand.add("toyota");
        brand.add("honda");
        brand.add("nissan");
        brand.add("isuzu");
        brand.add("mitsubishi");
        brand.add("chevrolet");
        brand.add("ford");
        brand.add("mazda");
        brand.add("benz");
        brand.add("audi");
        brand.add("bmw");
        brand.add("hyundai");
        brand.add("kia");
        brand.add("land rover");
        brand.add("mini");
        brand.add("suzuki");
        brand.add("volkswagen");
        brand.add("volvo");
        brand.add("tata");

        return brand;
    }

    private int[] getImageBrand() {
        return new int[]{R.drawable.toyota
                , R.drawable.honda, R.drawable.nissan
                , R.drawable.isuzu, R.drawable.mitsubishi
                , R.drawable.chevrolet, R.drawable.ford
                , R.drawable.mazda, R.drawable.benz
                , R.drawable.audi, R.drawable.bmw
                , R.drawable.hyundai, R.drawable.kia
                , R.drawable.landrover, R.drawable.mini
                , R.drawable.suzuki, R.drawable.volkswagen
                , R.drawable.volvo, R.drawable.tata
                , R.drawable.foton, R.drawable.hino
                , R.drawable.holden, R.drawable.honda
                , R.drawable.hummer, R.drawable.hyundai};
    }

    /******************
     *Listener zone*
     ******************/


    /******************
     *Inner class zone*
     ******************/
    public class ListViewAdapter extends BaseAdapter{

        List<String> data;

        public void setData(List<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            if (data == null) return 0;
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView != null){
                holder = (ViewHolder) convertView.getTag();
            }else {
               convertView = LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.adapter_item_filter,parent,false);
               holder = new ViewHolder(convertView);
               convertView.setTag(holder);
            }
            holder.iconFilter.setImageResource(getImageBrand()[position]);
            holder.tvFilter.setText(data.get(position));

            return convertView;
        }

        public class ViewHolder {
            @Bind(R.id.filter_icon) ImageView iconFilter;
            @Bind(R.id.filter_name) TextView tvFilter;
            public ViewHolder(View view) {
                ButterKnife.bind(this,view);
            }
        }



    }

}
