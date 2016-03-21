package com.beta.cls.angelcar.gao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by humnoy on 10/3/59.
 */
@Parcel
public class PostCarCollectionGao {
    @SerializedName("rows")
    @Expose
    List<PostCarGao> rows;

    public List<PostCarGao> getRows() {
        return rows;
    }

    public void setRows(List<PostCarGao> rows) {
        this.rows = rows;
    }

}
