package com.covidsaathi.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.covidsaathi.Db;
import com.covidsaathi.R;
import com.covidsaathi.models.UserModel;
import com.covidsaathi.utils.RandomCatcherClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import soup.neumorphism.NeumorphCardView;

public class SignupActivity extends BaseActivity {
    //global initializes
    UserModel userModel = new UserModel();
    Handler handler = new Handler();
    Runnable imageRunnable;
    Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private TextView signupHeadingText_Signup_EditText;
    private TextInputEditText email_Signup_EditText, password_Signup_EditText, confirmPassword_Signup_EditText, name_Signup_EditText, phoneNo_Signup_EditText, rollNo_Signup_EditText;
    private ImageView profilePic_Signup_ImageView, changeProfilePicButton_Signup_CardView,profilePicRandom_Signup_ImageView;
    private NeumorphCardView signupButton_Signup_Button;
    private Spinner branch_Signup_Spinner, semester_Signup_Spinner;
    private Bitmap imageBitmap;
    private LinearProgressIndicator progress_SignUp_ProgressBar;


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);
        context = this;
        initUI();
        methods();

    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(imageRunnable);
        gotoLoginActivity();
    }

    private void methods() {
        changePhoto();
        randomSignInText();
        signUpButtonClick();
        randomImages();
    }

    private void randomSignInText() {
        final int delay = 1000;
        String[] alternatives = RandomCatcherClass.getRandomGreeting().split(", ");
        Runnable r;
        handler.postDelayed(r = new Runnable() {
            public void run() {
                signupHeadingText_Signup_EditText.setText(alternatives[(int) (Math.random() * alternatives.length)] + "! " + name_Signup_EditText.getText().toString().split(" ")[0] + "\nSignup to\nget Started");
                handler.postDelayed(this, delay);
            }
        }, delay);


    }

    private void changePhoto() {
        changeProfilePicButton_Signup_CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(SignupActivity.this);
            }
        });
    }

    private void randomImages() {
        final int delay = 1200;
        db.document(Db.RANDOM_PROFILE_IMAGES_DOC).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                //ArrayList<String> urls = (ArrayList<String>) task.getResult().get("urls");

                handler.postDelayed(imageRunnable = new Runnable() {
                    public void run() {
                        /*Glide.with(context)
                                .load(urls.get((int) (Math.random() * urls.size())))
                                .centerCrop()
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(profilePicRandom_Signup_ImageView);
*/
                        handler.postDelayed(this, delay);
                    }
                }, delay);
            }
        });

    }

    private void initUI() {
        email_Signup_EditText = findViewById(R.id.email_Signup_EditText);
        signupHeadingText_Signup_EditText = findViewById(R.id.signupHeadingText_Signup_EditText);
        name_Signup_EditText = findViewById(R.id.name_Signup_EditText);
        password_Signup_EditText = findViewById(R.id.password_Signup_EditText);
        confirmPassword_Signup_EditText = findViewById(R.id.confirmPassword_Signup_EditText);
        phoneNo_Signup_EditText = findViewById(R.id.phoneNo_Signup_EditText);
        rollNo_Signup_EditText = findViewById(R.id.rollNo_Signup_EditText);
        signupButton_Signup_Button = findViewById(R.id.signupButton_Signup_Button);
        profilePicRandom_Signup_ImageView = findViewById(R.id.profilePicRandom_Signup_ImageView);
        branch_Signup_Spinner = findViewById(R.id.branch_Signup_Spinner);
        semester_Signup_Spinner = findViewById(R.id.semester_Signup_Spinner);
        profilePic_Signup_ImageView = findViewById(R.id.profilePic_Signup_ImageView);
        progress_SignUp_ProgressBar = findViewById(R.id.progress_SignUp_ProgressBar);
        changeProfilePicButton_Signup_CardView = findViewById(R.id.changeProfilePicButton_Signup_CardView);

    }

    private void signUpButtonClick() {
        signupButton_Signup_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_SignUp_ProgressBar.setProgress(10, true);
                String email = email_Signup_EditText.getText().toString();
                String password = password_Signup_EditText.getText().toString();
                String confirmPassword = confirmPassword_Signup_EditText.getText().toString();
                String name = name_Signup_EditText.getText().toString().trim();
                String rollNo = rollNo_Signup_EditText.getText().toString().toUpperCase().trim();
                String phone = phoneNo_Signup_EditText.getText().toString().trim();
                String branch = branch_Signup_Spinner.getSelectedItem().toString();
                String semester = semester_Signup_Spinner.getSelectedItem().toString();
                if (!isValidEmail(email)) {
                    email_Signup_EditText.setError("Please enter a valid Email");
                    email_Signup_EditText.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    password_Signup_EditText.setError(("Please Enter a Password"));
                    progress_SignUp_ProgressBar.setProgress(0, true);
                    password_Signup_EditText.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    confirmPassword_Signup_EditText.setError(("Please Confirm your Password"));
                    confirmPassword_Signup_EditText.requestFocus();
                    progress_SignUp_ProgressBar.setProgress(0, true);
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    confirmPassword_Signup_EditText.setError(("Both the passwords should be same"));
                    confirmPassword_Signup_EditText.requestFocus();
                    progress_SignUp_ProgressBar.setProgress(0, true);
                    return;
                }
                if (name.isEmpty()) {
                    name_Signup_EditText.setError(("Please Enter a Name"));
                    name_Signup_EditText.requestFocus();
                    progress_SignUp_ProgressBar.setProgress(0, true);
                    return;
                }
                /*if (phone.isEmpty()) {
                    phoneNo_Signup_EditText.setError(("Please Enter a Phone Number"));
                    phoneNo_Signup_EditText.requestFocus();
                    progress_SignUp_ProgressBar.setProgress(0, true);
                    return;
                }*/
                if (rollNo.isEmpty()) {
                    rollNo_Signup_EditText.setError(("Please Enter a Roll Number"));
                    rollNo_Signup_EditText.requestFocus();
                    progress_SignUp_ProgressBar.setProgress(0, true);
                    return;
                }
                progress_SignUp_ProgressBar.setProgress(20, true);

                userModel.setEmail(email);
                userModel.setName(name);
                userModel.setRoll_no(rollNo);
                userModel.setPhone_no(phone);
                userModel.setBranch(branch);
                userModel.setSemester(semester);
                userModel.setCollege("Jabalpur Engineering College, Jabalpur");
                checkExistingRoll(rollNo, password);


            }
        });
    }

    private void checkExistingRoll(String rollNo, String password) {
        Db.db.collection(Db.USER_COL).whereEqualTo("roll_no", rollNo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().size() == 0) {
                    progress_SignUp_ProgressBar.setProgress(30, true);
                    mAuth.createUserWithEmailAndPassword(userModel.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progress_SignUp_ProgressBar.setProgress(40, true);
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getUid();
                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> taskID) {
                                        progress_SignUp_ProgressBar.setProgress(50, true);
                                        ArrayList<String> ids = new ArrayList<>();
                                        ids.add(taskID.getResult());
                                        userModel.setFcmIDS(ids);
                                        uploadUser();
                                    }
                                });

                            } else {
                                progress_SignUp_ProgressBar.setProgress(0, true);
                                Log.w("SignupActivity", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    rollNo_Signup_EditText.setError(("RollNo Already Exists, Contact Us for any Issues"));
                    rollNo_Signup_EditText.requestFocus();
                }
            }
        });
    }

    private void uploadUser() {

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        userModel.setUser_id(userId);
        uploadFile(imageBitmap, "images/profile/" + userModel.getRoll_no() + ".jpg");


    }

    private void gotoApprovalActivity() {
        handler.removeCallbacks(imageRunnable);
        Intent intent = new Intent(SignupActivity.this, NewHomeActivity.class);
        {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.document(Db.getUserDoc(firebaseUser.getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    progress_SignUp_ProgressBar.setProgress(100, true);
                    Db.userModel = task.getResult().toObject(UserModel.class);
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        handler.removeCallbacks(imageRunnable);
        profilePicRandom_Signup_ImageView.setVisibility(View.GONE);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case CAMERA_PERMISSION_CODE: {
                    try {
                        if (resultCode == RESULT_OK && data != null) {
                            imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            profilePic_Signup_ImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            profilePic_Signup_ImageView.setPadding(0, 0, 0, 0);
                            profilePic_Signup_ImageView.setImageBitmap(imageBitmap);

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
                            profilePic_Signup_ImageView.setImageBitmap(imageBitmap);
                            profilePic_Signup_ImageView.setPadding(0, 0, 0, 0);
                            profilePic_Signup_ImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

        try{
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference imageRef = storage.getReference().child(location);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress_SignUp_ProgressBar.setProgress(65, true);
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progress_SignUp_ProgressBar.setProgress(70, true);
                            userModel.setPhoto_url(uri.toString());
                            Log.d("TAG", "onComplete: user download url" + uri.toString());
                            addUser();
                        }
                    });
                }
            });
        }
        catch (Exception e){
            addUser();
        }


    }

    private void addUser() {
        db.document(Db.getUserDoc(userModel.getUser_id())).set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progress_SignUp_ProgressBar.setProgress(75, true);
                gotoApprovalActivity();
            }
        });

    }

    public void gotoLoginActivity(View view) {
        gotoLoginActivity();
    }

    public void gotoLoginActivity() {
        handler.removeCallbacks(imageRunnable);
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}