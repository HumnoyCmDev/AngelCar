package com.beta.cls.angelcar.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beta.cls.angelcar.Adapter.ImageAdapter;
import com.beta.cls.angelcar.Adapter.MultipleChatAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.util.BlogMessage;
import com.beta.cls.angelcar.util.FeedPostItem;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailCarActivity extends AppCompatActivity {

    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.toolbar_top)
    Toolbar toolbar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.activity_detail_tvCarType)
    TextView tvCarType;

    @Bind(R.id.recycler_view_chat_layout_input_chat)
    EditText input_chat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_car);
        ButterKnife.bind(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
        TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        collapsingToolbarLayout.setTitle(mPhoneNumber);

        initInstances();
        initData();

    }
    List<BlogMessage> blogMessages;
    MultipleChatAdapter adapter;
    private void initData() {

        blogMessages = new ArrayList<>();
        BlogMessage b = new BlogMessage();
        b.setMessageid("111");
        b.setMessagecarid("111");
        b.setMessagefromuser("111");
        b.setMessagetext("1111111");
        b.setDisplayname("11111");
        b.setMessageby("user");
        b.setUserprofileimage("11111");
        blogMessages.add(b);

        for (int i = 0; i<10;i++){
            BlogMessage bb = new BlogMessage();
            bb.setMessageid("22");
            bb.setMessagecarid("22");
            bb.setMessagefromuser("22");
            bb.setMessagetext("22 :"+i);
            bb.setDisplayname("22");
            bb.setMessageby("shop");
            bb.setUserprofileimage("22");
            blogMessages.add(bb);
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        adapter = new MultipleChatAdapter(
                DetailCarActivity.this,
                blogMessages,"shop");
        recyclerView.setAdapter(adapter);
    }

    private void initInstances() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // getIntent
        Intent getIntent = getIntent();
        if (getIntent != null){
            FeedPostItem postItem = Parcels.unwrap(getIntent.getParcelableExtra("FeedPostItem"));
            tvCarType.setText(postItem.getCartype());
        }

    }

    @OnClick(R.id.recycler_view_chat_layout_button_send)
    public void onClickButtonSend(){
        BlogMessage b = new BlogMessage();
        b.setMessageid("111");
        b.setMessagecarid("111");
        b.setMessagefromuser("111");
        b.setMessagetext(input_chat.getText().toString());
        b.setDisplayname("11111");
        b.setMessageby("user");
        b.setUserprofileimage("11111");
        blogMessages.add(b);
        adapter.notifyDataSetChanged();
    }




}
