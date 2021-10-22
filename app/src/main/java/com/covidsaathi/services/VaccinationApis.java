package com.covidsaathi.services;

import com.covidsaathi.models.Centers;
import com.covidsaathi.models.CentersLatLongModel;
import com.covidsaathi.models.CentersModel;
import com.covidsaathi.models.DistrictList;
import com.covidsaathi.models.StatesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface VaccinationApis {
    @GET("v2/appointment/centers/public/findByLatLong")
    Call<CentersLatLongModel> getCentersFromLatLong(@Query("lat")String Lat, @Query("long") String Long);
    @GET("v2/appointment/sessions/public/calendarByPin")
    Call<CentersLatLongModel> getCentersByPinCode(@Query("pincode")String pincode, @Query("date")String date);
    @GET(" v2/appointment/sessions/public/findByDistrict")
    Call<CentersModel> getCenterByDistrict(@Query("district_id") String district_id, @Query("date") String date);
  @GET("v2/admin/location/states")
    Call<StatesList> getStates();
  @GET("v2/admin/location/districts/{state_id}")
    Call<DistrictList> getDistricts(@Path("state_id") String state_id);
  @GET("v2/appointment/sessions/public/calendarByCenter")
    Call<Centers> getCenterById(@Query("center_id")String center_id,@Query("date")String date);
}
