package com.covidsaathi.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeTableModel {


    Map<String, ArrayList<String>> Monday;
    Map<String, ArrayList<String>> Tuesday;
    Map<String, ArrayList<String>> Wednesday;
    Map<String, ArrayList<String>> Thursday;
    Map<String, ArrayList<String>> Friday;
    Map<String, ArrayList<String>> Saturday;

    @Override
    public String toString() {
        return "TimeTableModel{" +
                "Monday=" + Monday.keySet() +
                ", Tuesday=" + Tuesday.keySet() +
                ", Wednesday=" + Wednesday.keySet() +
                ", Thursday=" + Thursday.keySet() +
                ", Friday=" + Friday.keySet() +
                ", Saturday=" + Saturday.keySet() +
                '}';
    }

    public TimeTableModel() {
        setMonday(new HashMap<>());
        setTuesday(new HashMap<>());
        setWednesday(new HashMap<>());
        setThursday(new HashMap<>());
        setFriday(new HashMap<>());
        setSaturday(new HashMap<>());
    }

    public Map<String, ArrayList<String>> getMonday() {
        return Monday;
    }

    public void setMonday(Map<String, ArrayList<String>> monday) {
        Monday = monday;
    }

    public Map<String, ArrayList<String>> getTuesday() {
        return Tuesday;
    }

    public void setTuesday(Map<String, ArrayList<String>> tuesday) {
        Tuesday = tuesday;
    }

    public Map<String, ArrayList<String>> getWednesday() {
        return Wednesday;
    }

    public void setWednesday(Map<String, ArrayList<String>> wednesday) {
        Wednesday = wednesday;
    }

    public Map<String, ArrayList<String>> getThursday() {
        return Thursday;
    }

    public void setThursday(Map<String, ArrayList<String>> thursday) {
        Thursday = thursday;
    }

    public Map<String, ArrayList<String>> getFriday() {
        return Friday;
    }

    public void setFriday(Map<String, ArrayList<String>> friday) {
        Friday = friday;
    }

    public Map<String, ArrayList<String>> getSaturday() {
        return Saturday;
    }

    public void setSaturday(Map<String, ArrayList<String>> saturday) {
        Saturday = saturday;
    }
}
