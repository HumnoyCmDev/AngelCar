package com.beta.cls.angelcar.api.model;

import android.os.AsyncTask;

import com.beta.cls.angelcar.interfaces.CallBackResult;
import com.hndev.library.util.SendMessageAPI;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by humnoy on 25/1/59.
 */
@Deprecated
public class SendMessageAsync extends AsyncTask<SendMessageAPI,Void,String>{
    private boolean isSucceed = false;
    private CallBackResult callBackResult;

    public SendMessageAsync() {
    }
    public SendMessageAsync(CallBackResult callBackResult) {
        this.callBackResult = callBackResult;
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
        if (callBackResult != null) {
            if (isSucceed)
                callBackResult.onSucceed();
            else
                callBackResult.onFail();

        }
    }
}
