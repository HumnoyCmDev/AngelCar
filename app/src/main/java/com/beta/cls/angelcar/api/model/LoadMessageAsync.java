package com.beta.cls.angelcar.api.model;

import android.os.AsyncTask;

import com.beta.cls.angelcar.interfaces.AsyncResult;
import com.beta.cls.angelcar.interfaces.AsyncResultChat;
import com.google.gson.Gson;
import com.hndev.library.util.MessageAPI;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by humnoy on 25/1/59.
 */
@Deprecated
public class LoadMessageAsync extends AsyncTask<MessageAPI, Void, String> {
    private boolean isSucceed = false;
    private AsyncResultChat resultChat = null;
    private AsyncResult result = null;

    public LoadMessageAsync(AsyncResultChat resultChat) {
        this.resultChat = resultChat;
    }
    public LoadMessageAsync(AsyncResult result) {
        this.result = result;
    }

    @Override
    protected String doInBackground(MessageAPI... params) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(params[0].getURL()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                isSucceed = true;
                return response.body().string();
            } else {
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
        if (isSucceed && s != null) {
            if (resultChat!=null) {
                Gson gson = new Gson();
                PostBlogMessage postBlogMessage = gson.fromJson(s, PostBlogMessage.class);
                resultChat.onSucceed(postBlogMessage);
            }

            if (result != null){
                result.onSucceed(s);
            }

        } else {
            if (resultChat != null) resultChat.onFail();
            if (result != null) result.onFail();
        }
    }
}
