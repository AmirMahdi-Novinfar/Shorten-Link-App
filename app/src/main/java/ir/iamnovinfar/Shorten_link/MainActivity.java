package ir.iamnovinfar.Shorten_link;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andreseko.SweetAlert.SweetAlertDialog;


import ir.iamnovinfar.Shorten_link.Model.GsonModel.ShortenGsonModel;
import ir.iamnovinfar.Shorten_link.MyConnection.Shorte;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mmdev.progressbuttonlib.ProgressButton;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.iamnovinfar.Shorten_link.PostModel.ShortLinkPostModel;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class MainActivity extends AppCompatActivity {


    public static final String BASE_URL = "https://i.iamnovinfar.ir";

    EditText editText;
    SweetAlertDialog sweetAlertDialog;
    String ValidData;
    ArrayList<ViewGroup> slideModels;
    CarouselView carouselView;
    ProgressButton btn_giveshorturl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetUpViews();


        btn_giveshorturl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_giveshorturl.startAnim();


                String data = editText.getText().toString().trim();
                if (editText.getText().length() == 0) {
                    btn_giveshorturl.stopAnim(MainActivity.this::SetUpViews);

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

                    if (IsValidUrl(data)) {
                        btn_giveshorturl.stopAnim(MainActivity.this::SetUpViews);

                        ValidData = data;
                        GetshortLinkFromShorte(data);

                    } else {
                        btn_giveshorturl.stopAnim(MainActivity.this::SetUpViews);

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

            }
        });


    }

    private void SetUpViews() {

        editText = findViewById(R.id.edt_password_login);
        btn_giveshorturl = findViewById(R.id.btn_giveshorturl);
        setBackgroundImageSliders();

    }

    private void setBackgroundImageSliders() {


    }

    private void GetshortLinkFromShorte(String validurlstr) {

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
                    btn_giveshorturl.stopAnim(MainActivity.this::SetUpViews);
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


    private boolean IsValidUrl(String url) {
        Pattern pattern = Patterns.WEB_URL;
        Matcher matcher = pattern.matcher(url.toLowerCase().trim());
        return matcher.matches();
    }


}