package ir.iamnovinfar.Shorten_link.MyConnection;


import ir.iamnovinfar.Shorten_link.Model.PostModel.CheckOtpAndGetTokenPostModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CheckOtpAndGetToken {



    @POST("/api/v2/token")
    Call<ResponseBody> checkOtpAndGetTokenPostModel(@Body CheckOtpAndGetTokenPostModel checkOtpAndGetTokenPostModel);


}

