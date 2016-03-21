package com.beta.cls.angelcar.util;

/**
 * Created by humnoyDeveloper on 3/18/2016 AD. 14:50
 */
public class RegistrationResult {
    private int result;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RegistrationResult() {
    }

    public RegistrationResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
