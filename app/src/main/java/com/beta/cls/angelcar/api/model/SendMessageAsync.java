package com.beta.cls.angelcar.api.model;

import android.os.AsyncTask;

import com.beta.cls.angelcar.manager.AsyncSucceed;
import com.beta.cls.angelcar.api.SendMessageAPI;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by humnoy on 25/1/59.
 */
public class SendMessageAsync extends AsyncTask<SendMessageAPI,Void,String>{
    private boolean isSucceed = false;
    private AsyncSucceed asyncSucceed;

    public SendMessageAsync() {
    }
    public SendMessageAsync(AsyncSucceed asyncSucceed) {
        this.asyncSucceed = asyncSucceed;
    }

    @Override
    protected String doInBackground(SendMessageAPI... params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(params[0].getURL()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()){
                isSucceed = true;
            }else{
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
        if (asyncSucceed != null) {
            if (isSucceed)
                asyncSucceed.onSucceed();
            else
                asyncSucceed.onFail();

        }
    }
}
