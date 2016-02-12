package com.hndev.library.api;

import android.os.AsyncTask;


import java.io.IOException;
import com.hndev.library.manager.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by humnoy on 30/1/59.
 */
public class ConnectAPi extends AsyncTask<Void,Void,String>{
    private String url;
    private Callback callback;
    private boolean isSucceed = false;
    private static final String TAG = "ConnectAPi";

    public ConnectAPi(MessageAPi messageAPi,Callback callback) {
        this.url = messageAPi.getUrl();
        this.callback = callback;
        execute();
    }



    @Override
    protected String doInBackground(Void... params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                isSucceed = true;
                return response.body().string();
            }else {
                isSucceed = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.onSucceed(s,isSucceed);
//        Log.i(TAG, "onSucceed: "+s);
    }
}
