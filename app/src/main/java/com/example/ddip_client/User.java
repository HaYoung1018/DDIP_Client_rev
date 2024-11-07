package com.example.ddip_client;

public class User {
    private String name;
    private String phoneNumber;
    private int profileImageResId;

    public User(String name, String phoneNumber, int profileImageResId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImageResId = profileImageResId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }
}
