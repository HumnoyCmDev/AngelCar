package com.beta.cls.angelcar.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beta.cls.angelcar.Adapter.MultipleListViewChatAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.banner.ImageBanner;
import com.beta.cls.angelcar.dao.FollowCollectionDao;
import com.beta.cls.angelcar.dao.FollowDao;
import com.beta.cls.angelcar.dao.LogFromServerDao;
import com.beta.cls.angelcar.dao.MessageCollectionDao;
import com.beta.cls.angelcar.dao.MessageDao;
import com.beta.cls.angelcar.dao.PictureCollectionDao;
import com.beta.cls.angelcar.dao.PictureDao;
import com.beta.cls.angelcar.dao.PostCarDao;
import com.beta.cls.angelcar.dialog.DetailAlertDialog;
import com.beta.cls.angelcar.interfaces.WaitMessageOnBackground;
import com.beta.cls.angelcar.manager.MessageManager;
import com.beta.cls.angelcar.manager.Registration;
import com.beta.cls.angelcar.manager.WaitMessageSynchronous;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.beta.cls.angelcar.manager.http.HttpManager;
import com.beta.cls.angelcar.manager.http.OkHttpManager;
import com.beta.cls.angelcar.utils.ViewFindUtils;
import com.flyco.banner.anim.select.ZoomInEnter;
import com.flyco.banner.transform.DepthTransformer;
import com.flyco.banner.widget.Banner.base.BaseBanner;
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

    @Bind(R.id.list_view) ListView listView;
    @Bind(R.id.input_chat) EditText input_chat;
    @Bind(R.id.group_chat) LinearLayout groupChat;
    @Bind(R.id.button_follow) ToggleButton buttonFollow;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private View decorView;
    private String MESSAGE_BY = "shop";// shop & user

    MultipleListViewChatAdapter adapter;

    MessageManager messageManager ;
    WaitMessageSynchronous synchronous;

    PictureCollectionDao pictureCollectionDao;
    PostCarDao postItem;

    int intentForm; // 0 = form homeFragment , 1 = ChatAll,ChatBuy,ChatSell
    String messageFromUser;

    private static final String TAG = "DetailCarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_car);
        ButterKnife.bind(this);
        decorView = getWindow().getDecorView();
        initToolbar();
        initMessage();
        loadDataMessage();
        loadFollow();

    }

    private void loadFollow() {
        //init on//off Button Follow
        Call<FollowCollectionDao> call =
                HttpManager.getInstance().getService()
                        .loadFollow(Registration.getInstance().getShopRef());
        call.enqueue(followCollectionDaoCallback);
    }

    private void initMessage() {
        // getIntent
        /*init detail chat*/
        postItem = Parcels.unwrap(
                getIntent().getParcelableExtra("PostCarDao"));
        messageFromUser = getIntent().getStringExtra("messageFromUser");
        intentForm = getIntent().getIntExtra("intentForm",1);


        /*check user*/
            MESSAGE_BY = postItem.getShopRef().contains(Registration.getInstance().getShopRef()) ? "shop" : "user";
            Toast.makeText(DetailCarActivity.this, postItem.getCarId()+","+MESSAGE_BY, Toast.LENGTH_LONG).show();


        if (intentForm == 0 && MESSAGE_BY.contains("shop"))
            groupChat.setVisibility(View.GONE);

        /*load image*/
        Call<PictureCollectionDao> callLoadPictureAll =
                HttpManager.getInstance().getService().loadAllPicture(String.valueOf(postItem.getCarId()));
        callLoadPictureAll.enqueue(loadPictureCallback);

        // new Object MessageManager
        messageManager = new MessageManager();

        MessageCollectionDao gao = new MessageCollectionDao();
        List<MessageDao> messageDao = new ArrayList<>();

        MessageDao mGao = new MessageDao();
        mGao.setMessageBy("user");
        mGao.setMessageCarId(Registration.getInstance().getUserId());

//        String headerText = "<header><b><u><i><h2>Toyota fortuner 1.2v ปี 2016</h2></i></u></b>";
//        String subDetailText = "<p>รถใช้งานน้อย วิ่ง 100,000 กิโล</p> <p>  <b>ราคา.</b> 1xx,xxx <b>โทร.</b> 082-xxx-xxxx</p></header>";

        mGao.setMessageText(postItem.toMessage());
        messageDao.add(mGao);

        MessageDao mGaoAdmin = new MessageDao();
        mGaoAdmin.setMessageBy("user");
        mGaoAdmin.setMessageText("<header><p><b><u><i><h3>ข้อความจากระบบ “ราคากลาง”</h3></i></u></b></p>" +
                "<p>............................</p><p><b>ราคา.</b>1x,xxx,xxx$</p></header");
        messageDao.add(mGaoAdmin);

        gao.setListMessage(messageDao);
        messageManager.setMessageDao(gao);
        /*****************/
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick({R.id.message_button_send,R.id.message_button_personnel,R.id.message_button_image})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.message_button_send:
                OkHttpManager okHttpManager = new OkHttpManager.SendMessageBuilder()
                        .setMessage(postItem.getCarId()+"||"+messageFromUser+"||"+
                                input_chat.getText().toString() + "||"+
                                MESSAGE_BY).build();
                okHttpManager.putMessage();
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

                    new OkHttpManager.UploadFileBuilder(String.valueOf(postItem.getCarId())
                            ,messageFromUser,MESSAGE_BY)
                            .putImage(imgFile);

        }

    }

    private void loadDataMessage() {
        adapter = new MultipleListViewChatAdapter(MESSAGE_BY);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Intent View Image
                if (messageManager != null &&
                        messageManager.getMessageDao() != null &&
                        messageManager.getMessageDao().getListMessage() != null){

                    MessageDao item = messageManager.getMessageDao().getListMessage().get(position);
                    if (item.getMessageText().contains("<img>") &&
                            item.getMessageText().contains("</img>")) {
                        String url = item.getMessageText().substring("<img>".length(),item.getMessageText().lastIndexOf("</img>"));

                        Intent intent = new Intent(DetailCarActivity.this, SingleViewImageActivity.class);
                        intent.putExtra(SingleViewImageActivity.ARGS_PICTURE,url);
                        startActivity(intent);
                    }


                }

            }
        });


        Call<MessageCollectionDao> call =
                HttpManager.getInstance().getService()
                        .viewMessage(postItem.getCarId()+"||"+ messageFromUser +"||0");
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
    public void produceMessage(MessageCollectionDao messageGa){ //รับภายในคลาส
        Log.i(TAG, "produceMessage: mainthread");
        if (messageGa.getListMessage().size() > 0) {
            messageManager.appendDataToBottomPosition(messageGa);
            // คอมเม้นไว้ หาก list เด้งลงมา Last Position
            adapter.setMessages(messageManager.getMessageDao().getListMessage());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (synchronous != null){
            synchronous.killTask();
        }
    }

    @OnClick(R.id.button_follow)
    public void onFollowClick(){
        if (buttonFollow.isChecked()) {
            //Add Follow
            Call<LogFromServerDao> callAddFollow = HttpManager.getInstance().getService()
                    .follow("add",String.valueOf(postItem.getCarId()),
                    Registration.getInstance().getShopRef());
            callAddFollow.enqueue(callbackAddOrRemoveFollow);
        }else {
            //Delete Follow
            Call<LogFromServerDao> callDelete = HttpManager.getInstance().getService()
                    .follow("delete",String.valueOf(postItem.getCarId()),
                    Registration.getInstance().getShopRef());
            callDelete.enqueue(callbackAddOrRemoveFollow);

        }
    }

    private void pictureCarDetail(final PictureCollectionDao dao) {
        ImageBanner sib = ViewFindUtils.find(decorView, R.id.detailImage);

        sib
                .setTransformerClass(DepthTransformer.class)
                .setSelectAnimClass(ZoomInEnter.class)
                .setSource(dao.getListPicture())
                .startScroll();

        sib.setOnItemClickL(new BaseBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(DetailCarActivity.this, ViewPictureActivity.class);
                intent.putExtra("PICTURE_DAO", Parcels.wrap(dao));
                intent.putExtra("POSITION",position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*********************
     *Listener Class Zone
     *********************/
    DialogInterface.OnClickListener listenerDialogConfirm = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
                Call<LogFromServerDao> call =
                        HttpManager.getInstance().getService().regOfficer(postItem.getCarId()+"||"+messageFromUser);
                call.enqueue(new Callback<LogFromServerDao>() {
                    @Override
                    public void onResponse(Call<LogFromServerDao> call, Response<LogFromServerDao> response) {
                        if (response.isSuccessful()){
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
                    public void onFailure(Call<LogFromServerDao> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
        }
    };

    Callback<PictureCollectionDao> loadPictureCallback = new Callback<PictureCollectionDao>() {
        @Override
        public void onResponse(Call<PictureCollectionDao> call, Response<PictureCollectionDao> response) {
            if (response.isSuccessful()) {
                pictureCollectionDao = response.body();
                //Picture banner
                pictureCarDetail(response.body());

            } else {
                try {
                    Log.i(TAG, "onResponse: " + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PictureCollectionDao> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    };

    Callback<FollowCollectionDao> followCollectionDaoCallback = new Callback<FollowCollectionDao>() {
        @Override
        public void onResponse(Call<FollowCollectionDao> call, Response<FollowCollectionDao> response) {
            if (response.isSuccessful()) {
                if (response.body().getRows() != null) {
                    for (FollowDao dao : response.body().getRows()) {
                        if (dao.getCarRef().
                                contains(String.valueOf(postItem.getCarId()))) {
                            buttonFollow.setChecked(true);
                        }
                    }
                }

            } else {
                try {
                    Log.i(TAG, "onResponse: " + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<FollowCollectionDao> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    };

    Callback<LogFromServerDao> callbackAddOrRemoveFollow = new Callback<LogFromServerDao>() {
        @Override
        public void onResponse(Call<LogFromServerDao> call, Response<LogFromServerDao> response) {
            if (response.isSuccessful()) {
                Log.i(TAG, "onResponse:" + response.body().success);
            } else {
                try {
                    Log.i(TAG, "onResponse: " + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<LogFromServerDao> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    };

    /****************
    *Inner Class Zone
    *****************/
    private class LoadMessageCallback implements Callback<MessageCollectionDao> {

        @Override
        public void onResponse(Call<MessageCollectionDao> call, Response<MessageCollectionDao> response) {
            if (response.isSuccessful()){

                messageManager.appendDataToBottomPosition(response.body());
                adapter.setMessages(messageManager.getMessageDao().getListMessage());
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
        public void onFailure(Call<MessageCollectionDao> call, Throwable t) {
            Toast.makeText(DetailCarActivity.this,"Failure LogCat!!",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onFailure: ", t);
        }
    }

    private class WaitMessage extends WaitMessageOnBackground {
        Response<MessageCollectionDao> response;
        MessageManager manager;

        public WaitMessage(MessageManager manager) {
            this.manager = manager;
        }

        @Override
        public void onBackground() {
//            Log.i(TAG, "doInBackground: loop");
            int maxId = manager.getMaximumId();
            Call<MessageCollectionDao> call =
                    HttpManager.getInstance()
                            .getService(60 * 1000) // Milliseconds (1 นาที)
                            .waitMessage(postItem.getCarId()+"||"+ messageFromUser +"||" + maxId);
            try {
                response = call.execute(); // ทำงานเสร็จ จะเข้า onMainThread() ต่อ
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMainThread() {
            if (response.isSuccessful()){
                MessageCollectionDao messageGa = response.body();
                BusProvider.getInstance().post(messageGa);
            }else {
                Log.i(TAG, "doInBackground: "+response.errorBody());
            }
        }
    }

}
