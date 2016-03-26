
package com.beta.cls.angelcar.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class PictureAllDao {

    @SerializedName("carimagepath")
    @Expose
    String carImagePath;

    public String getCarImagePath() {
        return carImagePath;
    }

    public void setCarImagePath(String carImagePath) {
        this.carImagePath = carImagePath;
    }
}
