package com.beta.cls.angelcar.activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.Adapter.MainViewPagerAdapter;
import com.beta.cls.angelcar.fragment.RegistrationAlertFragment;
import com.beta.cls.angelcar.dao.RegisterResultDao;
import com.beta.cls.angelcar.gcm.GcmRegisterService;
import com.beta.cls.angelcar.manager.Registration;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.beta.cls.angelcar.manager.http.HttpManager;
import com.beta.cls.angelcar.util.RegistrationResult;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int EMAIL_RESOLUTION_REQUEST = 333;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private boolean isReceiverRegistered;

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

        registerReceiver();
        if (checkPlayServices()) {
            registerGcm();
        }

        initInstance();
        initToolbars();
        initViewPager();
        initTabIcons(); //ตั้งค่า tab


    }

//  googlePicker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMAIL_RESOLUTION_REQUEST && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Toast.makeText(MainActivity.this,accountName,Toast.LENGTH_LONG).show();
            Call<RegisterResultDao> call = HttpManager.getInstance().getService().registrationEmail(accountName);
            call.enqueue(callbackRegistrationEmail);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_PERMISSIONS:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission Granted
//                    dialogConfirmFragment();
//                } else {
//                    // Permission Denied
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    private void initInstance() {
        boolean first = Registration.getInstance().isFirstApp();
        checkRegistrationEmail(first);
    }

    private void checkRegistrationEmail(boolean first_init) {
        if (!first_init){
//            if (!checkPermissionAccountApi23()){
//                dialogConfirmFragment();
//            }
            Intent googlePicker =
                    AccountPicker.newChooseAccountIntent(null, null,
                            new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
            startActivityForResult(googlePicker, EMAIL_RESOLUTION_REQUEST);

        }else {
            // กรณีลงทะเบียนแล้วให้ เช็ค cache // หากไม่พบ ให้ Registration Email
            String cache_User = Registration.getInstance().getUserId();
            if (cache_User != null){
                Toast.makeText(MainActivity.this,cache_User,Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dialogConfirmFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("RegistrationAlertFragment");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        RegistrationAlertFragment fragment = RegistrationAlertFragment.newInstance();
        fragment.setCancelable(false);
        fragment.show(ft, "RegistrationAlertFragment");
    }

    private boolean checkPermissionAccountApi23(){
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.GET_ACCOUNTS)) {
                    showMessageOKCancel("AngelCar ต้องการสิทธิ์ในการเข้าถึงบัญชี", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.GET_ACCOUNTS},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    });

                }else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.GET_ACCOUNTS},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }

            }else {
                dialogConfirmFragment();
            }
        return true;
        }
        return false;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Subscribe //Produce form RegistrationAlertFragment.java
    public void onRegistrationEmail(RegistrationResult result){
        if (result.getResult() == RegistrationAlertFragment.REGISTRATION_OK){
            // ติดต่อ server
            Call<RegisterResultDao> call = HttpManager.getInstance().getService().registrationEmail(result.getEmail());
            call.enqueue(callbackRegistrationEmail);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BusProvider.getInstance().unregister(this);
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
        tabLayout.getTabAt(4).setCustomView(inflaterCustomTabLayout("โปรไฟล์",R.drawable.ic_tab_profile,tf));
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

//        toolbarTop.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_settings:
//                        break;
//
//                }
//                return true;
//            }
//        });
//        // Inflate a menu to be displayed in the toolbar
//        toolbarTop.inflateMenu(R.menu.menu_carline);
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

    @OnClick({R.id.menu_bottom_follow,R.id.menu_bottom_message,
            R.id.menu_bottom_list_shop,R.id.menu_bottom_help,
            R.id.menu_bottom_feedback,})
    public void bottomMenu(View view){
        if (Registration.getInstance().getUserId() != null) {
            switch (view.getId()) {
                case R.id.menu_bottom_follow:
                    startActivity(initIntent(FollowActivity.class));
                    break;
                case R.id.menu_bottom_message:
                    startActivity(initIntent(TotalChatActivity.class));
                    break;
                case R.id.menu_bottom_list_shop:
                    startActivity(initIntent(ShopActivity.class));
                    break;
                case R.id.menu_bottom_help:
                    startActivity(initIntent(GuideLineActivity.class));
                    break;
                case R.id.menu_bottom_feedback:
                    break;
            }
        }else {
            checkRegistrationEmail(false);
        }
    }

    private Intent initIntent(Class<?> cls){
        return new Intent(MainActivity.this,cls);
    }


    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GcmRegisterService.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

    private void registerGcm() {
        Intent intent = new Intent(this, GcmRegisterService.class);
        startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }


    /**************
    *Listener Zone*
    ***************/
    Callback<RegisterResultDao> callbackRegistrationEmail = new Callback<RegisterResultDao>() {
        @Override
        public void onResponse(Call<RegisterResultDao> call, Response<RegisterResultDao> response) {
            if (response.isSuccessful()) {
                // Save Cache
                Registration.getInstance().save(response.body());
                Toast.makeText(MainActivity.this, "ลงทะเบียนเรียบร้อยแล้ว "+response.body().getUserId() +" "+response.body().getShopId(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "" + response.errorBody(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<RegisterResultDao> call, Throwable t) {
            Toast.makeText(MainActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean sentToken = sharedPreferences.getBoolean(GcmRegisterService.SENT_TOKEN_TO_SERVER, false);
            // TODO Do something here
        }
    };
}
