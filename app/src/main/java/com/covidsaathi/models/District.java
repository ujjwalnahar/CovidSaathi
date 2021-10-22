package com.covidsaathi.models;

import com.google.gson.annotations.SerializedName;

public class District {
    @SerializedName("state_id")
    private Integer stateId;
    @SerializedName("district_id")
    private Integer districtId;
    @SerializedName("district_name")
    private String districtName;
    @SerializedName("district_name_l")
    private String districtNameL;

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictNameL() {
        return districtNameL;
    }

    public void setDistrictNameL(String districtNameL) {
        this.districtNameL = districtNameL;
    }
}
