package com.projectupma.models;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class SliderItem {
    String title;
    String imageUrl;

    public SliderItem(HashMap<String,Object> map){
        setTitle((String) map.get("title"));
        setImageUrl((String) map.get("image_url"));
    }

    @Override
    public String toString() {
        return "SliderItem{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
