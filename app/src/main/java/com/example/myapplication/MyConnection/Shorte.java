package com.example.myapplication.MyConnection;

import com.example.myapplication.Model.PostModel.ShortePostModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Shorte {


    @Headers({
            "content-type:application/x-www-form-urlencoded",
            "public-api-token:314eab5c8e36068eb03423d6cb84aee6"
    })
    @PUT("/v1/data/url")
    Call<ResponseBody> GETSHORTLINK(@Body ShortePostModel shortePostModel);


}

