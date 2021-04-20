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
import com.projectupma.DataClasses.User;
import com.projectupma.Db;
import com.projectupma.R;

public class ProfileActivity extends AppCompatActivity {
    private TextView txt_name, txt_rollno, txt_branch, txt_semester, txt_year, txt_college, txt_email, txt_dob;
    private ImageView img_profile;
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
        txt_name = findViewById(R.id.txt_name_profile);
        txt_rollno = findViewById(R.id.txt_rollNo_profile);
        txt_branch = findViewById(R.id.txt_branch);
        txt_semester = findViewById(R.id.txt_semester);
        txt_year = findViewById(R.id.txt_year);
        txt_college = findViewById(R.id.txt_college);
        txt_email = findViewById(R.id.txt_email);
        txt_dob = findViewById(R.id.txt_dob);
        img_profile = findViewById(R.id.img_profile_photo);
        getUser();
    }
    private void getUser(){
        FirebaseUser user=mAuth.getCurrentUser();
        if (user != null) {
             uid = user.getUid();
             email=user.getEmail();
            txt_email.setText(email);
             DocumentReference documentReference=db.collection(Db.USER).document(uid);
            db.collection(Db.USER).whereEqualTo("user_id",uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user1=setUser(document);
                            txt_name.setText(user1.getName().toUpperCase());
                            txt_rollno.setText((user1.getRoll_no()));
                            txt_branch.setText(user1.getBranch());
                            txt_year.setText(user1.getSemester());
                            txt_college.setText("Jec Jabalpur".toUpperCase());
                            txt_email.setText(email);
                            txt_semester.setText(user1.getSemester());
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Log.d(TAG, user1.getApproved()+user1.getBranch()+user1.getCollege()+user1.getName());

                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

        }
    }
    public User setUser(DocumentSnapshot doc){
        User user=new User(doc.getString("user_id"),doc.getString("name"),doc.getString("phone_no"),doc.getString("roll_no"),doc.getString("semester"),doc.getString("branch"),doc.getString("college"),doc.getString("password"),doc.getString("photo_url"),doc.getString("reward_id"),doc.getString("idcard_url"),doc.getString("email"),doc.getDate("date_created"),doc.get("auth").toString(),doc.getString("approval"));
        return user;
    }

}