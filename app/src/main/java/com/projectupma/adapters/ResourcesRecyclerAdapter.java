package com.projectupma.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.projectupma.DataClasses.Resource;
import com.projectupma.R;
import com.projectupma.activities.HomeActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourcesRecyclerAdapter extends RecyclerView.Adapter<ResourcesRecyclerAdapter.ResourcesRecyclerViewHolder> {
    List<Resource> resources;
    Context context;
    Map<String, String> contributionMap;

    public ResourcesRecyclerAdapter(Context context, List<Resource> resourceList, HashMap<String, String> contributionMap) {
        this.context = context;
        this.resources = resourceList;
        this.contributionMap = contributionMap;
    }

    @NonNull
    @Override
    public ResourcesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_resources, parent, false);
        ResourcesRecyclerAdapter.ResourcesRecyclerViewHolder holder = new ResourcesRecyclerAdapter.ResourcesRecyclerViewHolder(view);
        return new ResourcesRecyclerAdapter.ResourcesRecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ResourcesRecyclerViewHolder holder, int position) {
        Resource resource = resources.get(position);
        holder.txtDocName.setText(resource.getDoc_name());
        Timestamp stamp=resource.getDate();
        java.util.Date date =new Date(stamp.getSeconds()*1000L);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(date);
        holder.txtDate.setText(strDate);
        String name="Contributed By: " + contributionMap.get(resource.getUserId());
        Log.d("000", "onBindViewHolder: called" + resource.getDoc_link()+contributionMap.get(resource.getUserId())+contributionMap.toString());

        holder.txtContributedBy.setText(name);
        Picasso.get()
                .load(resource.getThumbnailUrl())
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_person_24)
                .into(holder.imgResourceThumbnail);

    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    class ResourcesRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtDocName, txtDate, txtContributedBy;
        ImageView imgResourceThumbnail;
        ImageView imgTypeIcon;

        ResourcesRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txt_doc_name);
            txtDate = itemView.findViewById(R.id.txt_resource_date);
            txtContributedBy = itemView.findViewById(R.id.txt_resource_contributor_name);
            imgResourceThumbnail = itemView.findViewById(R.id.img_resource_thumbnail);
            imgTypeIcon = itemView.findViewById(R.id.img_item_type_icon);


        }
    }
}
