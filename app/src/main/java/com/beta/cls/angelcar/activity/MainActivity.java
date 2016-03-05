package com.beta.cls.angelcar.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.Adapter.MainViewPagerAdapter;
import com.beta.cls.angelcar.fragment.YearFragmentDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_top) Toolbar toolbar;
    @Bind(R.id.tabLayout) TabLayout tabLayout;
    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.viewpager) ViewPager viewPager;

    private ActionBarDrawerToggle drawerToggle;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbars();
        initViewPager();
        initTabIcons(); //ตั้งค่า tab


    }


    private void initViewPager() {
        viewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initTabIcons() {
        String fontPath = "font/bangna.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        tabLayout.getTabAt(0).setCustomView(inflaterCustomTabLayout("หน้าแรก",R.drawable.ic_tab_home,tf));
        tabLayout.getTabAt(1).setCustomView(inflaterCustomTabLayout("ประกาศ",R.drawable.ic_tab_notice,tf));
        tabLayout.getTabAt(2).setCustomView(inflaterCustomTabLayout("ไฟแนนซ์",R.drawable.ic_tab_finance,tf));
        tabLayout.getTabAt(3).setCustomView(inflaterCustomTabLayout("ตัวกรอง",R.drawable.ic_tab_filter,tf));
        tabLayout.getTabAt(4).setCustomView(inflaterCustomTabLayout("ช่วยเหลือ",R.drawable.ic_tab_help,tf));
    }

    private View inflaterCustomTabLayout(String title,int drawble,Typeface tf){
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
        textView.setTypeface(tf, Typeface.BOLD);
        textView.setText(title);
        textView.setCompoundDrawablesWithIntrinsicBounds( 0 ,drawble, 0, 0);
        return textView;
    }

    private void initToolbars() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbarTop);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        toolbarTop.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        break;

                }
                return true;
            }
        });
        // Inflate a menu to be displayed in the toolbar
        toolbarTop.inflateMenu(R.menu.menu_carline);
    }
    public void onPostCreate(Bundle savedInstanceState) { //ตัว fix Hamburger
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    public boolean onOptionsItemSelected(MenuItem item) { //ตัว select เรียก slide จาก Hamburger
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.menu_bottom_tag,R.id.menu_bottom_chat,
            R.id.menu_bottom_list_shop,R.id.menu_bottom_profile,
            R.id.menu_bottom_feedback,})
    public void menuBottom(View view){
        switch (view.getId()){
            case R.id.menu_bottom_tag:
                break;
            case R.id.menu_bottom_chat:
                Intent intent = new Intent(MainActivity.this,MessageBlogActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_bottom_list_shop:
                break;
            case R.id.menu_bottom_profile:
                break;
            case R.id.menu_bottom_feedback:
                break;
        }
    }

}
