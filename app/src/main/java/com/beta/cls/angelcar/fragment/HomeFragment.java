package com.beta.cls.angelcar.fragment;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.beta.cls.angelcar.Adapter.ListViewPostAdapter;
import com.beta.cls.angelcar.activity.DetailCarActivity;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.anim.ResizeHeight;
import com.beta.cls.angelcar.gao.CountCarCollectionGao;
import com.beta.cls.angelcar.gao.PostCarCollectionGao;
import com.beta.cls.angelcar.gao.PostCarGao;
import com.beta.cls.angelcar.manager.http.HttpChatManager;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    public static final String SAVE_STATE_GAO = "SAVE_STATE_GAO";
    @Bind(R.id.list_view) ListView listView;
    @Bind(R.id.swipe_container) SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.ctFilter) RelativeLayout ctFilter;
    @Bind(R.id.btFilter) ImageView btFilter;
    @Bind(R.id.countCarAll) TextView countCarAll;
    @Bind(R.id.countCarDay) TextView countCarDay;
    @Bind(R.id.countCarMonth) TextView countCarMonth;

//    Animation animDown,animUp;

    private static final String TAG = "HomeFragment";

    private PostCarCollectionGao gao;
    private ListViewPostAdapter adapter;

    public HomeFragment() {
        super();
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initInstance();
        if (savedInstanceState == null){
            loadData();
        }else { // Restore
            gao = Parcels.unwrap(savedInstanceState.getParcelable(SAVE_STATE_GAO));
            adapter.setGao(gao);
            adapter.notifyDataSetChanged();
        }

//        animDown = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_slide_down);
//        animUp = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_slide_up);

        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctFilter.getVisibility() == View.GONE) {
                    ResizeHeight resizeHeight = new ResizeHeight(ctFilter,1000,true);
                    resizeHeight.setInterpolator(new BounceInterpolator());
                    resizeHeight.setDuration(1000);
                    ctFilter.startAnimation(resizeHeight);
                    ctFilter.setVisibility(View.VISIBLE);
                } else {
                    ResizeHeight resizeHeight = new ResizeHeight(ctFilter,0,false);
                    resizeHeight.setDuration(500);
                    ctFilter.startAnimation(resizeHeight);
//                    ctFilter.setVisibility(View.GONE);
//                    ctFilter.startAnimation(animUp);
                }

            }
        });
    }

    private void initInstance() {
        adapter = new ListViewPostAdapter();
        listView.setAdapter(adapter);
        mSwipeRefresh.setColorSchemeColors(
                Color.parseColor("#104F94"),
                Color.parseColor("#104F94"),
                Color.parseColor("#FFC11E"),
                Color.parseColor("#FFC11E"));

        mSwipeRefresh.setOnRefreshListener(pullRefresh);
        listView.setOnItemClickListener(onItemClickListViewListener);
        listView.setOnScrollListener(onScrollListener);

    }

    private void loadData(){

        Call<PostCarCollectionGao> call =
                HttpChatManager.getInstance().getService().loadPost();
        call.enqueue(callbackLoadPostCar);

        // load count car
        Call<CountCarCollectionGao> callLoadCountCar =
                HttpChatManager.getInstance().getService().loadCountCar();
        callLoadCountCar.enqueue(countCarCallback);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_STATE_GAO,Parcels.wrap(gao));

    }

    /**************
    *Listener Zone*
    ***************/
    SwipeRefreshLayout.OnRefreshListener pullRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            mSwipeRefresh.setEnabled(firstVisibleItem == 0);
        }
    };
    AdapterView.OnItemClickListener onItemClickListViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PostCarGao item = gao.getRows().get(position);
            Intent intent = new Intent(getActivity(), DetailCarActivity.class);
            intent.putExtra("PostCarGao", Parcels.wrap(item));
            startActivity(intent);

        }
    };

    Callback<PostCarCollectionGao> callbackLoadPostCar = new Callback<PostCarCollectionGao>() {
        @Override
        public void onResponse(Call<PostCarCollectionGao> call, Response<PostCarCollectionGao> response) {
            mSwipeRefresh.setRefreshing(false);
            if (response.isSuccess()) {
                gao = response.body();
                adapter.setGao(response.body());
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(),
                        response.errorBody().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<PostCarCollectionGao> call, Throwable t) {
            mSwipeRefresh.setRefreshing(false);
            Toast.makeText(getActivity(),
                    t.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    };

    Callback<CountCarCollectionGao> countCarCallback = new Callback<CountCarCollectionGao>() {
        @Override
        public void onResponse(Call<CountCarCollectionGao> call, Response<CountCarCollectionGao> response) {
            if (response.isSuccess()) {
                CountCarCollectionGao.CountCarGao countCarGao = response.body().getRows().get(0);
                countCarAll.setText("ทั้งหมด " + countCarGao.getCountAll() + " คัน");
                countCarMonth.setText("เดือนนี้ " + countCarGao.getCountMonth() + " คัน");
                countCarDay.setText("วันนี้ " + countCarGao.getCountDay() + " คัน");
            } else {
                try {
                    Log.i(TAG, "onResponse: " + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<CountCarCollectionGao> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    };

    /*****************
    *Inner Class Zone*
    ******************/

}