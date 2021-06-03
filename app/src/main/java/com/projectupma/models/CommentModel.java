package com.projectupma.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class CommentModel {
    String user_id;
    String comment;
    String thread_id;
    List<String> replies;
    List<String> likes;
    Timestamp date;

    public CommentModel() {
        replies = new ArrayList<>();
        likes=new ArrayList<>();
    }

    public CommentModel(String user_id, String comment, String thread_id, List<String> replies, List<String> likes, Timestamp date) {
        this.user_id = user_id;
        this.comment = comment;
        this.thread_id = thread_id;
        this.replies = replies;
        this.likes = likes;
        this.date = date;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public List<String> getReplies() {
        return replies;
    }

    public void setReplies(List<String> replies) {
        this.replies = replies;
    }

}
