package com.covidsaathi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistrictList {
    @SerializedName("districts")

    private List<District> districts = null;
    @SerializedName("ttl")
    private Integer ttl;

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
}
