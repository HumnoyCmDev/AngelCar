
package com.beta.cls.angelcar.dao;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageAdminDao {

    @SerializedName("messagefromcarid")
    @Expose
    private String messageFromCarId;

    @SerializedName("message")
    @Expose
    private List<MessageDao> message = new ArrayList<MessageDao>();

    public String getMessageFromCarId() {
        return messageFromCarId;
    }

    public void setMessageFromCarId(String messageFromCarId) {
        this.messageFromCarId = messageFromCarId;
    }

    public List<MessageDao> getMessage() {
        return message;
    }

    public void setMessage(List<MessageDao> message) {
        this.message = message;
    }

    // Convert MessageAdmin To MessageCollectionDao
    public MessageCollectionDao convertToMessageCollectionGao() {
        MessageCollectionDao gao = new MessageCollectionDao();
        gao.setMessage(message);
        return gao;
    }
}
