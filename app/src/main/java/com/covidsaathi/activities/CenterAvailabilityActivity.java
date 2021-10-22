package com.covidsaathi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.covidsaathi.R;
import com.covidsaathi.models.Centers;
import com.covidsaathi.models.CentersLatLongModel;
import com.covidsaathi.models.CentersResponse;
import com.covidsaathi.utils.AppHelper;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class CenterAvailabilityActivity extends AppCompatActivity {
    TextView center_name,center_location,center_District,center_pincode,available_dose,for_dose_1,for_dose_2,center_vaccine,date,center_minimum_age;
    MaterialButton button_book_slot;
    AppHelper appHelper = new AppHelper();
    Centers centersResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_availability);
        initiator(savedInstanceState);
        String center_id=getIntent().getStringExtra("center_id");
        String date=getIntent().getStringExtra("date");
        CentersResponse centersResponse=(CentersResponse)getIntent().getSerializableExtra("center");
        setCenterScreen(centersResponse);
        //new fetchCenterInfo().execute(center_id,date);
    }
    void initiator(Bundle savedInstanceState){
        center_name=findViewById(R.id.center_name);
        center_location=findViewById(R.id.center_location);
        center_District=findViewById(R.id.center_District);
        center_pincode=findViewById(R.id.center_pincode);
        available_dose=findViewById(R.id.available_dose);
        for_dose_1=findViewById(R.id.for_dose_1);
        for_dose_2=findViewById(R.id.for_dose_2);
        center_vaccine=findViewById(R.id.center_vaccine);
        button_book_slot=findViewById(R.id.button_book_slot);
        date=findViewById(R.id.date);
        center_minimum_age=findViewById(R.id.center_minimum_age);

    }
    void setCenterScreen(CentersResponse centers){
        if(centers!=null) {
            center_name.setText(centers.getName());
            center_location.setText(centers.getAddress());
            center_District.setText(centers.getDistrictName());
            center_pincode.setText(centers.getPincode().toString());

                available_dose.setText(String.format(centers.getAvailableCapacity().toString()));
                for_dose_1.setText(String.format(centers.getAvailableCapacityDose1().toString()));
                for_dose_2.setText(String.format(centers.getAvailableCapacityDose2().toString()));
                center_vaccine.setText(String.format(centers.getVaccine().toString()));
                date.setText(String.format(centers.getDate().toString()));
                center_minimum_age.setText(String.format(centers.getMinAgeLimit().toString()));
button_book_slot.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://selfregistration.cowin.gov.in/"));
        startActivity(browserIntent);
    }
});

        }
        //button_book_slot.setText(centers.getName());

    }
    class fetchCenterInfo extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            try { Log.d("11111", "call_RetroFitAPi: " + strings[0]+strings[1]);
                Call<Centers> center = appHelper.vaccinationApis.getCenterById(strings[0],strings[1]);
                Response<Centers> centers = center.execute();
                Log.d("11111", "call_RetroFitAPi: " + centers.body());
                centersResponse=centers.body();
                //setCenterScreen(centersResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}