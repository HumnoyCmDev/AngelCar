package com.beta.cls.angelcar.manager;

/**
 * Created by humnoy on 22/1/59.
 */
public class ChatModel {
    public static final int TYPE_MY_TALK = 0;
    public static final int TYPE_TALK = 1;

    private String data;
    private int type;

    public ChatModel(String data, int type) {
        this.data = data;
        this.type = type;
    }

    public ChatModel() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
