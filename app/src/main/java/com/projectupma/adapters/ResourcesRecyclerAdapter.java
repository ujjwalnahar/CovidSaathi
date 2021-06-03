package com.projectupma.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.projectupma.R;
import com.projectupma.models.ResourceModel;
import com.projectupma.utils.RandomCatcherClass;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResourcesRecyclerAdapter extends RecyclerView.Adapter<ResourcesRecyclerAdapter.ViewHolder> {
    List<ResourceModel> resourceModels;
    Context context;

    public ResourcesRecyclerAdapter(Context context, ArrayList<ResourceModel> resourceModelList) {
        this.context = context;
        this.resourceModels = resourceModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_resources, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.methods(position, resourceModels.get(position));


    }

    @Override
    public int getItemCount() {
        return resourceModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDocName, txtDate, txt_doc_downloads;
        ImageView imgResourceThumbnail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txt_doc_name);
            txt_doc_downloads = itemView.findViewById(R.id.txt_doc_downloads);
            txtDate = itemView.findViewById(R.id.txt_resource_date);
            imgResourceThumbnail = itemView.findViewById(R.id.img_resource_thumbnail);

        }

        private void methods(int pos, ResourceModel resourceModel) {
            changeRotatingColors(pos);
            setData(resourceModel);
        }

        private void setData(ResourceModel resourceModel) {
            Timestamp stamp = resourceModel.getDate();
            java.util.Date date = new Date(stamp.getSeconds() * 1000L);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = formatter.format(date);
            txtDate.setText(strDate);
            txtDocName.setText(resourceModel.getDoc_name());

            int one;
            if (Math.random() < 0.5)
                one = 0;
            else one = 1;

            Picasso.get()
                    .load(resourceModel.getThumbnailUrl()).rotate(180 * one)
                    .into(imgResourceThumbnail);
        }

        public void changeRotatingColors(int pos) {
            TypedArray ta = RandomCatcherClass.getColorList(context);
            int color = ta.getColor(pos % ta.length(), 0);
            txtDocName.setBackgroundColor(color);
            txt_doc_downloads.setBackgroundColor(color);
        }

    }
}
