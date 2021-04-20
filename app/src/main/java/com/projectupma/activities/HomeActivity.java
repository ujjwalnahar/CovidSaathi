package com.projectupma.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.fragments.DashboardFragment;
import com.projectupma.fragments.ResourcesFragment;
import com.projectupma.models.UserModel;

public class HomeActivity extends AppCompatActivity {

    //global initializes
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //local initializes
    Bundle savedInstanceState;
    public static FragmentManager fragmentManager;
    ImageView home_background_imageView;
    FloatingActionButton dayNightFAB, home_FAB;
    FrameLayout dashboard_frameLayout;
    NestedScrollView nested_home_scrollView;
    TextView urgent_message_title, urgent_message_text;
    MaterialCardView urgent_message_cardView;
    NavigationView sideNavigationView_Home;
    BottomAppBar bottomAppBar_Home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_home);
        initiators();
        methods(savedInstanceState);
    }

    private void initiators() {
        FirebaseFirestore.setLoggingEnabled(true);
        dayNightFAB = findViewById(R.id.dayNightFAB);
        home_FAB = findViewById(R.id.home_FAB);
        home_background_imageView = findViewById(R.id.home_background_imageView);
        dashboard_frameLayout = findViewById(R.id.dashboard_frameLayout);
        bottomAppBar_Home = findViewById(R.id.bottomAppBar_Home);
        nested_home_scrollView = findViewById(R.id.nested_home_scrollView);
        urgent_message_text = findViewById(R.id.urgent_message_text);
        sideNavigationView_Home = findViewById(R.id.sideNavigationView_Home);
        urgent_message_title = findViewById(R.id.urgent_message_title);
        urgent_message_cardView = findViewById(R.id.urgent_message_cardView);
        fragmentManager = getSupportFragmentManager();
    }

    private void methods(Bundle savedInstanceState) {
//        homeBackgroundSetter();
        setDashboardFragment(savedInstanceState);
        homeButtonClick(savedInstanceState);
        urgentMessageGetter();
        bottomAppBarFunctionality();
        sideNavigationBarFunctionality();


    }

    private void sideNavigationBarFunctionality() {
        sideNavigationView_Home.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile_side_navigation_bar:
                        break;
                    case R.id.resources_side_navigation_bar:
                        break;
                    case R.id.societies_side_navigation_bar:
                        break;
                    case R.id.leaderboard_side_navigation_bar:
                        break;
                    case R.id.events_side_navigation_bar:
                        break;
                    case R.id.logout_side_navigation_bar:
                        gotoLoginActivity();
                        break;
                }

                return true;
            }
        });

    }

    private void bottomAppBarFunctionality() {
        bottomAppBar_Home.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                }

                return true;
            }
        });
    }


    private void urgentMessageGetter() {
        db.document(Db.BASE).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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


    private void homeBackgroundSetter() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            db.document(Db.getStaticImages())
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
            db.document(Db.getStaticImages())
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

    public void gotoLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}