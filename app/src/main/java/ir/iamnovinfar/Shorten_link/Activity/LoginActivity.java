package ir.iamnovinfar.Shorten_link.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.adivery.sdk.Adivery;
import com.adivery.sdk.AdiveryAdListener;
import com.adivery.sdk.AdiveryBannerAdView;
import com.aghajari.rlottie.AXrLottie;
import com.aghajari.rlottie.AXrLottieDrawable;
import com.aghajari.rlottie.AXrLottieImageView;
import com.andreseko.SweetAlert.SweetAlertDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ir.iamnovinfar.Shorten_link.Model.GsonModel.LoginErrorGsonModel;
import ir.iamnovinfar.Shorten_link.Model.PostModel.LoginOtpPostModel;
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

public class LoginActivity extends AppCompatActivity {


    EditText edt_phone;
    Button btn_login;
    private final String BASE_URL = "https://lnkno.ir";
    SweetAlertDialog sweetAlertDialog;
    AXrLottieImageView lottieView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();
        Adivery.configure(getApplication(), "fdde4967-d1b8-42af-996b-4fde9b15ee4d");
        registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.main));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNetworkConnected(LoginActivity.this)) {
                    String dataphone = edt_phone.getText().toString().trim();


                    if (validPhone(dataphone)) {
                        setUpAnimation();
                        sendLoginSms(dataphone);


                    } else {

                        sweetAlertDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitle("شماره وارد شده اشتباه است..");
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });
                        sweetAlertDialog.show();
                    }

                }else {
                    sweetAlertDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitle("به اینترنت وصل نیستید!!!");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    sweetAlertDialog.show();
                }

            }
        });


    }
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            if (isNetworkConnected(LoginActivity.this)) {
                setupBannerAd();
            } else {
                sweetAlertDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitle("به اینترنت وصل نیستید!!!");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialog.show();
            }

        }
    };


    private void setupViews() {
        edt_phone = findViewById(R.id.edt_auth);
        btn_login = findViewById(R.id.btn_login_otp);
    }


    private boolean validPhone(String data) {
        if (data.matches("(\\+98|0)?9\\d{9}")) {
            return true;
        } else {
            return false;
        }
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

                    startActivity(new Intent(LoginActivity.this, OtpCheckActivity.class).putExtra("phone", phone));
                    finish();
                    lottieView.setVisibility(View.GONE);
                    lottieView.stopAnimation();


                } else if (response.code() == 200) {

                    try {
                        String dataError = response.body().string();
                        Gson gson = new Gson();
                        LoginErrorGsonModel gsonModel = gson.fromJson(dataError, LoginErrorGsonModel.class);
                        sweetAlertDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void setupBannerAd() {

        AdiveryBannerAdView bannerAd2 = findViewById(R.id.banner_ad_login);

        bannerAd2.setBannerAdListener(new AdiveryAdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onError(String reason) {
            }

            @Override
            public void onAdClicked() {
                // کاربر روی بنر کلیک کرده
            }
        });
        bannerAd2.setPlacementId("c67a6646-ad07-41fe-8b0f-246ce5473e3f");
        bannerAd2.loadAd();


    }



}