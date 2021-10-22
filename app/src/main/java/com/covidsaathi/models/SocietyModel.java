package com.covidsaathi.models;

public class SocietyModel {
    String clubId;
    String name;
    String photoLink;

    public SocietyModel(String clubId, String name, String photoLink) {
        this.clubId = clubId;
        this.name = name;
        this.photoLink = photoLink;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }
}
