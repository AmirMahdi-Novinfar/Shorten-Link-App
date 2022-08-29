package ir.iamnovinfar.Shorten_link.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adivery.sdk.Adivery;
import com.adivery.sdk.AdiveryAdListener;
import com.adivery.sdk.AdiveryBannerAdView;
import com.adivery.sdk.r2;
import com.aghajari.rlottie.AXrLottie;
import com.aghajari.rlottie.AXrLottieDrawable;
import com.aghajari.rlottie.AXrLottieImageView;
import com.andreseko.SweetAlert.SweetAlertDialog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ir.iamnovinfar.Shorten_link.Adapter.RecyclerViewGenaratesLink;
import ir.iamnovinfar.Shorten_link.Model.GsonModel.GetLinks.GetLinksGsonModel;
import ir.iamnovinfar.Shorten_link.Model.PostModel.ShortLinkPostModel;
import ir.iamnovinfar.Shorten_link.MyConnection.GetLinks;
import ir.iamnovinfar.Shorten_link.MyConnection.Shorte;
import ir.iamnovinfar.Shorten_link.R;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeneratedLinks extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    public static final String BASE_URL = "https://lnkno.ir/";
    String API_KEY, User_id;
    TextView txt_links_count;
    AXrLottieImageView lottieView;
    SweetAlertDialog sweetAlertDialog;
    ConstraintLayout epty_links_lay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_links);
        setupViews();

        Adivery.configure(getApplication(), "fdde4967-d1b8-42af-996b-4fde9b15ee4d");
        Adivery.prepareInterstitialAd(this, "88db1f31-c7c1-48b3-b62d-9ffdbd8bafd5");
        Adivery.showAd("88db1f31-c7c1-48b3-b62d-9ffdbd8bafd5");
        setUpAnimation();
        registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        getLinksFromServer();

    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            if (isNetworkConnected(GeneratedLinks.this)) {
                setupBannerAd();
            } else {
                sweetAlertDialog = new SweetAlertDialog(GeneratedLinks.this, SweetAlertDialog.ERROR_TYPE);
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
        sharedPreferences = getSharedPreferences("UserLoginData", MODE_PRIVATE);
        API_KEY = sharedPreferences.getString("API_KEY", "");
        User_id = sharedPreferences.getString("User_id", "");
        recyclerView = findViewById(R.id.Generated_links_list);
        txt_links_count = findViewById(R.id.txt_links_count);
        epty_links_lay = findViewById(R.id.epty_links_lay);
        epty_links_lay.setVisibility(View.GONE);

    }


    private void getLinksFromServer() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .build();
                return chain.proceed(request);
            }
        }).connectTimeout(60, TimeUnit.SECONDS).callTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS);
        //   OkHttpClient.Builder okBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
        Retrofit.Builder builderretrofit = new Retrofit.Builder();
        builderretrofit.baseUrl(BASE_URL);
        builderretrofit.build();
        Retrofit retrofit = builderretrofit.client(okBuilder.build()).addConverterFactory(GsonConverterFactory.create()).build();


        GetLinks getLinks = retrofit.create(GetLinks.class);
        Call<ResponseBody> responseBodyCall = getLinks.GETLINKS(User_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {

                    if(response.code()==200){
                        String data = response.body().string();

                        Gson gson = new Gson();
                        GetLinksGsonModel getLinksGsonModel = gson.fromJson(data,GetLinksGsonModel.class);

                        if (getLinksGsonModel.getUserLinks().size()==0){
                            txt_links_count.setText("");
                            lottieView.setVisibility(View.GONE);
                            epty_links_lay.setVisibility(View.VISIBLE);

                        }else {
                            txt_links_count.setText(getLinksGsonModel.getUserLinks().size()+" لینک توسط شما کوتاه شده است.");

                            RecyclerViewGenaratesLink recyclerViewGenaratesLink = new RecyclerViewGenaratesLink(GeneratedLinks.this,getLinksGsonModel);
                            recyclerView.setLayoutManager(new LinearLayoutManager(GeneratedLinks.this));
                            recyclerView.setAdapter(recyclerViewGenaratesLink);

                            lottieView.setVisibility(View.GONE);
                        }


                    }else {
                        sweetAlertDialog = new SweetAlertDialog(GeneratedLinks.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitle("مشکلی در برنامه رخ داده است لطفا دوباره تلاش کنید");
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        });
                        sweetAlertDialog.show();
                    }



                } catch (Exception e) {
                    sweetAlertDialog = new SweetAlertDialog(GeneratedLinks.this, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitle("مشکلی در برنامه رخ داده است لطفا دوباره تلاش کنید");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                        }
                    });
                    sweetAlertDialog.show();                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                sweetAlertDialog = new SweetAlertDialog(GeneratedLinks.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitle("مشکلی در برنامه رخ داده است لطفا دوباره تلاش کنید");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                });
                sweetAlertDialog.show();
            }
        });


    }


    private void setUpAnimation(){
        AXrLottie.init(this);

         lottieView=findViewById(R.id.lottie_view);
        lottieView.setLottieDrawable(AXrLottieDrawable.fromAssets(this,"loading2.json")
                .setSize(500,500)
                .build());
        lottieView.playAnimation();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    private void setupBannerAd() {

        AdiveryBannerAdView bannerAd2 = findViewById(R.id.banner_ad_links);

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
        bannerAd2.setPlacementId("e21d07a7-ec62-4e09-b3af-889a8fb0d3f5");
        bannerAd2.loadAd();


    }




}