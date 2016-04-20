package com.beta.cls.angelcar.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by humnoyDeveloper on 19/4/59. 11:53
 */
public class CarBrandCollectionDao {
    @SerializedName("rows") @Expose() private List<CarBrandDao> brandDao;

    public List<CarBrandDao> getBrandDao() {
        return brandDao;
    }

    public void setBrandDao(List<CarBrandDao> brandDao) {
        this.brandDao = brandDao;
    }
}
