package com.projectupma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.projectupma.R;
import com.projectupma.models.AppHelper;

public class SplashActivity extends AppCompatActivity {
    AppHelper appHelper=AppHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (ContextCompat.checkSelfPermission(
                SplashActivity.this,
                Manifest.permission_group.STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            SplashActivity.this,
                            new String[] { Manifest.permission_group.STORAGE },
                            1);
        }
        if(appHelper.checkUserLoggedIn(SplashActivity.this)){
            Intent i=new Intent(this,HomeActivity.class);
            startActivity(i);
        }
        else{
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);}
    }
    public void gotoLogin(View view) {
        Intent i=new Intent(this,SignupActivity.class);
        startActivity(i);
    }
    private int STORAGE_PERMISSION_CODE=100;
    private int STORAGE_READ_PERMISSION_CODE=101;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode ==1000 ) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(SplashActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(SplashActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SplashActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(SplashActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}