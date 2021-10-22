package com.covidsaathi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatesList {

    @SerializedName("states")
    private List<State> states = null;
    @SerializedName("ttl")
    private Integer ttl;

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

}
