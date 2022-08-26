package ir.iamnovinfar.Shorten_link.MyConnection;


import ir.iamnovinfar.Shorten_link.Model.PostModel.ShortLinkPostModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetLinks {

//    @Headers({
//            "Accept:application/json",
//            "Authorization:Bearer 48|UhbQ7oZ0g4G7oLmexF2FsDBPNjr4LrLqJc0SDMyE"
//    })

    @GET("api/v2/links/user/{user_id}")
    Call<ResponseBody> GETLINKS(@Path(value = "user_id", encoded = true) String userId);


}

