package com.projectupma.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projectupma.Db;
import com.projectupma.R;
import com.projectupma.models.ResourceModel;
import com.projectupma.utils.AppHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soup.neumorphism.NeumorphCardView;

public class AddResourceActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputEditText resourceName_AddResource_EditText;
    Long pdfSize;
    Spinner spinner_branch_AddResource, spinner_Semester_AddResource, spinner_type_AddResource, spinner_subject_AddResource;
    NeumorphCardView chooseFile_AddResource_CardView, submitResource_AddResource_CardView;
    TextView pdfName_AddResource_EditText;
    Uri pdfUri = null, thumbnailImgUri = null;
    String branch;
    HashMap<String, String> hashMapSubjectCode = new HashMap<>();
    String docId;
    ResourceModel resourceModel;
    private FirebaseStorage storageReference;

    public static String getKey(HashMap<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resource);
        initUI();
        methods();


    }

    private void initUI() {

        resourceName_AddResource_EditText = findViewById(R.id.resourceName_AddResource_EditText);
        storageReference = FirebaseStorage.getInstance();
        spinner_branch_AddResource = findViewById(R.id.spinner_branch_AddResource);
        spinner_Semester_AddResource = findViewById(R.id.spinner_Semester_AddResource);
        spinner_type_AddResource = findViewById(R.id.spinner_type_AddResource);
        spinner_subject_AddResource = findViewById(R.id.spinner_subject_AddResource);
        chooseFile_AddResource_CardView = findViewById(R.id.chooseFile_AddResource_CardView);
        submitResource_AddResource_CardView = findViewById(R.id.submitResource_AddResource_CardView);
        pdfName_AddResource_EditText = findViewById(R.id.pdfName_AddResource_EditText);
    }

    private void methods() {
        chooseResourceClickListener();
        changedBranchSem();
        uploadToDb();
    }


    private void uploadEditResource(String ResourceId) {
        String resName = resourceName_AddResource_EditText.getText().toString();
        String branch = AppHelper.branchToShortConverter(spinner_branch_AddResource.getSelectedItem().toString());
        String sem = spinner_Semester_AddResource.getSelectedItem().toString();
        String subject_code = hashMapSubjectCode.get(spinner_subject_AddResource.getSelectedItem().toString());
        String type = AppHelper.convertTypetoInt(spinner_type_AddResource.getSelectedItem().toString());
        ResourceModel resourceModel = new ResourceModel(this.resourceModel.getDate(), subject_code, this.resourceModel.getSize(), this.resourceModel.getDoc_link(), sem, type, this.resourceModel.getUserId(), resName, this.resourceModel.getTags(), this.resourceModel.getThumbnailUrl(), branch);
        db.collection(Db.RESOURCES_DOC + "/resources").document(ResourceId).set(resourceModel).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddResourceActivity.this, "Your Resources is Edited Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddResourceActivity.this, YourContributionActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }


    private void chooseResourceClickListener() {
        chooseFile_AddResource_CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, 1212);
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
                    pdfName_AddResource_EditText.setText(f.getName());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void changedBranchSem() {
        spinner_branch_AddResource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                branch = spinner_branch_AddResource.getItemAtPosition(position).toString().trim();
                spinner_Semester_AddResource.setEnabled(!branch.equals("Common-1st year"));
                setSubjectAdapter(AppHelper.branchToShortConverter(branch), spinner_Semester_AddResource.getSelectedItem().toString(), "1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        spinner_Semester_AddResource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setSubjectAdapter(AppHelper.branchToShortConverter(branch), spinner_Semester_AddResource.getSelectedItem().toString(), "1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    private void setSubjectAdapter(String branch, String semester, String subject_code) {
        List<String> subjectList = new ArrayList<>();
        if (branch.equals("ALL")) semester = "1,2";
        Toast.makeText(this, "" + branch, Toast.LENGTH_SHORT).show();
        db.collection(Db.SUBJECTS_COL).whereEqualTo("branch", branch).whereEqualTo("semester", semester).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, String> subjects = (Map<String, String>) document.get("subjects");
                        for (Map.Entry<String, String> entry : subjects.entrySet()) {
                            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                            subjectList.add(entry.getValue());
                            hashMapSubjectCode.put(entry.getValue(), entry.getKey());
                        }
                    }
                    ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(AddResourceActivity.this, android.R.layout.simple_spinner_dropdown_item, subjectList);
                    spinner_subject_AddResource.setAdapter(adapterSubject);
                    if (subject_code != "1") {
                        ArrayAdapter myAdap = (ArrayAdapter) spinner_subject_AddResource.getAdapter(); //cast to an ArrayAdapter
                        int spinnerPosition = myAdap.getPosition(getKey(hashMapSubjectCode, subject_code));
                        spinner_subject_AddResource.setSelection(spinnerPosition);
                    }

                } else {
                }
            }
        });


    }

    private void uploadResource() {
        if (resourceName_AddResource_EditText.getText() == null) {
            Toast.makeText(AddResourceActivity.this, "Please Enter a valid name for the resourceModel", Toast.LENGTH_SHORT).show();
        }
        if (pdfUri == null) {
            Toast.makeText(AddResourceActivity.this, "Please select a resourceModel", Toast.LENGTH_SHORT).show();
        } else if (thumbnailImgUri == null) {
            Toast.makeText(AddResourceActivity.this, "Please select a thumbnail", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(AddResourceActivity.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference riversRef = storageReference.getReference().child("resources/" + resourceName_AddResource_EditText.getText().toString() + ".pdf");
            riversRef.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Date today = new Date();
                                    Timestamp timestamp = new Timestamp(today);
                                    ArrayList<String> tags = new ArrayList<>();
                                    ResourceModel resourceModel = new ResourceModel(timestamp, hashMapSubjectCode.get(spinner_subject_AddResource.getSelectedItem().toString()), pdfSize.toString(), uri.toString(), spinner_Semester_AddResource.getSelectedItem().toString(), AppHelper.convertTypetoInt(spinner_type_AddResource.getSelectedItem().toString()), Db.getUserModel().getUser_id(), resourceName_AddResource_EditText.getText().toString(), tags, null, spinner_branch_AddResource.getSelectedItem().toString());
                                    db.collection(Db.RESOURCES_DOC + "/resources").add(resourceModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            docId = documentReference.getId();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });

        }
    }


    private void uploadToDb() {
        submitResource_AddResource_CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadResource();
            }
        });
    }
}