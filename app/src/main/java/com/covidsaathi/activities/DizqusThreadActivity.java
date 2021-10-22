package com.covidsaathi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.covidsaathi.models.ThreadModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.adapters.DizqusThreadMainItemAdatper;
import com.covidsaathi.models.DizqusThreadModel;

import java.util.ArrayList;
import java.util.List;

public class DizqusThreadActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMainThread;
    private List<String> threadIds = new ArrayList<>();
    private FloatingActionButton btn_add_post;
    private TextView txtDizqusTitle;
    private SwipeRefreshLayout swipeRefreshDizqusThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dizqus_thread);
        Intent i=getIntent();
        if(i.getStringExtra("ActivityType").equals("SocietyThread"))
        {
            methodsForSocietyThread(i.getStringExtra("SocietyId"),i.getStringExtra("SocietyName"));
        }
        else if(i.getStringExtra("ActivityType").equals("DizqusThread")){
        initiators();
        methodsForDizqusThread();
        }
    }

    private void methodsForSocietyThread(String societyId,String societyName) {
        initiators();
        txtDizqusTitle.setText(societyName);
        getAllSocietyThread(societyId);
        swipeRefreshDizqusThreadListnerForSociety(societyId);

    }
private void swipeRefreshDizqusThreadListnerForSociety(String societyId){
        swipeRefreshDizqusThread.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllSocietyThread(societyId);

            }
        });
}

    private void initiators() {
        recyclerViewMainThread = findViewById(R.id.rcvDisquzThreadItem);
        btn_add_post = findViewById(R.id.btn_add_post);
        txtDizqusTitle=findViewById(R.id.txtDizqusTitle);
        swipeRefreshDizqusThread=findViewById(R.id.swipeRefreshDizqusThread);
        swipeRefreshDizqusThread.setColorSchemeColors(getResources().getColor(R.color.color1),getResources().getColor(R.color.color2),getResources().getColor(R.color.color3),getResources().getColor(R.color.color4),getResources().getColor(R.color.color5));

    }

    private void methodsForDizqusThread() {
        getAllThread();
        swipeRefreshDizqusThreadListnerForDizqus();
    }
    private void swipeRefreshDizqusThreadListnerForDizqus(){
        swipeRefreshDizqusThread.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllThread();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getAllThread();
        on_click_add_post_btn();
    }

    private void on_click_add_post_btn() {
        btn_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DizqusThreadActivity.this, PostThreadActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void getAllThread() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(DizqusThreadActivity.this);
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
                        DizqusThreadMainItemAdatper dizqusThreadMainItemAdatper = new DizqusThreadMainItemAdatper(dizqusThreadModels, DizqusThreadActivity.this, threadIds);
                        recyclerViewMainThread.setAdapter(dizqusThreadMainItemAdatper);
                        swipeRefreshDizqusThread.setRefreshing(false);
                    }
                }
            }
        });

    }
    private void getAllSocietyThread(String societyId){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(DizqusThreadActivity.this);
        recyclerViewMainThread.setLayoutManager(mLayoutManager);
        Db.db.collection(Db.DIZCUS_THREAD_COL).whereEqualTo("user_id",societyId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ThreadModel> dizqusThreadModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        threadIds.add(document.getId());
                        ThreadModel dizqusThreadModel = document.toObject(ThreadModel.class);
                        dizqusThreadModels.add(dizqusThreadModel);
                        DizqusThreadMainItemAdatper dizqusThreadMainItemAdatper = new DizqusThreadMainItemAdatper(dizqusThreadModels, DizqusThreadActivity.this, threadIds);
                        recyclerViewMainThread.setAdapter(dizqusThreadMainItemAdatper);
                        swipeRefreshDizqusThread.setRefreshing(false);

                    }
                }
            }
        });
    }
}