package com.beta.cls.angelcar.manager;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.beta.cls.angelcar.activity.DetailCarActivity;
import com.beta.cls.angelcar.dao.PostCarCollectionDao;
import com.beta.cls.angelcar.dao.PostCarDao;

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by humnoyDeveloper on 25/3/59. 09:26
 */
// โหลดโมเดล รถ
public class CallbackLoadCarModel implements Callback<PostCarCollectionDao> {
    String massageFromUser;
    Context context;
    public CallbackLoadCarModel(Context context, String massageFromUser) {
        this.context = context;
        this.massageFromUser = massageFromUser;
    }
    @Override
    public void onResponse(Call<PostCarCollectionDao> call, Response<PostCarCollectionDao> response) {

        if (response.isSuccessful()) {
            PostCarDao item = response.body().getListCar().get(0);
            Intent intent = new Intent(context, DetailCarActivity.class);
            intent.putExtra("PostCarDao", Parcels.wrap(item));
            intent.putExtra("intentForm", 1);
            intent.putExtra("messageFromUser",massageFromUser);
            context.startActivity(intent);
        } else {
            Toast.makeText(context,
                    response.errorBody().toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<PostCarCollectionDao> call, Throwable t) {
        Toast.makeText(context,
                t.toString(),
                Toast.LENGTH_SHORT).show();
    }
}
