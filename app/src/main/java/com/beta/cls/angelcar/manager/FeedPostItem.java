package com.beta.cls.angelcar.manager;

import com.google.gson.annotations.SerializedName;

/**
 * Created by humnoy on 22/1/59.
 */
public class FeedPostItem {

    String id;
    String cartype;

    @SerializedName("cartype_sub")
    String carTypeSub;

    @SerializedName("cardetail_sub")
    String carDetailSub;

    String cardetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getCarTypeSub() {
        return carTypeSub;
    }

    public void setCarTypeSub(String carTypeSub) {
        this.carTypeSub = carTypeSub;
    }

    public String getCarDetailSub() {
        return carDetailSub;
    }

    public void setCarDetailSub(String carDetailSub) {
        this.carDetailSub = carDetailSub;
    }

    public String getCardetail() {
        return cardetail;
    }

    public void setCardetail(String cardetail) {
        this.cardetail = cardetail;
    }
}
