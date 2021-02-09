package com.example.kiitanonchat;

public class modelc
{
    private String chats;
    private String user;
    private String userColor;
    private String time;
    modelc()
    {
    }
    public modelc(String chats, String user , String userColor , String time, String show) {
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