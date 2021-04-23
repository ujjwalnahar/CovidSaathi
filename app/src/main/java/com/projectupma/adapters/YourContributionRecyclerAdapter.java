package com.projectupma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.projectupma.R;
import com.projectupma.models.OnListItemClick;
import com.projectupma.models.Resource;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class YourContributionRecyclerAdapter extends RecyclerView.Adapter<YourContributionRecyclerAdapter.YourContributionViewHolder> {
    private Context context;
    private List<Resource> resourceList;
    private OnListItemClick onListItemClick;


    public YourContributionRecyclerAdapter(Context context, List<Resource> resourceList/* OnListItemClick onListItemClick*/) {
        this.context = context;
        this.resourceList = resourceList;
        //this.onListItemClick = onListItemClick;
    }
    public void setClickListener(OnListItemClick context) {
        this.onListItemClick = context;
    }

    @NonNull
    @Override
    public YourContributionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_your_contribution, parent, false);
YourContributionViewHolder viewHolder=new YourContributionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull YourContributionViewHolder holder, int position) {
        Resource resource=resourceList.get(position);
        holder.txtResourceName.setText(resource.getDoc_name());
        holder.txtSubject.setText(resource.getSubject_code());
        holder.txtType.setText(resource.getType());
        Timestamp stamp=resource.getDate();
        java.util.Date date =new Date(stamp.getSeconds()*1000L);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(date);
        holder.txtDate.setText(strDate);
        Picasso.get()
                .load(resource.getThumbnailUrl())
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_person_24)
                .into(holder.img_resource_thumbnail);
        holder.imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    public class YourContributionViewHolder extends RecyclerView.ViewHolder {
       ImageButton imgBtnEdit,imgBtnDelete;
        private TextView txtResourceName,txtSubject,txtDate,txtType;
        ImageView img_resource_thumbnail;
        public YourContributionViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBtnDelete=itemView.findViewById(R.id.img_btn_delete);
            imgBtnEdit=itemView.findViewById(R.id.img_btn_edit);
            txtDate=itemView.findViewById(R.id.txt_your_contribution_rcv_date);
            txtSubject=itemView.findViewById(R.id.txt_your_contribution_rcv_subject);
            txtResourceName=itemView.findViewById(R.id.txt_your_contribution_resource_name);
            txtType=itemView.findViewById(R.id.txt_your_contribution_rcv_type);
            img_resource_thumbnail=itemView.findViewById(R.id.img_resource_thumbnail_your_contribution);
        }
    }
}
