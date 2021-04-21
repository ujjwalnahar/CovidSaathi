package com.projectupma.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.models.TimeTableModel;
import com.projectupma.models.UserModel;

import java.util.Map;

public class TimeTablePagerAdapter extends PagerAdapter {
    //global initializes
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Context context;
    RecyclerView recyclerView;
    UserModel userModel;
    TimeTableModel timeTableModel;

    public TimeTablePagerAdapter(Context context, TimeTableModel timeTableModel) {
        this.context = context;
        this.userModel = userModel;
        this.timeTableModel = timeTableModel;
        Log.d("Mayurs log", "mayurs data: " + timeTableModel.toString());

    }



    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = (ViewGroup) inflater.inflate(R.layout.timetable_pager_layout, container, false);
        recyclerView = itemView.findViewById(R.id.timetable_RecyclerView);
        container.addView(itemView);
        methods(position);
        return itemView;
    }

    private void methods(int position) {
        recyclerViewInitiate(position);
    }

    private void recyclerViewInitiate(int position) {
        String days[]= {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        switch (days[position]) {
            case "Monday":
                recyclerView.setAdapter(new TimeTableDayAdapter(context, timeTableModel.getMonday()));
                break;
            case "Tuesday":
                recyclerView.setAdapter(new TimeTableDayAdapter(context, timeTableModel.getTuesday()));
                break;
            case "Wednesday":
                recyclerView.setAdapter(new TimeTableDayAdapter(context, timeTableModel.getWednesday()));
                break;
            case "Thursday":
                recyclerView.setAdapter(new TimeTableDayAdapter(context, timeTableModel.getThursday()));
                break;
            case "Friday":
                recyclerView.setAdapter(new TimeTableDayAdapter(context, timeTableModel.getFriday()));
                break;
            case "Saturday":
                recyclerView.setAdapter(new TimeTableDayAdapter(context, timeTableModel.getSaturday()));
                break;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String days[]= {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return days[position];
    }
}
