package com.covidsaathi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.activities.ProfileActivity;
import com.covidsaathi.utils.RandomCatcherClass;

public class TopBarFragment extends Fragment {


    TextView txt_profile_name;
    ImageView userImage_TopBarFragment_ImageView;
    CardView userImage_TopBarFragment_CardView;
    Handler handler = new Handler();

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
    private void initUI(View view) {
        txt_profile_name = view.findViewById(R.id.userName_TopBarFragment_TextView);
        userImage_TopBarFragment_ImageView = view.findViewById(R.id.userImage_TopBarFragment_ImageView);
        userImage_TopBarFragment_CardView = view.findViewById(R.id.userImage_TopBarFragment_CardView);
    }

    private void methods() {
        setData();
        onProfileClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfilePic();
    }

    private void onProfileClick() {
        userImage_TopBarFragment_CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoProfileActivity();
            }
        });
        txt_profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoProfileActivity();
            }
        });
    }


    private void gotoProfileActivity() {
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        getContext().startActivity(i);
    }

    private void setData() {
        final int delay = 1500;
        String[] alternatives = RandomCatcherClass.getRandomGreeting().split(", ");
        Runnable r;
        handler.postDelayed(r = new Runnable() {
            public void run() {
                //txt_profile_name.setText(alternatives[(int) (Math.random() * alternatives.length)] + "! " + Db.getUserModel().getName().split(" ")[0]);
                handler.postDelayed(this, delay);
            }
        }, delay);
        setProfilePic();
    }

    private void setProfilePic() {
       try {
           Glide.with(getActivity()).load(Db.getUserModel().getPhoto_url()).into(userImage_TopBarFragment_ImageView);
       }catch (Exception e){
           String[] alternatives = RandomCatcherClass.getRandomGreeting().split(", ");
           txt_profile_name.setText(alternatives[(int) (Math.random() * alternatives.length)] + " User");

       }

    }

}