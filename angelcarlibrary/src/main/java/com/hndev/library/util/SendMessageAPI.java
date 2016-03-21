package com.hndev.library.util;


/**
 * Created by humnoy on 25/1/59.
 */
@Deprecated
public class SendMessageAPI {
    private static String BASE_URL = "http://api.paiyannoi.me/ga_chatcar.php?operation=%s&message=";
    private String URL;

    public SendMessageAPI(){}

    public String message(String id,String idUser,String message,String typeFrom){
        setURL(String.format(BASE_URL,"new")+id+"||"+idUser+"||"+message+"||"+typeFrom);
        return getURL();
    }

    public String getURL() {
        return URL;
    }

    private void setURL(String URL) {
        this.URL = URL;
    }
}




