package com.kyaw.springrevolution.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Post implements Serializable {
    transient String  timeStamp;
    transient String description,display_image,video_url,human_date;
    transient boolean is_cover;
    transient int ig_user;

    public Post(String timeStamp, String description, String display_image, String video_url, String human_date, boolean is_cover, int ig_user) {
        this.timeStamp = timeStamp;
        this.description = description;
        this.display_image = display_image;
        this.video_url = video_url;
        this.human_date = human_date;
        this.is_cover = is_cover;
        this.ig_user = ig_user;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_image() {
        return display_image;
    }

    public void setDisplay_image(String display_image) {
        this.display_image = display_image;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getHuman_date() {
        return human_date;
    }

    public void setHuman_date(String human_date) {
        this.human_date = human_date;
    }

    public boolean isIs_cover() {
        return is_cover;
    }

    public void setIs_cover(boolean is_cover) {
        this.is_cover = is_cover;
    }

    public int getIg_user() {
        return ig_user;
    }

    public void setIg_user(int ig_user) {
        this.ig_user = ig_user;
    }
}
