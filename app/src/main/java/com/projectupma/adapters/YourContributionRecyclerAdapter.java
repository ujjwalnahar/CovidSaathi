package com.projectupma.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.activities.AddResourceActivity;
import com.projectupma.models.ResourceModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class YourContributionRecyclerAdapter extends RecyclerView.Adapter<YourContributionRecyclerAdapter.YourContributionViewHolder> {
    private Context context;
    private List<ResourceModel> resourceModelList;
    private List<String> resourceIdList;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();


    public YourContributionRecyclerAdapter(Context context, List<ResourceModel> resourceModelList, List<String> ResourceIdList) {
        this.context = context;
        this.resourceModelList = resourceModelList;
        this.resourceIdList=ResourceIdList;
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
        ResourceModel resourceModel = resourceModelList.get(position);
        holder.txtResourceName.setText(resourceModel.getDoc_name());
        holder.txtSubject.setText(resourceModel.getSubject_code());
        holder.txtType.setText(resourceModel.getType());
        Timestamp stamp= resourceModel.getDate();
        java.util.Date date =new Date(stamp.getSeconds()*1000L);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(date);
        holder.txtDate.setText(strDate);
        Picasso.get()
                .load(resourceModel.getThumbnailUrl())
                .placeholder(R.drawable.ic_baseline_person_24)
                .error(R.drawable.ic_baseline_person_24)
                .into(holder.img_resource_thumbnail);
        holder.imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent intent=new Intent(context, AddResourceActivity.class);
intent.putExtra("resourceId",resourceIdList.get(position));
intent.putExtra("intentType","edit");
context.startActivity(intent);
            }
        });
        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection(Db.RESOURCES_DOC +"/resourceModels").document(resourceIdList.get(position)).delete();
            }
        });
    }

    @Override
    public int getItemCount() {
        return resourceModelList.size();
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
