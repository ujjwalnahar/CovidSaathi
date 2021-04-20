package com.projectupma.models;

import java.io.Serializable;
import java.util.Date;

public class UserModel implements Serializable {
    String user_id;
    String name;
    String phone_no;
    String roll_no;
    String semester;
    String branch;
    String college;
    String password;
    String photo_url;
    String reward_id;
    String idcard_url;
    String email;
    Date date_created;
    String auth;
    String approved;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getReward_id() {
        return reward_id;
    }

    public void setReward_id(String reward_id) {
        this.reward_id = reward_id;
    }

    public String getIdcard_url() {
        return idcard_url;
    }

    public void setIdcard_url(String idcard_url) {
        this.idcard_url = idcard_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
    public UserModel() {}

    public UserModel(String user_id, String name, String phone_no, String roll_no, String semester, String branch, String college, String password, String photo_url, String reward_id, String idcard_url, String email, Date date_created, String auth, String approved) {
        this.user_id = user_id;
        this.name = name;
        this.phone_no = phone_no;
        this.roll_no = roll_no;
        this.semester = semester;
        this.branch = branch;
        this.college = college;
        this.password = password;
        this.photo_url = photo_url;
        this.reward_id = reward_id;
        this.idcard_url = idcard_url;
        this.email = email;
        this.date_created = date_created;
        this.auth = auth;
        this.approved = approved;
    }



}
