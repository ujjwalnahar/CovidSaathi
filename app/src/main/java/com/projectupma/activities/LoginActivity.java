package com.projectupma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projectupma.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText edtEmail,edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        edtEmail=findViewById(R.id.text_email_login);
        edtPassword=findViewById(R.id.text_password_login);
    }

    public void signUpClick(View view) {
        Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
    }

    public void forgotPassword(View view) {

    }

    public void login(View view) {
        String email=edtEmail.getText().toString();
        String password=edtPassword.getText().toString();
        Boolean emailCorrect=isValidEmail(email);
        if(!emailCorrect){
            edtEmail.setError("Please enter a valid Email");
            edtEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            edtPassword.setError(("Please Enter a Password"));
            edtPassword.requestFocus();
            return;
        }
     auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
             if (task.isSuccessful()) {

                 Log.d("LoginActivity", "signInWithEmailAndPassword:success");

                 FirebaseUser user = auth.getCurrentUser();
                 String userId = user.getUid();
                 SharedPreferences sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
                 SharedPreferences.Editor edit = sharedPreferences.edit();
                 edit.putBoolean("isSignedIn",true);
                 edit.apply();
                 Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                 intent.putExtra("userId", userId);
                 startActivity(intent);
             } else {
                 Log.w("SignupActivity", "createUserWithEmail:failure", task.getException());
                 Toast.makeText(LoginActivity.this, "Authentication failed.",
                         Toast.LENGTH_SHORT).show();
             }

         }
     });
    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}