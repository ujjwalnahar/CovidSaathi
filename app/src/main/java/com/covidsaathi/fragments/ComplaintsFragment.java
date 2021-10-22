package com.covidsaathi.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.covidsaathi.models.CentersLatLongModel;
import com.covidsaathi.utils.AppHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.activities.PostThreadActivity;
import com.covidsaathi.adapters.DizqusThreadMainItemAdatper;
import com.covidsaathi.models.ThreadModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ComplaintsFragment extends Fragment {
    private RecyclerView recyclerViewMainThread;
    private List<String> threadIds = new ArrayList<>();
    private FloatingActionButton btn_add_post;
    private TextView txtDizqusTitle;
    protected LocationManager mLocationManager;
    protected LocationListener locationListener;
    private SwipeRefreshLayout swipeRefreshDizqusThread;
    AppHelper appHelper = new AppHelper();


    public ComplaintsFragment() {
        // Required empty public constructor
    }

    public static ComplaintsFragment newInstance(String param1, String param2) {
        ComplaintsFragment fragment = new ComplaintsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complaints, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initiators(view);
        try {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

                return;
            }

            methods();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onViewCreated(view, savedInstanceState);
    }
    private void initiators(View v) {
        recyclerViewMainThread = v.findViewById(R.id.rcvDisquzThreadItem);
        btn_add_post = v.findViewById(R.id.btn_add_post);
        txtDizqusTitle=v.findViewById(R.id.txtDizqusTitle);
        swipeRefreshDizqusThread=v.findViewById(R.id.swipeRefreshDizqusThread);
        swipeRefreshDizqusThread.setColorSchemeColors(getResources().getColor(R.color.color1),getResources().getColor(R.color.color2),getResources().getColor(R.color.color3),getResources().getColor(R.color.color4),getResources().getColor(R.color.color5));
    }
    private void methods() throws IOException {
        // setRecyclerAdapter("CS", "1");
        getAllThread();
        on_click_add_post_btn();
        call_RetroFitAPi();

    }
    private void call_RetroFitAPi() throws IOException {
    }

    private void getAllThread() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMainThread.setLayoutManager(mLayoutManager);
        Db.db.collection(Db.DIZCUS_THREAD_COL).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ThreadModel> dizqusThreadModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        threadIds.add(document.getId());
                        ThreadModel dizqusThreadModel = document.toObject(ThreadModel.class);
                         dizqusThreadModels.add(dizqusThreadModel);
                         DizqusThreadMainItemAdatper dizqusThreadMainItemAdatper = new DizqusThreadMainItemAdatper(dizqusThreadModels,getActivity(), threadIds);
                        recyclerViewMainThread.setAdapter(dizqusThreadMainItemAdatper);
                        swipeRefreshDizqusThread.setRefreshing(false);
                    }
                }
            }
        });
    }
    private void on_click_add_post_btn() {
        btn_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostThreadActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    class fetchCenters extends AsyncTask<String, Void, Void> {



        @Override
        protected Void doInBackground(String... strings) {
            try {
                Call<CentersLatLongModel> center= appHelper.vaccinationApis.getCentersFromLatLong(strings[0],strings[1]);
                Response<CentersLatLongModel>centers=center.execute();
                Log.d("11111", "call_RetroFitAPi: "+centers.body().centers.get(0).district_name.toString());} catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}