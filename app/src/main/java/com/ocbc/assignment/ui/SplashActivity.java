package com.ocbc.assignment.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ocbc.assignment.R;

import static com.ocbc.assignment.utils.Constants.SPLASH_DISPLAY_LENGTH;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
                    startActivity(new Intent(SplashActivity.this,
                            LoginActivity.class));
                    finish();
                },

        SPLASH_DISPLAY_LENGTH);
    }
}