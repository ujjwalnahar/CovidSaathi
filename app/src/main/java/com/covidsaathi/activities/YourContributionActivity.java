package com.covidsaathi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.adapters.YourContributionRecyclerAdapter;
import com.covidsaathi.models.ResourceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YourContributionActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<ResourceModel> resourceModelArrayList =new ArrayList<>();
    HashMap<String,String> contributionMap=new HashMap<>();
    TextView txt_not_available;
    //ResourcesRecyclerAdapter resourcesRecyclerAdapter;
    YourContributionRecyclerAdapter yourContributionRecyclerAdapter;
    RecyclerView recyclerViewResources;
    FloatingActionButton btnAddResource;
    private FirebaseAuth mAuth;
    List<String> resourceIdList=new ArrayList<>();

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
txt_not_available=findViewById(R.id.txt_your_contribution_no_available);
    }
    private void getAllResources() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(YourContributionActivity.this);
        recyclerViewResources.setLayoutManager(mLayoutManager);
       // String branch = getIntent().getExtras().getString(Db.BRANCH);
        //String sem = getIntent().getExtras().getString(Db.SEMESTER);
        //String subjectCode = getIntent().getExtras().getString(Db.CODE);
        //Log.d("90202", branch + " => " + sem + subjectCode.toUpperCase());

        db.document(Db.RESOURCES_DOC).collection("/resources").whereEqualTo(Db.CONST_USERID,mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("90202",mAuth.getUid());

                if (task.isSuccessful()) {
                    Log.d("90202", "1");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("90202", document.getId() + " => " + document.getData());
                        ResourceModel resourceModel = document.toObject(ResourceModel.class);
                        resourceModelArrayList.add(resourceModel);
                        resourceIdList.add(document.getId());
                        db.collection(Db.USER_COL).whereEqualTo("user_id", resourceModel.getUserId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document1 : task.getResult()) {
                                        contributionMap.put(document1.getString("user_id"),document1.getString("name"));
                                        Log.d("90202", document1.getId() + " => " + document1.getData()+document1.getString("name")+ resourceModel.getUserId());
                                    }
                                    yourContributionRecyclerAdapter=new YourContributionRecyclerAdapter(YourContributionActivity.this, resourceModelArrayList,resourceIdList);
                                    recyclerViewResources.setAdapter(yourContributionRecyclerAdapter);
                                    if(resourceModelArrayList.size()==0){
                                        recyclerViewResources.setVisibility(View.GONE);
                                        txt_not_available.setVisibility(View.VISIBLE);
                                    }
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