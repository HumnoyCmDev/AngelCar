package com.beta.cls.angelcar.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import com.beta.cls.angelcar.dao.LogFromServerDao;
import com.beta.cls.angelcar.interfaces.OnSelectData;
import com.beta.cls.angelcar.manager.Contextor;
import com.beta.cls.angelcar.manager.Registration;
import com.beta.cls.angelcar.manager.bus.BusProvider;
import com.beta.cls.angelcar.manager.http.HttpManager;
import com.beta.cls.angelcar.model.InformationFromUser;
import com.beta.cls.angelcar.util.LineUp;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***************************************
 * สร้างสรรค์ผลงานดีๆ
 * โดย humnoy Android Developer
 * ลงวันที่ 5/2/59. เวลา 10:41
 ***************************************/
public class PostFragment extends Fragment {
    @Bind({
            R.id.post_photo_1,R.id.post_photo_2,
            R.id.post_photo_3,R.id.post_photo_4,
            R.id.post_photo_5,R.id.post_photo_6,
            R.id.post_photo_7,R.id.post_photo_8,
    }) List<ImageView> photo;

    @Bind(R.id.fragment_all_post_etDescription) EditText editTextDescription;
    @Bind(R.id.fragment_all_post_tgGear) ToggleButton tgGear;
    @Bind(R.id.fragment_all_post_etName) EditText editTextName;
    @Bind(R.id.fragment_all_post_etPrice) EditText editTextPrice;
    @Bind(R.id.fragment_all_post_etRegister) EditText editTextRegister;
    @Bind(R.id.fragment_all_post_etTelephone) EditText editTextTelephone;
    @Bind(R.id.fragment_all_post_etTopic) EditText editTextTopic;

    @Bind(R.id.tvDetailPostCar) TextView detailPostCar;
    @Bind(R.id.spinnerProvince) Spinner spinnerProvince;


    private static final String TAG = "PostFragment";
    private static String ARG_InformationFromUser = "ARG_InformationFromUser";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    int RESULT_LOAD_IMAGE = 191;
    int id_photo = 0;
    int id_province = 1;
    HashMap<Integer, File> filesPhotoList;

    private InformationFromUser user;

    public PostFragment() {
        super();
    }

    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filesPhotoList = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
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
                id_province = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initInstance() {
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


        detailPostCar.setText(
                        user.getBrand().toUpperCase()+" "+
                        user.getTypeSub()+" "+
                        user.getTypeSubDetail()+" ปี"+
                        user.getYear());
    }

    @OnClick(R.id.fragment_all_post_ButtonPost)
    public void onClickPost(){

       String shopPref = Registration.getInstance().getShopRef(); // 1
       String carName = user.getBrand().toUpperCase() ; // toyota
       String carDetail = LineUp.getInstance().append(editTextTopic.getText().toString(),
               editTextDescription.getText().toString()); // ชื่อสั้นๆ
       int carYear = user.getYear(); // ปีรถ
       String carPrice = editTextPrice.getText().toString();// ราคารถ
       String carStatus = "wait";//wait,online,offline
       String province = String.valueOf(id_province); // 1 - 77
       String gear = tgGear.isChecked() ? "1":"2"; // 0 or 1
       String plate = editTextRegister.getText().toString(); // text ทะเบียนน
       String name = editTextName.getText().toString(); // ชื่อ นามสกุล

        Call<LogFromServerDao> call = HttpManager.getInstance().getService().postCar(
                shopPref, carName, carDetail, carYear,
                carPrice, carStatus, province, gear, plate, name);
        call.enqueue(postCallback);


        OnSelectData onSelectData = (OnSelectData) getActivity();
        onSelectData.onSelectedCallback(PostActivity.CALLBACK_ALL_POST);
        Toast.makeText(getActivity(),"Post",Toast.LENGTH_SHORT).show();

    }

    private static void uploadPicture(String id,HashMap<Integer, File> filesPhotoList, okhttp3.Callback responseCallbackUpFile) {
        //post picture
        OkHttpClient client = new OkHttpClient();
        for (int i = 0; i < 8; i++) {
            if (filesPhotoList.containsKey(i)) {
                Log.i(TAG, "onClickPost: " + filesPhotoList.get(i).isFile() + "  userfile ::" + (1 + i));

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("carid", id)
                        .addFormDataPart(
                        "userfile",
                        filesPhotoList.get(i).getName(),
                        RequestBody.create(MEDIA_TYPE_PNG, filesPhotoList.get(i))).build();

                Request request = new Request.Builder()
                        .url("http://www.angelcar.com/imgupload.php")
                        .post(requestBody)
                        .build();
                client.newCall(request).enqueue(responseCallbackUpFile);
            }
        }
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

            File file = new File(picturePath);
//            photo.get(id_photo).setImageBitmap(decodeFile(file));

            // list photo
            filesPhotoList.put(id_photo,file);
            photo.get(id_photo).setImageBitmap(
                    decodeFile(filesPhotoList.get(id_photo)));

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({
            R.id.post_photo_1,R.id.post_photo_2,
            R.id.post_photo_3,R.id.post_photo_4,
            R.id.post_photo_5,R.id.post_photo_6,
            R.id.post_photo_7,R.id.post_photo_8,
    })
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_photo_1 : id_photo = 0 ; break ;
            case R.id.post_photo_2 : id_photo = 1 ; break ;
            case R.id.post_photo_3 : id_photo = 2 ; break ;
            case R.id.post_photo_4 : id_photo = 3 ; break ;
            case R.id.post_photo_5 : id_photo = 4 ; break ;
            case R.id.post_photo_6 : id_photo = 5 ; break ;
            case R.id.post_photo_7 : id_photo = 6 ; break ;
            case R.id.post_photo_8 : id_photo = 7 ; break ;
        }
        addAndRemovePhotoList(id_photo);
    }

    private void addAndRemovePhotoList(int id_photo){
        // เช็คกรณี หากมีรูปอยู่แล้ว กดอีกครั้งให้ลบออก
        if (filesPhotoList.containsKey(id_photo)){
            filesPhotoList.remove(id_photo);
            photo.get(id_photo).setImageResource(R.drawable.photo);
        }else{
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
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

    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE=50;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    /*********
    *Listener Zone
    **********/
    Callback<LogFromServerDao> postCallback = new Callback<LogFromServerDao>() {
        @Override
        public void onResponse(Call<LogFromServerDao> call, Response<LogFromServerDao> response) {
            if (response.isSuccessful()) {
                // upload picture
                uploadPicture(response.body().getSuccess(),filesPhotoList, responseCallbackUpFile);
                Toast.makeText(Contextor.getInstance().getContext(),
                        "Completed"+response.body().getSuccess(), Toast.LENGTH_SHORT).show();
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
        public void onFailure(Call<LogFromServerDao> call, Throwable t) {
            Toast.makeText(Contextor.getInstance().getContext(),
                    t.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    okhttp3.Callback responseCallbackUpFile = new okhttp3.Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {
            Log.e(TAG, "onFailure: ", e);
        }

        @Override
        public void onResponse(okhttp3.Call call, okhttp3.Response response) {
            if (response.isSuccessful()) {
                try {
                    Log.i(TAG, "UpFile Completed: " + response.body().string());
                } catch (IOException e) {
                    Log.e(TAG, "UpFile Error: ", e);
                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, "onResponse: ");
            }
        }
    };
}
