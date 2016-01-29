package com.beta.cls.angelcar.manager;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by humnoy on 22/1/59.
 */
public class FeedBlogJson {
   //blog json
    List<FeedPostItem> rows;
    public List<FeedPostItem> getRows() {
        return rows;
    }
    public void setRows(List<FeedPostItem> rows) {
        this.rows = rows;
    }
}
