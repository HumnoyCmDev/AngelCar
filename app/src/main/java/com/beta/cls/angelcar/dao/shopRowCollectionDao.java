package com.beta.cls.angelcar.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by humnoyDeveloper on 28/3/59. 15:19
 */
public class ShopRowCollectionDao{
    @SerializedName("rows") @Expose List<ShopCollectionDao> rows;

    public List<ShopCollectionDao> getRows() {
        return rows;
    }

    public void setRows(List<ShopCollectionDao> rows) {
        this.rows = rows;
    }

    public class ShopCollectionDao {
        @SerializedName("Profile") @Expose  List<ProfileDao> profileDao;
        @SerializedName("Car") @Expose  List<PostCarDao> postCarDao;

        public List<ProfileDao> getProfileDao() {
            return profileDao;
        }

        public void setProfileDao(List<ProfileDao> profileDao) {
            this.profileDao = profileDao;
        }

        public List<PostCarDao> getPostCarDao() {
            return postCarDao;
        }

        public void setPostCarDao(List<PostCarDao> postCarDao) {
            this.postCarDao = postCarDao;
        }
    }
}


