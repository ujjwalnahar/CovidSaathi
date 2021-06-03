package com.projectupma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.projectupma.Db;
import com.projectupma.R;

import java.util.ArrayList;

public class ProfileBackgroundSelectorAdapter extends RecyclerView.Adapter<ProfileBackgroundSelectorAdapter.ViewHolder> {
    static int current;
    ArrayList<String> urls;
    Context context;

    public ProfileBackgroundSelectorAdapter(ArrayList<String> urls, int current, Context context) {
        this.urls = urls;
        this.current = current;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_profile_background_selector, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.methods(urls.get(position), position == current, position);

    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView profile_Background_Item_CardView;
        ImageView profile_Background_Item_ImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI(itemView);
        }

        private void initUI(View itemView) {
            profile_Background_Item_ImageView = itemView.findViewById(R.id.profile_Background_Item_ImageView);
            profile_Background_Item_CardView = itemView.findViewById(R.id.profile_Background_Item_CardView);
        }


        private void methods(String url, boolean selected, int pos) {
            setData(url, selected);
            onImageSelect(pos);
        }

        private void onImageSelect(int pos) {
            profile_Background_Item_CardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Db.userModel.setProfile_background(pos);
                    profile_Background_Item_CardView.setStrokeWidth(5);
                    Db.db.document(Db.getUserDoc(Db.getUserModel().getUser_id())).set(Db.userModel);
                }
            });
        }

        private void setData(String url, boolean selected) {
            Glide.with(context).load(url).into(profile_Background_Item_ImageView);
            if (selected) {
//                profile_Background_Item_CardView.setStrokeWidth(4);
            }
        }
    }
}
