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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.models.UserModel;
import com.projectupma.utils.AppHelper;

public class SplashActivity extends AppCompatActivity {
//    AppHelper appHelper = AppHelper.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission_group.STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission_group.STORAGE}, 1);
        }
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            gotoLoginActivity();
        } else {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.document(Db.getUserDoc(firebaseUser.getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Db.userModel = task.getResult().toObject(UserModel.class);
                    String s = Db.getUserModel().getApproved().toLowerCase();
                    if ("approved".equals(s)) {
                        gotoHomeActivity();
                    } else {
                        gotoApprovalActivity();
                    }
                }
            });
        }
    }


    public void gotoLoginActivity(View view) {
        gotoLoginActivity();
    }

    public void gotoHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    public void gotoApprovalActivity() {
        Intent i = new Intent(this, ApprovalActivity.class);
        startActivity(i);
        finish();
    }

    public void gotoLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();

    }

    private int STORAGE_PERMISSION_CODE = 100;
    private int STORAGE_READ_PERMISSION_CODE = 101;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == 1000) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(SplashActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SplashActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SplashActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SplashActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}