package com.projectupma.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.adapters.ProfileBackgroundSelectorAdapter;
import com.projectupma.models.UserModel;
import com.projectupma.utils.AppHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import soup.neumorphism.NeumorphCardView;

public class ProfileActivity extends BaseActivity {
    Context context;
    private TextView txt_name_profile, txt_rollNo_profile, txt_branch, txt_semester, txt_year, txt_college, txt_email, txt_mobile;
    private ImageView img_profile_photo, qr_profile_ImageView, img_backgroundProfile_photo, changeProfileBackgroundPicButton_Signup_CardView, changeProfilePicButton_Profile_CardView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String uid;
    private Uri photoUrl;
    private Boolean emailVerified;
    private String TAG = "ProfileActivity";
    private Bitmap imageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        initiators();
        methods();


    }

    private void initiators() {
        mAuth = FirebaseAuth.getInstance();
        txt_name_profile = findViewById(R.id.txt_name_profile);
        txt_rollNo_profile = findViewById(R.id.txt_rollNo_profile);
        txt_branch = findViewById(R.id.txt_branch);
        txt_semester = findViewById(R.id.txt_semester);
        txt_year = findViewById(R.id.txt_year);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_college = findViewById(R.id.txt_college);
        txt_email = findViewById(R.id.txt_email);
        img_profile_photo = findViewById(R.id.img_profile_photo);
        qr_profile_ImageView = findViewById(R.id.qr_profile_ImageView);
        img_backgroundProfile_photo = findViewById(R.id.img_backgroundProfile_photo);
        changeProfileBackgroundPicButton_Signup_CardView = findViewById(R.id.changeProfileBackgroundPicButton_Signup_CardView);
        changeProfilePicButton_Profile_CardView = findViewById(R.id.changeProfilePicButton_Profile_CardView);
    }

    private void methods() {
        setProfileData(Db.getUserModel());
        setQR();
        setBackgroundImage();
        onProfilePicImageChangeClickListner();
        onBackgroundProfileImageChangeClickListner();
    }

    private void onProfilePicImageChangeClickListner() {
        changeProfilePicButton_Profile_CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(ProfileActivity.this);
            }
        });
    }

    private void setBackgroundImage() {
        Glide.with(context).load(Db.PROFILE_BACKGROUND_URLS.get(Db.getUserModel().getProfile_background())).into(img_backgroundProfile_photo);

    }

    public void onBackgroundProfileImageChangeClickListner() {
        changeProfileBackgroundPicButton_Signup_CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_profile_background_selector, viewGroup, false);
                RecyclerView profileBackground_RecyclerView = dialogView.findViewById(R.id.profileBackgroundSelector_RecyclerView);
                NeumorphCardView profileBackgroundSelector_SetBackground_CardView = dialogView.findViewById(R.id.profileBackgroundSelector_SetBackground_CardView);
                ProfileBackgroundSelectorAdapter adapter = new ProfileBackgroundSelectorAdapter(Db.PROFILE_BACKGROUND_URLS, Db.getUserModel().getProfile_background(), context);
                profileBackground_RecyclerView.setLayoutManager(new GridLayoutManager(context, 5));
                profileBackground_RecyclerView.setAdapter(adapter);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                profileBackgroundSelector_SetBackground_CardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        setBackgroundImage();
                    }
                });
            }
        });


    }


    private void setQR() {
        QRGEncoder qrgEncoder = new QRGEncoder(Db.getUserModel().getRoll_no(), null, QRGContents.Type.TEXT, 200);
        if (AppHelper.isNightMode()) {
            qrgEncoder.setColorWhite(getColor(R.color.primarygrey));
            qrgEncoder.setColorBlack(getColor(R.color.white));

        } else {
            qrgEncoder.setColorWhite(getColor(R.color.lightgrey));
            qrgEncoder.setColorBlack(getColor(R.color.primarygrey));

        }
        Bitmap bitmap = qrgEncoder.getBitmap();

        qr_profile_ImageView.setImageBitmap(bitmap);
    }


    private void setProfileData(UserModel userModel) {
        txt_name_profile.setText(userModel.getName().toUpperCase());
        txt_rollNo_profile.setText((userModel.getRoll_no()));
        txt_branch.setText(userModel.getBranch());
        txt_year.setText("" + (int) Math.ceil(Integer.parseInt(userModel.getSemester()) / 2));
        txt_college.setText("Jec Jabalpur".toUpperCase());
        txt_email.setText(userModel.getEmail());
        txt_mobile.setText("+91-" + userModel.getPhone_no());
        txt_semester.setText(userModel.getSemester());
        Glide.with(this).load(userModel.getPhoto_url()).into(img_profile_photo);
        Log.d(TAG, userModel.getApproved() + userModel.getBranch() + userModel.getCollege() + userModel.getName());
    }

    public void yourContributionViewMore(View view) {
        Intent intent = new Intent(ProfileActivity.this, YourContributionActivity.class);
        startActivity(intent);
    }

    public void onLogoutClick(View view) {
        logOut();
        gotoLoginActivity();
    }

    private void gotoLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case CAMERA_PERMISSION_CODE: {
                    try {
                        if (resultCode == RESULT_OK && data != null) {
                            imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            img_profile_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            img_profile_photo.setPadding(0, 0, 0, 0);
                            img_profile_photo.setImageBitmap(imageBitmap);
                            uploadFile(imageBitmap, "images/profile/" + Db.userModel.getRoll_no() + ".jpg");



                        }
                    } catch (Exception e) {
                    }
                    break;
                }
                case STORAGE_PERMISSION_CODE: {
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        try {
                            imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            img_profile_photo.setImageBitmap(imageBitmap);
                            img_profile_photo.setPadding(0, 0, 0, 0);
                            img_profile_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            uploadFile(imageBitmap, "images/profile/" + Db.userModel.getRoll_no() + ".jpg");

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
        Log.d(TAG, "uploadFile: upload1 987");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child(location);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "uploadFile: upload2 987");

                        Db.userModel.setPhoto_url(uri.toString());
                        db.document(Db.getUserDoc(Db.userModel.getUser_id())).set(Db.userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "uploadFile: upload3 987");

                                Toast.makeText(context, "Profile Pic Changed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });


    }


    private void logOut() {
        mAuth.signOut();
    }

    public void onBackPressed_Profile(View view) {
        onBackPressed();
    }
}