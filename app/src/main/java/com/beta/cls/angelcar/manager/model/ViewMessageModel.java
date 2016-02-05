package com.beta.cls.angelcar.manager.model;

import java.util.List;

/**
 * Created by humnoy on 5/2/59.
 */
public class ViewMessageModel {
    private List<MessageEntity> message;

    public void setMessage(List<MessageEntity> message) {
        this.message = message;
    }

    public List<MessageEntity> getMessage() {
        return message;
    }

    public static class MessageEntity {
        private String messageid;
        private String messagecarid;
        private String messagefromuser;
        private String messagetext;
        private String displayname;
        private String messageby;
        private String userprofileimage;
        private String messagestamp;

        public void setMessageid(String messageid) {
            this.messageid = messageid;
        }

        public void setMessagecarid(String messagecarid) {
            this.messagecarid = messagecarid;
        }

        public void setMessagefromuser(String messagefromuser) {
            this.messagefromuser = messagefromuser;
        }

        public void setMessagetext(String messagetext) {
            this.messagetext = messagetext;
        }

        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }

        public void setMessageby(String messageby) {
            this.messageby = messageby;
        }

        public void setUserprofileimage(String userprofileimage) {
            this.userprofileimage = userprofileimage;
        }

        public void setMessagestamp(String messagestamp) {
            this.messagestamp = messagestamp;
        }

        public String getMessageid() {
            return messageid;
        }

        public String getMessagecarid() {
            return messagecarid;
        }

        public String getMessagefromuser() {
            return messagefromuser;
        }

        public String getMessagetext() {
            return messagetext;
        }

        public String getDisplayname() {
            return displayname;
        }

        public String getMessageby() {
            return messageby;
        }

        public String getUserprofileimage() {
            return userprofileimage;
        }

        public String getMessagestamp() {
            return messagestamp;
        }
    }
}
