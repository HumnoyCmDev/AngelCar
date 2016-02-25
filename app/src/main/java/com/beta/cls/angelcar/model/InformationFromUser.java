package com.beta.cls.angelcar.model;

import org.parceler.Parcel;

/**
 * Created by humnoy on 20/2/59.
 */
@Parcel
public class InformationFromUser {
    private String brand;
    private String typeSub;
    private String typeSubDetail;
    private int year;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTypeSub() {
        return typeSub;
    }

    public void setTypeSub(String typeSub) {
        this.typeSub = typeSub;
    }

    public String getTypeSubDetail() {
        return typeSubDetail;
    }

    public void setTypeSubDetail(String typeSubDetail) {
        this.typeSubDetail = typeSubDetail;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
