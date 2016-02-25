package com.beta.cls.angelcar.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by humnoy on 24/2/59.
 */
public class EncodeImageAsync extends AsyncTask<Void,Integer,Void>{

    private static final String TAG = "EncodeImageAsync";

    Bitmap bitmap;
    String encodedString;
    String nameImage;
    Uri fileUri;
    File file;
    ProgressDialog dialog;

    public EncodeImageAsync(Context context, File file) {
        this.file = file;
        fileUri = Uri.fromFile(file);
        nameImage = file.getName();
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("Upload Photo, please wait.");
        dialog.setMax(100);
        dialog.setCancelable(true);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setProgress(0);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        bitmap = BitmapFactory.decodeFile(fileUri.getPath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] array = stream.toByteArray();
        encodedString = Base64.encodeToString(array, 0);

//        int progress = 0;
//        int bytesRead = 0;
//        byte buf[] = new byte[1024];
//        try {
//            BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(file));
//            while ((bytesRead = bufInput.read(buf)) != -1){
//                progress += bytesRead;
//                publishProgress(progress);
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return null;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        dialog.setProgress(values[0]);
        Log.i(TAG, "onProgressUpdate: "+values[0]);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        request();
    }

    private void request() {
        RequestQueue requestQueue =
                Volley.newRequestQueue(Contextor.getInstance().getContext());
        StringRequest request = new StringRequest(Request.Method.POST, "http://www.usedcar.co.th/imgupload.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("encoded_string",encodedString);
                map.put("image_name","volley_"+nameImage);
                return map;
            }
        };
        requestQueue.add(request);
    }
}
