package com.covidsaathi.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.covidsaathi.R;
import com.covidsaathi.adapters.DistrictCenterRecyclerAdapter;
import com.covidsaathi.models.CentersLatLongModel;
import com.covidsaathi.models.CentersModel;
import com.covidsaathi.models.CentersResponse;
import com.covidsaathi.models.District;
import com.covidsaathi.models.DistrictList;
import com.covidsaathi.models.State;
import com.covidsaathi.models.StatesList;
import com.covidsaathi.utils.AppHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;


public class VaccinationFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    View fragment;
    TextView noCenterTextView;
    RecyclerView recycler_districtCenters;
    Spinner spinner_type_centers, spinner_state, spinner_district;
    List<Pair<Double, Double>> list = new ArrayList<>();
    List<String> listNames = new ArrayList<>();
    List<String> listCenterIds = new ArrayList<>();
    List<State> states = new ArrayList<>();
    List<District> districts = new ArrayList<>();
    List<CentersResponse> listCentersByDistrict=new ArrayList<>();
    MapView mapFragment;
    private FusedLocationProviderClient fusedLocationClient;
    AppHelper appHelper = new AppHelper();
    Activity context;
    LatLng home;


    public VaccinationFragment(Activity context) {
        this.context = context;
    }


    public static VaccinationFragment newInstance(Activity context) {
        VaccinationFragment fragment = new VaccinationFragment(context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void intiator(Bundle savedInstanceState, @NonNull View view) {
        fragment = view.findViewById(R.id.map);
        spinner_type_centers = view.findViewById(R.id.spinner_type_centers);
        spinner_district = view.findViewById(R.id.spinner_district);
        spinner_state = view.findViewById(R.id.spinner_state);
        noCenterTextView=view.findViewById(R.id.noCenterTextView);
        recycler_districtCenters=view.findViewById(R.id.recycler_districtCenters);
    }

    void methods() {

    }

    void setTypeToNearBy() {
        spinner_district.setVisibility(View.GONE);
        spinner_state.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams) fragment.getLayoutParams() ;
        params.topMargin=80;
        fragment.setLayoutParams(params);
        noCenterTextView.setVisibility(View.GONE);
        fragment.setVisibility(View.VISIBLE);
        recycler_districtCenters.setVisibility(View.GONE);

    }
    void setTypeToDistrict(){
        spinner_district.setVisibility(View.VISIBLE);
        spinner_state.setVisibility(View.VISIBLE);
        noCenterTextView.setVisibility(View.GONE);
        //ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams) fragment.getLayoutParams() ;
        //params.topMargin=0;
        //fragment.setLayoutParams(params);
        recycler_districtCenters.setVisibility(View.VISIBLE);
        fragment.setVisibility(View.GONE);

    }
    void setSpinner_type_centers() {
        spinner_type_centers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    getLocation();
                    setTypeToNearBy();
                }
                else if(position==1){
                    new fetchStates().execute();
                    setTypeToDistrict();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               // setTypeToNearBy();
            }
        });
    }


    void setSpinner_district() {
        List<String> listDistrictNames = new ArrayList<>();
        for (int i = 0; i < districts.size(); i++) {
            listDistrictNames.add(districts.get(i).getDistrictName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listDistrictNames);
        spinner_district.setAdapter(adapter);
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
        String dateString=dateFormat.format(date);
        //Log.d("12345", "setSpinner_district: "+dateString+districts.get(0).getDistrictId());
        //new fetchCentersByDistrict().execute(districts.get(0).getDistrictId().toString(),dateString);
        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Date date=new Date();
                SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                String dateString=dateFormat.format(date);
                if(districts.size()!=0){
                    Log.d("12345", "setSpinner_district: "+dateString+districts.get(position).getDistrictId());
                    //fragment.setVisibility(View.GONE);
                    new fetchCentersByDistrict().execute(districts.get(position).getDistrictId().toString(),dateString);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                new fetchCentersByDistrict().execute(districts.get(0).getDistrictId().toString(),dateString);
            }
        });
    }

    void setSpinner_state() {
        List<String> listStateNames = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            listStateNames.add(states.get(i).getStateName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listStateNames);
        spinner_state.setAdapter(adapter);
        new fetchDistricts().execute(states.get(0).getStateId().toString());
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new fetchDistricts().execute(states.get(position).getStateId().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                new fetchDistricts().execute(states.get(0).getStateId().toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vaccination, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // mapFragment = view.findViewById(R.id.map);
        //mapFragment.getMapAsync(this);
        intiator(savedInstanceState, view);
       // Toast.makeText(context, "It Is not working", Toast.LENGTH_LONG).show();
        getLocation();
        //setSpinner_type_centers();

        setTypeToNearBy();
        setSpinner_type_centers();
        super.onViewCreated(view, savedInstanceState);
    }
    void getLocation(){
        //noCenterTextView.setVisibility(View.GONE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        ((FusedLocationProviderClient) fusedLocationClient).getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d("11111", "onLocationChanged: " + location.getLatitude() + " " + location.getLongitude());
                            home = new LatLng(location.getLatitude(), location.getLongitude());
                            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (addresses.size() > 0) {
                                System.out.println(addresses.get(0).getLocality());
                                Log.d("11111", "onLocationChanged: " + addresses.get(0).getLocality() + addresses.get(0).getPostalCode());
                            } else {
                                // do your stuff
                            }
                            String[] strings = new String[]{Double.toString(location.getLatitude()), Double.toString(location.getLongitude())};
                            new fetchCenters().execute(strings);
                            // Logic to handle location object
                        }
                    }
                });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        for (int i = 0; i < list.size(); i++) {
            LatLng newCenter = new LatLng(list.get(i).first, list.get(i).second);
            //avgLang+=list.get(i).second;
            //avgLat+=list.get(i).first;
            mMap.addMarker(new MarkerOptions().position(newCenter).title(listNames.get(i)));
        }
        int n=list.size();

        if(list.size()!=0){
            fragment.setVisibility(View.VISIBLE);
            noCenterTextView.setVisibility(View.GONE);
            LatLng newCenter = new LatLng(list.get(0).first, list.get(0).second);
            mMap.addMarker(new MarkerOptions().position(home).title("Home").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newCenter, 13.0f));
        }
        else{
            fragment.setVisibility(View.GONE);
            noCenterTextView.setVisibility(View.VISIBLE);
        }
    }

    class fetchCenters extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            try {
                Call<CentersLatLongModel> center = appHelper.vaccinationApis.getCentersFromLatLong(strings[0], strings[1]);
                Response<CentersLatLongModel> centers = center.execute();
                list.clear();
                for (int i = 0; i < centers.body().centers.size(); i++) {
                    Log.d("11111", "call_RetroFitAPi: " + centers.body().centers.get(i).location.toString());
                    Pair<Double, Double> p = new Pair<Double, Double>(Double.parseDouble(centers.body().centers.get(i).lat), Double.parseDouble(centers.body().centers.get(i).longi));
                    list.add(p);
                    listNames.add(centers.body().centers.get(i).name);
                    listCenterIds.add(Integer.toString(centers.body().centers.get(i).center_id));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(VaccinationFragment.this);
            super.onPostExecute(aVoid);
        }
    }

    class fetchStates extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            states.clear();
            try {
                Call<StatesList> call = appHelper.vaccinationApis.getStates();
                Response<StatesList> response = call.execute();
                for (int i = 0; i < response.body().getStates().size(); i++) {
                    states.add(response.body().getStates().get(i));
                    Log.d("111", "doInBackground:" + states.get(i).getStateName() + states.get(i).getStateId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setSpinner_state();
            super.onPostExecute(aVoid);
        }
    }

    class fetchDistricts extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            districts.clear();
            try {
                Call<DistrictList> call = appHelper.vaccinationApis.getDistricts(strings[0]);
                Response<DistrictList> response = call.execute();
                for (int i = 0; i < response.body().getDistricts().size(); i++) {
                    districts.add(response.body().getDistricts().get(i));
                    Log.d("111", "doInBackground:" + districts.get(i).getDistrictName() + districts.get(i).getDistrictId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setSpinner_district();
            super.onPostExecute(aVoid);
        }
    }
    class fetchCentersByDistrict extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            listCentersByDistrict.clear();
            list.clear();
            try {
                Call<CentersModel> call=appHelper.vaccinationApis.getCenterByDistrict(strings[0],strings[1]);
                Response<CentersModel> response=call.execute();
                for(int i=0;i<response.body().getSessions().size();i++){
                    listCentersByDistrict.add(response.body().getSessions().get(i));
                    //Pair<Double,Double> p=new Pair<Double, Double>(response.body().getSessions().get(i).getLat(),response.body().getSessions().get(i).getLong());
                    //list.add(p);
                    Log.d("11111222", "call_RetroFitAPi: " + response.body().getSessions().get(i).getName().toString()+ " "+ response.body().getSessions().get(i).getLat().toString()+" "+response.body().getSessions().get(i).getLong().toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            //supportMapFragment.getMapAsync(VaccinationFragment.this);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
            if(listCentersByDistrict.size()==0){
                noCenterTextView.setVisibility(View.VISIBLE);
                recycler_districtCenters.setVisibility(View.GONE);
            }
            else{
                noCenterTextView.setVisibility(View.GONE);
                recycler_districtCenters.setVisibility(View.VISIBLE);
                DistrictCenterRecyclerAdapter districtCenterRecyclerAdapter=new DistrictCenterRecyclerAdapter(listCentersByDistrict,context);
                recycler_districtCenters.setLayoutManager(linearLayoutManager);
                recycler_districtCenters.setAdapter(districtCenterRecyclerAdapter);
                recycler_districtCenters.setVisibility(View.VISIBLE);
                fragment.setVisibility(View.GONE);
                super.onPostExecute(aVoid);
            }

        }
    }

}