package ir.iamnovinfar.Shorten_link.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adivery.sdk.Adivery;
import com.adivery.sdk.AdiveryAdListener;
import com.adivery.sdk.AdiveryBannerAdView;
import com.andreseko.SweetAlert.SweetAlertDialog;


import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import ir.iamnovinfar.Shorten_link.Model.GsonModel.ShortenGsonModel;
import ir.iamnovinfar.Shorten_link.MyConnection.Shorte;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.iamnovinfar.Shorten_link.Model.PostModel.ShortLinkPostModel;

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

public class MainActivity extends AppCompatActivity {


    public static final String BASE_URL = "https://lnkno.ir/";

    String API_KEY;
    EditText editText;
    SweetAlertDialog sweetAlertDialog;
    String ValidData;
    ImageView info_help, nv_drawer;

    CircularProgressButton btn_giveshorturl;
    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View headernav;
    TextView phone_header_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetUpViews();

        Adivery.configure(getApplication(), "fdde4967-d1b8-42af-996b-4fde9b15ee4d");
        Adivery.prepareInterstitialAd(this, "88db1f31-c7c1-48b3-b62d-9ffdbd8bafd5");
        Adivery.showAd("88db1f31-c7c1-48b3-b62d-9ffdbd8bafd5");
        registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));


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
                } else {
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
        headernav = navigationView.getHeaderView(0);
         phone_header_txt=headernav.findViewById(R.id.phone_header);
         String phone_user=sharedPreferences.getString("user_phone","");
         phone_header_txt.setText(phone_user);

        nv_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(Gravity.RIGHT);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){



                    case R.id.gen_links:
                        startActivity(new Intent(MainActivity.this,GeneratedLinks.class));
                        break;


                    case R.id.ruls:

                        Intent intent44 = new Intent(MainActivity.this, TermsAndConditions.class);
                        startActivity(intent44);
                        break;



                    case R.id.aboutus:
                        Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(intent);

                        break;

                    case R.id.donate:
                        Uri uri = Uri.parse("https://idpay.ir/iamnovinfarir"); // missing 'http://' will cause crashed
                        Intent intent3 = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent3);
                        break;

                    case R.id.website_dev:

                        Uri uri2 = Uri.parse("https://iamnovinfar.ir"); // missing 'http://' will cause crashed
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
                        startActivity(intent2);
                        break;



                }
                return true;
            }
        });


    }

    private void SetUpViews() {

        editText = findViewById(R.id.edt_password_login);
        btn_giveshorturl = findViewById(R.id.btn_giveshorturl);
        info_help = findViewById(R.id.info_help);
        sharedPreferences = getSharedPreferences("UserLoginData", MODE_PRIVATE);
        API_KEY = sharedPreferences.getString("API_KEY", "");
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        nv_drawer = findViewById(R.id.nv_drasswer);


    }


    private void getshortLinkFromShorte(String validurlstr) {


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

        Shorte shorte = retrofit.create(Shorte.class);
        ShortLinkPostModel shortLinkPostModel = new ShortLinkPostModel(ValidData, sharedPreferences.getString("User_id", ""));
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
                    String shorturl = gsonModel.getLink().getShortUrl();
                    String timeCreate = gsonModel.getLink().getCreatedAt();
                    btn_giveshorturl.stopAnimation();
                    btn_giveshorturl.revertAnimation();

                    Intent intent = new Intent(MainActivity.this, ToolsActivity.class);
                    intent.putExtra("status", status);
                    intent.putExtra("timeCreate", timeCreate);
                    intent.putExtra("finaldata", "https://lnkno.ir/" + shorturl);
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


    private void setupBannerAd() {

        AdiveryBannerAdView bannerAd2 = findViewById(R.id.banner_ad2);

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
        bannerAd2.setPlacementId("cfca2781-2bcc-4486-bbc6-04a97b4823e1");
        bannerAd2.loadAd();


    }


    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            if (isNetworkConnected(MainActivity.this)) {
                setupBannerAd();
            } else {
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