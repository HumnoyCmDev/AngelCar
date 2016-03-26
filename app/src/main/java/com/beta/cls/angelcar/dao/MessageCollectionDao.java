
package com.beta.cls.angelcar.dao;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageCollectionDao {
    @SerializedName("message")
    @Expose
    private List<MessageDao> message = new ArrayList<MessageDao>();
    public List<MessageDao> getMessage() {
        return message;
    }
    public void setMessage(List<MessageDao> message) {
        this.message = message;
    }

}
