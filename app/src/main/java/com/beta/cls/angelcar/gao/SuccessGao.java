package com.beta.cls.angelcar.gao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by humnoy on 17/2/59.
 */
public class SuccessGao {

    @SerializedName("success")
    @Expose
    public String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
