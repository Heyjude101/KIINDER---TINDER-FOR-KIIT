package com.example.kiitanonchat;

public class AllUsers {
    String allUsername;

    AllUsers(){

    }

    public AllUsers(String allUsername) {
        this.allUsername = allUsername;
    }

    public String getAllUsername() {
        return allUsername;
    }

    public void setAllUsername(String allUsername) {
        this.allUsername = allUsername;
    }
}
