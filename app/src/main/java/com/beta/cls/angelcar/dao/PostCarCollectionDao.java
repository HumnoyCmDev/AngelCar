package com.beta.cls.angelcar.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by humnoy on 10/3/59.
 */
@Parcel
public class PostCarCollectionDao {
    @SerializedName("rows")
    @Expose
    List<PostCarDao> listCar;

    public List<PostCarDao> getListCar() {
        return listCar;
    }

    public void setListCar(List<PostCarDao> rows) {
        this.listCar = rows;
    }

}
