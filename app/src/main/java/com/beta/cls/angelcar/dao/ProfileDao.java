package com.beta.cls.angelcar.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by humnoyDeveloper on 28/3/59. 15:12
 */
public class ProfileDao {

    @SerializedName("shopid")           @Expose String shopId;
    @SerializedName("shopname")         @Expose String shopName;
    @SerializedName("shopdescription")  @Expose String shopDescription;
    @SerializedName("shopnumber")       @Expose String shopNumber;
    @SerializedName("shoplogo")         @Expose String shopLogo;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }
}
