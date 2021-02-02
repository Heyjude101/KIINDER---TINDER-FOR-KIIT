package com.example.kiitanonchat;

public class model
{
    private String chats;
    private String user;
    private String userColor;
    private String time;
    private String imageUrl;
    model()
    {
    }
    public model(String chats, String user , String userColor , String time , String imageUrl) {
        this.chats = chats;
        this.user = user;
        this.userColor = userColor;
        this.time = time;
        this.imageUrl = imageUrl;
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
}