package com.beta.cls.angelcar.gao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by humnoy on 22/1/59.
 */
public class FeedPostCollectionGao {
    @SerializedName("rows")
    @Expose
    List<FeedPostGao> rows;

    public List<FeedPostGao> getRows() {
        return rows;
    }
    public void setRows(List<FeedPostGao> rows) {
        this.rows = rows;
    }
}
