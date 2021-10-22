package com.covidsaathi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.covidsaathi.activities.NewHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.activities.DizqusThreadActivity;
import com.covidsaathi.activities.DizqusCommentActivity;
import com.covidsaathi.models.ThreadModel;
import com.covidsaathi.models.UserModel;
import com.covidsaathi.utils.AppHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DizqusThreadMainItemAdatper extends RecyclerView.Adapter<DizqusThreadMainItemAdatper.ViewHolder> {
    List<ThreadModel> dizqusThreadModels;
    Context context;
    HashMap<String, Integer> userIndex;
    List<String> threadIds;
    UserModel userModel = new UserModel();

    public DizqusThreadMainItemAdatper(List<ThreadModel> dizqusThreadModels, Context context, List<String> threadIds) {
        this.dizqusThreadModels = dizqusThreadModels;
        this.context = context;
        this.threadIds = threadIds;
    }

    @NonNull
    @Override
    public DizqusThreadMainItemAdatper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_disquz_main, parent, false);
        DizqusThreadMainItemAdatper.ViewHolder viewHolder = new DizqusThreadMainItemAdatper.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull DizqusThreadMainItemAdatper.ViewHolder holder, int position) {
        holder.methods(dizqusThreadModels.get(position), threadIds.get(position), position);
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public int getItemCount() {
        return dizqusThreadModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfilePhotoDizcusThreadItem;
        ImageView imgPostDizcusThreadItem;
        LinearLayout parentDisquzThreadItem;
        ImageView imageView_ProfileBackground_DizcusThreadItem;
        ImageView imageView_like_dizqus_main;
        TextView txtUsernameDisquzThreadItem;
        TextView txtDateDisquzThreadItem;
        TextView txtBranchDisquzThreadItem;
        TextView txtTitleDiscuzThreadItem;
        TextView txtDescriptionDiscuzThreadItem;
        TextView no_likes_dizqus_main;
        TextView readMoreDiscuzThreadItem;
        BottomNavigationView bottom_navigation_dizqus_item;
        MaterialCardView moreOptionsDizqusThreadItem;
        CharSequence[] items;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initUI(itemView);
        }

        private void initUI(View itemView) {
            imgProfilePhotoDizcusThreadItem = itemView.findViewById(R.id.imgProfilePhotoDizcusThreadItem);
            txtUsernameDisquzThreadItem = itemView.findViewById(R.id.txtUsernameDisquzThreadItem);
            txtDateDisquzThreadItem = itemView.findViewById(R.id.txtDateDisquzThreadItem);
            moreOptionsDizqusThreadItem = itemView.findViewById(R.id.moreOptionsDizqusThreadItem);
            txtBranchDisquzThreadItem = itemView.findViewById(R.id.txtBranchDisquzThreadItem);
            txtTitleDiscuzThreadItem = itemView.findViewById(R.id.txtTitleDiscuzThreadItem);
            imgPostDizcusThreadItem = itemView.findViewById(R.id.imgPostDizcusThreadItem);
            no_likes_dizqus_main = itemView.findViewById(R.id.no_likes_dizqus_main);
            readMoreDiscuzThreadItem = itemView.findViewById(R.id.readMoreDiscuzThreadItem);
            imageView_ProfileBackground_DizcusThreadItem = itemView.findViewById(R.id.imageView_ProfileBackground_DizcusThreadItem);
            txtDescriptionDiscuzThreadItem = itemView.findViewById(R.id.txtDescriptionDiscuzThreadItem);
            parentDisquzThreadItem = itemView.findViewById(R.id.parentDisquzThreadItem);
            imageView_like_dizqus_main = itemView.findViewById(R.id.imageView_like_dizqus_main);


        }

        public void methods(ThreadModel dizqusThreadModel, String threadID, int position) {
            setData(dizqusThreadModel);
            onLikeImage(dizqusThreadModel.getLikes(), threadID);
            openPost(dizqusThreadModel, threadID);
            openMoreDialog(dizqusThreadModel, threadID, position);


        }

        private void openMoreDialog(ThreadModel dizqusThreadModel, String threadID, int position) {
            String flag = "Flag as inappropriate";
            if (dizqusThreadModel.getInappropriate().contains(Db.getUserModel().getUser_id()))
                flag = "Remove (Flagged as inappropriate)";

            if (dizqusThreadModel.getUser_id().equals(Db.getUserModel().getUser_id()))
                items = new CharSequence[]{flag, "Delete"};
            else items = new CharSequence[]{flag};


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("More");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int pos) {
                    switch (items[pos].toString()) {
                        case "Flag as inappropriate": {
                            List<String> list = dizqusThreadModel.getInappropriate();
                            list.add(Db.getUserModel().getUser_id());
                            dizqusThreadModel.setInappropriate(list);
                            Db.db.collection(Db.DIZCUS_THREAD_COL).document(threadID).update("inappropriate", list);
                            break;
                        }
                        case "Remove (Flagged as inappropriate)": {
                            List<String> list = dizqusThreadModel.getInappropriate();
                            list.remove(Db.getUserModel().getUser_id());
                            dizqusThreadModel.setInappropriate(list);
                            Db.db.collection(Db.DIZCUS_THREAD_COL).document(threadID).update("inappropriate", list);
                            break;
                        }
                        case "Delete": {
                            Db.db.collection(Db.DIZCUS_THREAD_COL).document(threadID).delete();
                            dizqusThreadModels.remove(dizqusThreadModel);
                            notifyItemRemoved(position);
                            break;
                        }
                    }
                    dialog.dismiss();
                    notifyItemChanged(position);
                    Toast.makeText(context, "" + items[pos], Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alert = builder.create();
            moreOptionsDizqusThreadItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.show();
                }
            });

        }

        private void openPost(ThreadModel dizqusThreadModel, String threadId) {
            parentDisquzThreadItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DizqusCommentActivity.class);
                    String transitionName = context.getString(R.string.transition_string);
                    i.putExtra("thread_id", threadId);
                    i.putExtra("user_id", userModel.getUser_id());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, parentDisquzThreadItem, transitionName);
                    ActivityCompat.startActivity(context, i, optionsCompat.toBundle());

                }
            });
        }

        private void onLikeImage(ArrayList<String> likes, String threadID) {
            if (likes.contains(Db.getUserModel().getUser_id())) {
                imageView_like_dizqus_main.setImageResource(R.drawable.like_icon);
            } else imageView_like_dizqus_main.setImageResource(R.drawable.not_like_icon);


            imageView_like_dizqus_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (likes.contains(Db.getUserModel().getUser_id())) {
                        imageView_like_dizqus_main.setImageResource(R.drawable.not_like_icon);
                        likes.remove(Db.getUserModel().getUser_id());
                    } else {
                        imageView_like_dizqus_main.setImageResource(R.drawable.like_icon);
                        likes.add(Db.getUserModel().getUser_id());
                    }
                    String upvoteText=" Upvotes";
                    if(likes.size()==1) upvoteText=" Upvote";
                    no_likes_dizqus_main.setText(likes.size() + upvoteText);
                    Db.db.collection(Db.DIZCUS_THREAD_COL).document(threadID).update("likes", likes);
                }
            });
        }

        private void setData(ThreadModel dizqusThreadModel) {
//            String threadId = threadIds.get(position);
//            UserModel userModel = userModels.get(position);
            Db.db.collection(Db.USER_COL).whereEqualTo("user_id", dizqusThreadModel.getUser_id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document1 : task.getResult()) {
                            UserModel userModel = document1.toObject(UserModel.class);
                            //userModels.add(userModel);
                            setUserModel(userModel);
                            Log.d("202011", "onComplete: something");
                            //Picasso.get().load(userModel.getPhoto_url()).into(imgProfilePhotoDizcusThreadItem);
                            txtUsernameDisquzThreadItem.setText(userModel.getName());
                          //  Glide.with(context).load(Db.PROFILE_BACKGROUND_URLS.get(userModel.getProfile_background())).centerCrop().into(imageView_ProfileBackground_DizcusThreadItem);
                        }
                    } else {
                    }
                }
            });
            if(dizqusThreadModel!=null){
                txtBranchDisquzThreadItem.setText(dizqusThreadModel.getType());
                txtDateDisquzThreadItem.setText(AppHelper.getDateFromTimestamp(dizqusThreadModel.getDate()));
                txtDescriptionDiscuzThreadItem.setText(dizqusThreadModel.getDescription());

            }
            if (dizqusThreadModel.getDescription().split(" ").length > 25)
                readMoreDiscuzThreadItem.setVisibility(View.VISIBLE);
            else
                readMoreDiscuzThreadItem.setVisibility(View.GONE);
            txtTitleDiscuzThreadItem.setText(dizqusThreadModel.getQuestion());
            no_likes_dizqus_main.setText(dizqusThreadModel.getLikes().size() + " Likes");
            {
                if (dizqusThreadModel.getImageUrl() != null) {
                    if (!dizqusThreadModel.getImageUrl().isEmpty()) {
                        Glide.with(context).load(dizqusThreadModel.getImageUrl()).fitCenter().into(imgPostDizcusThreadItem);
                        imgPostDizcusThreadItem.setVisibility(View.VISIBLE);
                    } else imgPostDizcusThreadItem.setVisibility(View.GONE);
                } else imgPostDizcusThreadItem.setVisibility(View.GONE);
                Point x = new Point();
                ((NewHomeActivity) context).getWindowManager().getDefaultDisplay().getSize(x);
                imgPostDizcusThreadItem.setMaxHeight((int) (x.y * 0.6));
            }
        }
    }
}
