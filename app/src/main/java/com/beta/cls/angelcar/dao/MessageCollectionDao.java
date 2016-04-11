
package com.beta.cls.angelcar.dao;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class MessageCollectionDao {
    @SerializedName("message")
    @Expose
    public List<MessageDao> listMessage = new ArrayList<MessageDao>();
    public List<MessageDao> getListMessage() {
        return listMessage;
    }
    public void setListMessage(List<MessageDao> listMessage) {
        this.listMessage = listMessage;
    }

}
