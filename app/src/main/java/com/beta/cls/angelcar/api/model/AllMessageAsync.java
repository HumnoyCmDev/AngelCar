package com.beta.cls.angelcar.api.model;

import android.os.AsyncTask;

import com.beta.cls.angelcar.api.MessageAPI;
import com.beta.cls.angelcar.util.LoggerFactory;
import com.beta.cls.angelcar.util.MessageAPi;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by humnoy on 25/1/59.
 */
public class AllMessageAsync extends AsyncTask<Void, Void, Void> {
    private CallBackResult result = null;

    PostBlogArrayMessage blogArrayMessage;
    PostBlogMessage postBlogMessage;

    public AllMessageAsync(CallBackResult result) {
        this.result = result;
    }

    @Override
    protected Void doInBackground(Void... params) {


        MessageAPi putApi = new MessageAPi.ViewMessageInBuilder().setMessage("26").build();
        MessageAPi outApi = new MessageAPi.ViewMessageOutBuilder().setMessage("2015062900001").build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();

        Request request = builder.url(putApi.getUrl()).build();
        Request request2 = builder.url(outApi.getUrl()).build();

        Gson gson = new Gson();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                blogArrayMessage = gson.fromJson(response.body().string(), PostBlogArrayMessage.class);

            }

            Response response2 = okHttpClient.newCall(request2).execute();
            if (response2.isSuccessful()) {
             postBlogMessage = gson.fromJson(response2.body().string(), PostBlogMessage.class);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void s) {
        super.onPostExecute(s);
            result.onPostResult(blogArrayMessage,postBlogMessage);
    }

    public interface CallBackResult{
        void onPostResult(PostBlogArrayMessage blogArrayMessage,PostBlogMessage postBlogMessage);
    }
}

