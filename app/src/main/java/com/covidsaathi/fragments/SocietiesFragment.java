package com.covidsaathi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.adapters.SocietyListRecyclerAdapter;
import com.covidsaathi.models.SocietyModel;

import java.util.ArrayList;
import java.util.List;

public class SocietiesFragment extends Fragment {
    RecyclerView recyclerViewSocietiesList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_societies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initiators(view);
        fetchClubsList();
        methods();
    }
    private void initiators(View view) {
        recyclerViewSocietiesList=view.findViewById(R.id.recyclerViewSocietiesList);
    }
    private void methods(){

    }
    private void  fetchClubsList(){
        Db.db.collection(Db.CLUBS_LIST_COL).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerViewSocietiesList.setLayoutManager(mLayoutManager);
                List<SocietyModel> societyModelList =new ArrayList<>();
                for(QueryDocumentSnapshot document: task.getResult()) {
                    SocietyModel societyModel =new SocietyModel(document.getString("clubId"),document.getString("name"),document.getString("photoLink"));
                    societyModelList.add(societyModel);
                    SocietyListRecyclerAdapter societyListRecyclerAdapter=new SocietyListRecyclerAdapter(societyModelList,getActivity());
                    recyclerViewSocietiesList.setAdapter(societyListRecyclerAdapter);
                }
            }
        });
    }
}
