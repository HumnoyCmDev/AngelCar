package com.beta.cls.angelcar.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.beta.cls.angelcar.utils.LineUp;
import com.squareup.otto.Subscribe;

import org.parceler.apache.commons.lang.StringUtils;

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
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final int REQUEST_CODE_LOAD_IMAGE = 191;
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

    int idPhoto = 0;
    int id_province = 1;
    HashMap<Integer, File> filesPhotoList;

    private InformationFromUser user;
//    ProgressDialog progress;

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
//        progress = new ProgressDialog(getActivity());
//        progress.setMessage("UpLoad...");
//        progress.setCancelable(false);
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

//        if (savedInstanceState == null) {
//            initData();
            initInstance();
            initDataProvince();

//        }
//
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
               editTextDescription.getText().toString()).trim(); // ชื่อสั้นๆ
       int carYear = user.getYear(); // ปีรถ
       String carPrice = editTextPrice.getText().toString().trim();// ราคารถ
       String carStatus = "wait";//wait,online,offline
       String province = String.valueOf(id_province).trim(); // 1 - 77
       String gear = tgGear.isChecked() ? "1":"2"; // 0 or 1
       String plate = editTextRegister.getText().toString().trim(); // text ทะเบียนน
       String name = editTextName.getText().toString().trim(); // ชื่อ นามสกุล


        //isEmpty = true หากมีค่าว่าง

        if (filesPhotoList.size() < 3) {
            Toast.makeText(getContext(), "กรุณาใส่รูปมากกว่า 3", Toast.LENGTH_SHORT).show();
            return;
        }

        if (shopPref.isEmpty() || carName.isEmpty()
                || carDetail.isEmpty() || carPrice.isEmpty()
                || province.isEmpty() || gear.isEmpty()
                || plate.isEmpty() || name.isEmpty()){
                Toast.makeText(getContext(),"กรุณากรอกข้อมูลให้ครบ!",Toast.LENGTH_SHORT).show();
            return;
        }


            Call<LogFromServerDao> call = HttpManager.getInstance().getService().postCar(
            "insert", shopPref, carName, carDetail, carYear, carPrice, carStatus, province, gear, plate, name);
            call.enqueue(postCallback);
            OnSelectData onSelectData = (OnSelectData) getActivity();
            onSelectData.onSelectedCallback(PostActivity.CALLBACK_ALL_POST);

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
        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            if (Build.VERSION.SDK_INT >= 18){
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    if (i < 8) {
                        Uri uri = clipData.getItemAt(i).getUri();
                        Cursor cursor = getActivity().getContentResolver()
                                .query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        assert cursor != null;
                        cursor.moveToFirst();
                        int id = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(id);
                        cursor.close();
                        // add path
                        addFilesPhotoList(i,picturePath);
                    }
                }
            }else {
                Uri selectedImage = data.getData();
                Cursor cursor = getActivity().getContentResolver()
                        .query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                // add path
                addFilesPhotoList(idPhoto,picturePath);
            }
        }

    }

    private void addFilesPhotoList(int newIdPhoto,String picturePath){
        // list photo
        filesPhotoList.put(idPhoto+newIdPhoto,new File(picturePath));
        photo.get(idPhoto+newIdPhoto).setImageBitmap(
                decodeFile(filesPhotoList.get(idPhoto+newIdPhoto)));
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
            case R.id.post_photo_1 : idPhoto = 0 ; break ;
            case R.id.post_photo_2 : idPhoto = 1 ; break ;
            case R.id.post_photo_3 : idPhoto = 2 ; break ;
            case R.id.post_photo_4 : idPhoto = 3 ; break ;
            case R.id.post_photo_5 : idPhoto = 4 ; break ;
            case R.id.post_photo_6 : idPhoto = 5 ; break ;
            case R.id.post_photo_7 : idPhoto = 6 ; break ;
            case R.id.post_photo_8 : idPhoto = 7 ; break ;
        }
        addOrRemovePhotoList(idPhoto);
    }

    private void addOrRemovePhotoList(int id_photo){
        // เช็คกรณี หากมีรูปอยู่แล้ว กดอีกครั้งให้ลบออก
        if (filesPhotoList.containsKey(id_photo)){
            filesPhotoList.remove(id_photo);
            photo.get(id_photo).setImageResource(R.drawable.photo);
        }else{
            if (!checkPermissionApi23()){ //ต่ำกว่า Android 23
                intentLoadPictureExternalStore();
            }
        }
    }

    /*Permission*/
    private boolean checkPermissionApi23(){
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //กรณีไม่ให้สิทธิ์// แสดงรายการคำขอ ผลหากไม่ให้สิท ขอ Permission ผ่าน Dialog
                    showMessageOKCancel("AngelCar ต้องการขอสิทธิ์ในการเข้าถึงรูปภาพ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    });

                } else {
                    // ขอสิทธิ์เข้าถึง
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);

                }
            }else {
                intentLoadPictureExternalStore();
            }
            return true;
        }
            return false;
    }

    private void intentLoadPictureExternalStore(){
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (Build.VERSION.SDK_INT >= 18)
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        startActivityForResult(i, REQUEST_CODE_LOAD_IMAGE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

   /* //TODO Method ไม่ทำงาน
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: "+requestCode);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intentLoadPictureExternalStore();
                    Log.i(TAG, "onRequestPermissionsResult: true");

                }
                break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/

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
    }

    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 50;
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
