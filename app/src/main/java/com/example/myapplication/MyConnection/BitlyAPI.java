package com.example.myapplication.MyConnection;

import com.example.myapplication.ShortlinkDataModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BitlyAPI {


    @Headers({
            "Authorization: Bearer 37cf8aceb93bd84c66c0092f0e4aa10c0738bf8b",
            "Content-Type: application/json"
    })


    @POST("/v4/shorten" )
    Call<ResponseBody> GETSHORTLINK(@Body ShortlinkDataModel shortlinkDataModel);


}

