package com.beta.cls.angelcar.util;

/***************************************
 * สร้างสรรค์ผลงานดีๆ
 * โดย humnoy Android Developer
 * ลงวันที่ 3/3/59. เวลา 15:21
 ***************************************/
public class FormatMessage {

    String message;
    public FormatMessage(UserChat userChat) {
            this.message = userChat.message;
    }

    public String getMessage() {
        return message;
    }

    public static class UserChat {

        private String message;
        public UserChat newMessage(String id, String userId, String msg, String userType) {
            message = String.format("%s||%s||%s||%s",id,userId,msg,userType);
            return this;
        }
        public UserChat viewMessage(String id, String userId, int lastId) {
            message = String.format("%s||%s||%d",id,userId,lastId);
            return this;
        }

        public FormatMessage build(){
            return new FormatMessage(this);
        }
    }


}
