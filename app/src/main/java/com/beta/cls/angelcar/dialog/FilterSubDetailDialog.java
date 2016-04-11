package com.beta.cls.angelcar.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dao.CarDetailCollectionDao;
import com.beta.cls.angelcar.dao.CarDetailDao;
import com.beta.cls.angelcar.manager.http.HttpUsedCarManager;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by humnoy on 24/2/59.
 */
public class FilterSubDetailDialog extends DialogFragment{
    @Bind(R.id.list_view) ListView listView;
    private static final String ARGS_BRAND = "ARGS_BRAND";
    private static final String ARGS_SUB = "ARGS_SUB";
    private static final String ARGS_LOGO = "ARGS_LOGO";
    private static final String TAG = "FilterSubDialogFragment";

    private String brand;
    private String sub;
    private CarDetailCollectionDao dao;
    private ListViewAdapter viewAdapter;
    private int resourceBrand;

    public static FilterSubDetailDialog newInstance(int resourceBrand, String brand, String sub) {
        Bundle args = new Bundle();
        FilterSubDetailDialog fragment = new FilterSubDetailDialog();
        args.putString(ARGS_BRAND,brand);
        args.putString(ARGS_SUB,sub);
        args.putInt(ARGS_LOGO,resourceBrand);
        fragment.setArguments(args);
        return fragment;
    }

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
        brand = getArguments().getString(ARGS_BRAND);
        sub = getArguments().getString(ARGS_SUB);
        resourceBrand = getArguments().getInt(ARGS_LOGO);

        Call<CarDetailCollectionDao> call = HttpUsedCarManager.getInstance()
                .getService().loadCarSubDetail(brand,sub);

        call.enqueue(new Callback<CarDetailCollectionDao>() {
            @Override
            public void onResponse(Call<CarDetailCollectionDao> call, Response<CarDetailCollectionDao> response) {
                if (response.isSuccessful()) {
                    dao = response.body();
                    viewAdapter.setData(dao);
                    viewAdapter.notifyDataSetChanged();

                } else {
                    try {
                        Log.i(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CarDetailCollectionDao> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

        viewAdapter = new ListViewAdapter();
        listView.setAdapter(viewAdapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarDetailDao result = dao.getRows().get(position);
                Intent intent = getActivity().getIntent();
                intent.putExtra("SUB_DETAIL",result.getCarDetailSub());
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
                dismiss();
            }
        });
    }


    /******************
     *Listener zone*
     ******************/


    /******************
     *Inner class zone*
     ******************/
    public class ListViewAdapter extends BaseAdapter{

        CarDetailCollectionDao dao;

        public void setData(CarDetailCollectionDao dao) {
            this.dao = dao;
        }

        @Override
        public int getCount() {
            if (dao == null) return 0;
            if (dao.getRows() == null) return 0;
            return dao.getRows().size();
        }

        @Override
        public CarDetailDao getItem(int position) {
            return dao.getRows().get(position);
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
            holder.iconFilter.setImageResource(resourceBrand);
            holder.tvFilter.setText(getItem(position).getCarDetailSub());

            return convertView;
        }

        public class ViewHolder {
            @Bind(R.id.filter_icon) ImageView iconFilter;
            @Bind(R.id.filter_name) TextView tvFilter;
            public ViewHolder(View view) {
                ButterKnife.bind(this,view);
            }
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

    }

}
