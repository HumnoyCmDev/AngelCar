package com.beta.cls.angelcar.manager;

import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by humnoy on 22/1/59.
 */
public class PostResultJson extends AsyncTask<String,Void,String>{

    private AsyncResult asyncResult;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public PostResultJson(AsyncResult asyncResult) {
        this.asyncResult = asyncResult;
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, "{name':'Bowling',...}");
        Request request = new Request.Builder()
                .url(params[0])
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
