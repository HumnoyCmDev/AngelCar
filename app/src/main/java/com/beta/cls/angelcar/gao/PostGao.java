package com.beta.cls.angelcar.gao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by humnoy on 24/2/59.
 */
@Parcel
public class PostGao {

    @SerializedName("usecerid")         @Expose String useCarId;
    @SerializedName("userid")           @Expose String userId;
    @SerializedName("accountname")      @Expose String accountName;
    @SerializedName("cartypemain")      @Expose String carTypeMain;
    @SerializedName("cartypesub")       @Expose String carTypeSub;
    @SerializedName("cartypesubdetail") @Expose String carTypeSubDetail;
    @SerializedName("gear")             @Expose String gear;
    @SerializedName("caryear")          @Expose String carYear;
    @SerializedName("platecar")         @Expose String plateCar;
    @SerializedName("province")         @Expose String province;
    @SerializedName("posttopic")        @Expose String postTopic;
    @SerializedName("postdetail")       @Expose String postDetail;
    @SerializedName("postname")         @Expose String postName;
    @SerializedName("posttel")          @Expose String postTel;
    @SerializedName("statusname")       @Expose String statusName;

    public String getUseCarId() {
        return useCarId;
    }

    public void setUseCarId(String useCarId) {
        this.useCarId = useCarId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCarTypeMain() {
        return carTypeMain;
    }

    public void setCarTypeMain(String carTypeMain) {
        this.carTypeMain = carTypeMain;
    }

    public String getCarTypeSub() {
        return carTypeSub;
    }

    public void setCarTypeSub(String carTypeSub) {
        this.carTypeSub = carTypeSub;
    }

    public String getCarTypeSubDetail() {
        return carTypeSubDetail;
    }

    public void setCarTypeSubDetail(String carTypeSubDetail) {
        this.carTypeSubDetail = carTypeSubDetail;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getPlateCar() {
        return plateCar;
    }

    public void setPlateCar(String plateCar) {
        this.plateCar = plateCar;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostTopic() {
        return postTopic;
    }

    public void setPostTopic(String postTopic) {
        this.postTopic = postTopic;
    }

    public String getPostDetail() {
        return postDetail;
    }

    public void setPostDetail(String postDetail) {
        this.postDetail = postDetail;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostTel() {
        return postTel;
    }

    public void setPostTel(String postTel) {
        this.postTel = postTel;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
