package com.covidsaathi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class sessionsModel {
    @SerializedName("session_id")
    public String session_id;
    @SerializedName("date")
    public String date;
    @SerializedName("available_capacity")
    public int available_capacity;
    @SerializedName("available_capacity_dose1")
    public int available_capacity_dose1;
    @SerializedName("available_capacity_dose2")
    public int available_capacity_dose2;
    @SerializedName("walkin_ind")
    public String walkin_ind;
    @SerializedName("min_age_limit")
    public int min_age_limit;
    @SerializedName("vaccine")
    public String vaccine;
    @SerializedName("slots")
    public List<String> slots;

}
