package com.vasi.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("FIRENOTESDATA", Context.MODE_PRIVATE);

    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {

            if(sharedPreferences.getBoolean("LOGINSTATUS",false))
            {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            finish();
        }
    }, 3000);

    }
}
