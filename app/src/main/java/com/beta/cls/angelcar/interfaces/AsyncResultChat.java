package com.beta.cls.angelcar.interfaces;

import com.beta.cls.angelcar.api.model.PostBlogMessage;


/**
 * Created by humnoy on 22/1/59.
 */
public interface AsyncResultChat {
    void onSucceed(PostBlogMessage messages);
    void onFail();
}