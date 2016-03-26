
package com.beta.cls.angelcar.dao;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PictureAllCollectionDao {

    @SerializedName("rows") @Expose
    private List<PictureAllDao> rows = new ArrayList<PictureAllDao>();

    public List<PictureAllDao> getRows() {
        return rows;
    }

    public void setRows(List<PictureAllDao> rows) {
        this.rows = rows;
    }

}
