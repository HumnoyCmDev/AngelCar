package com.beta.cls.angelcar.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.gao.SuccessGao;
import com.beta.cls.angelcar.manager.EncodeImageAsync;
import com.beta.cls.angelcar.manager.http.ApiService;
import com.beta.cls.angelcar.manager.http.HttpManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HelpFragment extends Fragment {

    private ImageView ivPhoto;
    String picturePath;

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

                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(i, RESULT_LOAD_IMAGE);


            }
        });


        Button up = (Button) v.findViewById(R.id.button_upload);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Up load By Retrofit",Toast.LENGTH_SHORT).show();
         new AsyncTask<Void,Void,Void>(){
             String encoded_string;
             @Override
             protected Void doInBackground(Void... params) {
                 File f = new File(picturePath);
                 Uri uri = Uri.fromFile(f);
                 Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
                 ByteArrayOutputStream stream = new ByteArrayOutputStream();
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                 byte[] array = stream.toByteArray();
                 encoded_string = Base64.encodeToString(array,Base64.DEFAULT);

                 ApiService service = HttpManager.getInstance().getService();

                 RequestBody request = RequestBody.create(MediaType.parse("text/plain"),encoded_string);
                 RequestBody request2 = RequestBody.create(MediaType.parse("text/plain"),"retrofit_"+f.getName());

                 try {
                     service.uploadImage(request,request2).execute();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 return null;
             }

             @Override
             protected void onPostExecute(Void aVoid) {
                 super.onPostExecute(aVoid);
                    Toast.makeText(getActivity(),"Success",Toast.LENGTH_SHORT).show();
             }
         }.execute();

            }
        });


        Button up2 = (Button) v.findViewById(R.id.button_upload2);
        up2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EncodeImageAsync(getActivity(),new File(picturePath)).execute();
                Toast.makeText(getActivity(),"Up load Volley",Toast.LENGTH_SHORT).show();
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
            picturePath = cursor.getString(columnIndex);
            cursor.close();


            File imgFile = new File(picturePath);
            Uri uri = Uri.fromFile(imgFile);

            Bitmap myBitmap = BitmapFactory.decodeFile(uri.getPath());
            ivPhoto.setImageBitmap(myBitmap);

            Log.i(TAG, "onActivityResult: "+imgFile.getName());


        }
    }



}