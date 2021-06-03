package com.projectupma.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.models.CommentModel;
import com.projectupma.models.UserModel;
import com.projectupma.utils.AnimationClass;
import com.projectupma.utils.AppHelper;
import com.projectupma.utils.RandomCatcherClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DizqusCommentItemAdapter extends RecyclerView.Adapter<DizqusCommentItemAdapter.ViewHolder> {
    Context context;
    List<CommentModel> commentModels;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> commentThreadIds;
    UserModel userModel;
    private int depth;

    public DizqusCommentItemAdapter(Context context, List<CommentModel> comments, List<String> commentThreadIds, int depth, UserModel userModel) {
        this.context = context;
        this.commentModels = comments;
        this.commentThreadIds = commentThreadIds;
        this.userModel = userModel;
        this.depth = depth;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_dizqus_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentModel commentModel = commentModels.get(position);
        Log.d("TAG", "onComplete: 12341234 mod SIZE " + commentModels.size());

        holder.methods(commentModel, position, commentThreadIds.get(position));

    }

    @Override
    public int getItemCount() {

        return commentModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String TAG = "DizqusCommentItemAdapter";
        ImageView imageView_ProfilePhoto_DizqusCommentItem;
        ImageView imageView_ProfileBackground_DizqusCommentItem;
        ImageView imageView_like_DizqusCommentItem;
        TextView textView_UserName_DizqusComment;
        View view_sideBar_DizqusCommentItem;
        TextView textView_date_DizqusCommentItem;
        TextView textView_Comment_DizqusComment;
        TextView textView_Likes_DizqusCommentItem;
        TextView textView_reply_DizqusCommentItem;
        TextView textView_ReplyTo_DizqusCommentItem;
        TextView textView_ShowReplies_DizqusCommentItem;
        LinearLayout linearLayout_onReply_DizqusCommentItem;
        MaterialButton button_sendReply_DizqusCommentItem;
        RecyclerView recyclerView_DizqusCommentItem;
        EditText editText_onReply_DizqusCommentItem;
        TextView txtBranchDisquzCommentItem;
        String threadID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            AnimationClass.setAnimationLTR(itemView);

            initUI(itemView);
        }

        private void initUI(View itemView) {
            imageView_ProfilePhoto_DizqusCommentItem = itemView.findViewById(R.id.imageView_ProfilePhoto_DizqusCommentItem);
            textView_UserName_DizqusComment = itemView.findViewById(R.id.textView_UserName_DizqusCommentItem);
            textView_Comment_DizqusComment = itemView.findViewById(R.id.textView_Comment_DizqusCommentItem);
            imageView_ProfileBackground_DizqusCommentItem = itemView.findViewById(R.id.imageView_ProfileBackground_DizqusCommentItem);
            textView_date_DizqusCommentItem = itemView.findViewById(R.id.textView_date_DizqusCommentItem);
            view_sideBar_DizqusCommentItem = itemView.findViewById(R.id.view_sideBar_DizqusCommentItem);
            button_sendReply_DizqusCommentItem = itemView.findViewById(R.id.button_sendReply_DizqusCommentItem);
            textView_Likes_DizqusCommentItem = itemView.findViewById(R.id.textView_Likes_DizqusCommentItem);
            linearLayout_onReply_DizqusCommentItem = itemView.findViewById(R.id.linearLayout_onReply_DizqusCommentItem);
            imageView_like_DizqusCommentItem = itemView.findViewById(R.id.imageView_like_DizqusCommentItem);
            textView_reply_DizqusCommentItem = itemView.findViewById(R.id.textView_reply_DizqusCommentItem);
            recyclerView_DizqusCommentItem = itemView.findViewById(R.id.recyclerView_DizqusCommentItem);
            editText_onReply_DizqusCommentItem = itemView.findViewById(R.id.editText_onReply_DizqusCommentItem);
            textView_ShowReplies_DizqusCommentItem = itemView.findViewById(R.id.textView_ShowReplies_DizqusCommentItem);
            txtBranchDisquzCommentItem = itemView.findViewById(R.id.txtBranchDisquzCommentItem);
            textView_ReplyTo_DizqusCommentItem = itemView.findViewById(R.id.textView_ReplyTo_DizqusCommentItem);

        }

        public void methods(CommentModel commentModel, int position, String threadID) {
            this.threadID = threadID;
            setData(commentModel, position);
            onLikeImage((ArrayList<String>) commentModel.getLikes(), threadID);

            randomSideBarColors();


        }

        private void onLikeImage(ArrayList<String> likes, String threadID) {
            if (likes.contains(Db.getUserModel().getUser_id())) {
                imageView_like_DizqusCommentItem.setImageResource(R.drawable.like_icon);
            } else imageView_like_DizqusCommentItem.setImageResource(R.drawable.not_like_icon);


            imageView_like_DizqusCommentItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (likes.contains(Db.getUserModel().getUser_id())) {
                        imageView_like_DizqusCommentItem.setImageResource(R.drawable.not_like_icon);
                        likes.remove(Db.getUserModel().getUser_id());
                    } else {
                        imageView_like_DizqusCommentItem.setImageResource(R.drawable.like_icon);
                        likes.add(Db.getUserModel().getUser_id());
                    }
                    String upvoteText = " Upvotes";
                    if (likes.size() == 1) upvoteText = " Upvote";
                    textView_Likes_DizqusCommentItem.setText(likes.size() + upvoteText);
                    Db.db.collection(Db.COMMENTS_COL).document(threadID).update("likes", likes);
                }
            });
        }

        private void randomSideBarColors() {
            TypedArray ta = RandomCatcherClass.getColorList(context);
            view_sideBar_DizqusCommentItem.setBackgroundColor(ta.getColor((int) (Math.random() * ta.length()), 0));
        }

        private void showMoreRepliesClickListner(CommentModel commentModel, UserModel userModel) {

            if (commentModel.getReplies().size() > 0)
                textView_ShowReplies_DizqusCommentItem.setVisibility(View.VISIBLE);
            else textView_ShowReplies_DizqusCommentItem.setVisibility(View.GONE);
            if (depth <= 1) {
                textView_ShowReplies_DizqusCommentItem.setVisibility(View.GONE);
                showMoreReplies(userModel);
            }

            textView_ShowReplies_DizqusCommentItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMoreReplies(userModel);
                }
            });
        }

        private void showMoreReplies(UserModel userModel) {
            editText_onReply_DizqusCommentItem.clearFocus();
            textView_ShowReplies_DizqusCommentItem.setVisibility(View.GONE);
            Db.db.collection(Db.COMMENTS_COL).whereEqualTo("thread_id", threadID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<CommentModel> commentModels = new ArrayList<>();
                        ArrayList<String> commentThreadIds = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CommentModel commentModel = document.toObject(CommentModel.class);
                            commentThreadIds.add(document.getId());
                            commentModels.add(commentModel);
                        }

                        DizqusCommentItemAdapter dizqusCommentItemAdapter = new DizqusCommentItemAdapter(context, commentModels, commentThreadIds, depth + 1, userModel);
                        recyclerView_DizqusCommentItem.setAdapter(dizqusCommentItemAdapter);
                        recyclerView_DizqusCommentItem.setLayoutManager(new LinearLayoutManager(context));
                    } else {

                    }
                }
            });

        }

        private void setData(CommentModel commentModel, int position) {
            db.collection(Db.USER_COL).document(commentModel.getUser_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: 12341234 mod1 " + commentModel.getComment() + " ID " + threadID);
                        UserModel userModel = task.getResult().toObject(UserModel.class);
                        textView_UserName_DizqusComment.setText(userModel.getName().toUpperCase());
                        textView_Comment_DizqusComment.setText(commentModel.getComment());
                        Glide.with(context).load(userModel.getPhoto_url()).centerCrop().into(imageView_ProfilePhoto_DizqusCommentItem);
                        Glide.with(context).load(Db.PROFILE_BACKGROUND_URLS.get(userModel.getProfile_background())).centerCrop().into(imageView_ProfileBackground_DizqusCommentItem);
                        textView_date_DizqusCommentItem.setText(AppHelper.getDateFromTimestamp(commentModel.getDate()));
                        textView_Likes_DizqusCommentItem.setText(commentModel.getLikes().size() + " Upvotes");
                        editText_onReply_DizqusCommentItem.setHint("Reply to " + userModel.getName());

                        txtBranchDisquzCommentItem.setText(userModel.getBranch());
                        showMoreRepliesClickListner(commentModel, userModel);
                        replyToThread(userModel);


                    }
                }
            });
            textView_ReplyTo_DizqusCommentItem.setText("Reply to " + userModel.getName());


        }

        private void replyToThread(UserModel userModel) {
            editText_onReply_DizqusCommentItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        linearLayout_onReply_DizqusCommentItem.setVisibility(View.GONE);
                    }
                }
            });
            textView_reply_DizqusCommentItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayout_onReply_DizqusCommentItem.setVisibility(View.VISIBLE);
                    editText_onReply_DizqusCommentItem.requestFocus();
                }
            });
            button_sendReply_DizqusCommentItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postComment(userModel, threadID);
                }
            });
        }

        private void postComment(UserModel userModel, String threadId) {
            Date today = new Date();
            Timestamp timestamp = new Timestamp(today);
            CommentModel commentModel = new CommentModel();
            commentModel.setUser_id(Db.getUserModel().getUser_id());
            if (!editText_onReply_DizqusCommentItem.getText().toString().trim().isEmpty()) {
                commentModel.setComment(editText_onReply_DizqusCommentItem.getText().toString().trim());
            } else {
                editText_onReply_DizqusCommentItem.clearFocus();
                linearLayout_onReply_DizqusCommentItem.setVisibility(View.GONE);
                return;
            }
            commentModel.setThread_id(threadId);
            commentModel.setDate(timestamp);
//            commentModels.add(commentModel);
            Db.db.collection(Db.COMMENTS_COL).add(commentModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        DocumentReference doc = task.getResult();
                        Db.db.collection(Db.COMMENTS_COL).document(threadId).update("replies", FieldValue.arrayUnion(doc.getId())).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                                commentThreadIds.add(doc.getId());
                                showMoreReplies(userModel);
                                editText_onReply_DizqusCommentItem.setText("");
                            }
                        });

                    }
                }
            });
        }
    }


}
