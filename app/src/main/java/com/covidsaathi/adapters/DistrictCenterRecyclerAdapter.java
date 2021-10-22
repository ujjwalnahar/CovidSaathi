package com.covidsaathi.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.covidsaathi.R;
import com.covidsaathi.activities.CenterAvailabilityActivity;
import com.covidsaathi.models.CentersModel;
import com.covidsaathi.models.CentersResponse;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DistrictCenterRecyclerAdapter extends RecyclerView.Adapter<DistrictCenterRecyclerAdapter.ViewHolder> {
    List<CentersResponse> listCenters;
    Context context;

    public DistrictCenterRecyclerAdapter(List<CentersResponse> listCenters, Context context) {
        this.listCenters = listCenters;
        this.context = context;
    }

    @NonNull
    @Override
    public DistrictCenterRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DistrictCenterRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_district_centers, parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull DistrictCenterRecyclerAdapter.ViewHolder holder, int position) {
        holder.centerName.setText(listCenters.get(position).getName());
        holder.centerAddress.setText(listCenters.get(position).getAddress()+","+listCenters.get(position).getPincode());
        holder.centerVaccinationName.setText(listCenters.get(position).getVaccine());
        Log.d("12323", "onBindViewHolder: "+listCenters.get(position).getDate());
        holder.btn_check_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, CenterAvailabilityActivity.class);
                i.putExtra("center",listCenters.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCenters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  centerName,centerAddress,centerVaccinationName;
        MaterialButton btn_check_availability;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI(itemView);
        }
        private void initUI(View itemView) {
            centerName=itemView.findViewById(R.id.centerName);
            centerAddress=itemView.findViewById(R.id.centerAddress);
            centerVaccinationName=itemView.findViewById(R.id.centerVaccinationName);
            btn_check_availability=itemView.findViewById(R.id.btn_check_availability);
        }


    }
}
