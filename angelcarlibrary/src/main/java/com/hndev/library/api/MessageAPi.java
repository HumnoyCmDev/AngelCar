package com.hndev.library.api;

/**
 * Created by humnoy on 30/1/59.
 */
public class MessageAPi {
    private String url;

    private MessageAPi() {}

    private MessageAPi(ViewMessageBuilder viewMessageBuilder) {
        this.url = viewMessageBuilder.url+
                viewMessageBuilder.id + "||" +
                viewMessageBuilder.idUser + "||" +
                viewMessageBuilder.idMessage;
    }

    private MessageAPi(ViewMessageOutBuilder viewMessageOutBuilder) {
        this.url = viewMessageOutBuilder.url;
    }

    private MessageAPi(ViewMessageInBuilder viewMessageInBuilder) {
        this.url = viewMessageInBuilder.url;
    }

    private MessageAPi(SendMessageBuilder sendMessageBuilder) {
        if (sendMessageBuilder.id == null) throw new NullPointerException("id Do not as the empty");
        if (sendMessageBuilder.idUser == null)
            throw new NullPointerException("idUser Do not as the empty");
        if (sendMessageBuilder.typeFrom == null)
            throw new NullPointerException("tyeFrom Do not as the empty");

        this.url = sendMessageBuilder.url+
                sendMessageBuilder.id + "||" +
                sendMessageBuilder.idUser + "||" +
                sendMessageBuilder.message + "||" +
                sendMessageBuilder.typeFrom;
    }

    public String getUrl() {
        return url;
    }

    // builder
    public static class ViewMessageBuilder {
        private String url = "http://api.paiyannoi.me/ga_chatcar.php?operation=view&message=";
        private String id;
        private String idUser;
        private String idMessage;

        public ViewMessageBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public ViewMessageBuilder setIdUser(String idUser) {
            this.idUser = idUser;
            return this;
        }

        public ViewMessageBuilder setIdMessage(String idMessage) {
            this.idMessage = idMessage;
            return this;
        }

        public MessageAPi build() {
            return new MessageAPi(this);
        }

    }

    public static class ViewMessageOutBuilder {
        private String url;

        public ViewMessageOutBuilder setMessage(String messagefromuser) {
            url = "http://api.paiyannoi.me/ga_chatcar.php?operation=viewclient&message=" +
                    messagefromuser;
            return this;
        }

        public MessageAPi build() {
            return new MessageAPi(this);
        }
    }

    public static class ViewMessageInBuilder {
        private String url;

        public ViewMessageInBuilder setMessage(String messagecarid) {
            url = "http://api.paiyannoi.me/ga_chatcar.php?operation=viewadminarray&message=" +
                    messagecarid;
            return this;
        }

        public MessageAPi build() {
            return new MessageAPi(this);
        }

    }

    public static class SendMessageBuilder {
        private String url = "http://api.paiyannoi.me/ga_chatcar.php?operation=new&message=";
        private String id;
        private String idUser;
        private String message;
        private String typeFrom;

        public SendMessageBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public SendMessageBuilder setIdUser(String idUser) {
            this.idUser = idUser;
            return this;
        }

        public SendMessageBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public SendMessageBuilder setTypeFrom(String typeFrom) {
            this.typeFrom = typeFrom;
            return this;
        }

        public MessageAPi build() {
            return new MessageAPi(this);
        }
    }


}
