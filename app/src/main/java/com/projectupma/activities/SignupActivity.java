
package com.projectupma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;
import com.google.firebase.installations.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projectupma.DataClasses.User;
import com.projectupma.Db;
import com.projectupma.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    //global initializes

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private EditText edtEmail,edtPassword,edtConfirmPassword,edtName,edtPhone,edtRollNo;
    private FrameLayout imgFrameLayout;
    private ShapeableImageView profileImage,cameraIcon,imgLogo;
    private MaterialButton btnSignup,btnAddInfo;
    private Spinner spinnerBranch,spinnerSem,spinnerCollege;
    private Uri filePath;
    private Bitmap imageBitmap;
    private String profilePhotoUrl;
    private  FirebaseStorage storageReference;
    private MaterialCardView layoutSpinnerBranch,layoutSpinnerSemester,layoutSpinnerCollege,layoutEmail,layoutPassword,layoutConfirmPassword,layoutName,layoutPhone,layoutRollNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);
       initiators();
       signUpButtonClick(savedInstanceState);
       addInfoButtonClick(savedInstanceState);
        storageReference=FirebaseStorage.getInstance();
       imgFrameLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectImage(SignupActivity.this);
           }
       });
    }
    private void initiators(){
        FirebaseFirestore.setLoggingEnabled(true);
         edtEmail=findViewById(R.id.text_email);
        edtName=findViewById(R.id.text_name);
        edtPassword=findViewById(R.id.text_password);
        edtConfirmPassword=findViewById(R.id.text_confirm_password);
        edtPhone=findViewById(R.id.text_phone);
        edtRollNo=findViewById(R.id.text_roll_no);
        btnSignup=findViewById(R.id.button_signup);
        spinnerBranch=findViewById(R.id.spinner_branch);

        spinnerSem=findViewById(R.id.spinner_semester);
        spinnerCollege=findViewById(R.id.spinner_college);
        layoutSpinnerBranch=findViewById(R.id.layout_text_branch);
        layoutSpinnerCollege=findViewById(R.id.layout_text_college);
        layoutSpinnerSemester=findViewById(R.id.layout_text_semester);
        layoutConfirmPassword=findViewById(R.id.layout_text_confirm_password);
        layoutEmail=findViewById(R.id.layout_text_email);
        layoutName=findViewById(R.id.layout_text_name);
        layoutPassword=findViewById(R.id.layout_text_password);
        layoutPhone=findViewById(R.id.layout_text_phone);
        layoutRollNo=findViewById(R.id.layout_text_roll_no);
        btnAddInfo=findViewById(R.id.button_add_information);
        profileImage=findViewById(R.id.profilePic);
        imgFrameLayout=findViewById(R.id.img_Profile);
        cameraIcon=findViewById(R.id.iv_camera);
        imgLogo=findViewById(R.id.img_logo);
        imgFrameLayout.setVisibility(View.GONE);

    }
    private void hideSignUpLayout(){
        edtEmail.setVisibility(View.GONE);
        edtPassword.setVisibility(View.GONE);
        edtConfirmPassword.setVisibility(View.GONE);
        imgLogo.setVisibility(View.INVISIBLE);
        edtName.setVisibility(View.VISIBLE);
        edtPhone.setVisibility(View.VISIBLE);
        edtRollNo.setVisibility(View.VISIBLE);
        layoutConfirmPassword.setVisibility(View.INVISIBLE);
        layoutPassword.setVisibility(View.INVISIBLE);
        layoutEmail.setVisibility(View.INVISIBLE);
        layoutName.setVisibility(View.VISIBLE);
        layoutRollNo.setVisibility(View.VISIBLE);
        layoutPhone.setVisibility(View.VISIBLE);
        layoutSpinnerSemester.setVisibility(View.VISIBLE);
        layoutSpinnerCollege.setVisibility(View.VISIBLE);
        layoutSpinnerBranch.setVisibility(View.VISIBLE);
        btnSignup.setVisibility(View.GONE);
        btnAddInfo.setVisibility(View.VISIBLE);
        imgFrameLayout.setVisibility(View.VISIBLE);

    }
    private void setSpinnerAdapter(){
        List<String> listBranch=new ArrayList<String>();
        listBranch.add("CS");
        listBranch.add("IT");
        listBranch.add("EC");
        listBranch.add("EE");
        listBranch.add("ME");
        listBranch.add("CE");
        listBranch.add("IP");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,listBranch);
        spinnerBranch.setAdapter(adapter);
        List<String> listSem=new ArrayList<String>();
        listSem.add("1");
        listSem.add("2");
        listSem.add("3");
        listSem.add("4");
        listSem.add("5");
        listSem.add("6");
        listSem.add("7");
        listSem.add("8");
        ArrayAdapter<String> adapterSem = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,listSem);
        spinnerSem.setAdapter(adapterSem);
        List<String> listCollege=new ArrayList<String>();
        listCollege.add("Jec");
        ArrayAdapter<String> adapterCollege = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,listCollege);
        spinnerCollege.setAdapter(adapterCollege);
    }
    private void signUpButtonClick(Bundle savedInstanceState){
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edtEmail.getText().toString();
                String password=edtPassword.getText().toString();
                String confirmPassword=edtConfirmPassword.getText().toString();
                Boolean emailCorrect=isValidEmail(email);
                if(!emailCorrect){
                    edtEmail.setError("Please enter a valid Email");
                    edtEmail.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    edtPassword.setError(("Please Enter a Password"));
                    edtPassword.requestFocus();
                    return;
                }
                if(confirmPassword.isEmpty()){
                    edtConfirmPassword.setError(("Please Confirm your Password"));
                    edtConfirmPassword.requestFocus();
                    return;
                }
                if(!password.equals(confirmPassword)){
                    edtConfirmPassword.setError(("Both the passwords should be same"));
                    edtConfirmPassword.requestFocus();
                    return;
                }
                Toast.makeText(SignupActivity.this, ""+email+" "+password+" "+confirmPassword+" ", Toast.LENGTH_SHORT).show();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("SignupActivity", "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId=user.getUid();
                            hideSignUpLayout();
                            setSpinnerAdapter();
                            //db.collection(Db.user).add()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignupActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        checkPermission(Manifest.permission_group.STORAGE,STORAGE_PERMISSION_CODE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private int STORAGE_PERMISSION_CODE=100;
    private int STORAGE_READ_PERMISSION_CODE=101;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode ==STORAGE_PERMISSION_CODE ) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(SignupActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(SignupActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SignupActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(SignupActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                SignupActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            SignupActivity.this,
                            new String[] { permission },
                            requestCode);
        }
        else {
            Toast
                    .makeText(SignupActivity.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImageUri = data.getData();
                        filePath=selectedImageUri;
                         imageBitmap = (Bitmap) data.getExtras().get("data");
                        profileImage.setImageBitmap(imageBitmap);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        filePath=selectedImage;
                        try {
                            imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.getReference().child("images/pic1.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();

                            if(downloadUri.isSuccessful()){
                                 profilePhotoUrl = downloadUri.getResult().toString();
                                System.out.println("## Stored path is "+profilePhotoUrl);
                                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            }}
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
    private void addInfoButtonClick(Bundle savedInstanceState){
        btnAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name=edtName.getText().toString().trim();
                String rollNo=edtRollNo.getText().toString().trim();
                String phone=edtPhone.getText().toString().trim();
String branch=spinnerBranch.getSelectedItem().toString();
                String college=spinnerCollege.getSelectedItem().toString();
                String sem=spinnerSem.getSelectedItem().toString();
                if(name.isEmpty()){
                    edtName.setError(("Please Enter a Name"));
                    edtName.requestFocus();
                    return;
                }
                if(phone.isEmpty()){
                    edtPhone.setError(("Please Enter a Phone Number"));
                    edtPhone.requestFocus();
                    return;
                }
                if(rollNo.isEmpty()){
                    edtRollNo.setError(("Please Enter a Roll Number"));
                    edtRollNo.requestFocus();
                    return;
                }

                Toast.makeText(SignupActivity.this, ""+name+" "+phone+" "+rollNo+" ", Toast.LENGTH_SHORT).show();
                uploadFile();
                FirebaseUser user = mAuth.getCurrentUser();
                String userId=user.getUid();
                String email=user.getEmail();

                Date today=new Date();
                User user1=new User(userId,name,phone,rollNo,sem,branch,college,edtPassword.getText().toString(),profilePhotoUrl,"","",email,today,"1","approved");
                db.collection(Db.user).document(userId).set(user1);
                Intent intent=new Intent(SignupActivity.this,HomeActivity.class);
    }});}
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public void DayNightMode(View view) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
    private void uploadFile(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("Your url for storage");
        StorageReference mountainImagesRef = storageRef.child("images/"+"11111"+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                Log.d("downloadUrl-->", "" + downloadUrl);
            }
        });

    }

}