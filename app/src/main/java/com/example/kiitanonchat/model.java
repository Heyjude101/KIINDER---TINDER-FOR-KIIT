package com.example.kiitanonchat;

import android.text.SpannableString;

public class model
{
    private String chats;
    private String user;
    private String userColor;
    private String time;
    private String imageUrl;
    private String androidId;
    model()
    {
    }
    public model(String chats, String user , String userColor , String time , String imageUrl , String androidId) {
        this.chats = chats;
        this.user = user;
        this.userColor = userColor;
        this.time = time;
        this.imageUrl = imageUrl;
        this.androidId = androidId;

    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getTime() {
        return time;
    }

    public String getChats() {
        return chats;
    }

    public String getUser() {
        return user;
    }

    public String getUserColor() {
        return userColor;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setChats(String chats) {
        this.chats = chats;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUserColor(String userColor) {
        this.userColor = userColor;
    }

    public void setTime(String time) {
        this.time = time;
    }
}