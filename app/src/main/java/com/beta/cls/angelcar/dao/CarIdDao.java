package com.beta.cls.angelcar.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by humnoyDeveloper on 23/3/59. 16:59
 */
public class CarIdDao {
    @SerializedName("allcarid") @Expose String allCarId;

    public String getAllCarId() {
        return allCarId;
    }

    public void setAllCarId(String allCarId) {
        this.allCarId = allCarId;
    }
}
