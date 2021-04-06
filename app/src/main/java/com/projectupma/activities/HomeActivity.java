package com.projectupma.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.fragments.DashboardFragment;
import com.projectupma.fragments.NoticeBoardFragment;
import com.projectupma.fragments.ResourcesFragment;

public class HomeActivity extends AppCompatActivity {

    //global initializes
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //local initializes
    public static FragmentManager fragmentManager;
    ImageView home_background_imageView;
    FloatingActionButton dayNightFAB;
    FloatingActionButton resources;
    FloatingActionButton home_FAB;
    FrameLayout dashboard_frameLayout;
    NestedScrollView nested_home_scrollView;
    TextView urgent_message_text;
    TextView urgent_message_title;
    TextView txt_proifleName;
    MaterialCardView urgent_message_cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initiators();
        methods(savedInstanceState);
    }

    private void initiators() {
        FirebaseFirestore.setLoggingEnabled(true);
        dayNightFAB = findViewById(R.id.dayNightFAB);
        home_FAB = findViewById(R.id.home_FAB);
        txt_proifleName=findViewById(R.id.txt_profile_name);
        home_background_imageView = findViewById(R.id.home_background_imageView);
        dashboard_frameLayout = findViewById(R.id.dashboard_frameLayout);
        nested_home_scrollView = findViewById(R.id.nested_home_scrollView);
        urgent_message_text = findViewById(R.id.urgent_message_text);
        urgent_message_title = findViewById(R.id.urgent_message_title);
        urgent_message_cardView = findViewById(R.id.urgent_message_cardView);
        fragmentManager = getSupportFragmentManager();
        resources=findViewById(R.id.btn_resources);
    }

    private void methods(Bundle savedInstanceState) {
        homeBackgroundSetter();
        setDashboardFragment(savedInstanceState);
        homeButtonClick(savedInstanceState);
        urgentMessageGetter();
        profileButtonClickListner();


    }

    private void urgentMessageGetter() {
        db.document(Db.base).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String color_string = (String) value.get("urgent_notice_color");
                int color = Color.parseColor("#" + color_string);
                urgent_message_cardView.setCardBackgroundColor(Color.parseColor("#22" + color_string));
                urgent_message_cardView.setStrokeColor(color);
                urgent_message_cardView.setStrokeWidth(2);
                urgent_message_text.setTextColor(color);
                urgent_message_title.setTextColor(color);
                if ((long) value.get("urgent_notice_status") == 0)
                    urgent_message_cardView.setVisibility(View.GONE);
                else urgent_message_cardView.setVisibility(View.VISIBLE);
                urgent_message_text.setText((String) value.get("urgent_notice_text"));
                urgent_message_title.setText((String) value.get("urgent_notice_title"));
            }
        });

    }

    private void homeButtonClick(Bundle savedInstanceState) {

        home_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nested_home_scrollView.getScrollY() >= 0 && nested_home_scrollView.getScrollY() <= 100) {
                    int count = fragmentManager.getBackStackEntryCount();
                    for (int i = 0; i < count; i++) {
                        fragmentManager.popBackStack();
                    }
                    fragmentManager.beginTransaction().replace(R.id.dashboard_frameLayout, new DashboardFragment()).commit();



                }
                nested_home_scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void setDashboardFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) return;
        fragmentManager.beginTransaction().replace(R.id.dashboard_frameLayout, new DashboardFragment()).commit();

    }
    private void profileButtonClickListner(){
        txt_proifleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent intent=new Intent(HomeActivity.this,ProfileActivity.class);
                //startActivity(intent);
Intent intent=new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(intent);}

    });
        resources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.dashboard_frameLayout,new ResourcesFragment()).commit();

            }
        });
}

    private void homeBackgroundSetter() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            db.document(Db.base + "/STATIC_IMAGES/HOME_BACKGROUND")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                            Glide.with(HomeActivity.this)
                                    .load((String) value.get("dark_url"))
                                    .centerCrop()
                                    .into(home_background_imageView);
                        }
                    });
        } else {
            db.document(Db.base + "/STATIC_IMAGES/HOME_BACKGROUND")
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                            Glide.with(HomeActivity.this)
                                    .load((String) value.get("light_url"))
                                    .centerCrop()
                                    .into(home_background_imageView);
                        }
                    });
        }
    }


    public void nightmode(View view) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

    }
}