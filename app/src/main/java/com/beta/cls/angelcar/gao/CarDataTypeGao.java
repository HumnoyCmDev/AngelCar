package com.beta.cls.angelcar.gao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by humnoy on 16/2/59.
 */
public class CarDataTypeGao {

    @SerializedName("cartype_sub")
    @Expose
    String carTypeSub;

    public String getCarTypeSub() {
        return carTypeSub;
    }

    public void setCarTypeSub(String carTypeSub) {
        this.carTypeSub = carTypeSub;
    }
}