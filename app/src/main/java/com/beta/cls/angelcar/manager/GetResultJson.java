package com.beta.cls.angelcar.manager;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by humnoy on 22/1/59.
 */
public class GetResultJson extends AsyncTask<String,Void,String>{
    private boolean isSucceed = false;
    private AsyncResult asyncResult;

    public GetResultJson(AsyncResult asyncResult) {
        this.asyncResult = asyncResult;
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(params[0]).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()){
                isSucceed = true;
                return response.body().string();
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
        if (isSucceed && s != null){
            Gson gson = new Gson();
            asyncResult.onSucceed(s);
        }else {
            asyncResult.onFail();
        }

    }
}
