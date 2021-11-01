package com.example.MyBTL.Model;

public class User {
    private String userName;
    private String email;
    private String imageProfile;
    public User()
    {

    }
    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }
    public User(String userName, String email, String imageProfile) {
        this.userName = userName;
        this.email = email;
        this.imageProfile = imageProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}

