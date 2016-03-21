package com.beta.cls.angelcar.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beta.cls.angelcar.Adapter.MultipleChatAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.gao.LogFromServerGao;
import com.beta.cls.angelcar.gao.MessageCollectionGao;
import com.beta.cls.angelcar.gao.MessageGao;
import com.beta.cls.angelcar.gao.PostCarGao;
import com.beta.cls.angelcar.interfaces.WaitMessageOnBackground;
import com.beta.cls.angelcar.manager.MessageManager;
import com.beta.cls.angelcar.manager.WaitMessageSynchronous;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.beta.cls.angelcar.manager.http.HttpChatManager;
import com.beta.cls.angelcar.manager.http.OkHttpManager;
import com.beta.cls.angelcar.interfaces.CallBackMainThread;
import com.beta.cls.angelcar.util.LineUp;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCarActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 988;

    @Bind(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.toolbar_top) Toolbar toolbar;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.viewpager) ViewPager viewPager;
    @Bind(R.id.recycler_view_chat_layout_input_chat) EditText input_chat;

//    @Bind(R.id.tvTopic) TextView tvTopic;
//    @Bind(R.id.tvCarType) TextView tvCarType;
//    @Bind(R.id.tvBrand) TextView tvBrand;
//    @Bind(R.id.tvCarSub) TextView tvCarSub;
//    @Bind(R.id.tvGear) TextView tvGear;
//    @Bind(R.id.tvYear) TextView tvYear;
//    @Bind(R.id.tvPrice) TextView tvPrice;
//    @Bind(R.id.tvPhone) TextView tvPhone;


    private final static String MESSAGE_BY = "shop";// shop & user

//    List<MessageGao> blogMessages;
    MultipleChatAdapter adapter;
    MessageManager messageManager ;
    WaitMessageSynchronous synchronous;

    private static final String TAG = "DetailCarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_car);
        ButterKnife.bind(this);

//        ImageAdapter adapter = new ImageAdapter(this);
//        viewPager.setAdapter(adapter);
//        TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        String mPhoneNumber = tMgr.getLine1Number();
//        collapsingToolbarLayout.setTitle(mPhoneNumber);

        initToolbar();
        initInstance();
        initDataMessage();
    }

    private void initInstance() {
        // getIntent
        /*init detail chat*/
        Intent getIntent = getIntent();
        PostCarGao postItem = Parcels.unwrap(
                getIntent.getParcelableExtra("PostCarGao"));


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
//        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);

        // new Object MessageManager
        messageManager = new MessageManager();

        MessageCollectionGao gao = new MessageCollectionGao();
        List<MessageGao> messageGao = new ArrayList<>();

        MessageGao mGao = new MessageGao();
        mGao.setMessageBy("user");
        mGao.setMessageCarId("2016010700001");

//        String headerText = "<header><b><u><i><h2>Toyota fortuner 1.2v ปี 2016</h2></i></u></b>";
//        String subDetailText = "<p>รถใช้งานน้อย วิ่ง 100,000 กิโล</p> <p>  <b>ราคา.</b> 1xx,xxx <b>โทร.</b> 082-xxx-xxxx</p></header>";

        mGao.setMessageText(postItem.toMessage());
        messageGao.add(mGao);

        MessageGao mGaoAdmin = new MessageGao();
        mGaoAdmin.setMessageBy("user");
        mGaoAdmin.setMessageText("<header><p><b><u><i><h3>ข้อความจากระบบ “ราคากลาง”</h3></i></u></b></p>" +
                "<p>............................</p><p><b>ราคา.</b>1x,xxx,xxx$</p></header");
        messageGao.add(mGaoAdmin);

        gao.setMessage(messageGao);
        messageManager.setMessageGao(gao);
        /*****************/
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @OnClick({R.id.message_button_send,R.id.message_button_personnel,R.id.message_button_image})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.message_button_send:
                OkHttpManager okHttpManager = new OkHttpManager.SendMessageBuilder()
                        .setMessage("26||2016010700001||"+
                                input_chat.getText().toString() + "||"+
                                MESSAGE_BY).build();
                okHttpManager.callEnqueue(new CallBackMainThread() {
                    @Override
                    public void onResponse(okhttp3.Response response) {
                        Toast.makeText(DetailCarActivity.this,"success",Toast.LENGTH_SHORT).show();
                    }
                });
                input_chat.setText("");

            break;

            case R.id.message_button_personnel :
                Dialog dialog = new AlertDialog.Builder(DetailCarActivity.this)
                        .setTitle("Message!")
                        .setMessage("เชิญบุคคลที่ 3")
                        .setNegativeButton("Ok", listenerDialogConfirm)
                        .create();
                dialog.show();
            break;

            case R.id.message_button_image :

                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            File imgFile = new File(picturePath);
            Toast.makeText(DetailCarActivity.this,imgFile.getPath(),Toast.LENGTH_SHORT).show();

                    new OkHttpManager.UploadFileBuilder("26","2016010700001",MESSAGE_BY)
                            .putImage(imgFile);

        }

    }

    private void initDataMessage() {
        adapter = new MultipleChatAdapter(MESSAGE_BY);
                recyclerView.setAdapter(adapter);

        Call<MessageCollectionGao> call =
                HttpChatManager.getInstance().getService().viewMessage("26||2016010700001||1");
        call.enqueue(new LoadMessageCallback());

    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void produceMessage(MessageCollectionGao messageGa){
        messageManager.updateDataToLastPosition(messageGa);
        adapter.setMessagesGao(messageManager.getMessageGao().getMessage()); // คอมเม้นไว้ หาก list เด้งลงมา Last Position
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (synchronous != null){
            synchronous.killTask();
        }
    }

    /*********************
     *Listener Class Zone
     *********************/

    DialogInterface.OnClickListener listenerDialogConfirm = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
                Call<LogFromServerGao> call = HttpChatManager.getInstance().getService().regOfficer("26||2016010700001");
                call.enqueue(new Callback<LogFromServerGao>() {
                    @Override
                    public void onResponse(Call<LogFromServerGao> call, Response<LogFromServerGao> response) {
                        if (response.isSuccess()){
                            Toast.makeText(DetailCarActivity.this,"success ::"+response.body().getResult(),Toast.LENGTH_SHORT).show();
                        }else {
                            try {
                                Toast.makeText(DetailCarActivity.this,"success"+response.errorBody().string(),Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LogFromServerGao> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
        }
    };

    /****************
    *Inner Class Zone
    *****************/
    private class LoadMessageCallback implements Callback<MessageCollectionGao> {

        @Override
        public void onResponse(Call<MessageCollectionGao> call, Response<MessageCollectionGao> response) {
            if (response.isSuccess()){

//                messageManager.setMessageGao(response.body());

                messageManager.updateDataToLastPosition(response.body());

                adapter.setMessagesGao(messageManager.getMessageGao().getMessage());
                adapter.notifyDataSetChanged();

                    /*start Time Out 1000L wait message */
                synchronous = new WaitMessageSynchronous(
                        new WaitMessage(messageManager));
                synchronous.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }else {
                try {
                    Toast.makeText(DetailCarActivity.this,response.errorBody().string(),Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<MessageCollectionGao> call, Throwable t) {
            Toast.makeText(DetailCarActivity.this,"Failure LogCat!!",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onFailure: ", t);
        }
    }

    private class WaitMessage extends WaitMessageOnBackground {
        Response<MessageCollectionGao> response;
        MessageManager manager;

        public WaitMessage(MessageManager manager) {
            this.manager = manager;
        }

        @Override
        public void onBackground() {
            Log.i(TAG, "doInBackground: loop");
            int maxId = manager.getMaximumId();
            Call<MessageCollectionGao> call =
                    HttpChatManager.getInstance()
                            .getService(60 * 1000) // Milliseconds (1 นาที)
                            .waitMessage("26||2016010700001||" + maxId);
            try {
                response = call.execute(); // ทำงานเสร็จ จะเข้า onMainThread() ต่อ
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMainThread() {
            Log.i(TAG, "onMainThread: ");
            if (response.isSuccess()){
                Log.i(TAG, "onMainThread: success");
                MessageCollectionGao messageGa = response.body();
                BusProvider.getInstance().post(messageGa);
            }else {
                Log.i(TAG, "doInBackground --- : "+response.errorBody());
            }
        }
    }

}
