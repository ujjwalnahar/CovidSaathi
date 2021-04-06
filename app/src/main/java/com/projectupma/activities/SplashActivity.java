package com.projectupma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.projectupma.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
    }
    public void gotoLogin(View view) {
        Intent i=new Intent(this,SignupActivity.class);
        startActivity(i);
    }
}