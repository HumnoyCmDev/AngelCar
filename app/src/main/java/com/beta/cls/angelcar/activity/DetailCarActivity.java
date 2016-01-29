package com.beta.cls.angelcar.activity;

/**
 * Created by ABaD on 12/16/2015.
 */
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.beta.cls.angelcar.Adapter.ImageAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.Adapter.CustomListViewBlack;
import com.beta.cls.angelcar.util.InService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class DetailCarActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;

    //Variable of Chat
    TextView txtIP, txtStatus;
    ListView listChat;
    Button btnSend;
    EditText etxtIP, etxtMessage;
    ArrayList<String> arr_list;
    List<Integer> arr_gravity;
    InService inTask;
    public static final int TCP_SERVER_PORT = 21111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_car);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
        TelephonyManager tMgr =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(mPhoneNumber);

        initInstances();

        //Code for Chat
//        arr_list = new ArrayList<String>();
//        arr_gravity = new ArrayList<Integer>();
//        listChat = (ListView)findViewById(R.id.listChat);
//        etxtIP = (EditText)findViewById(R.id.etxtIP);
//        etxtMessage = (EditText)findViewById(R.id.etxtMessage);
//        txtIP = (TextView)findViewById(R.id.txtIP);
//        txtIP.setText("Your IP : " + getIP());
//
//
//        SharedPreferences settings = getSharedPreferences("Pref", 0);
//        String ip = settings.getString("IP", "192.168.1.1");
//        etxtIP.setText(ip);
//
//        inTask = new InService(getApplicationContext()
//                , TCP_SERVER_PORT, listChat, arr_list
//                , arr_gravity);
//        inTask.execute();
//
//        btnSend = (Button) findViewById(R.id.btnSend);
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(etxtMessage.getText().toString().length() > 0) {
//
//                    //txtStatus.setText("Sending...");
//                    sendMessage(etxtIP.getText().toString()
//                            , etxtMessage.getText().toString());
//                    etxtMessage.setText("");
//                }
//            }
//        });


    }

    //Start Function For Chat
    public void sendMessage(String ip, String message) {
        final String IP_ADDRESS = ip;
        final String MESSAGE = message;

        Runnable runSend = new Runnable() {
            public void run() {
                try {
                    Socket s = new Socket(IP_ADDRESS, TCP_SERVER_PORT);
                    s.setSoTimeout(5000);
                    BufferedReader in = new BufferedReader
                            (new InputStreamReader(s.getInputStream()));
                    BufferedWriter out = new BufferedWriter
                            (new OutputStreamWriter(s.getOutputStream()));
                    String outgoingMsg = MESSAGE
                            + System.getProperty("line.separator");
                    out.write(outgoingMsg);
                    out.flush();
                    Log.i("TcpClient", "sent: " + outgoingMsg);
                    String inMsg = in.readLine()
                            + System.getProperty("line.separator");

                    Handler refresh = new Handler(Looper.getMainLooper());
                    refresh.post(new Runnable() {
                        public void run() {
                            arr_gravity.add(Gravity.LEFT);
                            arr_list.add("Me : " + MESSAGE);
                            listChat.setAdapter(new CustomListViewBlack
                                    (getApplicationContext()
                                            , android.R.layout.simple_list_item_1
                                            , arr_list, arr_gravity));
                            listChat.setSelection(listChat.getCount());
                            //txtStatus.setText("Message has been sent.");
                            etxtMessage.setText("");
                        }
                    });

                    s.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    setText("No device on this IP address.");
                } catch (Exception e) {
                    e.printStackTrace();
                    setText("Connection failed. Please try again.");
                }
            }

            public void setText(String str) {
                final String string = str;
                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.post(new Runnable() {
                    public void run() {
                        txtStatus.setText(string);
                    }
                });
            }
        };
        new Thread(runSend).start();
    }

    public String getIP() {
        WifiManager wifiManager =
                (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8 ) & 0xFF) + "." +
                ((ipAddress >> 16 ) & 0xFF) + "." +
                ((ipAddress >> 24 ) & 0xFF );
        if(ip.equals("0.0.0.0"))
            ip = "Please connect WIFI";
        return ip;
    }

//    public void onPause() {
//        super.onPause();
//        SharedPreferences settings = getSharedPreferences("Pref", 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("IP", etxtIP.getText().toString());
//        editor.commit();
//        inTask.killTask();
//    }//End Function for chat


    private void initInstances() {

        toolbar = (Toolbar) findViewById(R.id.toolbar_top); // ย้าย Action Bar ไปไว้ใน Toolbar
        setSupportActionBar(toolbar);

        //fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
        //fabBtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {

        //    }
        //});

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);     /////// ตัว fix ปุ่มกลับก่อน Hamburger
        drawerToggle = new ActionBarDrawerToggle(DetailCarActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void onPostCreate(Bundle savedInstanceState) { //ตัว fix Hamburger
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    public boolean onOptionsItemSelected(MenuItem item) { //ตัว select เรียก slide จาก Hamburger
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
