package ir.iamnovinfar.Shorten_link.MyConnection;


import ir.iamnovinfar.Shorten_link.Model.PostModel.ShortLinkPostModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Shorte {

//    @Headers({
//            "Accept:application/json",
//            "Authorization:Bearer 36|8BJuV1wvOUXakXm8iYZm9drfhsrCUFHD40s2nfAr"
//    })

    @POST("/api/v2/link")
    Call<ResponseBody> GETSHORTLINK(@Body ShortLinkPostModel shortLinkPostModel);


}

