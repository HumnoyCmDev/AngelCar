
package com.beta.cls.angelcar.gao;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MessageAdminCollectionGao {

    @SerializedName("messageviewbyadminaray")
    @Expose
    private List<MessageAdminGao> messageAdmin = new ArrayList<MessageAdminGao>();

    public List<MessageAdminGao> getMessageAdmin() {
        return messageAdmin;
    }

    public void setMessageAdmin(List<MessageAdminGao> messageAdmin) {
        this.messageAdmin = messageAdmin;
    }
}
