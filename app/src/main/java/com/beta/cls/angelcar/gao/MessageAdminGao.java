
package com.beta.cls.angelcar.gao;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageAdminGao {

    @SerializedName("messagefromcarid")
    @Expose
    private String messageFromCarId;

    @SerializedName("message")
    @Expose
    private List<MessageGao> message = new ArrayList<MessageGao>();

    public String getMessageFromCarId() {
        return messageFromCarId;
    }

    public void setMessageFromCarId(String messageFromCarId) {
        this.messageFromCarId = messageFromCarId;
    }

    public List<MessageGao> getMessage() {
        return message;
    }

    public void setMessage(List<MessageGao> message) {
        this.message = message;
    }

    // Convert MessageAdmin To MessageCollectionGao
    public MessageCollectionGao convertToMessageCollectionGao() {
        MessageCollectionGao gao = new MessageCollectionGao();
        gao.setMessage(message);
        return gao;
    }
}
