package com.beta.cls.angelcar.gao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarDetailGao {

    @SerializedName("cardetail_sub")
    @Expose
    String carDetailSub;

    public String getCarDetailSub() {
        return carDetailSub;
    }

    public void setCarDetailSub(String carDetailSub) {
        this.carDetailSub = carDetailSub;
    }
}
