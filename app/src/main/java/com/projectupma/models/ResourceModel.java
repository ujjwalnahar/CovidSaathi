package com.projectupma.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class ResourceModel {
    Timestamp date;
    String subject_code;
    String size;
    String doc_link;
    String semester;
    String type;
    String userId;
    String doc_name;
    List<String> tags;
    String thumbnailUrl;
    String branch;
    int downloads;

    public ResourceModel(Timestamp date, String subject_code, String size, String doc_link, String semester, String type, String userId, String doc_name, List<String> tags, String thumbnailUrl, String branch) {
        this.date = date;
        this.subject_code = subject_code;
        this.size = size;
        this.doc_link = doc_link;
        this.semester = semester;
        this.type = type;
        this.userId = userId;
        this.doc_name = doc_name;
        this.tags = tags;
        this.thumbnailUrl = thumbnailUrl;
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public ResourceModel() {
        this.downloads=0;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDoc_link() {
        return doc_link;
    }

    public void setDoc_link(String doc_link) {
        this.doc_link = doc_link;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
