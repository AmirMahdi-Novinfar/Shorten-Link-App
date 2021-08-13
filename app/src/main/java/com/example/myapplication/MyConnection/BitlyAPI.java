package com.example.myapplication.MyConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BitlyAPI {



    @GET("/api-create.php" )
    Call<ResponseBody> GETSHORTLINK(@Query("url") String url);


}

