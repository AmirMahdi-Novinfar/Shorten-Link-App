package ir.iamnovinfar.Shorten_link.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;



import ir.iamnovinfar.Shorten_link.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

//        AXrLottie.init(this);
//
//        AXrLottieImageView lottieView=findViewById(R.id.lottie_view);
//        lottieView.setLottieDrawable(AXrLottieDrawable.fromAssets(this,"loading.json")
//                .setSize(100,100)
//                .build());
//        lottieView.playAnimation();
    }
}