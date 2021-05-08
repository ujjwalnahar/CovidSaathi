package com.projectupma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.adapters.YourContributionRecyclerAdapter;
import com.projectupma.models.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class YourContributionActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Resource> resourceArrayList=new ArrayList<>();
    HashMap<String,String> contributionMap=new HashMap<>();
    //ResourcesRecyclerAdapter resourcesRecyclerAdapter;
    YourContributionRecyclerAdapter yourContributionRecyclerAdapter;
    RecyclerView recyclerViewResources;
    FloatingActionButton btnAddResource;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_your_contribution);
        initiators();
        getAllResources();
    }
    private void initiators(){
recyclerViewResources=findViewById(R.id.rcv_resources_your_contribution);
    }
    private void getAllResources() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(YourContributionActivity.this);
        recyclerViewResources.setLayoutManager(mLayoutManager);
       // String branch = getIntent().getExtras().getString(Db.BRANCH);
        //String sem = getIntent().getExtras().getString(Db.SEMESTER);
        //String subjectCode = getIntent().getExtras().getString(Db.CODE);
        //Log.d("90202", branch + " => " + sem + subjectCode.toUpperCase());

        db.document(Db.RESOURCES).collection("CS").whereEqualTo(Db.USERID,mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("90202",mAuth.getUid());

                if (task.isSuccessful()) {
                    Log.d("90202", "1");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("90202", document.getId() + " => " + document.getData());
                        Resource resource = document.toObject(Resource.class);
                        resourceArrayList.add(resource);
                        db.collection(Db.USER).whereEqualTo("user_id",resource.getUserId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document1 : task.getResult()) {
                                        contributionMap.put(document1.getString("user_id"),document1.getString("name"));
                                        Log.d("90202", document1.getId() + " => " + document1.getData()+document1.getString("name")+resource.getUserId());
                                    }
                                    yourContributionRecyclerAdapter=new YourContributionRecyclerAdapter(YourContributionActivity.this,resourceArrayList);
                                    recyclerViewResources.setAdapter(yourContributionRecyclerAdapter);
                                } else {
                                }
                            }
                        });
                    }
                    //Log.d(TAG, "onComplete: 1");

                    //Log.d(TAG, "onComplete: 1");
                    }

            }
        });
    }

}