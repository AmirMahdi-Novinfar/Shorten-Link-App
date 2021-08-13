package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.andreseko.SweetAlert.SweetAlertDialog;
import com.example.myapplication.MyConnection.BitlyAPI;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
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



    public static final String BASE_URL = "https://tinyurl.com/";
    public static final String[] BASE_PROTOCOLS = {"انتخاب کنید..","http","https","rtsp"};

    TextInputEditText editText;
    SweetAlertDialog sweetAlertDialog;
    RelativeLayout cardView;
    Spinner spinner;
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SetUpViews();
        ConfigSpinner();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String protocol="";
                if (spinner.getSelectedItemPosition()==0){
                    sweetAlertDialog=new SweetAlertDialog(MainActivity.this,SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitle("پروتکل را را انتخاب کنید...");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    sweetAlertDialog.show();
                }else {
                    if (editText.getText().length()==0){
                        sweetAlertDialog=new SweetAlertDialog(MainActivity.this,SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitle("آدرس را وارد کنید...");
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });
                        sweetAlertDialog.show();
                    }else {

                        if (spinner.getSelectedItemPosition()==1){
                            protocol="http";
                        }else if (spinner.getSelectedItemPosition()==2){
                            protocol="https";
                        }else if (spinner.getSelectedItemPosition()==3){
                            protocol="rtsp";
                        }
                        String data=protocol+"://"+editText.getText().toString().trim();
                        if (IsValidUrl(data)){
                            GetshortLinkFromBitly(data);
                        }else {
                            sweetAlertDialog=new SweetAlertDialog(MainActivity.this,SweetAlertDialog.ERROR_TYPE);
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


            }
        });


    }

    private void SetUpViews() {
        editText = findViewById(R.id.edt_link);
        cardView=findViewById(R.id.btn_giveshorturl);
        spinner=findViewById(R.id.protocolselector);

    }

    private void GetshortLinkFromBitly(String validurlstr) {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okhttpbuilder = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .connectTimeout(20000,TimeUnit.MILLISECONDS);
        Retrofit.Builder retrofitbuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL);
        retrofitbuilder.build();
        Retrofit retrofit = retrofitbuilder.client(okhttpbuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BitlyAPI bitlyAPI = retrofit.create(BitlyAPI.class);
        Call<ResponseBody> responseBodyCall = bitlyAPI.GETSHORTLINK(validurlstr);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                sweetAlertDialog=new SweetAlertDialog(MainActivity.this,SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitle("اطلاعات با موفقیت دریافت شد");
                try {
                    sweetAlertDialog.setContentText(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialog.show();

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
    private void ConfigSpinner(){
        arrayAdapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line
        ,BASE_PROTOCOLS);
        spinner.setAdapter(arrayAdapter);
    }
}