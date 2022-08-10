package ir.iamnovinfar.Shorten_link.MyConnection;


import ir.iamnovinfar.Shorten_link.Model.PostModel.LoginOtpPostModel;
import ir.iamnovinfar.Shorten_link.Model.PostModel.ShortLinkPostModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Login_sendsms {



    @POST("/api/v2/login")
    Call<ResponseBody> SENDOTPMESSAGE(@Body LoginOtpPostModel loginOtpPostModel);


}

