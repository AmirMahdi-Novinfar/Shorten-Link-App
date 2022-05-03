package ir.iamnovinfar.Shorten_link.MyConnection;


import ir.iamnovinfar.Shorten_link.PostModel.ShortLinkPostModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Shorte {



    @POST("/api/link")
    Call<ResponseBody> GETSHORTLINK(@Body ShortLinkPostModel shortLinkPostModel);


}

