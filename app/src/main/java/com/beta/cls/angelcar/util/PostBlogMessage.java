package com.beta.cls.angelcar.util;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by humnoy on 25/1/59.
 */
public class PostBlogMessage {

    @Expose
    List<BlogMessage> message;

    public List<BlogMessage> getMessage() {
        return message;
    }

    public void setMessage(List<BlogMessage> message) {
        this.message = message;
    }
}
