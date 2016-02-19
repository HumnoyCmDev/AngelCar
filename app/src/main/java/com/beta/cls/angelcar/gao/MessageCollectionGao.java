
package com.beta.cls.angelcar.gao;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageCollectionGao {

    @SerializedName("message")
    @Expose
    private List<MessageGao> message = new ArrayList<MessageGao>();
    public List<MessageGao> getMessage() {
        return message;
    }
    public void setMessage(List<MessageGao> message) {
        this.message = message;
    }

}
