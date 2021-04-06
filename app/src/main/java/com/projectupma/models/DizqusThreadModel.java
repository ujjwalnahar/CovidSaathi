package com.projectupma.models;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DizqusThreadModel {
    Timestamp time;
    String text;
    String question;
    String tag;
    String user_id;
    boolean reply;
    int flag;
    int likes;
    List<String> children;
    List<String> tags;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public DizqusThreadModel() {
    }


    public DizqusThreadModel(Timestamp time, String text, String tag, String user_id, String question, boolean reply, int flag, int likes, List<String> children, List<String> tags) {
        this.time = time;
        this.text = text;
        this.question = question;
        this.tag = tag;
        this.user_id = user_id;
        this.reply = reply;
        this.flag = flag;
        this.likes = likes;
        this.children = children;
        this.tags = tags;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
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

    public String getDate() {
        Date formatedDate = time.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy ");
        String S = sdf.format(formatedDate);
        return S.toString();


    }
}
