package com.projectupma.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DizqusThreadModel  {
    Timestamp date;
    String description;
    String question;
    String user_id;
    String imageUrl;
    boolean reply;
    int flag;
    ArrayList<String> likes;
    List<String> children;
    List<String> tags;
    List<String> inappropriate;
    String type;

    public DizqusThreadModel() {
        likes = new ArrayList<>();
        inappropriate = new ArrayList<>();
    }

    public List<String> getInappropriate() {
        return inappropriate;
    }

    public void setInappropriate(List<String> inappropriate) {
        this.inappropriate = inappropriate;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
