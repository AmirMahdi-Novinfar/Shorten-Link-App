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
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.andreseko.SweetAlert.SweetAlertDialog;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import ir.iamnovinfar.Shorten_link.Model.GsonModel.ShortenGsonModel;
import ir.iamnovinfar.Shorten_link.Model.PostModel.ShortePostModel;
import ir.iamnovinfar.Shorten_link.MyConnection.Shorte;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.droidsonroids.gif.GifDrawable;
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
    ImageSlider imageSlider;
    View gifDrawable;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetUpViews();
        gifDrawable.setVisibility(View.GONE);



//



        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data=editText.getText().toString().trim();


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

                    if (IsValidUrl(data)) {
                        gifDrawable.setVisibility(View.VISIBLE);
                        ValidData=data;
                        GetshortLinkFromShorte(data);

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
            }



        });


    }

    private void SetUpViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        editText = findViewById(R.id.edt_password_login);
        cardView = findViewById(R.id.btn_giveshorturl);
        gifDrawable=findViewById(R.id.loading_gif);
        slideModels=new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.one, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.two, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.three, ScaleTypes.CENTER_CROP));

        imageSlider=findViewById(R.id.image_slider);
        imageSlider.setImageList(slideModels);
        imageSlider.buildDrawingCache();

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
            ShortePostModel shortePostModel=new ShortePostModel(ValidData);
        Call<ResponseBody> responseBodyCall = shorte.GETSHORTLINK(shortePostModel);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    gifDrawable.setVisibility(View.GONE);

                    String responsestring=response.body().string();
                    Log.i("amirnovin",responsestring);
                    Gson gson=new Gson();
                    ShortenGsonModel gsonModel=gson.fromJson(responsestring,ShortenGsonModel.class);
                   String finallink= gsonModel.getShortenedUrl();
                    Intent intent=new Intent(MainActivity.this,ToolsActivity.class);
                    intent.putExtra("finaldata",finallink);
                    startActivity(intent);


                } catch (IOException e) {
                    gifDrawable.setVisibility(View.GONE);

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
                gifDrawable.setVisibility(View.GONE);

            }
        });
    }


   private boolean IsValidUrl(String url) {
        Pattern pattern = Patterns.WEB_URL;
        Matcher matcher = pattern.matcher(url.toLowerCase().trim());
        return matcher.matches();
    }
    
}