package com.projectupma.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.utils.RandomCatcherClass;

import java.util.ArrayList;

public class TopBarFragment extends Fragment {


    TextView txt_profile_name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_bar, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initUI(view);
        methods();
    }

    private void methods() {
        setName();
    }

    private void setName() {
        String[] alternatives = RandomCatcherClass.getRandomGreeting().split(", ");
        txt_profile_name.setText(alternatives[(int)(Math.random() * alternatives.length)]+"! " + Db.getUserModel().getName());
        txt_profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setName();
            }
        });

    }

    private void initUI(View view) {
        txt_profile_name = view.findViewById(R.id.txt_profile_name);
    }
}