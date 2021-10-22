package com.covidsaathi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.covidsaathi.R;
import com.covidsaathi.fragments.ComplaintsFragment;
import com.covidsaathi.fragments.VaccinationFragment;

public class NewHomeActivity extends AppCompatActivity {
    ViewPager mViewPagerHome;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        initiators();
    }
    void initiators(){
        mViewPagerHome=findViewById(R.id.mViewPagerHome);
        tabLayout=findViewById(R.id.mTabLayoutHome);
        ViewPagerAdapter mViewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerHome.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(mViewPagerHome);
    }
    public class ViewPagerAdapter
            extends FragmentPagerAdapter {

        public ViewPagerAdapter(
                @NonNull FragmentManager fm)
        {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = null;
            if (position == 0)
                fragment = new ComplaintsFragment();
            else if( position == 1)
                fragment = new VaccinationFragment(NewHomeActivity.this);

            return fragment;
        }

        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            String title = null;
            if (position == 0)
                title = "Complaints";
            if(position == 1)
                title="Vaccination Centers";
            return title;
        }
    }
}