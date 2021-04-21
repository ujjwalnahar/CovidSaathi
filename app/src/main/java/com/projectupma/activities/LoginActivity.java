package com.projectupma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.models.UserModel;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextInputEditText edtEmail, edtPassword;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseAuth.getInstance().signOut();
        auth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.text_email_login);
        edtPassword = findViewById(R.id.text_password_login);
    }

    public void signUpClick(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void forgotPassword(View view) {

    }

    public void login(View view) {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        Boolean emailCorrect = isValidEmail(email);
        if (!emailCorrect) {
            edtEmail.setError("Please enter a valid Email");
            edtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edtPassword.setError(("Please Enter a Password"));
            edtPassword.requestFocus();
            return;
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Log.d("LoginActivity", "signInWithEmailAndPassword:success");

                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("userId", userId);
                    db.document(Db.getUserDoc(firebaseUser.getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Db.userModel = task.getResult().toObject(UserModel.class);
                            startActivity(intent);
                        }
                    });


                } else {
                    Toast.makeText(LoginActivity.this, "UserName/Password is incorrect", Toast.LENGTH_SHORT).show();
                    edtPassword.setText("");
                }

            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}