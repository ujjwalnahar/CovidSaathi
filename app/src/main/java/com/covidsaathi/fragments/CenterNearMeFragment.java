package com.covidsaathi.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.covidsaathi.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;


public class CenterNearMeFragment extends Fragment {
    private GoogleMap mMap;
private Activity context;
    public CenterNearMeFragment(Activity context) {
        this.context=context;
        // Required empty public constructor
    }


    public static CenterNearMeFragment newInstance(Activity context) {
        CenterNearMeFragment fragment = new CenterNearMeFragment(context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_center_near_me, container, false);
    }
}