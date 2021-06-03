package com.projectupma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.adapters.DizqusThreadMainItemAdatper;
import com.projectupma.models.DizqusThreadModel;

import java.util.ArrayList;
import java.util.List;

public class DizqusThreadActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMainThread;
    private List<String> threadIds = new ArrayList<>();
    private FloatingActionButton btn_add_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dizqus_thread);
        initiators();
        methods();
    }


    private void initiators() {
        recyclerViewMainThread = findViewById(R.id.rcvDisquzThreadItem);
        btn_add_post = findViewById(R.id.btn_add_post);

    }

    private void methods() {

        getAllThread();
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
        Db.db.collection(Db.DIZCUS_THREAD_COL).orderBy("date", Query.Direction.DESCENDING).limit(50).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DizqusThreadModel> dizqusThreadModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        threadIds.add(document.getId());
                        DizqusThreadModel dizqusThreadModel = document.toObject(DizqusThreadModel.class);
                        dizqusThreadModels.add(dizqusThreadModel);
                        DizqusThreadMainItemAdatper dizqusThreadMainItemAdatper = new DizqusThreadMainItemAdatper(dizqusThreadModels, DizqusThreadActivity.this, threadIds);
                        recyclerViewMainThread.setAdapter(dizqusThreadMainItemAdatper);
                    }
                }
            }
        });

    }
}