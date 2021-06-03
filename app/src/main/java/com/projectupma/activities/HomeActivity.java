package com.projectupma.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.fragments.DashboardFragment;
import com.projectupma.utils.AppHelper;

public class HomeActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    //AppHelper appHelper = AppHelper.getInstance();
    //global initializes
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //local initializes
    Bundle savedInstanceState;
    ImageView home_background_imageView;
    FloatingActionButton home_FAB;
    FrameLayout dashboard_frameLayout;
    NestedScrollView nested_home_scrollView;
    MaterialCardView urgent_message_cardView;
    TextView urgent_message_title, urgent_message_text;
    BottomAppBar bottomAppBar_Home;
    DrawerLayout home_DrawerLayout;
    Menu menu;
    boolean doubleBackToExitPressedOnce = false;
    private String currentFragment;

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
        home_FAB = findViewById(R.id.home_FAB);
        home_background_imageView = findViewById(R.id.home_background_imageView);
        dashboard_frameLayout = findViewById(R.id.dashboard_frameLayout);
        bottomAppBar_Home = findViewById(R.id.bottomAppBar_Home);
        nested_home_scrollView = findViewById(R.id.nested_home_scrollView);
        urgent_message_text = findViewById(R.id.urgent_message_text);
        urgent_message_title = findViewById(R.id.urgent_message_title);
        urgent_message_cardView = findViewById(R.id.urgent_message_cardView);
        home_DrawerLayout = findViewById(R.id.home_DrawerLayout);
        fragmentManager = getSupportFragmentManager();
    }

    private void methods(Bundle savedInstanceState) {
//        homeBackgroundSetter();
        setDashboardFragment(savedInstanceState);
        homeButtonClick(savedInstanceState);
        urgentMessageGetter();
        bottomAppBarFunctionality();


    }

    private void bottomAppBarFunctionality() {
        bottomAppBar_Home.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.theme_menu: {
                        switchThemes();
                        break;
                    }
                }
                return true;
            }
        });

    }

    private void urgentMessageGetter() {
        db.document(Db.BASE_DOC).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
                    replaceFragments(DashboardFragment.class);



                }
                nested_home_scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void setDashboardFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) return;
        replaceFragments(DashboardFragment.class);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void switchThemes() {

        if (AppHelper.isNightMode()) {
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

    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        currentFragment = fragmentClass.getSimpleName();

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard_frameLayout, fragment)
                .commit();
    }

}