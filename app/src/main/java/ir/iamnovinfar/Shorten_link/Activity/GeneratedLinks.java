package ir.iamnovinfar.Shorten_link.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adivery.sdk.r2;
import com.aghajari.rlottie.AXrLottie;
import com.aghajari.rlottie.AXrLottieDrawable;
import com.aghajari.rlottie.AXrLottieImageView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_links);
        setupViews();
        setUpAnimation();

        getLinksFromServer();

    }


    private void setupViews() {
        sharedPreferences = getSharedPreferences("UserLoginData", MODE_PRIVATE);
        API_KEY = sharedPreferences.getString("API_KEY", "");
        User_id = sharedPreferences.getString("User_id", "");
        recyclerView = findViewById(R.id.Generated_links_list);
        txt_links_count = findViewById(R.id.txt_links_count);

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

                    String data = response.body().string();

                    Gson gson = new Gson();
                    GetLinksGsonModel getLinksGsonModel = gson.fromJson(data,GetLinksGsonModel.class);
                    txt_links_count.setText(" تعداد لینک های کوتاه شده شما: "+getLinksGsonModel.getUserLinks().size());

                    RecyclerViewGenaratesLink recyclerViewGenaratesLink = new RecyclerViewGenaratesLink(GeneratedLinks.this,getLinksGsonModel);
                    recyclerView.setLayoutManager(new LinearLayoutManager(GeneratedLinks.this));
                    recyclerView.setAdapter(recyclerViewGenaratesLink);

                    lottieView.setVisibility(View.GONE);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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

}