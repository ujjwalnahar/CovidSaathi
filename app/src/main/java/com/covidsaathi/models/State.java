package com.covidsaathi.models;

import com.google.gson.annotations.SerializedName;

public class State {
    @SerializedName("state_id")
    private Integer stateId;
    @SerializedName("state_name")
    private String stateName;
    @SerializedName("state_name_l")
    private String stateNameL;

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateNameL() {
        return stateNameL;
    }

    public void setStateNameL(String stateNameL) {
        this.stateNameL = stateNameL;
    }
}
