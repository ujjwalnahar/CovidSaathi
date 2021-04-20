
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
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projectupma.models.UserModel;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.utils.RandomCatcherClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import soup.neumorphism.NeumorphCardView;

public class SignupActivity extends AppCompatActivity {
    //global initializes

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private TextView signupHeadingText;
    private TextInputEditText edtEmail, edtPassword, edtConfirmPassword, edtName, edtPhone, edtRollNo;
    private ImageView profileImage,cameraChangeImageButton;
    private NeumorphCardView button_signup;
    private Spinner spinnerBranch, spinnerSem;
    private Uri filePath;
    private Bitmap imageBitmap;
    private String profilePhotoUrl;
    Handler handler = new Handler();
    Runnable imageRunnable;
    private final FirebaseStorage storageReference = FirebaseStorage.getInstance();

    Context context;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);
        context=this;
        initUI();
        methods();

    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(imageRunnable);
        super.onBackPressed();
    }

    private void methods() {
        checkPermission(Manifest.permission_group.STORAGE, STORAGE_PERMISSION_CODE);
        changePhoto();
        randomSignInText();
        signUpButtonClick();
        randomImages();
    }

    private void randomSignInText() {
        final int delay = 1000;

        String[] alternatives = RandomCatcherClass.getRandomGreeting().split(", ");
        Runnable r;
        handler.postDelayed(r=new Runnable() {
            public void run() {
                signupHeadingText.setText(alternatives[(int)(Math.random() * alternatives.length)]+"! "+edtName.getText().toString().split(" ")[0]+"\nSignup to\nget Started");
                handler.postDelayed(this, delay);
            }
        }, delay);


    }

    private void changePhoto() {

        cameraChangeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(SignupActivity.this);
            }
        });
    }

    private void randomImages() {
        final int delay = 1200;
        db.document(Db.getProfileImages()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> urls = (ArrayList<String>) task.getResult().get("urls");

                handler.postDelayed(imageRunnable=new Runnable() {
                    public void run() {
                        Glide.with(context)
                                .load(urls.get((int)(Math.random()*urls.size())))
                                .centerCrop()
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(profileImage);
                        handler.postDelayed(this, delay);
                    }
                }, delay);
            }
        });

    }

    private void initUI() {
        FirebaseFirestore.setLoggingEnabled(true);
        edtEmail = findViewById(R.id.text_email);
        signupHeadingText = findViewById(R.id.signupHeadingText);
        edtName = findViewById(R.id.text_name);
        edtPassword = findViewById(R.id.text_password);
        edtConfirmPassword = findViewById(R.id.text_confirm_password);
        edtPhone = findViewById(R.id.text_phone);
        edtRollNo = findViewById(R.id.text_roll_no);
        button_signup = findViewById(R.id.button_signup);
        spinnerBranch = findViewById(R.id.spinner_branch);
        spinnerSem = findViewById(R.id.spinner_semester);
        profileImage = findViewById(R.id.profilePic);
        cameraChangeImageButton = findViewById(R.id.cameraChangeImageButton);

    }

    private void signUpButtonClick() {
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();
                String name = edtName.getText().toString().trim();
                String rollNo = edtRollNo.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String branch = spinnerBranch.getSelectedItem().toString();
                String sem = spinnerSem.getSelectedItem().toString();
                Boolean emailCorrect = isValidEmail(email);
                if (!emailCorrect) {
                    edtEmail.setError("Please enter a valid Email");
                    edtEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    edtPassword.setError(("Please Enter a Password"));
                    edtPassword.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    edtConfirmPassword.setError(("Please Confirm your Password"));
                    edtConfirmPassword.requestFocus();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    edtConfirmPassword.setError(("Both the passwords should be same"));
                    edtConfirmPassword.requestFocus();
                    return;
                }
                Toast.makeText(SignupActivity.this, "" + email + " " + password + " " + confirmPassword + " ", Toast.LENGTH_SHORT).show();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("SignupActivity", "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignupActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                {

                    if (name.isEmpty()) {
                        edtName.setError(("Please Enter a Name"));
                        edtName.requestFocus();
                        return;
                    }
                    if (phone.isEmpty()) {
                        edtPhone.setError(("Please Enter a Phone Number"));
                        edtPhone.requestFocus();
                        return;
                    }
                    if (rollNo.isEmpty()) {
                        edtRollNo.setError(("Please Enter a Roll Number"));
                        edtRollNo.requestFocus();
                        return;
                    }

                    Toast.makeText(SignupActivity.this, "" + name + " " + phone + " " + rollNo + " ", Toast.LENGTH_SHORT).show();
                    uploadFile();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userId = user.getUid();

                    Date today = new Date();
                    UserModel userModel1 = new UserModel(userId, name, phone, rollNo, sem, branch, "JEC", edtPassword.getText().toString(), profilePhotoUrl, "", "", email, today, "0", "notapproved");
                    db.collection(Db.USERDOC_USER).document(userId).set(userModel1);
                    handler.removeCallbacks(imageRunnable);
                    Intent intent = new Intent(SignupActivity.this, ApprovalActivity.class);
                    {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.document(Db.getUserDoc(firebaseUser.getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Db.userModel = task.getResult().toObject(UserModel.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        });
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        checkPermission(Manifest.permission_group.STORAGE, STORAGE_PERMISSION_CODE);

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
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private int STORAGE_PERMISSION_CODE = 100;
    private int STORAGE_READ_PERMISSION_CODE = 101;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == 1000) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(SignupActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(SignupActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SignupActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(SignupActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                STORAGE_PERMISSION_CODE);
    }

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    public void checkPermission(String permission, int requestCode) {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                SignupActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            requestCameraPermission();
        } else {
            Toast
                    .makeText(SignupActivity.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        handler.removeCallbacks(imageRunnable);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImageUri = data.getData();
                        filePath = selectedImageUri;
                        imageBitmap = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Glide.with(context)
                                .load(stream.toByteArray())
                                .into(profileImage);
                        profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        profileImage.setPadding(0,0,0,0);

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        filePath = selectedImage;
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
                                imageBitmap=BitmapFactory.decodeFile(picturePath);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                Glide.with(context)
                                        .load(stream.toByteArray())
                                        .into(profileImage);
                                profileImage.setPadding(0,0,0,0);
                                profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

                            if (downloadUri.isSuccessful()) {
                                profilePhotoUrl = downloadUri.getResult().toString();
                                System.out.println("## Stored path is " + profilePhotoUrl);
                                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            }
                        }
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
        StorageReference mountainImagesRef = storageRef.child("images/" + "11111" + ".jpg");
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

    public void gotoLoginActivity(View view) {
        gotoLoginActivity();
    }
    public void gotoLoginActivity(){
        handler.removeCallbacks(imageRunnable);
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}