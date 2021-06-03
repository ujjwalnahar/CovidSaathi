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
import com.projectupma.models.ResourceModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ResourcesElementActivity extends AppCompatActivity {
    private static final String TAG= "ResourceElementActivity";
    Spinner spinnerType;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<ResourceModel> resourceModelArrayList =new ArrayList<>();
    HashMap<String,String> contributionMap=new HashMap<>();
    ResourcesRecyclerAdapter resourcesRecyclerAdapter;
    RecyclerView recyclerViewResources;
    FloatingActionButton btnAddResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_resources_element);
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
                intent.putExtra("intentType","add");
                startActivity(intent);
            }
        });
}

    private void getAllResources() {
    GridLayoutManager mLayoutManager = new GridLayoutManager(ResourcesElementActivity.this,3);
        recyclerViewResources.setLayoutManager(mLayoutManager);
        String branch = getIntent().getExtras().getString(Db.CONST_BRANCH);
        String sem = getIntent().getExtras().getString(Db.CONST_SEMESTER);
        String subjectCode = getIntent().getExtras().getString(Db.SUBJECT_CODE);
        Log.d("90202", branch + " => " + sem + subjectCode.toUpperCase());

        db.document(Db.RESOURCES_DOC).collection(branch).whereEqualTo(Db.CONST_SEMESTER, sem).whereEqualTo(Db.CONST_SUBJECTCODE, subjectCode.toUpperCase()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("90202", document.getId() + " => " + document.getData());
                        ResourceModel resourceModel = document.toObject(ResourceModel.class);
                        resourceModelArrayList.add(resourceModel);
                        resourcesRecyclerAdapter = new ResourcesRecyclerAdapter(ResourcesElementActivity.this, resourceModelArrayList);
                        recyclerViewResources.setAdapter(resourcesRecyclerAdapter);
                    }
                }

            }
        });
    }

}