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
import com.beta.cls.angelcar.dao.CarDataTypeCollectionDao;
import com.beta.cls.angelcar.dao.CarDataTypeDao;
import com.beta.cls.angelcar.manager.http.ApiCarService;
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
public class FilterSubDialog extends DialogFragment{
    @Bind(R.id.list_view) ListView listView;
    private static final String ARGS_BRAND = "ARGS_BRAND";
    private static final String ARGS_LOGO = "ARGS_LOGO";
    private static final String TAG = "FilterSubDialogFragment";

    private String brand;
    private int resourceBrand;
    private CarDataTypeCollectionDao dao;
    private ListViewAdapter viewAdapter;

    public static FilterSubDialog newInstance(int resourceBrand, String brand) {
        Bundle args = new Bundle();
        FilterSubDialog fragment = new FilterSubDialog();
        args.putString(ARGS_BRAND,brand);
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
        resourceBrand = getArguments().getInt(ARGS_LOGO);
        ApiCarService server = HttpUsedCarManager.getInstance().getService();
        Call<CarDataTypeCollectionDao> call = server.loadCarType(brand);
        call.enqueue(carDataTypeCollectionGaoCallback);

        viewAdapter = new ListViewAdapter();
        listView.setAdapter(viewAdapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarDataTypeDao result = dao.getRows().get(position);
                Intent intent = getActivity().getIntent();
                intent.putExtra("SUB",result.getCarTypeSub());
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
                dismiss();
            }
        });
    }


    /******************
     *Listener zone*
     ******************/
    Callback<CarDataTypeCollectionDao> carDataTypeCollectionGaoCallback = new Callback<CarDataTypeCollectionDao>() {
        @Override
        public void onResponse(Call<CarDataTypeCollectionDao> call, Response<CarDataTypeCollectionDao> response) {
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
        public void onFailure(Call<CarDataTypeCollectionDao> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    };

    /******************
     *Inner class zone*
     ******************/
    public class ListViewAdapter extends BaseAdapter{

        CarDataTypeCollectionDao dao;

        public void setData(CarDataTypeCollectionDao dao) {
            this.dao = dao;
        }

        @Override
        public int getCount() {
            if (dao == null) return 0;
            if (dao.getRows() == null) return 0;
            return dao.getRows().size();
        }

        @Override
        public CarDataTypeDao getItem(int position) {
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
            holder.tvFilter.setText(getItem(position).getCarTypeSub());

            return convertView;
        }

        public class ViewHolder {
            @Bind(R.id.filter_icon)
            ImageView iconFilter;
            @Bind(R.id.filter_name)
            TextView tvFilter;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
