package com.hndev.library.util;



/**
 * Created by humnoy on 25/1/59.
 */
@Deprecated
public class MessageAPI {

    private static String BASE_URL = "http://api.paiyannoi.me/ga_chatcar.php?operation=%s&message=";
    private String URL;

    public MessageAPI() {

    }

    private String getBaseUrl(TypeChat typeView) {
        switch (typeView) {
            case WAIT:
                return String.format(BASE_URL, typeView.toString());
            case VIEW:
                return String.format(BASE_URL, typeView.toString());
        }
        return null;
    }

    public void message(TypeChat typeChat, String id, String idUser, String idMessage) {
        if (id == null || id.equals("")) {
            throw new NullPointerException("id is null");
        }
        if (idUser == null || idUser.equals("")) {
            throw new NullPointerException("idUser is null");
        }
        if (idMessage == null || idMessage.equals("")) {
            throw new NullPointerException("idMessage is null");
        }
        setURL(getBaseUrl(typeChat) + id + "||" + idUser + "||" + idMessage);

    }

    public void outMessage(String messagefromuser) {
        setURL(String.format(BASE_URL, "viewclient") + messagefromuser);
    }
    public void putMessage(String messagecarid) {
        setURL(String.format(BASE_URL, "viewadminarray") + messagecarid);
    }

    public String getURL() {
        return URL;
    }

    private void setURL(String URL) {
        this.URL = URL;
    }

}