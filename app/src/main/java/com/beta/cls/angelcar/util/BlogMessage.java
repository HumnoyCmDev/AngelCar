package com.beta.cls.angelcar.util;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

/**
 * Created by humnoy on 25/1/59.
 */
@Parcel
public class BlogMessage {
    @Expose  String messageid;//": "113",
    @Expose  String messagecarid;//": "26",
    @Expose  String messagefromuser;//": "2016010700001",
    @Expose  String messagetext;//": "hello",
    @Expose  String displayname;//": "Customer Name",
    @Expose  String messageby;//": "user",
    @Expose  String userprofileimage;//": "http://cls.paiyannoi.me/profileimages/default.png",
    @Expose  String messagestamp;//": "2016-01-09 09:19:51"

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getMessagecarid() {
        return messagecarid;
    }

    public void setMessagecarid(String messagecarid) {
        this.messagecarid = messagecarid;
    }

    public String getMessagefromuser() {
        return messagefromuser;
    }

    public void setMessagefromuser(String messagefromuser) {
        this.messagefromuser = messagefromuser;
    }

    public String getMessagetext() {
        return messagetext;
    }

    public void setMessagetext(String messagetext) {
        this.messagetext = messagetext;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getMessageby() {
        return messageby;
    }

    public void setMessageby(String messageby) {
        this.messageby = messageby;
    }

    public String getUserprofileimage() {
        return userprofileimage;
    }

    public void setUserprofileimage(String userprofileimage) {
        this.userprofileimage = userprofileimage;
    }

    public String getMessagestamp() {
        return messagestamp;
    }

    public void setMessagestamp(String messagestamp) {
        this.messagestamp = messagestamp;
    }
}
