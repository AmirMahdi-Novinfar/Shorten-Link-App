package ir.iamnovinfar.Shorten_link.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.adivery.sdk.Adivery;
import com.adivery.sdk.AdiveryAdListener;
import com.adivery.sdk.AdiveryBannerAdView;
import com.andreseko.SweetAlert.SweetAlertDialog;


import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import ir.iamnovinfar.Shorten_link.Model.GsonModel.ShortenGsonModel;
import ir.iamnovinfar.Shorten_link.MyConnection.Shorte;

import com.google.gson.Gson;


import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.iamnovinfar.Shorten_link.PostModel.ShortLinkPostModel;

import ir.iamnovinfar.Shorten_link.R;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    public static final String BASE_URL = "https://i.iamnovinfar.ir";

    EditText editText;
    SweetAlertDialog sweetAlertDialog;
    String ValidData;
    ImageView info_help;

    CircularProgressButton btn_giveshorturl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetUpViews();
        Adivery.configure(getApplication(),"fdde4967-d1b8-42af-996b-4fde9b15ee4d");
        Adivery.prepareInterstitialAd(this, "88db1f31-c7c1-48b3-b62d-9ffdbd8bafd5");
        Adivery.showAd("88db1f31-c7c1-48b3-b62d-9ffdbd8bafd5");
        registerReceiver(broadcastReceiver,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));






        btn_giveshorturl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkConnected(MainActivity.this)) {

                String data = editText.getText().toString().trim();
                if (editText.getText().length() == 0) {

                    sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitle("آدرس را وارد کنید...");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    sweetAlertDialog.show();
                } else {

                    if (isValidUrl(data)) {
                        //   btn_giveshorturl.stopAnim(MainActivity.this::SetUpViews);
                        btn_giveshorturl.startAnimation();

                        ValidData = data;
                        getshortLinkFromShorte(data);

                    } else {

                        sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitle("آدرس به صورت اشتباه وارد شده است...");
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
            }else {
                    sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
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




//
            }
        });

        info_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                    startActivity(intent);

            }
        });


    }

    private void SetUpViews() {

        editText = findViewById(R.id.edt_password_login);
        btn_giveshorturl = findViewById(R.id.btn_giveshorturl);
        info_help = findViewById(R.id.info_help);



    }


    private void getshortLinkFromShorte(String validurlstr) {

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

        Shorte shorte = retrofit.create(Shorte.class);
        ShortLinkPostModel shortLinkPostModel = new ShortLinkPostModel(ValidData);
        Call<ResponseBody> responseBodyCall = shorte.GETSHORTLINK(shortLinkPostModel);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    String responsestring = response.body().string();
                    Log.i("amirnovin", responsestring);
                    Gson gson = new Gson();
                    ShortenGsonModel gsonModel = gson.fromJson(responsestring, ShortenGsonModel.class);
                    String status = gsonModel.getStatus();
                    String shorturl = gsonModel.getShortUrl();
                    String timeCreate = gsonModel.getCreatedAt();
                    btn_giveshorturl.stopAnimation();
                    btn_giveshorturl.revertAnimation();

                    Intent intent = new Intent(MainActivity.this, ToolsActivity.class);
                    intent.putExtra("status", status);
                    intent.putExtra("timeCreate", timeCreate);
                    intent.putExtra("finaldata", "https://i.iamnovinfar.ir/" + shorturl);
                    startActivity(intent);


                } catch (IOException e) {

                    e.printStackTrace();
                    sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);

                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setTitle("عملیات با خطا مواجه شد..");

                    sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    sweetAlertDialog.show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private boolean isValidUrl(String url) {
        Pattern pattern = Patterns.WEB_URL;
        Matcher matcher = pattern.matcher(url.toLowerCase().trim());
        return matcher.matches();
    }


    private void setupBannerAd(){
        AdiveryBannerAdView bannerAd = findViewById(R.id.banner_ad);

        bannerAd.setBannerAdListener(new AdiveryAdListener() {
            @Override
            public void onAdLoaded() {
            }
            @Override
            public void onError(String reason){
            }

            @Override
            public void onAdClicked(){
                // کاربر روی بنر کلیک کرده
            }
        });
        bannerAd.setPlacementId("73369a4a-3ae8-4a4c-bc7f-4ce621aa3874");
        bannerAd.loadAd();

 AdiveryBannerAdView bannerAd2 = findViewById(R.id.banner_ad2);

        bannerAd2.setBannerAdListener(new AdiveryAdListener() {
            @Override
            public void onAdLoaded() {
            }
            @Override
            public void onError(String reason){
            }

            @Override
            public void onAdClicked(){
                // کاربر روی بنر کلیک کرده
            }
        });
        bannerAd2.setPlacementId("cfca2781-2bcc-4486-bbc6-04a97b4823e1");
        bannerAd2.loadAd();



    }






    public BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            if (isNetworkConnected(MainActivity.this)){
                setupBannerAd();
            }else{
                sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



}