package com.projectupma.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.projectupma.R;
import com.projectupma.utils.AnimationClass;
import com.projectupma.utils.RandomCatcherClass;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class TimeTableDayAdapter extends RecyclerView.Adapter<TimeTableDayAdapter.ViewHolder> {
    private Context context;
    private Map<String, ArrayList<String>> day;
    private String times[];

    public TimeTableDayAdapter(Context context, Map<String, ArrayList<String>> day) {
        this.context = context;
        this.day = sortbykey(day);
        this.times = this.day.keySet().toArray(new String[day.size()]);


    }

    public Map<String, ArrayList<String>> sortbykey(Map<String, ArrayList<String>> day) {
        // TreeMap to store values of HashMap
        TreeMap<String, ArrayList<String>> sorted = new TreeMap<>();
        sorted.putAll(day);

        return sorted;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_recycler_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (times[position].length() > 0) {
            holder.holder_methods(times[position], day.get(times[position]).get(0));
        }


    }

    @Override
    public int getItemCount() {
        return times.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_class_timetable;
        public TextView time_class_timetable;
        public TextView text_icon_class_timetable;
        public MaterialCardView rotatingCardView_Timetable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI(itemView);
            methods(itemView);
            setAnimation(itemView);
        }

        private void methods(View itemView) {
            randomizeRotatingColors(itemView);
        }

        private void randomizeRotatingColors(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeRotatingColors();
                }
            });
        }

        public void setAnimation(View itemView) {
            AnimationClass.setAnimationLTR(itemView);
        }

        public void initUI(View itemView) {
            text_class_timetable = itemView.findViewById(R.id.text_class_timetable);
            time_class_timetable = itemView.findViewById(R.id.time_class_timetable);
            rotatingCardView_Timetable = itemView.findViewById(R.id.rotatingCardView_Timetable);

        }

        public void holder_methods(String time, String subject) {
            setData(time, subject);
            changeRotatingColors();
        }

        public void changeRotatingColors() {
            TypedArray ta = RandomCatcherClass.getColorList(context);
            rotatingCardView_Timetable.setCardBackgroundColor(ta.getColor((int) (Math.random() * ta.length()), 0));
        }

        public void setData(String time, String subject) {
            String formattedTime = formatTime(time);
            time_class_timetable.setText(formattedTime);
            text_class_timetable.setText(subject);
        }

        private String formatTime(String time) {
            String half;
            if (time.charAt(0) == 'A') {
                half = "AM";
            } else {
                half = "PM";
            }
            int timeInt = Integer.parseInt(time.substring(2, 4));
            if (timeInt == 0) {
                timeInt = 12;
                half = "PM";
            }
            String finalTime = timeInt + " " + half;
            return finalTime;

        }
    }
}
