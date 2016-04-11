package com.beta.cls.angelcar.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.beta.cls.angelcar.dao.PostCarCollectionDao;
import com.beta.cls.angelcar.dao.PostCarDao;
import com.google.gson.Gson;

import org.parceler.Parcels;

import java.util.ArrayList;

/***************************************
 * สร้างสรรค์ผลงานดีๆ
 * โดย humnoy Android Developer
 * ลงวันที่ 29/2/59. เวลา 11:24
 ***************************************/
public class PostCarManager {

    private Context mContext;
    private PostCarCollectionDao dao;

    public PostCarManager() {
        mContext = Contextor.getInstance().getContext();
        //Load Cache
        loadCache();
    }

    public PostCarCollectionDao getDao() {
        return dao;
    }

    public void setDao(PostCarCollectionDao messageDao) {
        this.dao = messageDao;
        //Save Cache
        saveCache();
    }

    public int getCount(){
        if (dao == null) return 0;
        if (dao.getListCar() == null) return 0;

        return dao.getListCar().size();
    }
    
    public void insertDaoAtTopPosition(PostCarCollectionDao newDao){
        if (dao == null)
            dao = new PostCarCollectionDao();
        if (dao.getListCar() == null)
            dao.setListCar(new ArrayList<PostCarDao>());
        dao.getListCar().addAll(0,newDao.getListCar());
        //Save Cache
        saveCache();
    }

    public void appendDataToBottomPosition(PostCarCollectionDao gao){
        if (dao == null){
            dao = new PostCarCollectionDao();
        }
        if (dao.getListCar() == null){
            dao.setListCar(new ArrayList<PostCarDao>());
        }
        dao.getListCar().addAll(getCount(),gao.getListCar());
        //Save Cache
        saveCache();
    }

    public int getMaximumId(){
        if (dao == null)
            return 0;
        if (dao.getListCar().size() == 0)
            return 0;
        int maxId = dao.getListCar().get(0).getCarId();
        for (int i = 0; i < dao.getListCar().size(); i++)
            maxId = Math.max(maxId, dao.getListCar().get(i).getCarId());
        return maxId;
    }

    public int getMinimumId(){
        if (dao == null)
            return 0;
        if (dao.getListCar() == null)
            return 0;
        if (dao.getListCar().size() == 0)
            return 0;

        int minId = dao.getListCar().get(0).getCarId();
        for (int i = 0; i < dao.getListCar().size(); i++)
            minId = Math.min(minId, dao.getListCar().get(i).getCarId());
        return minId;
    }

    public Bundle onSaveInstanceState(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("dao", Parcels.wrap(dao));
        return bundle;
    }

    public void onRestoreInstanceState(Bundle saveInstanceState){
        dao = Parcels.unwrap(saveInstanceState.getParcelable("dao"));
    }

    private void saveCache(){

        PostCarCollectionDao cacheDao =
                new PostCarCollectionDao();
        if (dao != null && dao.getListCar() != null)
            cacheDao.setListCar(dao.getListCar().subList(0,
                    Math.min(20, dao.getListCar().size())));

        String json = new Gson().toJson(cacheDao);

        SharedPreferences prefs =
                mContext.getSharedPreferences("postDao", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("json",json);
        edit.apply();

    }

    private void loadCache(){
        SharedPreferences prefs =
                mContext.getSharedPreferences("postDao",Context.MODE_PRIVATE);
        String json = prefs.getString("json",null);
        if (json == null)
            return;
        dao = new Gson().fromJson(json,PostCarCollectionDao.class);
    }

}
