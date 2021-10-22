package com.covidsaathi.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.adapters.TimeTablePagerAdapter;
import com.covidsaathi.models.TimeTableModel;

public class TimeTableFragment extends Fragment {
    //global initializes
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ViewPager timetable_ViewPager;
    TabLayout timetable_TabLayout;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_time_table, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initUI(view);
        methods();
    }

    private void initUI(View view) {
        timetable_ViewPager = view.findViewById(R.id.timetable_ViewPager);
        timetable_TabLayout = view.findViewById(R.id.timetable_TabLayout);
    }

    private void methods() {
        viewPagerListner();
        tabLayoutListner();
    }

    private void tabLayoutListner() {
        timetable_TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void viewPagerListner() {

        db.document(Db.getTimeTableDoc(Db.getUserModel().getBranch(), Db.getUserModel().getSemester())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                    TimeTableModel model = document.toObject(TimeTableModel.class);
                    TimeTablePagerAdapter adapter = new TimeTablePagerAdapter(context,  model);
                    timetable_ViewPager.setAdapter(adapter);
                    timetable_ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(timetable_TabLayout));
                    timetable_TabLayout.setupWithViewPager(timetable_ViewPager);
            }
        });

    }

}