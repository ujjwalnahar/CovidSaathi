package com.covidsaathi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.models.UserModel;
import com.covidsaathi.utils.AppHelper;

public class ApprovalActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        userApproved();
    }

    private void userApproved() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        db.document(Db.getUserDoc(userId)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserModel userModel = task.getResult().toObject(UserModel.class);
                if (userModel.getApproved().equals("approved")) {
                    Toast.makeText(ApprovalActivity.this, "You had been approved", Toast.LENGTH_SHORT).show();
                    AppHelper.setUserType(getParent(),AppHelper.getUserTypeFromAuthInt(userModel.getAuth()));
                    Intent intent = new Intent(ApprovalActivity.this, HomeActivity.class);
//                    startActivity(intent);
                }


            }
        });
    }

}