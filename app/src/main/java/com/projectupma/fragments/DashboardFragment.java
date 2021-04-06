package com.projectupma.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.adapters.DizqusThreadTileAdapter;
import com.projectupma.adapters.HomePageImageSliderAdapter;
import com.projectupma.adapters.JECNoticeAdapter;
import com.projectupma.models.DizqusThreadModel;
import com.projectupma.models.JECNoticeModel;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    //global initializes
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    //local initializes
    SliderView dashboard_image_slider_view;
    RecyclerView jec_notice_recyclerView;
    RecyclerView dizqus_recyclerView_dashboard;
    TextView jec_notice_view_more_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initiator(view);
        methods();
    }

    private void initiator(View view) {
        dashboard_image_slider_view = view.findViewById(R.id.dashboard_image_slider_view);
        jec_notice_recyclerView = view.findViewById(R.id.jec_notice_recyclerView_dashboard);
        dizqus_recyclerView_dashboard = view.findViewById(R.id.dizqus_recyclerView_dashboard);
        jec_notice_view_more_button = view.findViewById(R.id.jec_notice_view_more_button);

    }

    private void methods() {
        loadImagesIntoSlider();
        loadNotice();
        loadNoticeBoardFragment();
        loadDizqus();
    }



    private void loadNoticeBoardFragment() {
        jec_notice_view_more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.dashboard_frameLayout,new NoticeBoardFragment()).addToBackStack(null).commit();

            }
        });
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
                            jec_notice_recyclerView.setAdapter(new JECNoticeAdapter(models,getActivity(),3));
                        }
                    }
                });
        jec_notice_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void loadDizqus() {
        List<DizqusThreadModel> models = new ArrayList<>();
        db.collection("test/DIZQUS/THREAD")
                .orderBy("likes", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot snapshot: task.getResult())
                            {
                                DizqusThreadModel model=snapshot.toObject(DizqusThreadModel.class);
                                models.add(model);
                            }
                            dizqus_recyclerView_dashboard.setAdapter(new DizqusThreadTileAdapter(models,getActivity(),3));
                        }
                    }
                });
        dizqus_recyclerView_dashboard.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    public void loadImagesIntoSlider() {
        HomePageImageSliderAdapter adapter=new HomePageImageSliderAdapter(getActivity());
        dashboard_image_slider_view.setSliderAdapter(adapter);
        dashboard_image_slider_view.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        dashboard_image_slider_view.startAutoCycle();
    }

}