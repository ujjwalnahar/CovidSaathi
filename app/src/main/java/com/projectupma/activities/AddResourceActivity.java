
package com.projectupma.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projectupma.DataClasses.Resource;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.models.AppHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddResourceActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText resourceName;
    Long pdfSize;
    Spinner spinnerBranch, spinnerSem, spinnerType, spinnerSubject;
    MaterialCardView chooseResource, chooseThumbnail, submitResource;
    TextView txtPdfName, txtThumbnailName;
    Uri pdfUri = null, thumbnailImgUri = null;
    String branch,  thumbnailDownloadUrl;
    HashMap<String, String> hashMapSubjectCode=new HashMap<>();
    String userId;
    AppHelper appHelper = AppHelper.getInstance();
    private FirebaseStorage storageReference;
    String docId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resource);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId= mAuth.getUid();
        methods();

    }

    private void methods() {
        initiators();
        chooseResourceClickListener();
        chooseThumbnailClickListener();
        //setSpinnerAdapters();
        changedBranchSem();
        uploadToDb();
    }

    private void initiators() {
        resourceName = findViewById(R.id.edt_resource_name);
        storageReference = FirebaseStorage.getInstance();
        spinnerBranch = findViewById(R.id.spinner_branch_add_resource);
        spinnerSem = findViewById(R.id.spinner_semester_add_resource);
        spinnerType = findViewById(R.id.spinner_type_add_resource);
        spinnerSubject = findViewById(R.id.spinner_subject_add_resource);
        chooseResource = findViewById(R.id.mtrl_card_choose_file);
        chooseThumbnail = findViewById(R.id.mtrl_card_choose_image);
        submitResource = findViewById(R.id.mtrl_card_submit_resource);
        txtPdfName = findViewById(R.id.txt_pdf_name);
        txtThumbnailName = findViewById(R.id.txt_thumbnail_name);
    }

    private void chooseResourceClickListener() {
        chooseResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, 1212);
            }
        });
    }

    private void chooseThumbnailClickListener() {
        chooseThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1212:
                if (resultCode == RESULT_OK) {
                    pdfUri = data.getData();
                    File f = new File(pdfUri.getPath());
                    pdfSize = f.length();
                    txtPdfName.setText(f.getName());
                }
                break;
            case 100:
                if (resultCode == RESULT_OK) {
                    thumbnailImgUri = data.getData();
                    File f = new File(thumbnailImgUri.getPath());
                    txtThumbnailName.setText(f.getName());
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*private void setSpinnerAdapters() {
        List<String> listSem = new ArrayList<String>();
        listSem.add("1");
        listSem.add("2");
        listSem.add("3");
        listSem.add("4");
        listSem.add("5");
        listSem.add("6");
        listSem.add("7");
        listSem.add("8");
        ArrayAdapter<String> adapterSem = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSem);
        spinnerSem.setAdapter(adapterSem);
        List<String> listTypes = new ArrayList<String>();
        listTypes.add("Previous Year Paper");
        listTypes.add("Books");
        listTypes.add("Notes");
        listTypes.add("Others");
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, listTypes);
        spinnerType.setAdapter(adapterType);
    }*/

    private void changedBranchSem() {
        spinnerBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                branch = spinnerBranch.getItemAtPosition(position).toString().trim();
                String branchShort = appHelper.branchConverter(branch);
                setSubjectAdapter(branchShort, spinnerSem.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinnerSem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                setSubjectAdapter(appHelper.branchConverter(branch), spinnerSem.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void setSubjectAdapter(String branch, String semester) {
        Log.d("02020", "1");

        List<String> subjectList = new ArrayList<>();

        Log.d("02020", "1");

        db.collection(Db.SUBJECTS).whereEqualTo("branch", branch).whereEqualTo("semester", semester).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("90202", document.getId() + " => " + document.getData());
                        Map<String, String> subjects = (Map<String, String>) document.get("subjects");
                        for (Map.Entry<String, String> entry : subjects.entrySet()) {
                            System.out.println("Key = " + entry.getKey() +
                                    ", Value = " + entry.getValue());
                            subjectList.add(entry.getValue());
                            hashMapSubjectCode.put(entry.getValue(), entry.getKey());
                        }
                    }
                    ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(AddResourceActivity.this, android.R.layout.simple_spinner_dropdown_item, subjectList);
                    spinnerSubject.setAdapter(adapterSubject);
                    Log.d("SomeTag", "1");

                } else {
                    Log.d("02020", "Error getting documents: ", task.getException());
                }
            }
        });


    }

    private void uploadResource() {
        if (resourceName.getText() == null) {
            Toast.makeText(AddResourceActivity.this, "Please Enter a valid name for the resource", Toast.LENGTH_SHORT).show();
        }
        if (pdfUri == null) {
            Toast.makeText(AddResourceActivity.this, "Please select a resource", Toast.LENGTH_SHORT).show();
        } else if (thumbnailImgUri == null) {
            Toast.makeText(AddResourceActivity.this, "Please select a thumbnail", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(AddResourceActivity.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference riversRef = storageReference.getReference().child("resources/" + resourceName.getText().toString() + ".pdf");
            riversRef.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            //and displaying a success toast
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Date today=new Date();
                                    Timestamp timestamp=new Timestamp(today);
                                    ArrayList<String> tags=new ArrayList<>();
                                    Log.d("1122", "onSuccess: "+uri.toString());
                                    Resource resource=new Resource(timestamp,hashMapSubjectCode.get(spinnerSubject.getSelectedItem().toString()),pdfSize.toString(),uri.toString(),spinnerSem.getSelectedItem().toString(),appHelper.convertTypetoInt(spinnerType.getSelectedItem().toString()),userId,tags,thumbnailDownloadUrl,resourceName.getText().toString());
                                    db.collection(Db.RESOURCES+"/"+appHelper.branchConverter(spinnerBranch.getSelectedItem().toString())).add(resource).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            docId=documentReference.getId();
                                        }
                                    });
                                    uploadThumbnail();
                                }
                            });
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
    }
    private void uploadThumbnail(){
        final ProgressDialog progressDialog = new ProgressDialog(AddResourceActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        final StorageReference riversRef2 = storageReference.getReference().child("thumbnails/" + txtThumbnailName.getText() + ".jpeg");

            riversRef2.putFile(thumbnailImgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            Log.d("1122", "onSuccess: "+docId);

                            riversRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    db.collection(Db.RESOURCES+"/"+appHelper.branchConverter(spinnerBranch.getSelectedItem().toString())).document(docId).update("thumbnailUrl",uri.toString());
                                }
                            });
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
    private void uploadToDb(){
        submitResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    uploadResource();

            }
        });
    }
}

