package ir.iamnovinfar.Shorten_link.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;


import com.aghajari.rlottie.AXrLottie;
import com.aghajari.rlottie.AXrLottieDrawable;
import com.aghajari.rlottie.AXrLottieImageView;

import ir.iamnovinfar.Shorten_link.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        setUpAnimation();

        SharedPreferences sharedPreferences=getSharedPreferences("UserLoginData",MODE_PRIVATE);
        boolean isLogin=sharedPreferences.getBoolean("isLogin",false);




        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                if(isLogin){

                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }else {

                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },1500);


    }




    private void setUpAnimation(){
        AXrLottie.init(this);

        AXrLottieImageView lottieView=findViewById(R.id.lottie_view);
        lottieView.setLottieDrawable(AXrLottieDrawable.fromAssets(this,"loading2.json")
                .setSize(500,500)
                .build());
        lottieView.playAnimation();
    }
}