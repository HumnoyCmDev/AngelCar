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
    List<PostCarDao> rows;

    public List<PostCarDao> getRows() {
        return rows;
    }

    public void setRows(List<PostCarDao> rows) {
        this.rows = rows;
    }

}
