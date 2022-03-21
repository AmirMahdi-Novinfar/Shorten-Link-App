package ir.iamnovinfar.Shorten_link.MyConnection;

import ir.iamnovinfar.Shorten_link.Model.PostModel.ShortePostModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface Shorte {


    @Headers({
            "content-type:application/x-www-form-urlencoded",
            "public-api-token:314eab5c8e36068eb03423d6cb84aee6"
    })
    @PUT("/v1/data/url")
    Call<ResponseBody> GETSHORTLINK(@Body ShortePostModel shortePostModel);


}

