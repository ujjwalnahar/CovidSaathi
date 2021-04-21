package com.projectupma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projectupma.R;
import com.projectupma.models.DizqusThreadModel;
import com.projectupma.utils.AnimationClass;

import java.util.List;

public class DizqusThreadTileAdapter extends RecyclerView.Adapter<DizqusThreadTileAdapter.ViewHolder> {

    private List<DizqusThreadModel> models;
    private Context context;
    private int size;

    public DizqusThreadTileAdapter() {
    }

    public DizqusThreadTileAdapter(List<DizqusThreadModel> models, Context context, int size) {
        this.models = models;
        this.context = context;
        this.size = size;

    }

    @NonNull
    @Override
    public DizqusThreadTileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dizqus_thread_recycler_tile_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DizqusThreadTileAdapter.ViewHolder holder, int position) {
        methods(holder, position);
    }

    private void methods(ViewHolder holder, int position) {

//        setData(holder, position);


    }

//    private void setData(ViewHolder holder, int position) {
//        JECNoticeModel model = models.get(position);
//        holder.notice_title_textView1.setText(model.getText());
//        holder.notice_title_textView2.setText(model.getText());
//        holder.notice_date_textView1.setText(model.getDate());
//        holder.notice_date_textView2.setText(model.getDate());
//    }


    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            AnimationClass.setAnimationTTB(itemView);
            initiator(itemView);
            methods();
        }

        private void initiator(View itemView) {
        }

        private void methods() {

        }


    }
}
