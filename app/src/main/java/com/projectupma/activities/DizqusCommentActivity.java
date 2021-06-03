package com.projectupma.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.adapters.DizqusCommentItemAdapter;
import com.projectupma.models.CommentModel;
import com.projectupma.models.DizqusThreadModel;
import com.projectupma.models.UserModel;
import com.projectupma.utils.AppHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DizqusCommentActivity extends AppCompatActivity {
    private final String TAG = "ProfileActivity";
    DizqusCommentItemAdapter dizqusCommentItemAdapter;
    DizqusThreadModel dizqusThreadModel;
    String thread_id;
    ImageView imageView_ProfilePhoto_DizqusCommentActivity;
    private RecyclerView recyclerView_DizqusCommentActivity;
    private TextView textView_UserName_DizqusCommentActivity;
    private TextView textView_ThreadTitle_DizqusCommentActivity;
    private TextView textView_ThreadDescription_DizqusCommentActivity;
    private TextView textView_Comment_DizqusCommentActivity;
    private TextView textView_No_Likes_DizqusCommentActivity;
    private TextView textView_Branch_DizqusCommentActivity;
    private LinearLayout linearLayout_onReply_DizqusCommentActivity;
    private EditText editText_onReply_DizqusCommentActivity;
    private MaterialButton button_sendReply_DizqusCommentActivity;
    private TextView textView_Date_DizqusCommentActivity;
    private ImageView ImageView_Like_DizqusCommentActivity;
    private ImageView imageView_ProfileBackground_DizqusCommentActivity;
    private ImageView imageView_ThreadImage_DizqusCommentActivity;
    private MaterialCardView cardView_moreOptions_DizqusCommentActivity;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dizqus_comment);
        initUI();
        methods();

    }

    private void initUI() {
        linearLayout_onReply_DizqusCommentActivity = findViewById(R.id.linearLayout_onReply_DizqusCommentActivity);
        editText_onReply_DizqusCommentActivity = findViewById(R.id.editText_onReply_DizqusCommentActivity);
        button_sendReply_DizqusCommentActivity = findViewById(R.id.button_sendReply_DizqusCommentActivity);
        recyclerView_DizqusCommentActivity = findViewById(R.id.recyclerView_DizqusCommentActivity);
        textView_UserName_DizqusCommentActivity = findViewById(R.id.textView_UserName_DizqusCommentActivity);
        textView_Branch_DizqusCommentActivity = findViewById(R.id.textView_Branch_DizqusCommentActivity);
        textView_Date_DizqusCommentActivity = findViewById(R.id.textView_Date_DizqusCommentActivity);
        cardView_moreOptions_DizqusCommentActivity = findViewById(R.id.cardView_moreOptions_DizqusCommentActivity);
        imageView_ThreadImage_DizqusCommentActivity = findViewById(R.id.imageView_ThreadImage_DizqusCommentActivity);
        textView_ThreadTitle_DizqusCommentActivity = findViewById(R.id.textView_ThreadTitle_DizqusCommentActivity);
        textView_ThreadDescription_DizqusCommentActivity = findViewById(R.id.textView_ThreadDescription_DizqusCommentActivity);
        textView_Comment_DizqusCommentActivity = findViewById(R.id.textView_Comment_DizqusCommentActivity);
        textView_No_Likes_DizqusCommentActivity = findViewById(R.id.textView_No_Likes_DizqusCommentActivity);
        ImageView_Like_DizqusCommentActivity = findViewById(R.id.ImageView_Like_DizqusCommentActivity);
        imageView_ProfileBackground_DizqusCommentActivity = findViewById(R.id.imageView_ProfileBackground_DizqusCommentActivity);
        imageView_ProfilePhoto_DizqusCommentActivity = findViewById(R.id.imageView_ProfilePhoto_DizqusCommentActivity);

    }

    private void methods() {
        getActivityData();
        getThread();
        replyToThread();
    }

    private void onLikeImage(ArrayList<String> likes, String threadID) {
        if (likes.contains(Db.getUserModel().getUser_id())) {
            ImageView_Like_DizqusCommentActivity.setImageResource(R.drawable.like_icon);
        } else ImageView_Like_DizqusCommentActivity.setImageResource(R.drawable.not_like_icon);


        ImageView_Like_DizqusCommentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likes.contains(Db.getUserModel().getUser_id())) {
                    ImageView_Like_DizqusCommentActivity.setImageResource(R.drawable.not_like_icon);
                    likes.remove(Db.getUserModel().getUser_id());
                } else {
                    ImageView_Like_DizqusCommentActivity.setImageResource(R.drawable.like_icon);
                    likes.add(Db.getUserModel().getUser_id());
                }
                String upvoteText = " Upvotes";
                if (likes.size() == 1) upvoteText = " Upvote";
                textView_No_Likes_DizqusCommentActivity.setText(likes.size() + upvoteText);
                Db.db.collection(Db.DIZCUS_THREAD_COL).document(threadID).update("likes", likes);
            }
        });
    }

    private void replyToThread() {
        textView_Comment_DizqusCommentActivity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    linearLayout_onReply_DizqusCommentActivity.setVisibility(View.GONE);
                }
            }
        });
        textView_Comment_DizqusCommentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_onReply_DizqusCommentActivity.setVisibility(View.VISIBLE);
                editText_onReply_DizqusCommentActivity.requestFocus();
            }
        });
        button_sendReply_DizqusCommentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment(thread_id);
            }
        });
    }

    private void postComment(String threadId) {
        Date today = new Date();
        Timestamp timestamp = new Timestamp(today);
        CommentModel commentModel = new CommentModel();
        commentModel.setUser_id(Db.getUserModel().getUser_id());
        if (!editText_onReply_DizqusCommentActivity.getText().toString().trim().isEmpty()) {
            commentModel.setComment(editText_onReply_DizqusCommentActivity.getText().toString().trim());
        } else {
            editText_onReply_DizqusCommentActivity.clearFocus();
            linearLayout_onReply_DizqusCommentActivity.setVisibility(View.GONE);
            return;
        }
        commentModel.setThread_id(threadId);
        commentModel.setDate(timestamp);
        Db.db.collection(Db.COMMENTS_COL).add(commentModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    DocumentReference doc = task.getResult();
                    Db.db.collection(Db.COMMENTS_COL).document(threadId).update("replies", FieldValue.arrayUnion(doc.getId())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            getThread();
                            linearLayout_onReply_DizqusCommentActivity.setVisibility(View.GONE);
                            editText_onReply_DizqusCommentActivity.setText("");
                        }
                    });

                }
            }
        });
    }

    private void getActivityData() {
        user_id = getIntent().getStringExtra("user_id");
        thread_id = getIntent().getStringExtra("thread_id");
    }

    private void getThread() {
        Db.db.collection(Db.USER_COL).document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                Db.db.collection(Db.DIZCUS_THREAD_COL).document(thread_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        dizqusThreadModel = documentSnapshot.toObject(DizqusThreadModel.class);

                        setThreadData(dizqusThreadModel, userModel);
                        onLikeImage(dizqusThreadModel.getLikes(), thread_id);
                        getAllComments(userModel);

                    }
                });
            }
        });

    }


    private void setThreadData(DizqusThreadModel dizqusThreadModel, UserModel userModel) {
        textView_UserName_DizqusCommentActivity.setText(userModel.getName());
        textView_Branch_DizqusCommentActivity.setText(userModel.getBranch());
        textView_Date_DizqusCommentActivity.setText(AppHelper.getDateFromTimestamp(dizqusThreadModel.getDate()));
        textView_ThreadTitle_DizqusCommentActivity.setText(dizqusThreadModel.getQuestion());
        textView_ThreadDescription_DizqusCommentActivity.setText(dizqusThreadModel.getDescription());
        textView_No_Likes_DizqusCommentActivity.setText(dizqusThreadModel.getLikes().size() + " Upvotes");
        Glide.with(this).load(Db.PROFILE_BACKGROUND_URLS.get(userModel.getProfile_background())).centerCrop().into(imageView_ProfileBackground_DizqusCommentActivity);
        Glide.with(this).load(userModel.getPhoto_url()).into(imageView_ProfilePhoto_DizqusCommentActivity);
        {
            if (dizqusThreadModel.getImageUrl() != null) {
                if (!dizqusThreadModel.getImageUrl().isEmpty()) {
                    Glide.with(this).load(dizqusThreadModel.getImageUrl()).fitCenter().into(imageView_ThreadImage_DizqusCommentActivity);
                    imageView_ThreadImage_DizqusCommentActivity.setVisibility(View.VISIBLE);
                } else imageView_ThreadImage_DizqusCommentActivity.setVisibility(View.GONE);
            } else imageView_ThreadImage_DizqusCommentActivity.setVisibility(View.GONE);
            Point x = new Point();
            (this).getWindowManager().getDefaultDisplay().getSize(x);
            imageView_ThreadImage_DizqusCommentActivity.setMaxHeight((int) (x.y * 0.6));
        }
    }


    private void getAllComments(UserModel userModel) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(DizqusCommentActivity.this);
        recyclerView_DizqusCommentActivity.setLayoutManager(mLayoutManager);
        List<CommentModel> commentModels = new ArrayList<>();
        List<String> commentThreadIds = new ArrayList<>();
        Db.db.collection(Db.COMMENTS_COL).whereEqualTo("thread_id", thread_id).orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CommentModel commentModel = document.toObject(CommentModel.class);
                        commentThreadIds.add(document.getId());
                        commentModels.add(commentModel);
                        Log.d(TAG, "onComplete: 12341234 modx " + commentModel.getComment() + commentThreadIds.toString() + " searched " + thread_id);

                    }
                    dizqusCommentItemAdapter = new DizqusCommentItemAdapter(DizqusCommentActivity.this, commentModels, commentThreadIds, 1, userModel);
                    recyclerView_DizqusCommentActivity.setAdapter(dizqusCommentItemAdapter);
                } else {

                }
            }
        });
    }


}