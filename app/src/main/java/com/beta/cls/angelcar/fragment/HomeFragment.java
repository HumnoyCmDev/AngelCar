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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.beta.cls.angelcar.Adapter.ListViewPostAdapter;
import com.beta.cls.angelcar.activity.DetailCarActivity;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.manager.FeedBlogJson;
import com.beta.cls.angelcar.manager.FeedPostItem;
import com.beta.cls.angelcar.manager.GetResultJson;
import com.beta.cls.angelcar.interfaces.AsyncResult;
import com.google.gson.Gson;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    @Bind(R.id.list_view) ListView listView;
    @Bind(R.id.swipe_container) SwipeRefreshLayout mSwipeRefresh;

    private static final String TAG = "HomeFragment";

    private List<FeedPostItem> postItems;

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

        loadData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeedPostItem item = postItems.get(position);
                Intent intent = new Intent(getActivity(), DetailCarActivity.class);
                intent.putExtra("FeedPostItem", Parcels.wrap(item));
                startActivity(intent);

            }
        });

    }

    void loadData(){
        new GetResultJson(new AsyncResult() {
            @Override
            public void onSucceed(String s) {
                Gson gson = new Gson();
                FeedBlogJson blogJson = gson.fromJson(s,FeedBlogJson.class);
                postItems = blogJson.getRows();
                ListViewPostAdapter adapter = new ListViewPostAdapter(getActivity(),blogJson.getRows());
                listView.setAdapter(adapter);
            }
            @Override
            public void onFail() {
            }
        }).execute("http://www.usedcar.co.th/yobtestjson.php");
    }
}