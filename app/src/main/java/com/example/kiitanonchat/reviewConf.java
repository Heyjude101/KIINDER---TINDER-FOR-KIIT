package com.example.kiitanonchat;

public class reviewConf
{
    private String chats;
    private String user;
    private String userColor;
    private String time;
    reviewConf()
    {
    }
    public reviewConf(String chats, String user , String userColor , String time) {
        this.chats = chats;
        this.user = user;
        this.userColor = userColor;
        this.time = time;
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