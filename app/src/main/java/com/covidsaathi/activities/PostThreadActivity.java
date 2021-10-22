package com.covidsaathi.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.models.DizqusThreadModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import soup.neumorphism.NeumorphCardView;

public class PostThreadActivity extends BaseActivity {
    private static final String TAG = "PostThreadActivity";
    DizqusThreadModel dizqusThreadModel = new DizqusThreadModel();
    private NeumorphCardView txtPostAddPost;
    private TextView txtAddBannerAddPost;
    private EditText edtPostTitleAddPost;
    private EditText edtPostDescriptionAddPost;
    private ImageView edtPostImageAddPost,imgPostBannerAddPost;
    private Bitmap imageBitmap;
    private Spinner postTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_thread);
        initiators();
        methods();

    }

    private void initiators() {
        txtPostAddPost = findViewById(R.id.txtPostAddPost);
        edtPostDescriptionAddPost = findViewById(R.id.edtPostDescriptionAddPost);
        edtPostTitleAddPost = findViewById(R.id.edtPostTitleAddPost);
        edtPostImageAddPost = findViewById(R.id.edtPostImageAddPost);
        imgPostBannerAddPost=findViewById(R.id.imgPostBannerAddPost);
        txtAddBannerAddPost=findViewById(R.id.txtAddBannerAddPost);
        postTypeSpinner=findViewById(R.id.postTypeSpinner);


    }

    private void methods() {
        postBtnClickListener();
        onImageClickListner();
    }

    private void onImageClickListner() {
        edtPostImageAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(PostThreadActivity.this);
            }
        });
    }


    private void postBtnClickListener() {
        txtPostAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtPostTitleAddPost.getText().toString();
                String postDescription = edtPostDescriptionAddPost.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(PostThreadActivity.this, "Required Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (postDescription.isEmpty()) {
                    Toast.makeText(PostThreadActivity.this, "Required Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                Timestamp timestamp = new Timestamp(new Date());
                dizqusThreadModel.setDate(timestamp);
                dizqusThreadModel.setDescription(postDescription);
                dizqusThreadModel.setQuestion(title);
                dizqusThreadModel.setTags(new ArrayList<>());
                dizqusThreadModel.setChildren(new ArrayList<>());
                dizqusThreadModel.setUser_id(Db.getUserModel().getUser_id());
                dizqusThreadModel.setImageUrl("");
                dizqusThreadModel.setType(postTypeSpinner.getSelectedItem().toString());
                if (imageBitmap == null) {
                    Db.db.collection(Db.DIZCUS_THREAD_COL).add(dizqusThreadModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PostThreadActivity.this, "The post had been saved successfully", Toast.LENGTH_SHORT).show();
                                gotoDizqusHome();
                            } else {
                                Toast.makeText(PostThreadActivity.this, "The post failed to save", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    uploadFile(imageBitmap, "images/threads/" + Db.getUserModel().getRoll_no() + "_" + timestamp.getSeconds() + ".jpg");
                }
            }
        });
    }

    private void gotoDizqusHome() {
        Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, NewHomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        gotoDizqusHome();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case CAMERA_PERMISSION_CODE: {
                    try {
                        if (resultCode == RESULT_OK && data != null) {
                            imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            edtPostImageAddPost.setImageBitmap(imageBitmap);
                            edtPostImageAddPost.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            edtPostImageAddPost.setPadding(0, 0, 0, 0);
                            Point x = new Point();
                            getWindowManager().getDefaultDisplay().getSize(x);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) x.x, (int) x.x);
                            edtPostImageAddPost.setLayoutParams(params);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case STORAGE_PERMISSION_CODE: {
                    if (resultCode == RESULT_OK && data.getData() != null) {
                        Uri selectedImage = data.getData();
                        try {
                            imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            edtPostImageAddPost.setImageBitmap(imageBitmap);
                            edtPostImageAddPost.setPadding(0, 0, 0, 0);
                            edtPostImageAddPost.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            Point x = new Point();
                            getWindowManager().getDefaultDisplay().getSize(x);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) x.x, (int) x.x);
                            edtPostImageAddPost.setLayoutParams(params);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile(Bitmap bitmap, String location) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReference().child(location);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dizqusThreadModel.setImageUrl(uri.toString());
                        Db.db.collection(Db.DIZCUS_THREAD_COL).add(dizqusThreadModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PostThreadActivity.this, "The post had been saved successfully", Toast.LENGTH_SHORT).show();
                                    gotoDizqusHome();
                                } else {
                                    Toast.makeText(PostThreadActivity.this, "The post failed to save", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

}