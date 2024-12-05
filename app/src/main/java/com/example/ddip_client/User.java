package com.example.ddip_client;

public class User {
    private String name;
    private String phoneNumber;
    private int profileImageResId;
    private int salary;      // 급여
    private int workHours;   // 근무시간

    public User(String name, String phoneNumber, int profileImageResId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImageResId = profileImageResId;
    }

    public User(String name, String phoneNumber, int profileImageResId, int salary, int workHours) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImageResId = profileImageResId;
        this.salary = salary;
        this.workHours = workHours;
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
    public int getSalary() {
        return salary;
    }

    public int getWorkHours() {
        return workHours;
    }
}
