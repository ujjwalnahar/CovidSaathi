package com.projectupma.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.activities.AddResourceActivity;
import com.projectupma.adapters.ResourcesRecyclerAdapter;
import com.projectupma.models.ResourceModel;

import java.util.ArrayList;

import soup.neumorphism.NeumorphCardView;

public class ResourceElementFragment extends Fragment {
    Spinner spinnerType;
    ArrayList<ResourceModel> resourceModelArrayList = new ArrayList<>();
    ResourcesRecyclerAdapter resourcesRecyclerAdapter;
    RecyclerView recyclerViewResources;
    NeumorphCardView btnAddResource;
    private String branch;
    private String sem;
    private String subjectCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        branch = getArguments().getString(Db.CONST_BRANCH);
        sem = getArguments().getString(Db.CONST_SEMESTER);
        subjectCode = getArguments().getString(Db.SUBJECT_CODE);
        return inflater.inflate(R.layout.fragment_resources_element, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initUI(view);
        methods();
    }


    private void initUI(View view) {
        spinnerType = view.findViewById(R.id.spinner_resources_type);
        recyclerViewResources = view.findViewById(R.id.rcv_resources_element);
        btnAddResource = view.findViewById(R.id.btn_add_resource);
    }

    private void methods() {
        getAllResources();
        // setSpinnerType();
        setBtnAddResource();
    }


    private void getAllResources() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewResources.setLayoutManager(mLayoutManager);

        Log.d("90202", branch + " => " + sem + subjectCode.toUpperCase());

        Db.db.document(Db.RESOURCES_DOC).collection(branch).whereEqualTo(Db.CONST_SEMESTER, sem).whereEqualTo(Db.CONST_SUBJECTCODE, subjectCode.toUpperCase()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    Log.d("90202", "1");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("90202", document.getId() + " => " + document.getData());
                        ResourceModel resourceModel = document.toObject(ResourceModel.class);
                        resourceModelArrayList.add(resourceModel);
                        resourcesRecyclerAdapter = new ResourcesRecyclerAdapter(getContext(), resourceModelArrayList);
                        recyclerViewResources.setAdapter(resourcesRecyclerAdapter);
                    }
                }

            }
        });
    }

    private void setBtnAddResource() {
        btnAddResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddResourceActivity.class);
                intent.putExtra("intentType", "add");
                startActivity(intent);
            }
        });
    }

}
