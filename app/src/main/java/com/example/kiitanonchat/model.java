package com.example.kiitanonchat;

public class model
{
    private String chats;
    private String user;
    private String userColor;
    private String time;
    model()
    {
    }
    public model(String chats, String user , String userColor , String time) {
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
}