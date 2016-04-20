package com.beta.cls.angelcar.model;

import android.graphics.drawable.Drawable;

import com.beta.cls.angelcar.dao.CarBrandDao;
import com.beta.cls.angelcar.dao.CarSubDao;

import org.parceler.Parcel;

/**
 * Created by humnoyDeveloper on 19/4/59. 13:45
 */
@Parcel
public class FilterCarModel {

    int resIdLogo; //logo
    int year;
    String gear = "0"; // all = 0 , mt = 1 , at = 2
    //brand
    CarBrandDao brandDao;
    //sub
    CarSubDao subDao;
    //sub detail
    CarSubDao subDetailDao;

    public int getResIdLogo() {
        return resIdLogo;
    }

    public void setResIdLogo(int resIdLogo) {
        this.resIdLogo = resIdLogo;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public CarBrandDao getBrandDao() {
        return brandDao;
    }

    public void setBrandDao(CarBrandDao brandDao) {
        this.brandDao = brandDao;
    }

    public CarSubDao getSubDao() {
        return subDao;
    }

    public void setSubDao(CarSubDao subDao) {
        this.subDao = subDao;
    }

    public CarSubDao getSubDetailDao() {
        return subDetailDao;
    }

    public void setSubDetailDao(CarSubDao subDetailDao) {
        this.subDetailDao = subDetailDao;
    }

    public void clear(){
        brandDao = null;
        subDao = null;
        subDetailDao = null;
    }

}
