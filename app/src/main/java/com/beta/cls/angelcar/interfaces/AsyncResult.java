package com.beta.cls.angelcar.interfaces;

import com.google.gson.Gson;

/**
 * Created by humnoy on 22/1/59.
 */
public interface AsyncResult {
    void onSucceed(String s);
    void onFail();
}
