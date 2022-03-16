package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.RelativeLayout;

import com.andreseko.SweetAlert.SweetAlertDialog;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.myapplication.Model.PostModel.ShortePostModel;
import com.example.myapplication.MyConnection.Shorte;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    public static final String BASE_URL = "https://api.shorte.st";

    TextInputEditText editText;
    SweetAlertDialog sweetAlertDialog;
    RelativeLayout cardView;
    String ValidData;
    ArrayList<SlideModel> slideModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        slideModels=new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.one, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.two, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.three, ScaleTypes.CENTER_CROP));

        ImageSlider imageSlider=findViewById(R.id.image_slider);
        imageSlider.setImageList(slideModels);

        //SetUpViews();

//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String data=editText.getText().toString().trim();
//
//
//                if (editText.getText().length() == 0) {
//                    sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
//                    sweetAlertDialog.setTitle("آدرس را وارد کنید...");
//                    sweetAlertDialog.setCancelable(false);
//                    sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            sweetAlertDialog.dismiss();
//                        }
//                    });
//                    sweetAlertDialog.show();
//                } else {
//
//                    if (IsValidUrl(data)) {
//                        ValidData=data;
//                        GetshortLinkFromShorte(data);
//
//                    } else {
//                        sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
//                        sweetAlertDialog.setTitle("آدرس به صورت اشتباه وارد شده است...");
//                        sweetAlertDialog.setCancelable(false);
//                        sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismiss();
//                            }
//                        });
//                        sweetAlertDialog.show();
//                    }
//
//
//                }
//            }
//
//
//
//        });


    }

//    private void SetUpViews() {
//        editText = findViewById(R.id.edt_link);
//        cardView = findViewById(R.id.btn_giveshorturl);
//
//    }

//  private void GetshortLinkFromShorte(String validurlstr) {
//
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient.Builder okhttpbuilder = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
//                .connectTimeout(20000, TimeUnit.MILLISECONDS);
//        Retrofit.Builder retrofitbuilder = new Retrofit.Builder()
//                .baseUrl(BASE_URL);
//        retrofitbuilder.build();
//        Retrofit retrofit = retrofitbuilder.client(okhttpbuilder.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        Shorte shorte = retrofit.create(Shorte.class);
//            ShortePostModel shortePostModel=new ShortePostModel(ValidData);
//        Call<ResponseBody> responseBodyCall = shorte.GETSHORTLINK(shortePostModel);
//        responseBodyCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
//                sweetAlertDialog.setTitle("اطلاعات با موفقیت دریافت شد");
//                try {
//                    String responsestring=response.body().string();
//                    sweetAlertDialog.setContentText(responsestring);
//                    sweetAlertDialog.show();
//                    Log.i("amirnovin",responsestring);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
//
//                    sweetAlertDialog.setCancelable(false);
//                    sweetAlertDialog.setTitle("عملیات با خطا مواجه شد..");
//
//                    sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            sweetAlertDialog.dismiss();
//                        }
//                    });
//                    sweetAlertDialog.show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }


//   private boolean IsValidUrl(String url) {
//        Pattern pattern = Patterns.WEB_URL;
//        Matcher matcher = pattern.matcher(url.toLowerCase().trim());
//        return matcher.matches();
//    }
    
}