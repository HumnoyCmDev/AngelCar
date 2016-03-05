package com.beta.cls.angelcar.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.activity.PostActivity;
import com.beta.cls.angelcar.gao.LogFromServerGao;
import com.beta.cls.angelcar.interfaces.OnSelectData;
import com.beta.cls.angelcar.manager.Contextor;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.beta.cls.angelcar.manager.http.ApiChatService;
import com.beta.cls.angelcar.manager.http.ApiService;
import com.beta.cls.angelcar.manager.http.HttpManager;
import com.beta.cls.angelcar.model.InformationFromUser;
import com.beta.cls.angelcar.util.IpAddress;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***************************************
 * สร้างสรรค์ผลงานดีๆ
 * โดย humnoy Android Developer
 * ลงวันที่ 5/2/59. เวลา 10:41
 ***************************************/
public class AllPostFragment extends Fragment implements View.OnClickListener{
    @Bind({
            R.id.fragment_all_post_photo_1,R.id.fragment_all_post_photo_2,
            R.id.fragment_all_post_photo_3,R.id.fragment_all_post_photo_4,
            R.id.fragment_all_post_photo_5,R.id.fragment_all_post_photo_6,
            R.id.fragment_all_post_photo_7,R.id.fragment_all_post_photo_8,
    }) List<ImageView> photo;

    @Bind(R.id.fragment_all_post_etBrand) EditText editTextBrand;
    @Bind(R.id.fragment_all_post_etDescription) EditText editTextDescription;
    @Bind(R.id.fragment_all_post_tgGear) ToggleButton tgGear;
    @Bind(R.id.fragment_all_post_etName) EditText editTextName;
    @Bind(R.id.fragment_all_post_etPrice) EditText editTextPrice;
    @Bind(R.id.fragment_all_post_etRegister) EditText editTextRegister;
    @Bind(R.id.fragment_all_post_etTypeSub) EditText editTextTypeSub;
    @Bind(R.id.fragment_all_post_etType) EditText editTextType;
    @Bind(R.id.fragment_all_post_etTelephone) EditText editTextTelephone;
    @Bind(R.id.fragment_all_post_etTopic) EditText editTextTopic;
    @Bind(R.id.fragment_all_post_etYear) EditText editTextYear;

    @Bind(R.id.tvDetailPostCar) TextView detailPostCar;
    @Bind(R.id.spinnerProvince) Spinner spinnerProvince;


    private static final String TAG = "AllPostFragment";
    int RESULT_LOAD_IMAGE = 191;
    int id_photo = 0;
    int id_province = 1;
    private static String ARG_InformationFromUser = "ARG_InformationFromUser";
    private InformationFromUser user;

    public AllPostFragment() {
        super();
    }

    public static AllPostFragment newInstance() {
        AllPostFragment fragment = new AllPostFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_post, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here


    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
//            initData();
            initInstance();
            initDataProvince();
            
        }

    }

    private void initDataProvince() {
        List<String> list = new ArrayList<String>();
        getResources().getStringArray(R.array.province);
        Collections.addAll(list, getResources().getStringArray(R.array.province));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(dataAdapter);

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(parent.getContext(),parent.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_LONG).show();
                id_province = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initInstance() {
        for (ImageView imv : photo) {
            imv.setOnClickListener(this);
        }
        editTextDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    onClickPost();
                    return true;
                }
                return false;
            }
        });
    }

    private void initData() {


//        editTextBrand.setText(user.getBrand());
//        editTextType.setText(user.getTypeSub());
//        editTextTypeSub.setText(user.getTypeSubDetail());
//        editTextYear.setText(""+user.getYear());

        //
        detailPostCar.setText(
                        user.getBrand().toUpperCase()+" "+
                        user.getTypeSub()+" "+
                        user.getTypeSubDetail()+" ปี"+
                        user.getYear());
    }

    @OnClick(R.id.fragment_all_post_ButtonPost)
    public void onClickPost(){

        String postUserId = "1111111";
        String carTypeMain = user.getBrand().toUpperCase(); // toyota
        String carTypeSub = user.getTypeSub(); // fortuner
        String carTypeSubDetail = user.getTypeSubDetail(); // 1.6 v
        int carYear = user.getYear(); // 2016
        String gear = tgGear.isChecked() ? "1":"2"; // 1 or 2
        String price = editTextPrice.getText().toString(); // 900,000
        String carRegister = editTextRegister.getText().toString(); // กท.0001
        String province = String.valueOf(id_province);//editTextProvince.getText().toString(); // nonthaburi (1-77)
        String postTopic = editTextTopic.getText().toString(); // รถใหม่ใช้น้อย
        String postDetail = editTextDescription.getText().toString(); // รถใหม่ใช้น้อย......
        String postName = editTextName.getText().toString(); // ชื่อ....
        String postTel = editTextTelephone.getText().toString();
        String ipPost = IpAddress.getInstance().getIp();
        Date postDate = new Date();

        ApiService service = HttpManager.getInstance().getService();
        Call<LogFromServerGao> call = service.postCar(
                postUserId, carTypeMain, carTypeSub, carTypeSubDetail,
                carYear, gear, price, carRegister, province, postTopic,
                postDetail, postName, postTel, ipPost, postDate);
        call.enqueue(postCallback);

        OnSelectData onSelectData = (OnSelectData) getActivity();
        onSelectData.onSelectedCallback(PostActivity.CALLBACK_ALL_POST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1 && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            File imgFile = new File(picturePath);

            Picasso.with(getActivity())
                    .load(imgFile)
                    .error(R.drawable.photo)
                    .placeholder(R.drawable.photo)
                    .into(photo.get(id_photo));
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_all_post_photo_1: id_photo = 0; break;
            case R.id.fragment_all_post_photo_2: id_photo = 1; break;
            case R.id.fragment_all_post_photo_3: id_photo = 2; break;
            case R.id.fragment_all_post_photo_4: id_photo = 3; break;
            case R.id.fragment_all_post_photo_5: id_photo = 4; break;
            case R.id.fragment_all_post_photo_6: id_photo = 5; break;
            case R.id.fragment_all_post_photo_7: id_photo = 6; break;
            case R.id.fragment_all_post_photo_8: id_photo = 7; break;
        }
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
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
    public void getProduceData(InformationFromUser user){
        this.user = user;
        initData();
        Log.i(TAG, "getProduceData3: "+user.getBrand());
        Log.i(TAG, "getProduceData3: "+user.getTypeSub());
        Log.i(TAG, "getProduceData3: "+user.getTypeSubDetail());
        Log.i(TAG, "getProduceData3: "+user.getYear());

    }

    /*********
    *Listener Zone
    **********/
    Callback<LogFromServerGao> postCallback = new Callback<LogFromServerGao>() {
        @Override
        public void onResponse(Call<LogFromServerGao> call, Response<LogFromServerGao> response) {
            if (response.isSuccess()) {
                Toast.makeText(Contextor.getInstance().getContext(),
                        "Completed", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Toast.makeText(Contextor.getInstance().getContext(),
                            response.errorBody().string(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<LogFromServerGao> call, Throwable t) {
            Toast.makeText(Contextor.getInstance().getContext(),
                    t.toString(), Toast.LENGTH_SHORT).show();
        }
    };
}
