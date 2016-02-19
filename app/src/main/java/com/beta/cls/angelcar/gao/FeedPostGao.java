package com.beta.cls.angelcar.gao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by humnoy on 22/1/59.
 */

@Parcel
public class FeedPostGao {
    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("cartype")
    @Expose
    String carType;

    @SerializedName("cartype_sub")
    @Expose
    String carTypeSub;

    @SerializedName("cardetail_sub")
    @Expose
    String carDetailSub;

    @SerializedName("cardetail")
    @Expose
    String carDetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
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

    public String getCarDetail() {
        return carDetail;
    }

    public void setCarDetail(String carDetail) {
        this.carDetail = carDetail;
    }
}
