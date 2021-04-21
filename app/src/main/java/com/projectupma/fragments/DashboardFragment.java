package com.projectupma.fragments;

import android.content.Context;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
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
    Context context;

    //local initializes
    SliderView dashboard_image_slider_view;
    RecyclerView jec_notice_recyclerView;
    RecyclerView dizqus_recyclerView_dashboard;
    TextView jec_notice_view_more_button;
    Chip view_timetable_chip_dashboard,view_library_chip_dashboard;
    MaterialButton importantNotice_toggle_button_dashboard, examinationNotice_toggle_button_dashboard;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initUI(view);
        methods();
    }

    private void initUI(View view) {
        dashboard_image_slider_view = view.findViewById(R.id.dashboard_image_slider_view);
        jec_notice_recyclerView = view.findViewById(R.id.jec_notice_recyclerView_dashboard);
        dizqus_recyclerView_dashboard = view.findViewById(R.id.dizqus_recyclerView_dashboard);
        jec_notice_view_more_button = view.findViewById(R.id.jec_notice_view_more_button);
        view_timetable_chip_dashboard = view.findViewById(R.id.view_timetable_chip_dashboard);
        view_library_chip_dashboard = view.findViewById(R.id.view_library_chip_dashboard);
        importantNotice_toggle_button_dashboard = view.findViewById(R.id.importantNotice_toggle_button_dashboard);
        examinationNotice_toggle_button_dashboard = view.findViewById(R.id.examinationNotice_toggle_button_dashboard);

    }

    private void methods() {
        loadImagesIntoSlider();
        loadNotice();
        loadNoticeBoardFragment();
        loadDizqus();
        loadTimeTableFragment();
        loadLibraryFragment();
    }

    private void loadLibraryFragment() {
        view_library_chip_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.dashboard_frameLayout, new ResourcesFragment()).addToBackStack(null).commit();

            }
        });

    }

    private void loadTimeTableFragment() {
        view_timetable_chip_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.dashboard_frameLayout, new TimeTableFragment()).addToBackStack(null).commit();

            }
        });
    }


    private void loadNoticeBoardFragment() {
        jec_notice_view_more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.dashboard_frameLayout, new NoticeBoardFragment()).addToBackStack(null).commit();

            }
        });
    }

    private void loadNotice() {
{
            List<JECNoticeModel> models = new ArrayList<>();
            db.collection(Db.JEC_NOTICE)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    JECNoticeModel model = snapshot.toObject(JECNoticeModel.class);
                                    models.add(model);
                                }
                                jec_notice_recyclerView.setAdapter(new JECNoticeAdapter(models, getActivity(),  Math.min(models.size(),3)));
                            }
                        }
                    });
            jec_notice_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        }
        importantNotice_toggle_button_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<JECNoticeModel> models = new ArrayList<>();
                db.collection(Db.JEC_NOTICE)
                        .orderBy("date", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        JECNoticeModel model = snapshot.toObject(JECNoticeModel.class);
                                        models.add(model);
                                    }
                                    jec_notice_recyclerView.setAdapter(new JECNoticeAdapter(models, getActivity(),  Math.min(models.size(),3)));
                                }
                            }
                        });
                jec_notice_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            }
        });
        examinationNotice_toggle_button_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<JECNoticeModel> models = new ArrayList<>();
                db.collection(Db.JEC_EXAM_NOTICE)
                        .orderBy("date", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        JECNoticeModel model = snapshot.toObject(JECNoticeModel.class);
                                        models.add(model);
                                    }
                                    jec_notice_recyclerView.setAdapter(new JECNoticeAdapter(models, getActivity(), Math.min(models.size(),3)));
                                }
                            }
                        });
                jec_notice_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });


    }

    private void loadDizqus() {
        List<DizqusThreadModel> models = new ArrayList<>();
        db.collection("test/DIZQUS/THREAD")
                .orderBy("likes", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                DizqusThreadModel model = snapshot.toObject(DizqusThreadModel.class);
                                models.add(model);
                            }
                            dizqus_recyclerView_dashboard.setAdapter(new DizqusThreadTileAdapter(models, getActivity(), 3));
                        }
                    }
                });
        dizqus_recyclerView_dashboard.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    public void loadImagesIntoSlider() {
        HomePageImageSliderAdapter adapter = new HomePageImageSliderAdapter(getActivity());
        dashboard_image_slider_view.setSliderAdapter(adapter);
        dashboard_image_slider_view.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        dashboard_image_slider_view.startAutoCycle();
    }

}