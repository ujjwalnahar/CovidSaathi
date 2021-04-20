package com.projectupma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.projectupma.models.UserModel;
import com.projectupma.Db;
import com.projectupma.R;

public class ProfileActivity extends AppCompatActivity {
    private TextView txt_name_profile, txt_rollNo_profile, txt_branch, txt_semester, txt_year, txt_college, txt_email, txt_dob;
    private ImageView img_profile_photo;
    private FirebaseStorage storageReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String name,email,uid;
    private Uri photoUrl;
    private Boolean emailVerified;
    private String TAG="ProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initiators();
        getUser();


    }

    private void initiators() {
        mAuth=FirebaseAuth.getInstance();
        txt_name_profile = findViewById(R.id.txt_name_profile);
        txt_rollNo_profile = findViewById(R.id.txt_rollNo_profile);
        txt_branch = findViewById(R.id.txt_branch);
        txt_semester = findViewById(R.id.txt_semester);
        txt_year = findViewById(R.id.txt_year);
        txt_college = findViewById(R.id.txt_college);
        txt_email = findViewById(R.id.txt_email);
        txt_dob = findViewById(R.id.txt_dob);
        img_profile_photo = findViewById(R.id.img_profile_photo);
        getUser();
    }
    private void getUser(){
        FirebaseUser user=mAuth.getCurrentUser();
        if (user != null) {
             uid = user.getUid();
             txt_name_profile.setText(uid);

             email=user.getEmail();
            txt_email.setText(email);
             DocumentReference documentReference=db.collection(Db.USERDOC_USER).document(uid);
            db.collection(Db.USERDOC_USER).whereEqualTo("user_id",uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserModel userModel1 =setUser(document);
                            txt_name_profile.setText(userModel1.getName().toUpperCase());
                            txt_rollNo_profile.setText((userModel1.getRoll_no()));
                            txt_branch.setText(userModel1.getBranch());
                            txt_year.setText(userModel1.getSemester());
                            txt_college.setText("Jec Jabalpur".toUpperCase());
                            txt_email.setText(email);
                            txt_semester.setText(userModel1.getSemester());
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Log.d(TAG, userModel1.getApproved()+ userModel1.getBranch()+ userModel1.getCollege()+ userModel1.getName());

                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

        }
    }
    public UserModel setUser(DocumentSnapshot doc){
        UserModel userModel =new UserModel(doc.getString("user_id"),doc.getString("name"),doc.getString("phone_no"),doc.getString("roll_no"),doc.getString("semester"),doc.getString("branch"),doc.getString("college"),doc.getString("password"),doc.getString("photo_url"),doc.getString("reward_id"),doc.getString("idcard_url"),doc.getString("email"),doc.getDate("date_created"),doc.get("auth").toString(),doc.getString("approval"));
        return userModel;
    }

}