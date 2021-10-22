package com.covidsaathi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CentersModel {
    @SerializedName("sessions")
    private List<CentersResponse> sessions = null;

    public List<CentersResponse> getSessions() {
        return sessions;
    }

    public void setSessions(List<CentersResponse> sessions) {
        this.sessions = sessions;
    }

}
