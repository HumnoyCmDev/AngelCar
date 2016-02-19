package com.beta.cls.angelcar.gao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by humnoy on 16/2/59.
 */
public class CarDataTypeCollectionGao {
    @SerializedName("rows")
    @Expose
    List<CarDataTypeGao> rows;

    public List<CarDataTypeGao> getRows() {
        return rows;
    }

    public void setRows(List<CarDataTypeGao> rows) {
        this.rows = rows;
    }
}
