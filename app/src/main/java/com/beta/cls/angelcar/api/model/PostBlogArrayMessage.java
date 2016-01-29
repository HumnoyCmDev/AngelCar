package com.beta.cls.angelcar.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by humnoy on 26/1/59.
 */
public class PostBlogArrayMessage {


    @SerializedName("messageviewbyadminaray")
    @Expose
    List<BlogMessageJson> messageviewbyadmin;

    public List<BlogMessageJson> getMessageViewByAdmin() {
        return messageviewbyadmin;
    }

    public void setMessageViewByAdmin(List<BlogMessageJson> messageviewbyadmin) {
        this.messageviewbyadmin = messageviewbyadmin;
    }


    public class BlogMessageJson {
        @Expose
        String messagefromcarid;
        @Expose
        List<BlogMessage> message;

        public String getMessagefromcarid() {
            return messagefromcarid;
        }

        public void setMessagefromcarid(String messagefromcarid) {
            this.messagefromcarid = messagefromcarid;
        }

        public List<BlogMessage> getMessage() {
            return message;
        }

        public void setMessage(List<BlogMessage> message) {
            this.message = message;
        }
    }

}
