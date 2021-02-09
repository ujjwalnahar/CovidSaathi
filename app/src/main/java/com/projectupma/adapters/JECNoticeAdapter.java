package com.projectupma.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.projectupma.R;
import com.projectupma.models.JECNoticeModel;

import java.util.List;

public class JECNoticeAdapter extends RecyclerView.Adapter<JECNoticeAdapter.ViewHolder> {

    private List<JECNoticeModel> models;
    private Context context;
    private int size;

    public JECNoticeAdapter() {
    }

    public JECNoticeAdapter(List<JECNoticeModel> models, Context context, int size) {
        this.models = models;
        this.context = context;
        this.size = size;

    }

    @NonNull
    @Override
    public JECNoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jec_notice_recycler_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JECNoticeAdapter.ViewHolder holder, int position) {
        methods(holder, position);

    }

    private void methods(ViewHolder holder, int position) {

        randomRotation(holder, position);
        setData(holder, position);
        removeSecondary(holder, position);
        onMoreClick(holder, position);
        moveCardView(holder, position);
        moveCardViewOnCardClick(holder, position);
        viewMoreTextOnClick(holder,position);



    }

    private void viewMoreTextOnClick(ViewHolder holder, int position) {
        holder.notice_title_textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.notice_title_textView1.getMaxLines()>2){
                    holder.notice_title_textView1.setMaxLines(2);
                }
                else {
                    holder.notice_title_textView1.setMaxLines(15);
                }
            }
        });
        holder.notice_title_textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.notice_title_textView2.getMaxLines()>2){
                    holder.notice_title_textView2.setMaxLines(2);
                }
                else {
                    holder.notice_title_textView2.setMaxLines(15);
                }
            }
        });
    }

    private void moveCardViewOnCardClick(ViewHolder holder, int position) {
        holder.notice_rotate_cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveCardView(holder,position);
                randomRotation(holder, position);

            }
        });holder.notice_rotate_cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveCardView(holder,position);
                randomRotation(holder, position);

            }
        });
    }

    private void moveCardView(ViewHolder holder, int position) {


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.moving_cardView.getLayoutParams();
        float d = context.getResources().getDisplayMetrics().density;
        params.setMargins(0, (int) (20*d + Math.random() * 30*d), 0, 0);
        if (position % 2 != 0)
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
        holder.moving_cardView.setLayoutParams(params);

    }

    private void onMoreClick(ViewHolder holder, int position) {
        holder.notice_more_imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Position Selected" + position, Toast.LENGTH_SHORT).show();

            }
        });
        holder.notice_more_imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Position Selected" + position, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void removeSecondary(ViewHolder holder, int position) {
        if (position % 2 != 0) {
            holder.notice_item_cardView1.setVisibility(View.GONE);
        } else {
            holder.notice_item_cardView2.setVisibility(View.GONE);
        }
    }

    private void setData(ViewHolder holder, int position) {
        JECNoticeModel model=models.get(position);
        holder.notice_title_textView1.setText(model.getText());
        holder.notice_title_textView2.setText(model.getText());
        holder.notice_date_textView1.setText(model.getDate());
        holder.notice_date_textView2.setText(model.getDate());
    }

    private void randomRotation(ViewHolder holder, int position) {
        holder.notice_rotate_cardView1.setRotation((float) (Math.random() * 180));
        holder.notice_rotate_cardView2.setRotation((float) (Math.random() * -180));
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView moving_cardView;
        MaterialCardView notice_item_cardView1;
        MaterialCardView notice_rotate_cardView1;
        TextView notice_title_textView1;
        TextView notice_date_textView1;
        ImageView notice_more_imageView1;
        MaterialCardView notice_item_cardView2;
        MaterialCardView notice_rotate_cardView2;
        TextView notice_title_textView2;
        TextView notice_date_textView2;
        ImageView notice_more_imageView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initiator(itemView);
            methods();
        }

        private void initiator(View itemView) {
            moving_cardView = itemView.findViewById(R.id.moving_cardView);
            notice_item_cardView1 = itemView.findViewById(R.id.notice_item_cardView1);
            notice_rotate_cardView1 = itemView.findViewById(R.id.notice_rotate_cardView1);
            notice_title_textView1 = itemView.findViewById(R.id.notice_title_textView1);
            notice_date_textView1 = itemView.findViewById(R.id.notice_date_textView1);
            notice_more_imageView1 = itemView.findViewById(R.id.notice_more_imageView1);
            notice_rotate_cardView2 = itemView.findViewById(R.id.notice_rotate_cardView2);
            notice_item_cardView2 = itemView.findViewById(R.id.notice_item_cardView2);
            notice_title_textView2 = itemView.findViewById(R.id.notice_title_textView2);
            notice_date_textView2 = itemView.findViewById(R.id.notice_date_textView2);
            notice_more_imageView2 = itemView.findViewById(R.id.notice_more_imageView2);
        }

        private void methods() {

        }


    }
}
