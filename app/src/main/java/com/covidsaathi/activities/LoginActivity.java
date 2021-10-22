package com.covidsaathi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.models.UserModel;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    LinearProgressIndicator progress_Login_ProgressBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextInputEditText rollNumber_Login_TextInputEditText, password_Login_TextInputEditText;

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseAuth.getInstance().signOut();
        initUI();
        methods();

    }

    private void initUI() {
        rollNumber_Login_TextInputEditText = findViewById(R.id.rollNumber_Login_TextInputEditText);
        password_Login_TextInputEditText = findViewById(R.id.password_Login_TextInputEditText);
        progress_Login_ProgressBar = findViewById(R.id.progress_Login_ProgressBar);
    }

    private void methods() {
    }

    public void onSignupUpClick(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void forgotPassword(View view) {

    }

    public void onLoginClick(View view) {

        String roll = rollNumber_Login_TextInputEditText.getText().toString().toUpperCase();
        Db.db.collection(Db.USER_COL).whereEqualTo("roll_no", roll).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().size() > 0) {
                    progress_Login_ProgressBar.setProgress(25, true);
                    String email = task.getResult().getDocuments().get(0).getString("email");
                    String password = password_Login_TextInputEditText.getText().toString();
                    Boolean emailCorrect = isValidEmail(email);
                    if (!emailCorrect) {
                        rollNumber_Login_TextInputEditText.setError("Please enter a valid Email");
                        rollNumber_Login_TextInputEditText.requestFocus();
                        progress_Login_ProgressBar.setProgress(0, true);

                        return;
                    }
                    if (password.isEmpty()) {
                        password_Login_TextInputEditText.setError(("Please Enter a Password"));
                        password_Login_TextInputEditText.requestFocus();
                        progress_Login_ProgressBar.setProgress(0, true);
                        return;
                    }
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progress_Login_ProgressBar.setProgress(50, true);
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String userId = firebaseUser.getUid();
                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> taskID) {
                                        db.document(Db.getUserDoc(firebaseUser.getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                Db.userModel = task.getResult().toObject(UserModel.class);
                                                progress_Login_ProgressBar.setProgress(60, true);
                                                try {
                                                    checkFCM(Db.userModel, taskID.getResult());
                                                }catch (Exception e){
                                                    if(Db.userModel.getApproved().toLowerCase().equals("approved"))
                                                        gotoHomeActivity();
                                                    else gotoApprovalActivity();                                                }
                                            }
                                        });
                                    }
                                });


                            } else {
                                Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                progress_Login_ProgressBar.setProgress(0, true);
                                password_Login_TextInputEditText.setError("Incorrect Password");
                                password_Login_TextInputEditText.setText("");
                            }

                        }
                    });
                } else {
                    rollNumber_Login_TextInputEditText.setError(roll + " is not registered");
                }
            }
        });


    }

    private void checkFCM(UserModel userModel, String token) {
        ArrayList<String> ids = userModel.getFcmIDS();
        for (String id : ids) {
            if (id.equals(token)) {
                progress_Login_ProgressBar.setProgress(progress_Login_ProgressBar.getProgress() + ((100 - progress_Login_ProgressBar.getProgress()) / 2), true);
                progress_Login_ProgressBar.setIndicatorColor(getResources().getColor(R.color.primarygrey));
                if(userModel.getApproved().toLowerCase().equals("approved"))
                gotoHomeActivity();
                else gotoApprovalActivity();
                return;
            }
        }
        ids.add(token);
        db.document(Db.getUserDoc(userModel.getUser_id())).update("fcmIDS", ids).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progress_Login_ProgressBar.setProgress(100, true);
                if(userModel.getApproved().toLowerCase().equals("approved"))
                    gotoHomeActivity();
                else gotoApprovalActivity();            }
        });

    }

    private void gotoApprovalActivity() {
        Intent intent = new Intent(LoginActivity.this, NewHomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void gotoHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, NewHomeActivity.class);
        startActivity(intent);
        finish();
    }

}