package com.beta.cls.angelcar.util;

/**
 * Created by ABaD on 12/29/2015.
 */

import com.beta.cls.angelcar.manager.CarDataType;

import java.util.List;

public class TotalCarData {

    String cartype_sub;

    List<CarDataType> rows;

    public String getCartypeSub() {
        return cartype_sub;
    }

    public void setCartypeSub(String cartype_sub) {
        this.cartype_sub = cartype_sub;
    }

    public List<CarDataType> getRows() {
        return rows;
    }

    public void setPosts(List<CarDataType> rows) {
        this.rows = rows;
    }
}