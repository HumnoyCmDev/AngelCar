package com.beta.cls.angelcar.gao;

/**
 package com.beta.cls.angelcar;

 /**
 * Created by ABaD on 12/29/2015.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class CarDetailCollectionGao {

    @SerializedName("rows") @Expose
    List<CarDetailGao> rows;


    public List<CarDetailGao> getRows() {
        return rows;
    }

    public void setPosts(List<CarDetailGao> rows) {
        this.rows = rows;
    }
}