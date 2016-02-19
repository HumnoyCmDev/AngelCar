
package com.beta.cls.angelcar.gao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MessageGao {

    @SerializedName("messageid")
    @Expose
    private String messageId;
    @SerializedName("messagecarid")
    @Expose
    private String messageCarId;
    @SerializedName("messagefromuser")
    @Expose
    private String messageFromUser;
    @SerializedName("messagetext")
    @Expose
    private String messageText;
    @SerializedName("displayname")
    @Expose
    private String displayName;
    @SerializedName("messageby")
    @Expose
    private String messageBy;
    @SerializedName("userprofileimage")
    @Expose
    private String userProfileImage;
    @SerializedName("messagestamp")
    @Expose
    private Date messagesTamp;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageCarId() {
        return messageCarId;
    }

    public void setMessageCarId(String messageCarId) {
        this.messageCarId = messageCarId;
    }

    public String getMessageFromUser() {
        return messageFromUser;
    }

    public void setMessageFromUser(String messageFromUser) {
        this.messageFromUser = messageFromUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMessageBy() {
        return messageBy;
    }

    public void setMessageBy(String messageBy) {
        this.messageBy = messageBy;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public Date getMessagesTamp() {
        return messagesTamp;
    }

    public void setMessagesTamp(Date messagesTamp) {
        this.messagesTamp = messagesTamp;
    }
}
