package ir.iamnovinfar.Shorten_link.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import ir.iamnovinfar.Shorten_link.Model.PostModel.LoginOtpPostModel;
import ir.iamnovinfar.Shorten_link.Model.PostModel.ShortLinkPostModel;
import ir.iamnovinfar.Shorten_link.MyConnection.Login_sendsms;
import ir.iamnovinfar.Shorten_link.MyConnection.Shorte;
import ir.iamnovinfar.Shorten_link.R;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    EditText edt_phone;
    Button btn_login;
    private final String BASE_URL="http://lnkno.ir";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String dataphone=edt_phone.getText().toString().trim();
             if (validPhone(dataphone)){

                 sendLoginSms(dataphone);

             }else {
                 Toast.makeText(LoginActivity.this, "not valid", Toast.LENGTH_SHORT).show();
             }
            }
        });



    }

    private void setupViews() {
        edt_phone = findViewById(R.id.edt_auth);
        btn_login = findViewById(R.id.btn_login);
    }


    private boolean validPhone(String data) {
        if (data.matches("(\\+98|0)?9\\d{9}")) {
            return true;
        } else {
            return false;
        }
    }


    private void sendLoginSms(String phone){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okhttpbuilder = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .connectTimeout(20000, TimeUnit.MILLISECONDS);
        Retrofit.Builder retrofitbuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL);
        retrofitbuilder.build();
        Retrofit retrofit = retrofitbuilder.client(okhttpbuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Login_sendsms login_sendsms = retrofit.create(Login_sendsms.class);
        LoginOtpPostModel loginOtpPostModel = new LoginOtpPostModel(phone);
        Call<ResponseBody> responseBodyCall = login_sendsms.SENDOTPMESSAGE(loginOtpPostModel);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code()==201){

                }else {

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}