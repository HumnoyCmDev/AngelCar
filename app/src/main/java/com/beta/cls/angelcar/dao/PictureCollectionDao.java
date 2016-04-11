
package com.beta.cls.angelcar.dao;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class PictureCollectionDao {

    @SerializedName("rows") @Expose
    private List<PictureDao> listPicture = new ArrayList<PictureDao>();

    public List<PictureDao> getListPicture() {
        return listPicture;
    }

    public void setListPicture(List<PictureDao> rows) {
        this.listPicture = rows;
    }

}
