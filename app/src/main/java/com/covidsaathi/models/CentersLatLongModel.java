package com.covidsaathi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CentersLatLongModel {
    @SerializedName("centers")
    public List<CentersResponseLatLong> centers;

    public List<CentersResponseLatLong> getCenters() {
        return centers;
    }
}
