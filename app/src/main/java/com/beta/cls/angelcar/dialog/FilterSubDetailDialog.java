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
import com.beta.cls.angelcar.dao.CarSubCollectionDao;
import com.beta.cls.angelcar.dao.CarSubDao;
import com.beta.cls.angelcar.fragment.HomeFragment;
import com.beta.cls.angelcar.manager.http.HttpManager;
import com.beta.cls.angelcar.manager.http.HttpUsedCarManager;
import com.beta.cls.angelcar.model.FilterCarModel;

import org.parceler.Parcels;

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
    private static final String ARGS_BRAND = "ARGS_BRAND";
    private static final String TAG = "FilterSubDialogFragment";

    @Bind(R.id.list_view) ListView listView;
    private CarSubCollectionDao dao;
    private ListViewAdapter adapter;
    private FilterCarModel filterCarModel;

    public static FilterSubDetailDialog newInstance(FilterCarModel filterCarModel) {
        Bundle args = new Bundle();
        FilterSubDetailDialog fragment = new FilterSubDetailDialog();
        args.putParcelable(ARGS_BRAND,Parcels.wrap(filterCarModel));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new CarSubCollectionDao();

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

        filterCarModel = Parcels.unwrap(getArguments().getParcelable(ARGS_BRAND));

        adapter = new ListViewAdapter();
        adapter.setData(dao);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarSubDao result = dao.getCarSubDao().get(position);
                Intent intent = getActivity().getIntent();
                intent.putExtra(HomeFragment.ARG_SUB_DETAIL,Parcels.wrap(result));
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
                dismiss();
            }
        });

        if (savedInstanceState == null) {
            Call<CarSubCollectionDao> call = HttpManager.getInstance().getService().loadDataBrandSubDetail(filterCarModel.getSubDao().getSubId());
            call.enqueue(carSubCollectionDaoCallback);
        }

    }


    /******************
     *Listener zone*
     ******************/
    Callback<CarSubCollectionDao> carSubCollectionDaoCallback = new Callback<CarSubCollectionDao>() {
        @Override
        public void onResponse(Call<CarSubCollectionDao> call, Response<CarSubCollectionDao> response) {
            if (response.isSuccessful()) {
                dao = response.body();
                adapter.setData(dao);
                adapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "onResponse: " + response.errorBody().toString());
            }
        }

        @Override
        public void onFailure(Call<CarSubCollectionDao> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    };

    /******************
     *Inner class zone*
     ******************/
    public class ListViewAdapter extends BaseAdapter{

        CarSubCollectionDao dao;

        public void setData(CarSubCollectionDao dao) {
            this.dao = dao;
        }

        @Override
        public int getCount() {
            if (dao == null) return 0;
            if (dao.getCarSubDao() == null) return 0;
            return dao.getCarSubDao().size();
        }

        @Override
        public CarSubDao getItem(int position) {
            return dao.getCarSubDao().get(position);
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
            holder.iconFilter.setImageResource(R.drawable.toyota);
            holder.tvFilter.setText(getItem(position).getSubName());

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
