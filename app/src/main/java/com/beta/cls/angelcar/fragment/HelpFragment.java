package com.beta.cls.angelcar.fragment;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.gao.SuccessGao;
import com.beta.cls.angelcar.manager.http.ApiService;
import com.beta.cls.angelcar.manager.http.HttpPostManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HelpFragment extends Fragment {

    private ImageView ivPhoto;

    public HelpFragment() {
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_help, container, false);
        findViewRootView(v);
        return v;

    }

    private static final String TAG = "HelpFragment";
    
    private void findViewRootView(View v) {
        ivPhoto = (ImageView) v.findViewById(R.id.ivPhoto);
        final FrameLayout post = (FrameLayout) v.findViewById(R.id.testAnim);
        final Button button = (Button) v.findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ApiService service = HttpPostManager.getInstance().getService();
//
//                RequestBody.create(MediaType.parse("text/plain"),"");
//
//                SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
//                String dfm = format.format(new Date());
//                Date d = null;
//                try {
//                     d = format.parse(dfm);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                Call<SuccessGao> call = service.postCar(
//                        "Toyota", "Fortuner", "1.6 v", "2016",
//                        2016, "1 ro 2", "900000", "กท 0100", "Nonthaburi", "รถใหม่ใช่น้อย",
//                        "รถใหม่ใช่น้อย เจ้าของใช้งานเอง", "นายสุวิท สกิดติ่ง", "099 999 9999", "192.168.1.1", d, "userID"
//                );
//
//                call.enqueue(new Callback<SuccessGao>() {
//                    @Override
//                    public void onResponse(Call<SuccessGao> call, Response<SuccessGao> response) {
//                        if (response.isSuccess()) {
//                            Toast.makeText(getActivity(), "Success :" + response.body().getSuccess() ,Toast.LENGTH_LONG).show();
//                        }else {
//                            try {
//                                Toast.makeText(getActivity(), "e:: "+response.errorBody().string(),Toast.LENGTH_LONG).show();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SuccessGao> call, Throwable t) {
//                        Toast.makeText(getActivity(), t.toString() ,Toast.LENGTH_LONG).show();
//
//                        Log.e(TAG, "onFailure :", t);
//                    }
//                });




                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(i, RESULT_LOAD_IMAGE);


            }
        });


//        ValueAnimator amin = ValueAnimator.ofInt(post.getMeasuredHeight(),500);
//        amin.setInterpolator(new BounceInterpolator());
//        amin.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int val = (int) animation.getAnimatedValue();
//                ViewGroup.LayoutParams params = post.getLayoutParams();
//                params.height = val;
//                post.setLayoutParams(params);
//            }
//        });
//        amin.setDuration(3000);
//        amin.start();


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    int RESULT_LOAD_IMAGE = 99;
    int RESULT_OK = -1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            File imgFile = new File(picturePath);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivPhoto.setImageBitmap(myBitmap);

            ApiService service = HttpPostManager.getInstance().getService();

            RequestBody request = RequestBody.create(MediaType.parse("multipart/form-data"),imgFile);
            RequestBody naem = RequestBody.create(MediaType.parse("multipart/form-data"),"name,jpg");

            Call<SuccessGao> call = service.uploadImage(request,naem);
            call.enqueue(new Callback<SuccessGao>() {

                @Override
                public void onResponse(Call<SuccessGao> call, Response<SuccessGao> response) {
                    if (response.isSuccess()) {
                            Toast.makeText(getActivity(), "Success :" + response.body().getSuccess() ,Toast.LENGTH_LONG).show();
                    }else {
                            try {
                                Toast.makeText(getActivity(), "e:: "+response.errorBody().string(),Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                }

                @Override
                public void onFailure(Call<SuccessGao> call, Throwable t) {
                    Toast.makeText(getActivity(), "Fail :: LogCat",Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onFailure: ", t);
                }
            });
        }
    }



}