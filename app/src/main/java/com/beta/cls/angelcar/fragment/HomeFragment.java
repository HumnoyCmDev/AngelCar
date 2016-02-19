package com.beta.cls.angelcar.fragment;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.beta.cls.angelcar.Adapter.ListViewPostAdapter;
import com.beta.cls.angelcar.activity.DetailCarActivity;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.anim.ResizeHeight;
import com.beta.cls.angelcar.gao.FeedPostCollectionGao;
import com.beta.cls.angelcar.gao.FeedPostGao;
import com.beta.cls.angelcar.manager.http.ApiService;
import com.beta.cls.angelcar.manager.http.HttpManager;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    @Bind(R.id.list_view) ListView listView;
    @Bind(R.id.swipe_container) SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.ctFilter) RelativeLayout ctFilter;
    @Bind(R.id.btFilter) ImageView btFilter;
    Animation animDown,animUp;

    private static final String TAG = "HomeFragment";
    private List<FeedPostGao> postItems;

    private ListViewPostAdapter adapter;
    public HomeFragment() {
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
        mSwipeRefresh.setColorSchemeColors(
                Color.parseColor("#104F94"),
                Color.parseColor("#104F94"),
                Color.parseColor("#FFC11E"),
                Color.parseColor("#FFC11E"));

        int[] resId = {R.drawable.profile_car
                , R.drawable.profile_car, R.drawable.profile_car
                , R.drawable.profile_car, R.drawable.profile_car
                , R.drawable.profile_car, R.drawable.profile_car
                , R.drawable.profile_car, R.drawable.profile_car
                , R.drawable.profile_car, R.drawable.profile_car};

        String[] list = {"TOYOTA", "HONDA", "MAZDA"
                , "SUBARU", "ISUZU", "AUDI", "BENZ"
                , "BMW", "NISSAN", "MITSUBISHI"
                , "FORD"};

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefresh.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        if (savedInstanceState == null){
            loadData();
            initInstance();

        }


        animDown = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_slide_down);
        animUp = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_slide_up);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeedPostGao item = postItems.get(position);
                Intent intent = new Intent(getActivity(), DetailCarActivity.class);
                intent.putExtra("FeedPostGao", Parcels.wrap(item));
                startActivity(intent);

            }
        });
    }

    void loadData(){

        ApiService call = HttpManager.getInstance().getService();
        call.loadPost().enqueue(new Callback<FeedPostCollectionGao>() {
            @Override
            public void onResponse(Call<FeedPostCollectionGao> call, Response<FeedPostCollectionGao> response) {
                if (response.isSuccess()){
                    postItems = response.body().getRows();
                    adapter = new ListViewPostAdapter(postItems);
                    listView.setAdapter(adapter);
                }else {
                    Toast.makeText(getActivity(),
                            response.errorBody().toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FeedPostCollectionGao> call, Throwable t) {
                Toast.makeText(getActivity(),
                        t.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}