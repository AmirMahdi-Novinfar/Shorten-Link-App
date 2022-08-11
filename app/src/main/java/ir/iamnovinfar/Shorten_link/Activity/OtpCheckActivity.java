package ir.iamnovinfar.Shorten_link.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.aghajari.rlottie.AXrLottie;
import com.aghajari.rlottie.AXrLottieDrawable;
import com.aghajari.rlottie.AXrLottieImageView;
import com.andreseko.SweetAlert.SweetAlertDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ir.iamnovinfar.Shorten_link.Model.GsonModel.GetTokenGsonModel;
import ir.iamnovinfar.Shorten_link.Model.GsonModel.LoginErrorGsonModel;
import ir.iamnovinfar.Shorten_link.Model.GsonModel.OtpErrorGsonModel;
import ir.iamnovinfar.Shorten_link.Model.PostModel.CheckOtpAndGetTokenPostModel;
import ir.iamnovinfar.Shorten_link.Model.PostModel.LoginOtpPostModel;
import ir.iamnovinfar.Shorten_link.MyConnection.CheckOtpAndGetToken;
import ir.iamnovinfar.Shorten_link.MyConnection.Login_sendsms;
import ir.iamnovinfar.Shorten_link.R;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timerx.Timer;
import timerx.TimerBuilder;

public class OtpCheckActivity extends AppCompatActivity {


    TextView txt_timer, txt_resend_otp;
    EditText edt_auth_otp;

    private final String BASE_URL = "http://lnkno.ir";
    SweetAlertDialog sweetAlertDialog;
    AXrLottieImageView lottieView;
    String API_KEY;
    Button btn_login_otp;
    String phone;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_check);
        setupViews();

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.main));
        txt_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLoginSms(phone);
                setupTimer();
                setUpAnimation();
            }
        });
        setupTimer();

        btn_login_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String edt_token=edt_auth_otp.getText().toString().trim();
                
                if (edt_token.isEmpty()){
                    sweetAlertDialog = new SweetAlertDialog(OtpCheckActivity.this, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitle("مقداری را وارد کنید...");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    sweetAlertDialog.show();
                }else {
                    checkOtpAndGetToken(phone,edt_token);
                    setUpAnimation();
                    lottieView.setVisibility(View.VISIBLE);
                }
            }
        });




    }


    private void setupViews() {

        txt_timer = findViewById(R.id.txt_timer);
        txt_resend_otp = findViewById(R.id.txt_resend_otp);
        btn_login_otp = findViewById(R.id.btn_login_otp);
        edt_auth_otp = findViewById(R.id.edt_auth_otp);
        txt_resend_otp.setEnabled(false);
        txt_resend_otp.setTextColor(Color.GRAY);
        phone = getIntent().getStringExtra("phone");
        sharedPreferences=getSharedPreferences("UserLoginData",MODE_PRIVATE);
        editor=sharedPreferences.edit();




    }


    private void setupTimer() {


        Timer timer = new TimerBuilder()
                // Set start time
                .startTime(2, TimeUnit.MINUTES)
                // Set the initial format
                .startFormat("MM:SS")
                // Set the tick listener that receives formatted time
                .onTick(time -> txt_timer.setText(time))
                // Run an action when the remaining time is 30 seсonds
                .actionWhen(30, TimeUnit.SECONDS, () -> {
                    Toast.makeText(OtpCheckActivity.this, "30 ثانیه مونده", Toast.LENGTH_LONG).show();
                }).actionWhen(0, TimeUnit.SECONDS, () -> {
                    txt_resend_otp.setEnabled(true);
                    txt_resend_otp.setTextColor(Color.BLACK);
                    txt_timer.setTextColor(Color.GRAY);


                })
                .build();

// Start the timer
        timer.start();

    }

    private void sendLoginSms(String phone) {
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

                if (response.code() == 201) {
                    txt_resend_otp.setEnabled(false);
                    txt_resend_otp.setTextColor(Color.GRAY);
                    Toast.makeText(OtpCheckActivity.this, "کد ارسال شد", Toast.LENGTH_SHORT).show();
                    lottieView.stopAnimation();
                    lottieView.setVisibility(View.GONE);
                    txt_timer.setTextColor(Color.BLACK);


                } else {

                    try {
                        String dataError = response.body().string();
                        Gson gson = new Gson();
                        LoginErrorGsonModel gsonModel = gson.fromJson(dataError, LoginErrorGsonModel.class);
                        sweetAlertDialog = new SweetAlertDialog(OtpCheckActivity.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitle(gsonModel.getPhoneNumber().toString());
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });
                        sweetAlertDialog.show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setUpAnimation() {
        AXrLottie.init(this);
        lottieView = findViewById(R.id.lottie_view);
        lottieView.setLottieDrawable(AXrLottieDrawable.fromAssets(this, "loading2.json")
                .setSize(500, 500)
                .build());
        lottieView.playAnimation();
    }

    private void checkOtpAndGetToken(String phonecheck,String token) {

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
        CheckOtpAndGetToken checkOtpAndGetToken = retrofit.create(CheckOtpAndGetToken.class);
        CheckOtpAndGetTokenPostModel checkOtpAndGetTokenPostModel = new CheckOtpAndGetTokenPostModel(phonecheck, token);
        Call<ResponseBody> responseBodyCall = checkOtpAndGetToken.checkOtpAndGetTokenPostModel(checkOtpAndGetTokenPostModel);


        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String loginissuccess = response.body().string();
                    boolean ISLOGIN=false;

                    if (response.code() == 201){
                        ISLOGIN=true;
                        Gson gson = new Gson();
                        GetTokenGsonModel gsonModel = gson.fromJson(loginissuccess, GetTokenGsonModel.class);
                        API_KEY = gsonModel.getApiKey();
                        lottieView.stopAnimation();
                        lottieView.setVisibility(View.GONE);
                        editor.putBoolean("isLogin",ISLOGIN);
                        editor.putString("API_KEY",API_KEY);
                        editor.putString("DateOfCreateUser",gsonModel.getUser().getCreatedAt());
                        editor.putString("User_id",gsonModel.getUser().getId()+"");
                        editor.apply();

                        startActivity(new Intent(OtpCheckActivity.this,MainActivity.class));
                        finish();



                    } else if (response.code() == 200) {
                        ISLOGIN=false;

                        lottieView.stopAnimation();
                        lottieView.setVisibility(View.GONE);

                        Gson gson = new Gson();
                        OtpErrorGsonModel gsonModel = gson.fromJson(loginissuccess, OtpErrorGsonModel.class);

                        sweetAlertDialog = new SweetAlertDialog(OtpCheckActivity.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitle(gsonModel.getStatus());
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });
                        sweetAlertDialog.show();



                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


}