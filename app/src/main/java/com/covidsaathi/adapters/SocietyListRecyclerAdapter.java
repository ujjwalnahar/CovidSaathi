package com.covidsaathi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.covidsaathi.R;
import com.covidsaathi.activities.DizqusThreadActivity;
import com.covidsaathi.models.SocietyModel;

import java.util.List;

public class SocietyListRecyclerAdapter  extends RecyclerView.Adapter<SocietyListRecyclerAdapter.ViewHolder>{
    List<SocietyModel> societyModels;
    Context context;

    public SocietyListRecyclerAdapter(List<SocietyModel> societyModels, Context context) {
        this.societyModels = societyModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_society, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
SocietyModel societyModel=societyModels.get(position);
holder.methods(societyModel,position);
    }

    @Override
    public int getItemCount() {
        return societyModels.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_society_name;
        MaterialCardView cardView_Img_Society;
        ImageView imageViewSociety;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI(itemView);
        }


        private void initUI(View itemView) {
            cardView_Img_Society=itemView.findViewById(R.id.cardView_Img_Society);
            imageViewSociety=itemView.findViewById(R.id.imageViewSociety);
            txt_society_name=itemView.findViewById(R.id.txt_society_name);
        }

        private void methods(SocietyModel societyModel, int position) {
            txt_society_name.setText(societyModel.getName());
            Glide.with(context).load(societyModel.getPhotoLink()).centerCrop().into(imageViewSociety);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SocietyModel societyModel1=societyModels.get(position);
                    Intent i=new Intent(context, DizqusThreadActivity.class);
                    i.putExtra("ActivityType","SocietyThread");
                    i.putExtra("SocietyName",societyModel1.getName());
                    i.putExtra("SocietyId",societyModel1.getClubId());
                    i.putExtra("PhotoLink",societyModel1.getPhotoLink());
                    context.startActivity(i);

                }
            });
        }
    }
}
