package com.covidsaathi.models;

import com.google.gson.annotations.SerializedName;

public class CentersResponseLatLong {
    @SerializedName("center_id")
    public int center_id;
    @SerializedName("name")
    public String name;
    @SerializedName("district_name")
    public String district_name;
    @SerializedName("state_name")
    public String state_name;
    @SerializedName("location")
    public String location;
    @SerializedName("pincode")
    public String pincode;
    @SerializedName("block_name")
    public String block_name;
    @SerializedName("lat")
    public String lat;
    @SerializedName("long")
    public String longi;
}
