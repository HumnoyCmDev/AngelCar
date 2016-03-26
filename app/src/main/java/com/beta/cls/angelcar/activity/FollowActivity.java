package com.beta.cls.angelcar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.beta.cls.angelcar.Adapter.ListViewPostAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dao.PostCarCollectionDao;

import butterknife.Bind;

/**
 * Created by humnoyDeveloper on 26/3/59. 09:06
 */
public class FollowActivity extends AppCompatActivity{

    @Bind(R.id.listView) ListView listView;

    private PostCarCollectionDao gao;
    private ListViewPostAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        initInstance();
    }

    private void initInstance() {

    }

}
