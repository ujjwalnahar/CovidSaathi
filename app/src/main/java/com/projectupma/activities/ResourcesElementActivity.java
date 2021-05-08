package com.projectupma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.adapters.ResourcesRecyclerAdapter;
import com.projectupma.models.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class ResourcesElementActivity extends AppCompatActivity {
    private static final String TAG= "ResourceElementActivity";
    Spinner spinnerType;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Resource> resourceArrayList=new ArrayList<>();
    HashMap<String,String> contributionMap=new HashMap<>();
    ResourcesRecyclerAdapter resourcesRecyclerAdapter;
    RecyclerView recyclerViewResources;
    FloatingActionButton btnAddResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_element);
        initiators();
        getAllResources();
       // setSpinnerType();
        setBtnAddResource();
    }

    private void initiators() {
        spinnerType = findViewById(R.id.spinner_resources_type);
        recyclerViewResources=findViewById(R.id.rcv_resources_element);
        btnAddResource=findViewById(R.id.btn_add_resource);


    }
    /*private void setSpinnerType(){
        List<String> Types=new ArrayList<>();
        Types.add("Question Paper");
        Types.add("Course Books");
        Types.add("Hand Written Notes");
        ArrayAdapter<String> adapterTypes = new ArrayAdapter<String>(ResourcesElementActivity.this, android.R.layout.simple_spinner_item, Types);
        spinnerType.setAdapter(adapterTypes);
    }*/
private void setBtnAddResource(){
        btnAddResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResourcesElementActivity.this,AddResourceActivity.class);
                startActivity(intent);
            }
        });
}

    private void getAllResources() {
    GridLayoutManager mLayoutManager = new GridLayoutManager(ResourcesElementActivity.this,2);
        recyclerViewResources.setLayoutManager(mLayoutManager);
        String branch = getIntent().getExtras().getString(Db.BRANCH);
        String sem = getIntent().getExtras().getString(Db.SEMESTER);
        String subjectCode = getIntent().getExtras().getString(Db.CODE);
        Log.d("90202", branch + " => " + sem + subjectCode.toUpperCase());

        db.document(Db.RESOURCES).collection(branch).whereEqualTo(Db.SEMESTER, sem).whereEqualTo(Db.SUBJECTCODE, subjectCode.toUpperCase()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("90202", branch + " => " + sem + subjectCode.toUpperCase());

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
                                    resourcesRecyclerAdapter=new ResourcesRecyclerAdapter(ResourcesElementActivity.this,resourceArrayList,contributionMap);
                                    recyclerViewResources.setAdapter(resourcesRecyclerAdapter);
                                } else {
                                }
                            }
                        });
                    }
                    Log.d(TAG, "onComplete: 1");

                    Log.d(TAG, "onComplete: 1");}

            }
        });
    }

}