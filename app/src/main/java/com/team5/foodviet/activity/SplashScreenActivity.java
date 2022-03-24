package com.team5.foodviet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.team5.foodviet.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_OUT = 2000;
    ImageView img_splash;
    private Animation slideAnimation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        img_splash=findViewById(R.id.img_splash);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        slideAnimation = AnimationUtils.loadAnimation(SplashScreenActivity.this,R.anim.slide_splash);
        img_splash.startAnimation(slideAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }, SPLASH_OUT);

    }
}