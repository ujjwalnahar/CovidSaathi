package com.projectupma.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectupma.models.SubjectModel;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.activities.ResourcesElementActivity;
import com.projectupma.adapters.SubjectsRecyclerAdatper;
import com.projectupma.utils.AppHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResourcesFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerViewSubjects;
    Spinner spinnerBranch, spinnerSemester;
    SubjectsRecyclerAdatper subjectsRecyclerAdatper;
    String branch;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragement_resources, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initiator(view);
        methods();

    }

    private void initiator(View view) {
        recyclerViewSubjects = view.findViewById(R.id.rcv_resources_subjects);
        spinnerBranch = view.findViewById(R.id.spinner_resources_branch);
        spinnerSemester = view.findViewById(R.id.spinner_resources_semester);
    }

    private void methods() {
        // setRecyclerAdapter("CS", "1");
        changedBranchSem();

    }

    private void changedBranchSem() {
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                branch = spinnerBranch.getItemAtPosition(position).toString().trim();
                String branchShort = AppHelper.branchConverter(branch);

                setRecyclerAdapter(branchShort, spinnerSemester.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                setRecyclerAdapter(spinnerBranch.getSelectedItem().toString(), spinnerSemester.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void setRecyclerAdapter(String branch, String semester) {
        Log.d("02020", "1");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewSubjects.setLayoutManager(mLayoutManager);
        List<SubjectModel> subjectList = new ArrayList<>();

        Log.d("02020", "1");

        db.collection(Db.SUBJECTS).whereEqualTo("branch", branch).whereEqualTo("semester", semester).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("90202", document.getId() + " => " + document.getData());
                        Map<String, String> subjects = (Map<String, String>) document.get("subjects");
                        for (Map.Entry<String, String> entry : subjects.entrySet()) {
                            System.out.println("Key = " + entry.getKey() +
                                    ", Value = " + entry.getValue());
                            SubjectModel subject = new SubjectModel(entry.getValue(), entry.getKey());
                            subjectList.add(subject);
                            subjectsRecyclerAdatper = new SubjectsRecyclerAdatper(getActivity(), subjectList);
                            recyclerViewSubjects.setVisibility(View.VISIBLE);
                            recyclerViewSubjects.setAdapter(subjectsRecyclerAdatper);
                            Log.d("SomeTag", "1");

                        }
                    }
                    subjectsRecyclerAdatper = new SubjectsRecyclerAdatper(getActivity(), subjectList);
//                    onListItemClick = new OnListItemClick() {
//                        @Override
//                        public void onClick(View view, int position) {
//                            SubjectModel subject1 = subjectList.get(position);
//                            Intent intent = new Intent(getActivity(), ResourcesElementActivity.class);
//                            intent.putExtra(Db.CODE, subject1.getSubjectCode());
//                            intent.putExtra(Db.BRANCH, branch);
//                            intent.putExtra(Db.SEMESTER, semester);
//                            startActivity(intent);
//                        }
//                    };
                    recyclerViewSubjects.setVisibility(View.VISIBLE);
                    recyclerViewSubjects.setAdapter(subjectsRecyclerAdatper);
                    Log.d("SomeTag", "1");

                } else {
                    Log.d("02020", "Error getting documents: ", task.getException());
                }
            }
        });


    }
}

