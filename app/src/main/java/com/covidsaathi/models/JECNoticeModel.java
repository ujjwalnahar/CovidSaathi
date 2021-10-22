package com.covidsaathi.models;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JECNoticeModel {
    Timestamp date;
    String download_url;
    String text;
    String url;

    @Override
    public String toString() {
        return "JECNoticeModel{" +
                "date=" + date +
                ", download_url='" + download_url + '\'' +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getDate() {
        Date formatedDate=date.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy ");
        String S = sdf.format(formatedDate);
        return S.toString();
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JECNoticeModel() {
    }

    public JECNoticeModel(Timestamp date, String download_url, String text, String url) {
        this.date = date;
        this.download_url = download_url;
        this.text = text;
        this.url = url;
    }
}
