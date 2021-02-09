package com.projectupma.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.adapters.JECNoticeAdapter;
import com.projectupma.models.JECNoticeModel;

import java.util.ArrayList;
import java.util.List;

public class NoticeBoardFragment extends Fragment {

    //global initializes
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView jec_notice_recyclerView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initiator(view);
        methods();
    }


    private void initiator(View view) {
        jec_notice_recyclerView = view.findViewById(R.id.jec_notice_recyclerView);
    }
    private void methods() {
        loadNotice();
    }

    private void loadNotice() {
        List<JECNoticeModel> models = new ArrayList<>();
        db.collection(Db.base+"/JEC_NOTICE")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot snapshot: task.getResult())
                            {
                                JECNoticeModel model=snapshot.toObject(JECNoticeModel.class);
                                models.add(model);
                            }
                            jec_notice_recyclerView.setAdapter(new JECNoticeAdapter(models,getActivity(),models.size()));
                        }
                    }
                });
        jec_notice_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notice_board, container, false);
    }
}